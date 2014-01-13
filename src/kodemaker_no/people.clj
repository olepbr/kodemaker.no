(ns kodemaker-no.people
  (:require [kodemaker-no.homeless :refer [slurp-files]]))

(def people
  (->> (slurp-files "resources/people/" #"\.edn$")
       (map read-string)))
