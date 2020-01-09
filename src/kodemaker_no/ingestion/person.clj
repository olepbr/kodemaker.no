(ns kodemaker-no.ingestion.person
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.ingestion.video :as video]))

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
   :endorsement-highlight :person/endorsement-highlight
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
   :tech :presentation/techs
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
      (h/select-renamed-keys presentation-keys)
      (merge
       (h/select-renamed-keys (:urls presentation) presentation-url-keys)
       (h/select-renamed-keys (:location presentation) location-keys)
       (h/select-renamed-keys (:call-to-action presentation) call-to-action-keys))
      (h/update-in-existing [:presentation/techs] h/prep-techs)
      (h/update-in-existing [:presentation/date] h/parse-local-date)))

(def screencast-keys
  {:title :screencast/title
   :blurb :screencast/blurb
   :description :screencast/description
   :illustration :screencast/illustration
   :tech :screencast/techs
   :published :screencast/published
   :url :screencast/url
   :cv/blurb :cv/blurb})

(defn screencast-data [screencast]
  (-> screencast
      (h/update-in-existing [:published] h/parse-local-date)
      (h/update-in-existing [:tech] h/prep-techs)
      (h/select-renamed-keys screencast-keys)))

(def tech-keys
  {:using-at-work :person/using-at-work
   :favorites-at-the-moment :person/favorites-at-the-moment
   :want-to-learn-more :person/want-to-learn-more})

(def project-keys
  {:customer :project/customer
   :summary :project/summary
   :employer :project/employer
   :description :project/description
   :exclude-from-profile? :project/exclude-from-profile?
   :years :project/years
   :start :project/start
   :end :project/end
   :tech :project/techs
   :cv/description :cv/description})

(defn year-range [start end]
  (let [end (if (= :ongoing end)
              (java.time.LocalDate/now)
              end)]
    (cond
      (and (nil? start) (nil? end)) nil
      (or (nil? start) (nil? end)) [(.getYear (or start end))]
      :default (range (.getYear start) (inc (.getYear end))))))

(defn possibly-infer-years [project]
  (if (nil? (:project/years project))
    (assoc project :project/years (year-range (:project/start project) (:project/end project)))
    project))

