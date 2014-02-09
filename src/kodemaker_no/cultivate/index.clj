(ns kodemaker-no.cultivate.index
  (:require [clojure.string :as str]))

(defn- update-face [content face]
  (let [person (-> content :people face)
        id (-> person :id str (subs 1))]
    {:name (str/join " " (:name person))
     :photo (str "/photos/people/" id "/side-profile-cropped.jpg")
     :url (str "/" id "/")}))

(defn cultivate-index [content]
  (-> (:index content)
      (update-in [:faces] #(map (partial update-face content) %))))
