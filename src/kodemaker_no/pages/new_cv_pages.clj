(ns kodemaker-no.pages.new-cv-pages
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [kodemaker-no.formatting :as f]))

(defn- project-highlight [{:keys [blurb logo logo-width logo-height customer]}]
  [:div.grey-box
   [:div.bd
    [:div [:strong customer]]
    [:div.content blurb]]])

(def tech-labels
  {:proglang "Programmeringsspråk"
   :devtools "Utviklingsverktøy"
   :vcs "Versjonskontroll"
   :methodology "Metodikk"
   :os "Operativsystem"
   :database "Database"
   :devops "Devops"
   :cloud "Skytjenester"
   :security "Sikkerhet"
   :tool "Verktøy"
   :frontend "Frontend"})

(def tech-order
  [:proglang :devtools :vcs :methodology :os :database :devops :cloud :security :tool :frontend])

(defn- prep-tech [all-techs tech-type]
  (when-let [techs (tech-type all-techs)]
    {:label (tech-labels tech-type)
     :techs techs}))

(defn- render-tech [{:keys [label techs]}]
  (list [:dt label]
        [:dd (->> techs
                  (map :name)
                  (str/join ", "))]))

(defn- section [title id & content]
  (apply vector
         :div {:id id :class "print-block"}
         [:h2.mhn title]
         content))

