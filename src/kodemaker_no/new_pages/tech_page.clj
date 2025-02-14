(ns kodemaker-no.new-pages.tech-page
  (:require [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h :refer [map-vals]]
            [kodemaker-no.markup :as m]
            [kodemaker-no.new-pages.blog :as blog]
            [kodemaker-no.new-pages.open-source :as oss]
            [ui.elements :as e]))

(defn presentation-uri [pres]
  (or (:page/uri pres)
      (:presentation/video-url pres)
      (:presentation/source-url pres)))

(defn change-1st-person-to-3rd [value entity person-lookup]
  (str/replace value #"\b[jJ]eg\b"
               (let [person (person-lookup entity)]
                 (str "[" (:person/given-name person) "](" (:page/uri person) ")"))))

(defn choose-and-change [choose change k & args]
  (fn [entities]
    (when-let [entity (choose entities)]
      {k (apply change (k entity) entity args)})))

(defn merge-presentations [presentations]
  (-> (h/select-keys-by presentations
                        {:presentation/thumb first
                         :presentation/date first
                         :presentation/video-url first
                         :presentation/source-url first
                         :presentation/title first
                         :presentation/description (choose-and-change first
                                                                      change-1st-person-to-3rd
                                                                      :presentation/description
                                                                      :person/_presentations)
                         :page/uri first})
      (assoc :presentation/people (keep :person/_presentations presentations))))

(defn merge-recommendations [recommendations]
  (-> (h/select-keys-by recommendations
                        {:recommendation/title first
                         :recommendation/url first
                         :recommendation/description first})
      (assoc :recommendation/people (keep :person/_recommendations recommendations))))

(defn recommendations-section [tech]
  (when-let [merged-recommendations (->> (:recommendation/_techs tech)
                                         (group-by :recommendation/url)
                                         vals
                                         (map merge-recommendations)
                                         seq)]
    {:kind :titled
     :title "Våre anbefalinger"
     :contents (for [recommendation (->> merged-recommendations
                                         (sort-by (juxt (comp - count :recommendation/people)
                                                        (comp - count :recommendation/description)))
                                         (take 5))]
                 (e/teaser
                  {:title (:recommendation/title recommendation)
                   :tags (e/people-tags {:prefix "Anbefalt av"
                                         :people (:recommendation/people recommendation)
                                         :class "tags"})
                   :url (:recommendation/url recommendation)
                   :content (f/to-html (:recommendation/description recommendation))}))}))

