(ns kodemaker-no.new-pages.cv-page
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.new-pages.person :as person]
            [ui.elements :as e]))

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

(defn preferred-techs [cv]
  (->> (:cv/preferred-techs cv)
       (sort-by :list/idx)
       (map :list/ref)))

(defn prefer-techs [preferred techs]
  (if (seq preferred)
    (let [pref-count (count preferred)]
      (sort-by (fn [tech]
                 (let [idx (.indexOf preferred tech)]
                   ;; If there is a preference for this tech, use it's index in
                   ;; the sorted list of preferences. Otherwise, return the
                   ;; number of preferences, which will keep the prior sort
                   ;; order for techs for which there is no preference, and
                   ;; place them all after the preferred techs.
                   (if (<= 0 idx)
                     idx
                     pref-count))) techs))
    techs))

(defn all-techs [db cv person]
  (->> (concat (:person/using-at-work person)
               (:person/innate-skills person)
               (side-project-techs person)
               (blog-post-techs person)
               (screencast-techs person)
               (presentation-techs person)
               (open-source-techs person)
               (project-techs person))
       (remove (or (:cv/exclude-techs cv) #{}))
       frequencies
       (sort-by (comp - second))
       (map first)
       (prefer-techs (preferred-techs cv))
       (map #(d/entity db %))
       (group-by :tech/type)))

(comment
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  (def person (d/entity db :person/christian))
  (def cv (:cv/_person person))

  (->> (d/db conn)
       (d/q '[:find ?e .
              :in $
              :where
              [?e :page/uri "/cv/christian/"]])
       (d/entity db)
       :cv/preferred-techs
       (sort-by :list/idx)
       (map :list/ref))

  (all-techs db cv person))

(defn- prep-tech [all-techs tech-type]
  (when-let [techs (get all-techs tech-type)]
    {:title (get tech-labels tech-type)
     :contents [[:p.text (e/enumerate-techs techs)]]}))

(defn technology-section [cv person]
  (let [techs (all-techs (d/entity-db cv) cv person)]
    {:kind :definitions
     :title "Teknologi"
     :id "technology"
     :definitions (->> tech-order
                       (map #(prep-tech techs %))
                       (filter identity))}))

(defn create-page [cv]
  (let [person (cv-profile cv)]
    {:sections
     [{:kind :cv-intro
       :image (h/profile-picture person)
       :friendly-name (:person/given-name person)
       :full-name (:person/full-name person)
       :title (:person/title person)
       :contact-lines [(:person/phone-number person)
                       (:person/email-address person)]
       :links (person/prep-presence-links (:person/presence person))
       :experience (format "%s med %s erfaring" (:person/title person) (years-of-experience person))
       :qualifications (:person/qualifications person)
       :quote (endorsement-highlight person)
       :description (f/to-html (:person/description person))
       :highlights (map project-highlight (:person/project-highlights person))}
      (technology-section cv person)
      {:kind :footer}]}))

