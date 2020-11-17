(ns kodemaker-no.new-pages.cv-page
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.new-pages.person :as person]
            [ui.elements :as e])
  (:import java.time.format.DateTimeFormatter))

(defn years-of-experience [{:person/keys [experience-since]}]
  (let [years (when experience-since
                (- (+ 1900 (.getYear (java.util.Date.))) experience-since))]
    (cond
      (nil? years) "lite"
      (<= years 30) (str years " års")
      :default "mange års")))

(defn endorsement-highlight [{:person/keys [endorsement-highlight]}]
  (when-let [{:keys [quote author title]} endorsement-highlight]
    {:text quote
     :source (str author (when title (str ", " title)))}))

(defn project-highlight [{:keys [blurb customer link]}]
  {:title customer
   :text blurb
   :href link})

(defn cv-profile [cv]
  (d/entity (d/entity-db cv) (:cv/person cv)))

(defn side-project-techs [person]
  (mapcat :side-project/techs (:person/side-projects person)))

(defn blog-post-techs [person]
  (mapcat :blog-post/techs (:blog-post/_author person)))

(defn screencast-techs [person]
  (mapcat :screencast/techs (:person/screencasts person)))

(defn presentation-techs [person]
  (mapcat :presentation/techs (:person/presentations person)))

(defn business-presentation-techs [person]
  (mapcat :presentation-product/techs (:person/business-presentations person)))

(defn open-source-techs [person]
  (->> (concat (:person/open-source-projects person)
               (:person/open-source-contributions person))
       (mapcat :tech)))

(defn project-techs [person]
  (mapcat :project/techs (:person/projects person)))

