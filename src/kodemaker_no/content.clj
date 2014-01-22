(ns kodemaker-no.content
  (:require [stasis.core :refer [slurp-directory]]))

(defn- slurp-edn-maps [directory]
  (->> (slurp-directory directory #"\.edn$")
       (vals)
       (map read-string)
       (map (juxt :id identity))
       (into {})))

(defn load-content []
  {:people (slurp-edn-maps "resources/people/")
   :tech (slurp-edn-maps "resources/tech/")
   :articles (slurp-directory "resources/articles/" #"\.adoc$")})
