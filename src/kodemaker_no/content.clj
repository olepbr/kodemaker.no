(ns kodemaker-no.content
  (:require [stasis.core :refer [slurp-directory]]))

(defn load-content []
  {:people (->> (slurp-directory "resources/people/" #"\.edn$")
                (vals)
                (map read-string)
                (map (juxt :id identity))
                (into {}))
   :articles (slurp-directory "resources/articles/" #"\.adoc$")})
