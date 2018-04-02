(ns kodemaker-no.pages.cv-pages
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [kodemaker-no.formatting :as f]))

(defn- section [header content]
  [:section
   [:header header]
   content
   [:hr]])

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

(defn- render-tech [{:keys [label techs]}]
  (list [:dt label]
        [:dd (->> techs
                  (map :name)
                  (str/join ", "))]))

(defn- prep-tech [all-techs tech-type]
  (when-let [techs (tech-type all-techs)]
    {:label (tech-labels tech-type)
     :techs techs}))

(defn- technologies [techs]
  (section
   "Teknologi"
   [:dl
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

(defn- table [items headings columns]
  [:table
   [:thead [:tr (map (fn [h] [:th h]) headings)]]
   [:tbody
    (map (fn [item]
           [:tr
            (map (fn [col] [:td (col item)]) columns)])
         items)]])

(defn- table-section [header items headings columns]
  (when (< 0 (count items))
    (section header
             (table items headings columns))))

(defn- list-section [header items render-item]
  (when (< 0 (count items))
    (section header
             [:ul
              (map (fn [item] [:li {} (render-item item)]) items)])))

(defn- render-projects [employments [employer projects]]
  (list
   [:h3 (:name employer)]
   (when-let [employment (and (:id employer) ((:id employer) employments))]
     [:p (:description employment)])
   (table projects
          ["Oppdragsgiver" "Periode" "Oppdrag"]
          [:customer
           year-range
           (fn [{:keys [description tech]}]
             (list (f/to-html description)
                   [:div {:style "margin: 1em 0"}
                    [:strong "Teknologi: "]
                    (->> tech (map :name) (str/join ", "))]))])))

(defn- projects-fullview [person]
  (when (< 0 (count (:projects person)))
    (section "Prosjekter"
             (->> (:projects person)
                  (filter :description)
                  (group-by :employer)
                  (map #(render-projects (:employments person) %))))))

(defn- render-open-source-contributions [[lang contributions]]
  (list
   [:h3 lang]
   [:ul.open-source
    (list
     (->> contributions
          (filter #(= :developer (:role %)))
          (map (fn [{:keys [url name description]}]
                 [:li "Utviklet " [:a {:href url} name] ". " (f/to-html description)])))
     (let [contribs (filter #(= :contributer (:role %)) contributions)]
       (when (< 0 (count contribs))
         [:li "Har bidratt til "
          (interpose ", "
                     (map (fn [{:keys [url name]}]
                            [:a {:href url} name]) contribs))])))]))

(defn- open-source-contributions [{:keys [open-source-contributions]}]
  (when (< 0 (count open-source-contributions))
    (section "Bidrag til open source"
             (map render-open-source-contributions open-source-contributions))))

(defn- short-fact [person attr label]
  (when-let [val (attr person)]
    (list [:dt label]
          [:dd val])))

(defn- endorsements [{:keys [endorsements]}]
  (when (< 0 (count endorsements))
    (section "Anbefalinger"
             (map (fn [{:keys [author title quote]}]
                    (list [:h3 author (when title (list ", " title))]
                          [:blockquote quote]))
                  endorsements))))

(defn linked-title [{:keys [title urls]}]
  (if-let [url (or (:video urls) (:slides urls) (:source urls))]
    [:a {:href url} title]
    title))

(defn certification-name [{:keys [name url]}]
  (if url
    (f/to-html [:a {:href url} name])
    (f/to-html name)))

(defn certificate [{:keys [certificate]}]
  (when certificate
    [:a {:href (:url certificate)} (or (:text certificate) "Kurssertifikat")]))

(defn- cv-page [person]
  {:title (format "%s CV" (:full-name person))
   :layout :cv
   :body
   (list [:header
          [:section#card
           [:section#logo
            [:img.favicon {:src "/images/cv/favicon-pointed-right.png"}]
            [:img.kodemaker {:src "/images/cv/kodemaker.png"}]]
           [:section#info
            [:div#name (:full-name person)]
            [:div#title (:title person)]
            [:div#phone (:phone-number person)]
            [:div#mail (:email-address person)]]]
          [:section#picture
           [:img {:height "190"
                  :src (format "/photos/people/%s/side-profile-cropped.jpg" (:str person))}]]
          [:section#personal
           [:dl
            (short-fact person :born "Født")
            (short-fact person :relationship-status "Sivilstatus")
            (short-fact person :education-summary "Utdanning")
            (short-fact person :experience-since "Erfaring")]]]
         [:article
          (section "Sammendrag" (f/to-html (:description person)))
          (technologies (:techs person))
          (when (< 0 (count (:qualifications person)))
            (section "Kvalifikasjoner"
                     [:ul (map #(vector :li %) (:qualifications person))]))
          (table-section "Prosjekter, sammendrag"
                         (:projects person)
                         ["Oppdragsgiver" "Periode" "Oppdrag"]
                         [:customer year-range :summary])
          (projects-fullview person)
          (table-section "Foredrag/kurs"
                         (:appearances person)
                         ["Navn" "Sted" "Når"]
                         [linked-title :event :year-month])
          (open-source-contributions person)
          (list-section "Andre faglige bidrag"
                        (:other person)
                        (fn [{:keys [title url summary]}]
                          (if url
                            (list [:a {:href url} title] " - " (f/to-html summary))
                            (list (when title (str title " - ")) (f/to-html summary)))))
          (table-section "Utdanning"
                         (:education person)
                         ["Skole" "År" "Retning"]
                         [:institution year-range :subject])
          (table-section "Sertifiseringer/ kurs"
                         (:certifications person)
                         ["Kursnavn" "År"]
                         [certification-name :year certificate])
          (table-section "Domenekunnskap"
                         (:domain-skills person)
                         ["Område" "Kjennskap"]
                         [:title :description])
          (table-section "Språk"
                         (:languages person)
                         ["Språk" "Muntlig" "Skriftlig"]
                         [:language :orally :written])
          (endorsements person)])})

(defn cv-pages [cvs]
  (->> cvs
       (map (juxt :url #(partial cv-page %)))
       (into {})))