(defn- technologies [{:keys [techs]}]
  (section "Teknologi" "technology"
           [:dl.dtable
            (->> tech-order
                 (map #(prep-tech techs %))
                 (filter identity)
                 (map render-tech))
            (render-tech {:label "Annet"
                          :techs (->> (set (keys tech-labels))
                                      (set/difference (set (keys techs)))
                                      (select-keys techs)
                                      vals
                                      (apply concat)
                                      (filter :type))})]))

(defn- year-range [{:keys [years start end]}]
  (if start
    (str (f/year-month-str start) " - " (f/year-month-str end))
    (f/year-range years)))

(defn- render-projects [employments [employer projects]]
  [:div.mod
   [:table.listing.padded
    [:thead
     [:tr
      [:th.tr [:h3 "Arbeidsgiver:"]]
      [:th [:h3 (:name employer)]]]
     (when-let [employment (and (:id employer) ((:id employer) employments))]
       [:tr
        [:td]
        [:td [:p.smaller (:description employment)]]])]
    [:tbody
     (map (fn [project]
            [:tr
             [:th.nw
              [:h4 (:customer project)]
              [:p (year-range project)]]
             [:td.fw
              [:h4 (:summary project)]
              (f/to-html (:description project))
              [:div.smaller.mbxl
               [:span "■ "]
               (->> project :tech (map :name) (str/join ", "))]]])
          projects)]]])

(defn- projects [{:keys [projects employments]}]
  (when (< 0 (count projects))
    (section "Prosjekter"
             "projects"
             (->> projects
                  (group-by :employer)
                  (map #(render-projects employments %))))))

(defn- endorser [{:keys [author title]}]
  (str/join ", " (concat [author] (when title [title]))))

(defn- render-endorsement [endorsement]
  [:div
   [:h3 (endorser endorsement)]
   [:div.mod
    [:blockquote (:quote endorsement)]]])

(defn- endorsements [{:keys [endorsements] :as person}]
  (when (< 0 (count endorsements))
    (section "Anbefalinger"
             "endorsements"
             (map render-endorsement endorsements))))

(defn- table-rows [items columns & [classes]]
  (let [classes (or classes [])]
    (map (fn [item]
           [:tr
            (map-indexed (fn [idx col]
                           [:td {:class (first (drop idx classes))} (col item)]) columns)])
         items)))

(defn- certification-detail [{:keys [name url certificate]}]
  (apply vector :div
         (if url
           [:a {:href url} name]
           name)
         (when certificate
           (list " / " [:a {:href (:url certificate)}
                       (or (:text certificate) "Kursbevis")]))))

(defn- education-label [{:keys [education certifications]}]
  (if (< 0 (count certifications))
    "Utdanning, sertifiseringer og kurs"
    "Utdanning"))

(defn- education [{:keys [education certifications] :as person}]
  (when (< 0 (+ (count education) (count certifications)))
    (section (education-label person)
             "education"
             [:div.mod
              [:table.table.padded
               [:tbody
                (table-rows certifications [:year :institution certification-detail] ["nw"])
                (table-rows education [year-range :institution :subject] ["nw"])]]])))

(defn- languages [{:keys [languages]}]
  (when (< 0 (count languages))
    (section "Språk"
             "languages"
             [:div.mod
              [:table.padded
               [:thead
                [:tr
                 [:th "Språk"]
                 [:th "Muntlig"]
                 [:th "Skriftlig"]]]
               [:tbody
                (table-rows languages [:language :orally :written])]]])))

(defn- linked-title [{:keys [title urls]}]
  (if-let [url (or (:video urls) (:slides urls) (:source urls))]
    [:a {:href url} title]
    title))

(defn- appearances [{:keys [appearances]}]
  (when (< 0 (count appearances))
    (section "Foredrag/kurs"
             "appearances"
             [:div.mod
              [:table.padded
               [:tbody
                (table-rows appearances
                            [:year-month-numeric linked-title :event]
                            [nil "nw"])]]])))

(defn- render-open-source-contributions [[lang contributions]]
  (list
   [:h3 lang]
   [:ul.open-source
    (list
     (->> contributions
          (filter #(= :developer (:role %)))
          (map (fn [{:keys [url name description]}]
                 [:li (f/to-html (format "Utviklet [%s](%s). %s" name url description))])))
     (let [contribs (filter #(= :contributer (:role %)) contributions)]
       (when (< 0 (count contribs))
         [:li "Har bidratt til "
          (interpose ", "
                     (map (fn [{:keys [url name]}]
                            [:a {:href url} name]) contribs))])))]))

(defn- open-source-contributions [{:keys [open-source-contributions]}]
  (when (< 0 (count open-source-contributions))
    (section "Bidrag til open source"
             "open-source-contributions"
             (map render-open-source-contributions open-source-contributions))))

(defn- other-contributions [{:keys [other]}]
  (when (< 0 (count other))
    (section "Andre faglige bidrag"
             "other"
             (map (fn [{:keys [title url summary]}]
                    [:div
                     [:h5 (if url
                            [:a {:href url} title]
                            title)]
                     (f/to-html summary)]) other))))

(defn- anchors [person]
  (concat
   [["#projects" "Prosjekter"]
    ["#endorsements" "Anbefalinger"]
    ["#technology" "Teknologi"]
    ["#education" (education-label person)]]
   (when (< 0 (count (:appearances person)))
     [["#appearances" "Foredrag/kurs"]])
   (when (< 0 (count (:open-source-contributions person)))
     [["#open-source-contributions" "Bidrag til open source"]])))

(defn- cv-page [person]
  {:title (format "%s CV" (:full-name person))
   :layout :new-cv
   :body
   (list [:div#cv-header.bd.rel
          [:p.mvn.picture
           [:img.image {:src (format "/photos/people/%s/side-profile-cropped.jpg" (:str person))}]]
          [:div.heading
           [:h1.hn [:span.black.hide-lt-460-il "CV / "] (f/no-widows (:full-name person))]]
          [:p.summary
           (:phone-number person)
           [:br]
           [:a {:href (format "mailto:%s" (:email-address person))} (:email-address person)]]]
         [:hr.mtn]
         [:h3.bigger
          (format "%s med %s års erfaring" (:title person)
            (let [years (:years-experience person)]
              (if (<= years 30) years "mange")))]
         [:ul.spacey
          (map #(vector :li %) (:qualifications person))]

         (when (< 0 (count (:project-highlights person)))
           [:div.flex-l.tc.mtl.mod
            (map project-highlight (:project-highlights person))])

         [:div.mod.mbl.show-lt-810 [:hr]]

         (if-let [endorsement (:endorsement-highlight person)]
           [:div.bc.flex.mod
            [:div.f2o3
             [:blockquote
              [:div.mbm (:quote endorsement)]
              [:div.smaller [:strong (endorser endorsement)]]]]
            [:div.f1o3.noprint.hide-lt-460
             [:p
              (map #(vector :div.tr [:a {:href (first %)} (second %)]) (anchors person))]]]
           [:p.spread.noprint.hide-lt-460 (interpose " " (map #(vector :a {:href (first %)} (second %)) (anchors person)))])

         [:div#about.print-block
          [:h2.mhn "Om " (:first-name person)]
          (f/to-html (:description person))]

         (technologies person)
         (projects person)
         (endorsements person)
         (education person)
         (languages person)
         (appearances person)
         (open-source-contributions person)
         (other-contributions person))})

(defn new-cv-pages [cvs]
  (->> cvs
       (map (juxt (comp #(str "/ny" %) :url) #(partial cv-page %)))
       (into {})))
