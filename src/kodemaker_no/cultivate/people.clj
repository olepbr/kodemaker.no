(ns kodemaker-no.cultivate.people
  (:require [clojure.string :as str]
            [kodemaker-no.homeless :refer [update-vals]]))

(defn- add-str [person]
  (assoc person :str (-> person :id str (subs 1))))

(defn- add-url [person]
  (assoc person :url (str "/" (:str person) "/")))

(defn- fix-names [person]
  (-> person
      (assoc :full-name (str/join " " (:name person)))
      (assoc :first-name (first (:name person)))))

(defn- add-photos [person]
  (assoc person :photos
         {:side-profile (str "/photos/people/" (:str person) "/side-profile.jpg")
          :half-figure (str "/photos/people/" (:str person) "/half-figure.jpg")}))

(defn- cultivate-person [person]
  (-> person
      add-str
      add-url
      fix-names
      add-photos))

(defn cultivate-people [content]
  (update-in content [:people] #(update-vals % cultivate-person)))
