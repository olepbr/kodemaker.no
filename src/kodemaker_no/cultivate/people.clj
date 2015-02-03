(ns kodemaker-no.cultivate.people
  (:require [clojure.string :as str]
            [kodemaker-no.cultivate.tech :as tech]
            [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.cultivate.videos :refer [replace-video-urls]]
            [kodemaker-no.date :refer [parse-ymd]]
            [kodemaker-no.homeless :refer [update-vals update-in-existing update-in*]]))

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
      (update-in-existing [:tech :using-at-work] #(tech/look-up-tech content %))
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

(defn- add-link-to-next-person [content person]
  (let [sorted-peeps (->> content :people vals
                          (sort-by :start-date)
                          reverse)
        next-person (or (->> sorted-peeps
                             (remove :quit?)
                             (drop-while #(not= % person))
                             (drop 1)
                             first)
                        (first sorted-peeps))]
    (assoc person :next-person-url
           (str "/" (name (:id next-person)) "/"))))

(defn- cultivate-person [content person]
  (->> person
       (add-link-to-next-person content)
       add-str
       add-url
       fix-names
       add-genitive
       add-photos
       replace-video-urls
       (look-up-tech content)
       parse-dates))

(defn cultivate-people [content]
  (update-vals (:people content) (partial cultivate-person content)))
