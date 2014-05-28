(ns kodemaker-no.cultivate.people
  (:require [clojure.string :as str]
            [kodemaker-no.homeless :refer [update-vals update-in-existing update-in*]]
            [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.cultivate.tech :as tech]
            [kodemaker-no.cultivate.projects :as projects]
            [kodemaker-no.date :refer [parse-ymd]]))

(defn- add-str [person]
  (assoc person :str (-> person :id name)))

(defn- add-url [person]
  (assoc person :url (str "/" (:str person) "/")))

(defn- fix-names [person]
  (-> person
      (assoc :full-name (str/join " " (:name person)))
      (assoc :first-name (first (:name person)))))

(defn- add-genitive [person]
  (assoc person :genitive
         (str (:first-name person)
              (if (.endsWith (:first-name person) "s")
                "'"
                "s"))))

(defn- add-photos [person]
  (assoc person :photos
         {:side-profile (str "/photos/people/" (:str person) "/side-profile.jpg")
          :side-profile-near (str "/photos/people/" (:str person) "/side-profile-near.jpg")
          :half-figure (str "/photos/people/" (:str person) "/half-figure.jpg")}))

(defn- parse-dates [person]
  (update-in-existing person [:upcoming]
                      #(map (fn [u] (update-in u [:date] parse-ymd)) %)))

(defn- look-up-tech-in-maps [content maps]
  (map (fn [m] (update-in m [:tech] #(tech/look-up-tech content %)))
       maps))

(defn- look-up-tech [content person]
  (-> person
      (update-in-existing [:tech :favorites-at-the-moment] #(tech/look-up-tech content %))
      (update-in-existing [:tech :want-to-learn-more] #(tech/look-up-tech content %))
      (update-in-existing [:recommendations] #(look-up-tech-in-maps content %))
      (update-in-existing [:presentations] #(look-up-tech-in-maps content %))
      (update-in-existing [:upcoming] #(look-up-tech-in-maps content %))
      (update-in-existing [:blog-posts] #(look-up-tech-in-maps content %))
      (update-in-existing [:projects] #(look-up-tech-in-maps content %))
      (update-in-existing [:side-projects] #(look-up-tech-in-maps content %))
      (update-in-existing [:open-source-projects] #(look-up-tech-in-maps content %))
      (update-in-existing [:open-source-contributions] #(look-up-tech-in-maps content %))))

(defn- find-my-project [person id]
  (or (->> person
           :projects
           (filter #(= id (:id %)))
           first)
      (throw (Exception. (str "No project " id " found!")))))

(defn- update-endorsement-project [content person endorsement]
  (if-let [id (:project endorsement)]
    (assoc endorsement :project
           (or (projects/look-up-project content id)
               {:id id, :name (:customer (find-my-project person id))}))
    endorsement))

(defn- add-url-to-project [content project]
  (if-let [url (->> project :id (projects/look-up-project content) :url)]
    (assoc project :url url)
    project))

(defn- look-up-projects [content person]
  (-> person
      (update-in-existing [:endorsements] #(map (partial update-endorsement-project content person) %))
      (update-in-existing [:projects] #(map (partial add-url-to-project content) %))))

(defn- cultivate-person [content person]
  (->> person
       add-str
       add-url
       fix-names
       add-genitive
       add-photos
       (look-up-tech content)
       (look-up-projects content)
       parse-dates))

(defn cultivate-people [content]
  (update-vals (:people content) (partial cultivate-person content)))
