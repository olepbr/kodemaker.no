(ns kodemaker-no.ingestion.person
  (:require [clojure.string :as str]
            [kodemaker-no.homeless :refer [map-vals parse-local-date parse-local-date-time
                                           select-renamed-keys update-in-existing
                                           prep-techs qualify]]
            [kodemaker-no.ingestion.video :as video]
            [clojure.java.io :as io]))

(def person-keys
  {:cv/description :cv/description
   :title :person/title
   :start-date :person/start-date
   :description :person/description
   :administration? :person/administration?
   :phone-number :person/phone-number
   :email-address :person/email-address
   :presence :person/presence
   :recommendations :person/recommendations
   :hobbies :person/hobbies
   :side-projects :person/side-projects
   :screencasts :person/screencasts
   :open-source-projects :person/open-source-projects
   :open-source-contributions :person/open-source-contributions
   :projects :person/projects
   :endorsements :person/endorsements
   :experience-since :person/experience-since
   :qualifications :person/qualifications
   :innate-skills :person/innate-skills
   :employments :person/employments
   :education :person/education
   :languages :person/languages
   :project-highlights :person/project-highlights
   :endorsement-highligh :person/endorsement-highligh
   :business-presentations :person/business-presentations
   :workshops :person/workshops})

(defn url [file-name]
  (str (second (re-find #"people(.*).edn" file-name)) "/"))

(defn maybe-pagify [person file-name]
  (if (get person :person/profile-active?)
    (-> person
        (assoc :page/uri (url file-name))
        (assoc :page/kind :page.kind/profile))
    person))

(def presentation-keys
  {:id :db/ident
   :title :presentation/title
   :description :presentation/description
   :blurb :presentation/description
   :tech :presentation/tech
   :event :presentation/event-name
   :date :presentation/date
   :direct-link? :presentation/direct-link?
   :url :presentation/source-url
   :thumb :presentation/thumb})

(def presentation-url-keys
  {:video :presentation/video-url
   :slides :presentation/slides-url
   :source :presentation/source-url})

(def location-keys
  {:title :presentation/event-name
   :url :presentation/event-url})

(def call-to-action-keys
  {:text :presentation/call-to-action-text
   :url :presentation/call-to-action-url})

(defn presentation-data [presentation]
  (-> presentation
      (select-renamed-keys presentation-keys)
      (merge
       (select-renamed-keys (:urls presentation) presentation-url-keys)
       (select-renamed-keys (:location presentation) location-keys)
       (select-renamed-keys (:call-to-action presentation) call-to-action-keys))
      (update-in-existing [:presentation/tech] prep-techs)
      (update-in-existing [:presentation/date] parse-local-date)))

(def screencast-keys
  {:title :screencast/title
   :blurb :screencast/blurb
   :description :screencast/description
   :illustration :screencast/illustration
   :tech :screencast/tech
   :published :screencast/published
   :url :screencast/url
   :cv/blurb :cv/blurb})

(defn screencast-data [screencast]
  (-> screencast
      (update-in-existing [:published] parse-local-date)
      (update-in-existing [:tech] prep-techs)
      (select-renamed-keys screencast-keys)))

(def tech-keys
  {:using-at-work :person/using-at-work
   :favorites-at-the-moment :person/favorites-at-the-moment
   :want-to-learn-more :person/want-to-learn-more})

(defn project-data [project]
  (-> project
      (update-in-existing [:employer] #(qualify "employer" %))
      (update-in-existing [:tech] prep-techs)
      (update-in-existing [:start] #(parse-local-date (str % "-01")))
      (update-in-existing [:end] #(parse-local-date (str % "-01")))))

(defn data-with-tech [xs]
  (mapv #(update-in-existing % [:tech] prep-techs) xs))

(def presentation-product-keys
  {:title :presentation-product/title
   :description :presentation-product/description
   :tech :presentation-product/tech
   :duration :presentation-product/duration
   :min-participants :presentation-product/min-participants
   :max-participants :presentation-product/max-participants})

(defn presentation-product-data [kind presentation]
  (-> presentation
      (select-renamed-keys presentation-product-keys)
      (update-in-existing [:presentation-product/tech] prep-techs)
      (assoc :presentation-product/kind kind)))

(def side-project-keys
  {:title :side-project/title
   :description :side-project/description
   :illustration :side-project/illustration
   :tech :side-project/tech})

(defn side-project-data [side-project]
  (-> side-project
      (select-renamed-keys side-project-keys)
      (update-in-existing [:side-project/tech] prep-techs)
      (cond-> (:link side-project)
        (assoc :side-project/url (-> side-project :link :url)
               :side-project/link-text (-> side-project :link :text)))))

(def recommendation-keys
  {:title :recommendation/title
   :blurb :recommendation/description
   :url :recommendation/url
   :link-text :recommendation/link-text
   :tech :recommendation/tech})

(defn recommendation-data [idx recommendation]
  (-> recommendation
      (update-in-existing [:tech] prep-techs)
      (select-renamed-keys recommendation-keys)
      (assoc :list/idx idx)
      (cond-> (:link recommendation)
        (assoc :recommendation/url (-> recommendation :link :url)
               :recommendation/link-text (-> recommendation :link :text)))))

(defn profile-data [file-name person]
  (let [ident (qualify "person" (:id person))
        presentations (concat (:presentations person)
                              (:upcoming person)
                              (:appearances person))]
    (-> person
        (select-renamed-keys person-keys)
        (assoc :db/ident ident)
        (assoc :person/presentations (mapv presentation-data presentations))
        (update-in-existing [:person/screencasts] #(mapv screencast-data %))
        (update-in-existing [:person/projects] #(mapv project-data %))
        (update-in-existing [:person/open-source-projects] data-with-tech)
        (update-in-existing [:person/open-source-contributions] data-with-tech)
        (update-in-existing [:person/side-projects] (partial mapv #(side-project-data %)))
        (update-in-existing [:person/recommendations] (partial map-indexed #(recommendation-data %1 %2)))
        (update-in-existing [:person/business-presentations] (partial mapv #(presentation-product-data :presentation %)))
        (update-in-existing [:person/workshops] (partial mapv #(presentation-product-data :workshop %)))
        (update-in-existing [:person/start-date] parse-local-date-time)
        (update-in-existing [:person/innate-skills] prep-techs)
        (update-in-existing [:person/experience-since] str)
        (assoc :person/given-name (first (:name person)))
        (assoc :person/family-name (last (:name person)))
        (assoc :person/full-name (str/join " " (:name person)))
        (assoc :person/profile-active? (get person :profile-active? true))
        (assoc :person/quit? (get person :quit? false))
        (merge (map-vals prep-techs (select-renamed-keys (:tech person) tech-keys)))
        (maybe-pagify file-name))))

(def cv-keys
  {:preferred-techs :cv/preferred-techs
   :exclude-techs :cv/exclude-techs})

(defn cv-data [file-name person]
  (when (and (not (:administration? person))
             (not (:quit? person)))
    [(merge {:page/uri (str "/cv" (url file-name))
             :page/kind :page.kind/cv}
            (-> (:cv person)
                (select-renamed-keys cv-keys)
                (update-in-existing [:cv/preferred-techs] prep-techs)
                (update-in-existing [:cv/exclude-techs] prep-techs)))]))

(def blog-post-keys
  {:url :blog-post/external-url
   :title :blog-post/title
   :blurb :blog-post/blurb
   :published :blog-post/published
   :tech :blog-post/tech
   :cv/blurb :cv/blurb})

(defn blog-post-data [author-id blog-post]
  (-> blog-post
      (select-renamed-keys blog-post-keys)
      (assoc :blog-post/author {:db/ident author-id})
      (update-in-existing [:blog-post/published] parse-local-date)
      (update-in-existing [:blog-post/tech] prep-techs)))

(defn profile-pics [{:keys [id]}]
  (some->> (str "public/foto/profiles/" (name id))
           io/resource
           io/as-file
           file-seq
           (map #(.getPath %))
           (filter #(re-find #"\.jpg$" %))
           (map #(second (str/split % #"public")))))

(defn create-tx [file-name person]
  (let [person-ident (qualify "person" (:id person))
        profile (profile-data file-name person)]
    (concat
     [(let [pics (profile-pics person)]
        (cond-> profile
          (seq pics) (assoc :person/profile-pictures (vec pics))))]
     (cv-data file-name person)
     (map (partial blog-post-data person-ident) (:blog-posts person))
     (keep (partial video/video-data person-ident) (:person/presentations profile)))))

(comment
  (create-tx "people/magnar.edn" (read-string (slurp (clojure.java.io/resource "people/magnar.edn"))))

  (require '[datomic.api :as d])
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  (d/touch (first (:person/recommendations (d/entity db :person/odin))))

  )