(defn gather-all-techs [db cv person]
  (->> (concat (map :list/ref (:person/using-at-work person))
               (:person/innate-skills person)
               (side-project-techs person)
               (blog-post-techs person)
               (screencast-techs person)
               (presentation-techs person)
               (open-source-techs person)
               (project-techs person))
       (remove (or (:person/exclude-techs person) #{}))
       frequencies
       (sort-by (comp - second))
       (map first)
       (person/prefer-techs (person/preferred-techs person))
       (map #(d/entity db %))))

(comment
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))
  (def person (d/entity db :person/christian))
  (def cv (:cv/_person person))

  (into {} (d/entity db [:db/ident :tech/clojure]))

  (:person/profile-overview-picture person)
  (:person/profile-page-picture person)
  (:person/cv-picture person)
  (:person/profile-pictures person)

  (->> (:person/certifications person)
       (group-by :year)
       (sort-by (comp - first)))

  (->> (:person/projects person)
       (sort-by :list/idx)
       first
       :project/employer
       (d/entity db)
       :employer/name)

  (into {}  (first (:person/projects person)))

  (->> (gather-all-techs db cv person)
       (group-by :tech/type)
       (map (fn [[category techs]]
              [(d/entity db [:db/ident category]) techs])))

  )

(defn prep-tech-category [[category techs]]
  {:title (:tech-category/label category)
   :contents [[:p.text (e/enumerate-techs techs)]]})

(defn get-category [db cat]
  (d/entity db [:db/ident cat]))

(defn expand-category [db categories to-expand techs]
  (let [parent (:tech-category/parent (get-category db to-expand))]
    (-> categories
        (dissoc to-expand)
        (update parent concat techs))))

(defn square [n]
  (* n n))

(defn mean [xs]
  (/ (reduce + xs) (count xs)))

(defn standard-deviation [xs]
  (Math/sqrt (/ (reduce + (map square (map - xs (repeat (mean xs)))))
                (- (count xs) 1 ))))

(defn calculate-category-deviation [categories]
  (->> categories
       (map (comp count second))
       standard-deviation))

(defn limit-category-count [{:keys [desired-categories]} categories]
  (let [db (d/entity-db (-> categories first second first))]
    (loop [categories categories]
      (let [expandable (some->> categories
                                (sort-by (comp count second))
                                (filter (fn [[cat techs]]
                                          (:tech-category/parent (get-category db cat))))
                                )]
        (if (or (<= (count categories) desired-categories)
                (nil? (seq expandable)))
          categories
          (recur (->> expandable
                      (map #(apply expand-category db categories %))
                      (map (juxt identity calculate-category-deviation))
                      (sort-by second)
                      ffirst)))))))

(defn maintain-sort-order [techs categories]
  (map (fn [[category xs]]
         [category (sort-by #(.indexOf techs %) xs)])
       categories))

(defn compile-cv-techs [person]
  (let [db (d/entity-db person)
        techs (gather-all-techs db (:cv/_person person) person)]
    (->> techs
         (group-by :tech/type)
         (limit-category-count {:desired-categories (min 9 (/ (count techs) 4))})
         (maintain-sort-order techs)
         (map (fn [[cat techs]]
                [(d/entity db [:db/ident cat]) techs]))
         (filter (comp :tech-category/label first))
         (sort-by (comp :list/idx first)))))

(defn technology-section [cv person]
  {:kind :definitions
   :title "Teknologi"
   :id "technology"
   :definitions (->> (compile-cv-techs person)
                     (map prep-tech-category))})

(defn format-year-month [date]
  (when date
    (.format (DateTimeFormatter/ofPattern "MM.yyyy") date)))

(defn- year-range [years & [start end]]
  (if start
    (str (format-year-month start) " - " (format-year-month end))
    (f/year-range years)))

(defn- project-year-range [project]
  (year-range (:project/years project) (:project/start project) (:project/end project)))

(defn render-project [project]
  {:title (list (or (:cv/customer project)
                    (:project/customer project))
                [:br] (project-year-range project))
   :contents [[:h4.h6 [:em (:project/summary project)]]
              [:div.text
               (f/to-html (or (:cv/description project) (:project/description project)))]
              [:p.text-s.annotation.mts
               (->> (h/unwrap-ident-list project :project/tech-list)
                    (map :tech/name)
                    e/comma-separated)]]})

(defn render-employer [employer employment projects]
  (into
   [{:type :separator
     :category "Arbeidsgiver"
     :title (:employer/name employer)
     :description (some-> employment :description f/to-html)}]
   (map render-project projects)))

(defn employment [person employer]
  (get-in person [:person/employments (some-> employer name keyword)]))

(defn projects-section [cv person]
  (let [db (d/entity-db person)]
    {:kind :definitions
     :title "Prosjekter"
     :id "projects"
     :definitions
     (->> (:person/projects person)
          (sort-by :list/idx)
          (group-by :project/employer)
          (mapcat (fn [[employer projects]]
                    (render-employer (d/entity db employer) (employment person employer) projects))))}))

(defn endorsements-section [cv person]
  (when-let [endorsements (seq (sort-by :list/idx (:person/endorsements person)))]
    {:kind :definitions
     :id "endorsements"
     :title "Anbefalinger"
     :definitions (->> endorsements
                       (map (fn [endorsement]
                              {:type :complex-title
                               :title [:div
                                       [:h3.h4-light (:author endorsement)]
                                       [:p (:title endorsement)]]
                               :contents [(e/blockquote endorsement)]})))}))

(defn render-certifications [[year certifications]]
  {:title (str year)
   :contents
   [[:ul.dotted.dotted-tight
     (map (fn [{:keys [name year institution url certificate]}]
            [:li
             (if url
               [:a.link {:href url} name]
               name)
             (when institution
               (format " (%s)" institution))
             (when certificate
               (list " - " [:a.link {:href (:url certificate)} (or (:text certificate) "Kursbevis")]))])
          certifications)]]})

(defn certifications-section [cv person]
  (when-let [certifications (seq (:person/certifications person))]
    {:kind :definitions
     :title "Sertifiseringer og kurs"
     :definitions
     (->> certifications
          (group-by :year)
          (sort-by (comp - first))
          (map render-certifications))}))

(defn education-section [cv person]
  (when-let [educations (seq (sort-by :list/idx (:person/education person)))]
    {:kind :definitions
     :title "Utdanning"
     :definitions
     (map (fn [{:keys [institution years subject]}]
            {:title (year-range years)
             :contents [[:h4.h6 [:em institution]]
                        [:p subject]]})
          educations)}))

(defn presentation-url [presentation]
  (or (:page/uri presentation)
      (:presentation/video-url presentation)
      (:presentation/slides-url presentation)
      (:presentation/source-url presentation)))

(defn render-presentations [[year presentations]]
  {:title (str year)
   :contents
   [[:ul.dotted.dotted-tight
     (map (fn [presentation]
            [:li
             (if-let [url (presentation-url presentation)]
               [:a.link {:href url} (:presentation/title presentation)]
               (:presentation/title presentation))
             (when-let [event (:presentation/event-name presentation)]
               (list " ("
                     (if-let [url (:presentation/event-url presentation)]
                       [:a {:href url} event]
                       event)
                     ")"))])
          presentations)]]})

(defn presentation-section [cv person]
  (when-let [presentations (seq (:person/presentations person))]
    {:kind :definitions
     :title "Presentasjoner"
     :definitions
     (->> presentations
          (sort-by :presentation/date)
          reverse
          (group-by (comp #(.getYear %) :presentation/date))
          (sort-by first)
          reverse
          (map render-presentations))}))

(defn open-source-section [cv person]
  (when-let [{:keys [title techs]} (person/prepare-open-source-projects person)]
    {:kind :definitions
     :title title
     :definitions (for [{:keys [title markup]} techs]
                    {:title title
                     :contents [markup]})}))

(defn one-of [m ks]
  (loop [[k & ks] ks]
    (if (nil? k)
      nil
      (or (get m k)
          (recur ks)))))

(defn side-project [project]
  {:title (one-of project [:cv/title :screencast/title :side-project/title :blog-post/title])
   :url (one-of project [:page/uri :screencast/url :side-project/url :blog-post/external-url])
   :summary (one-of project [:cv/description :cv/blurb :side-project/description :screencast/description :blog-post/blurb])})

(defn prefix-title [prefix]
  (fn [project]
    (update-in project [:title] #(str prefix %))))

(defn other-contributions [person]
  (concat
   (map side-project (sort-by :list/idx (:person/screencasts person)))
   (map side-project (sort-by :list/idx (:person/side-projects person)))
   (->> (:blog-post/_author person)
        (remove :blog-post/archived?)
        (sort-by :blog-post/published)
        reverse
        (map (comp (prefix-title "Artikkel: ") side-project)))))

(defn other-contributions-section [cv person]
  (when-let [contribs (seq (other-contributions person))]
    {:kind :definitions
     :title "Andre faglige bidrag"
     :definitions
     [{:breakable? true
       :contents
       (->> contribs
            (map (fn [{:keys [title url summary]}]
                   (e/teaser
                    {:url url
                     :title title
                     :content [:div.text (f/to-html summary)]}))))}]}))

(defn create-page [cv]
  (let [person (cv-profile cv)]
    {:title (format "%s CV" (:person/full-name person))
     :sections
     (->> [{:kind :cv-intro
            :image (:person/cv-picture person)
            :friendly-name (:person/given-name person)
            :full-name (:person/full-name person)
            :title (:person/title person)
            :contact-lines [(:person/phone-number person)
                            (:person/email-address person)]
            :links (person/prep-presence-links (:person/presence person))
            :experience (format "Utvikler med %s erfaring" (years-of-experience person))
            :qualifications (->> (:person/qualifications person)
                                 (sort-by :list/idx)
                                 (map :qualification/text))
            :quote (endorsement-highlight person)
            :description (f/to-html (:person/description person))
            :highlights (map project-highlight (:person/project-highlights person))}
           (technology-section cv person)
           (projects-section cv person)
           (endorsements-section cv person)
           (certifications-section cv person)
           (education-section cv person)
           (presentation-section cv person)
           (open-source-section cv person)
           (other-contributions-section cv person)
           {:kind :footer}]
          (remove nil?))}))