(defn project-data [project]
  (-> project
      (h/update-in-existing [:employer] (fn [employer] {:db/ident (h/qualify "employer" employer)}))
      (h/update-in-existing [:tech] h/prep-techs)
      (h/update-in-existing [:start] #(h/parse-local-date (str % "-01")))
      (h/update-in-existing [:end] #(h/parse-local-date (str % "-01")))
      possibly-infer-years
      (h/select-renamed-keys project-keys)))

(def open-source-keys
  {:url :oss-project/url
   :name :oss-project/name
   :description :oss-project/description
   :tech :oss-project/techs})

(defn open-source-project [project]
  (-> project
      (h/update-in-existing [:tech] h/prep-techs)
      (h/select-renamed-keys open-source-keys)))

(def presentation-product-keys
  {:title :presentation-product/title
   :description :presentation-product/description
   :tech :presentation-product/techs
   :duration :presentation-product/duration
   :min-participants :presentation-product/min-participants
   :max-participants :presentation-product/max-participants})

(defn presentation-product-data [kind presentation]
  (-> presentation
      (h/select-renamed-keys presentation-product-keys)
      (h/update-in-existing [:presentation-product/techs] h/prep-techs)
      (assoc :presentation-product/kind kind)))

(def side-project-keys
  {:title :side-project/title
   :description :side-project/description
   :illustration :side-project/illustration
   :tech :side-project/techs})

(defn side-project-data [side-project]
  (-> side-project
      (h/select-renamed-keys side-project-keys)
      (h/update-in-existing [:side-project/techs] h/prep-techs)
      (cond-> (:link side-project)
        (assoc :side-project/url (-> side-project :link :url)
               :side-project/link-text (-> side-project :link :text)))))

(def recommendation-keys
  {:title :recommendation/title
   :blurb :recommendation/description
   :url :recommendation/url
   :link-text :recommendation/link-text
   :tech :recommendation/techs})

(defn recommendation-data [idx recommendation]
  (-> recommendation
      (h/update-in-existing [:tech] h/prep-techs)
      (h/select-renamed-keys recommendation-keys)
      (assoc :list/idx idx)
      (cond-> (:link recommendation)
        (assoc :recommendation/url (-> recommendation :link :url)
               :recommendation/link-text (-> recommendation :link :text)))))

(defn profile-data [file-name person]
  (let [ident (h/qualify "person" (:id person))
        presentations (concat (:presentations person)
                              (:upcoming person)
                              (:appearances person))]
    (-> person
        (h/select-renamed-keys person-keys)
        (assoc :db/ident ident)
        (assoc :person/presentations (mapv presentation-data presentations))
        (h/update-in-existing [:person/screencasts] #(mapv screencast-data %))
        (h/update-in-existing [:person/projects] #(mapv project-data %))
        (h/update-in-existing [:person/open-source-projects] #(map open-source-project %))
        (h/update-in-existing [:person/open-source-contributions] #(map open-source-project %))
        (h/update-in-existing [:person/side-projects] (partial mapv #(side-project-data %)))
        (h/update-in-existing [:person/recommendations] (partial map-indexed #(recommendation-data %1 %2)))
        (h/update-in-existing [:person/business-presentations] (partial mapv #(presentation-product-data :presentation %)))
        (h/update-in-existing [:person/endorsements] #(mapv (fn [idx item] (assoc item :list/idx idx)) (range) %))
        (h/update-in-existing [:person/workshops] (partial mapv #(presentation-product-data :workshop %)))
        (h/update-in-existing [:person/start-date] h/parse-local-date-time)
        (h/update-in-existing [:person/innate-skills] h/prep-techs)
        (h/update-in-existing [:person/experience-since] str)
        (h/update-in-existing [:person/experience-since] #(Integer/parseInt %))
        (assoc :person/given-name (first (:name person)))
        (assoc :person/family-name (last (:name person)))
        (assoc :person/full-name (str/join " " (:name person)))
        (assoc :person/profile-active? (get person :profile-active? true))
        (assoc :person/quit? (get person :quit? false))
        (merge (h/map-vals h/prep-techs (h/select-renamed-keys (:tech person) tech-keys)))
        (maybe-pagify file-name))))

(def cv-keys
  {:preferred-techs :cv/preferred-techs
   :exclude-techs :cv/exclude-techs})

(defn prep-tech-preferences [techs]
  (->> techs
       h/prep-techs
       (map-indexed (fn [idx tech]
                      {:list/idx idx
                       :list/ref tech}))))

(defn cv-data [file-name person profile]
  (when (and (not (:administration? person))
             (not (:quit? person)))
    [(merge {:page/uri (str "/cv" (url file-name))
             :page/kind :page.kind/cv
             :cv/person (select-keys profile [:db/ident])}
            (select-keys person [:cv/description])
            (-> (get-in person [:cv :default])
                (h/select-renamed-keys cv-keys)
                (h/update-in-existing [:cv/preferred-techs] prep-tech-preferences)
                (h/update-in-existing [:cv/exclude-techs] h/prep-techs)))]))

(def blog-post-keys
  {:blog-post/external-url :url
   :blog-post/title :title
   :blog-post/blurb :blurb
   :blog-post/published :published
   :blog-post/techs :tech
   :blog-post/tech-list :tech
   :cv/blurb :cv/blurb})

(defn blog-post-data [author-id blog-post]
  (-> blog-post
      (h/keep-vals blog-post-keys)
      (assoc :blog-post/author {:db/ident author-id})
      (h/update-in-existing [:blog-post/published] h/parse-local-date)
      (h/update-in-existing [:blog-post/techs] h/prep-techs)
      (h/update-in-existing [:blog-post/tech-list] h/prep-tech-list)))

(defn profile-pics [{:keys [id]}]
  (some->> (str "public/foto/profiles/" (name id))
           io/resource
           io/as-file
           file-seq
           (map #(.getPath %))
           (filter #(re-find #"\.jpg$" %))
           (map #(second (str/split % #"public")))))

(defn create-tx [file-name person]
  (let [person-ident (h/qualify "person" (:id person))
        profile (profile-data file-name person)]
    (concat
     [(let [pics (profile-pics person)]
        (cond-> profile
          (seq pics) (assoc :person/profile-pictures (vec pics))))]
     (cv-data file-name person profile)
     (map (partial blog-post-data person-ident) (:blog-posts person))
     (keep (partial video/video-data person-ident) (:person/presentations profile)))))

(comment
  (create-tx "people/magnar.edn" (read-string (slurp (clojure.java.io/resource "people/magnar.edn"))))

  (require '[datomic.api :as d])
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  (d/touch (first (:person/recommendations (d/entity db :person/odin))))

  )
