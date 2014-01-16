(ns kodemaker-no.people
  (:require [stasis.core :refer [slurp-directory]]))

(defn id [person]
  (-> person :id str (subs 1)))

(defn- enrich-person [person]
  (-> person
      (assoc :url (str "/" (id person) ".html"))))

(def everyone
  (->> (slurp-directory "resources/people/" #"\.edn$")
       (vals)
       (map read-string)
       (map enrich-person)))

(def consultants
  (remove :administration? everyone))

(defn full-name [person]
  (str (:first-name person) " "
       (when-let [middle (:middle-name person)]
         (str middle " "))
       (:last-name person)))
