(ns kodemaker-no.pages.tech-pages
  (:require [kodemaker-no.formatting :refer [to-html]]))

(defn- tech-page [tech]
  {:title (:name tech)
   :illustration (:illustration tech)
   :lead (to-html :md (:description tech))})

(defn tech-pages [techs]
  (into {} (map (juxt :url #(partial tech-page %)) techs)))