(defn classify-presentations [tech]
  (let [all-presentations (->> (:presentation/_techs tech)
                               (filter presentation-uri)
                               (group-by presentation-uri)
                               vals
                               (map merge-presentations)
                               (filter #(< 80 (count (:presentation/description % ""))))
                               (sort-by :presentation/date)
                               (reverse)
                               seq)
        presentation-stars (->> all-presentations
                                (filter :presentation/thumb)
                                (filter :presentation/video-url))
        headliner (when (:tech/description tech)
                    (first presentation-stars))
        to-show-in-grid (take 4 (remove #{headliner} presentation-stars))]
    {:headliner headliner
     :to-show-in-grid to-show-in-grid
     :remaining (remove (into #{headliner} to-show-in-grid)
                        all-presentations)}))

(defn presentations-section [{:keys [to-show-in-grid remaining]}]
  (when (seq (concat to-show-in-grid remaining))
    {:kind :titled
     :title "Våre foredrag"
     :contents (->> [(when (seq to-show-in-grid)
                       (e/tango-grid
                        (map
                         (fn [pres style video-class]
                           {:content [:div
                                      (e/video-thumb
                                       {:class (str style " " video-class)
                                        :img (str "/" style (:presentation/thumb pres))
                                        :tags (e/people-tags {:class "tags"
                                                              :people (:presentation/people pres)})
                                        :url (presentation-uri pres)
                                        :title (:presentation/title pres)})
                                      [:div.text (f/to-html (:presentation/description pres))]]})
                         to-show-in-grid
                         ["video-thumb-rouge" "video-thumb-chocolate" "video-thumb-chocolate" "video-thumb-rouge"]
                         ["curtain curtain-short-right" nil nil "curtain curtain-short-top"])))
                     (when (seq remaining)
                       [:div
                        (for [pres remaining]
                          [:div.mbm
                           [:a.link {:href (presentation-uri pres)}
                            (:presentation/title pres)]
                           [:div
                            (e/people-tags {:class "tags"
                                            :people (:presentation/people pres)})]
                           [:div.text
                            (f/to-html (:presentation/description pres))]])])]
                    (remove nil?))}))

(defn merge-screencasts [screencasts]
  (-> (h/select-keys-by screencasts
                        {:screencast/title first
                         :screencast/illustration first
                         :screencast/url first
                         :screencast/published first
                         :list/idx first
                         :screencast/blurb (choose-and-change first
                                                              change-1st-person-to-3rd
                                                              :screencast/blurb
                                                              :person/_screencasts)
                         :screencast/description (choose-and-change first
                                                                    change-1st-person-to-3rd
                                                                    :screencast/description
                                                                    :person/_screencasts)})
      (assoc :screencast/people (keep :person/_screencasts screencasts))))

(defn names-sort-key [people]
  (->> people
       (map :person/given-name)
       sort
       str/join))

(defn screencasts-section [tech]
  (when-let [screencasts (->> (:screencast/_techs tech)
                              (group-by :screencast/url)
                              vals
                              (map merge-screencasts)
                              (sort-by (comp names-sort-key :screencast/people))
                              seq)]
    {:kind :titled
     :title "Våre screencasts"
     :contents (for [screencast screencasts]
                 (let [url (:screencast/url screencast)]
                   (e/illustrated-teaser
                    {:title (:screencast/title screencast)
                     :tags (e/people-tags {:prefix "Av"
                                           :people (:screencast/people screencast)
                                           :class "tags"})
                     :url url
                     :illustration (:screencast/illustration screencast)
                     :content (f/to-html (or (:screencast/description screencast)
                                             (:screencast/blurb screencast)))
                     :link (when url {:text "Se screencast" :href url})})))}))

(defn merge-side-projects [side-projects]
  (-> (h/select-keys-by side-projects
                        {:side-project/title first
                         :side-project/url first
                         :side-project/link-text first
                         :side-project/illustration first
                         :list/idx first
                         :side-project/description (choose-and-change first
                                                                      change-1st-person-to-3rd
                                                                      :side-project/description
                                                                      :person/_side-projects)})
      (assoc :side-project/people (keep :person/_side-projects side-projects))))

(defn side-projects-section [tech]
  (when-let [side-projects (->> (:side-project/_techs tech)
                                (group-by :side-project/url)
                                vals
                                (map merge-side-projects)
                                (sort-by (comp names-sort-key :side-project/people))
                                seq)]
    {:kind :titled
     :title "Våre sideprosjekter"
     :contents (for [side-project side-projects]
                 (let [url (:side-project/url side-project)]
                   (e/illustrated-teaser
                    {:title (:side-project/title side-project)
                     :tags (e/people-tags {:prefix "Av"
                                           :people (:side-project/people side-project)
                                           :class "tags"})
                     :url url
                     :illustration (:side-project/illustration side-project)
                     :content (f/to-html (:side-project/description side-project))
                     :link (when url {:text (:side-project/link-text side-project) :href url})})))}))

(defn merge-oss-projects [projects]
  (let [developers (keep :person/_open-source-projects projects)
        contributors (keep :person/_open-source-contributions projects)]
    (-> (h/select-keys-by projects
                          {:oss-project/url first
                           :oss-project/name first
                           :oss-project/description first
                           :list/idx first})
        (assoc :oss-project/developers (seq developers))
        (assoc :oss-project/people (concat developers contributors)))))

(defn format-oss-project [{:oss-project/keys [url name description]}]
  [:li.text.inline-text
   [:a {:href url} name]
   (some->> description f/to-html m/strip-paragraph (str " - "))])

(defn open-source-section [tech]
  (when-let [projects (->> (:oss-project/_techs tech)
                           (group-by :oss-project/url)
                           vals
                           (map merge-oss-projects)
                           seq)]
    {:kind :titled
     :title "Vår fri programvare"
     :contents [(e/teaser
                 {:content (let [projects (concat
                                           (sort-by :oss-project/name (filter :oss-project/description projects))
                                           (sort-by :oss-project/name (remove :oss-project/description projects)))
                                 developments (filter :oss-project/developers projects)]
                             [:ul.dotted.dotted-tight
                              (map format-oss-project developments)
                              (when-let [contributions (seq (remove :oss-project/developers projects))]
                                [:li.text.inline-text
                                 (format "Vi har%s bidratt til "
                                         (if (seq developments) " også" ""))
                                 (e/comma-separated
                                  (map oss/format-contribution contributions))])])})]}))

(defn blog-post-section [tech]
  (when-let [blog-posts (->> (:blog-post/_techs tech)
                             (group-by blog/post-url)
                             vals
                             (apply concat)
                             (sort-by :blog-post/published)
                             reverse
                             seq)]
    {:kind :titled
     :title "Våre blogginnlegg"
     :contents (cond->
                (for [post (take 3 blog-posts)]
                  (let [url (blog/post-url post)]
                    (e/teaser
                     {:title (:blog-post/title post)
                      :tags (e/people-tags {:prefix "Av"
                                            :people [(blog/author post)]
                                            :class "tags"})
                      :url url
                      :content (f/to-html (:blog-post/blurb post))})))
                 (< 3 (count (filter :page/uri blog-posts)))
                 (concat [(let [url (format "/blogg/%s/" (name (:db/ident tech)))]
                            (e/teaser
                             {:url url
                              :link {:text (format "Se flere skriverier om %s" (:tech/name tech))
                                     :href url}}))]))}))

(defn people-section [tech db]
  (when-let [people
             (->> (concat
                   (->> (:project/_techs tech)
                        (map :person/_projects)
                        (map :db/ident))
                   (->> (:side-project/_techs tech)
                        (map :person/_side-projects)
                        (map :db/ident))
                   (->> (:presentation/_techs tech)
                        (map :person/_presentations)
                        (map :db/ident))
                   (->> (:screencast/_techs tech)
                        (map :person/_screencasts)
                        (map :db/ident))
                   (->> (:oss-project/_techs tech)
                        (map :person/_open-source-projects)
                        (map :db/ident))
                   (->> (:presentation-product/_techs tech)
                        (map :person/_workshops)
                        (map :db/ident))
                   (->> (:presentation-product/_techs tech)
                        (map :person/_business-presentations)
                        (map :db/ident))
                   (->> (:blog-post/_techs tech)
                        (map :blog-post/author)))
                  (remove nil?)
                  frequencies
                  (sort-by (comp - second))
                  (map first)
                  (map #(d/entity db %))
                  (remove (or (:person/_exclude-techs tech) :person/quit?))
                  seq)]
    {:kind :titled
     :title (if (< 1 (count people)) (format "Spør oss om %s" (:tech/name tech)) (format "Spør meg om %s" (:tech/name tech)))
     :contents (if (> 20 (count people))
                 [(e/round-card-grid (for [person people]
                                       {:content
                                        (e/centered-vert-round-media
                                         {:image (str "/vcard-small" (first (:person/portraits person)))
                                          :href (:page/uri person)
                                          :lines [(:person/given-name person)]})}))]
                 [(e/teaser {:link {:text "Stort sett alle"
                                    :href "/folk/"}})])}))

(defn create-page [db tech]
  (let [presentations (classify-presentations tech)]
    {:title (:tech/name tech)
     :sections
     (->>
      [{:kind :header :background :chablis}
       {:kind :tech-intro
        :title (:tech/name tech)
        :logo (:tech/illustration tech)
        :article {:content [:div.text
                            (f/to-html (:tech/description tech))]
                  :alignment :front
                  :aside (when-let [headliner (:headliner presentations)]
                           [:div.hide-below-1000
                            (e/video-thumb
                             {:img (str "/rouge-duotone" (:presentation/thumb headliner))
                              :tags (e/people-tags {:prefix "Av"
                                                    :class "tags"
                                                    :people (:presentation/people headliner)})
                              :url (presentation-uri headliner)
                              :title (:presentation/title headliner)})])}
        :pønt [{:kind :greater-than
                :position "top -410px right 60vw"}
               {:kind :dotgrid
                :position "top -110px left 80vw"}]}
       (recommendations-section tech)
       (presentations-section presentations)
       (screencasts-section tech)
       (side-projects-section tech)
       (open-source-section tech)
       (blog-post-section tech)
       (people-section tech db)
       {:kind :footer}]
      (remove nil?)
      (map (fn [color section]
             (assoc section :background color))
           (cycle [:chablis :blanc])))}))

(comment

  (require '[datomic-type-extensions.api :as d])
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))
  (def tech (d/entity db :tech/clojure))

  (screencasts-section tech)

  (into {} (second (:screencast/_techs tech)))

  (->> (:blog-post/_techs tech)
       (group-by blog/post-url)
       vals
       (apply concat)
       (sort-by :blog-post/published)
       reverse
       seq
       first
       (into {}))

  (create-page tech)

  (->> (:presentation/_techs tech)
       (group-by presentation-uri)
       vals
       (map merge-presentations))

  (->>
   (for [[e file] (d/q '[:find ?e ?file
                         :where
                         [?e :presentation/video-url _ ?tx]
                         [?tx :tx-source/file-name ?file]] db)]
     (let [pres (d/entity db e)]
       [file
        (:presentation/video-url pres)
        (:presentation/thumb pres)
        (:presentation/title pres)]))
   (group-by first)
   (map-vals #(mapv (comp vec next) %))))
