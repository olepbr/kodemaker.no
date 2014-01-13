(ns kodemaker-no.people
  (:require [kodemaker-no.homeless :refer [slurp-files]]))

(def everyone
  (->> (slurp-files "resources/people/" #"\.edn$")
       (map read-string)))

(def consultants
  (remove :administration? everyone))

(defn full-name [person]
  (str (:first-name person) " "
       (when-let [middle (:middle-name person)]
         (str middle " "))
       (:last-name person)))
