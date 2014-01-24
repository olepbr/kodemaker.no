(ns kodemaker-no.cultivate.people
  (:require [clojure.string :as str]
            [kodemaker-no.homeless :refer [update-vals]]
            [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.cultivate.tech :as tech]))

(defn- add-str [person]
  (assoc person :str (-> person :id str (subs 1))))

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
          :half-figure (str "/photos/people/" (:str person) "/half-figure.jpg")}))

(defn- look-up-tech-in-maps [content maps]
  (map (fn [m] (update-in m [:tech] #(tech/look-up-tech content %)))
       maps))

(defn- update-in-existing [m path f]
  (if-not (nil? (get-in m path))
    (update-in m path f)
    m))

(defn- look-up-tech [content person]
  (-> person
      (update-in-existing [:tech :favorites-at-the-moment] #(tech/look-up-tech content %))
      (update-in-existing [:tech :want-to-learn-more] #(tech/look-up-tech content %))
      (update-in-existing [:recommendations] #(look-up-tech-in-maps content %))
      (update-in-existing [:presentations] #(look-up-tech-in-maps content %))))

(defn- cultivate-person [content person]
  (->> person
       add-str
       add-url
       fix-names
       add-genitive
       add-photos
       (look-up-tech content)))

(defn cultivate-people [content]
  (update-vals (:people content) (partial cultivate-person content)))
