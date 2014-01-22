(ns kodemaker-no.pages.tech-pages
  (:require [kodemaker-no.formatting :refer [to-html comma-separated]]))

(defn- link-to-person [person]
  [:a {:href (:url person)} (:name person)])

(defn- render-recommendation [rec]
  (list [:h3 (:title rec)]
        [:p.near.cookie-w [:span.cookie "Anbefalt av " (interpose " " (map link-to-person (:recommended-by rec)))]]
        [:p (:blurb rec) " "
         [:a.nowrap {:href (:url rec)} "Les mer"]]))

(defn- render-recommendations [recs]
  (list [:h2 "Våre anbefalinger"]
        (map render-recommendation recs)))

(defn- tech-page [tech]
  {:title (:name tech)
   :illustration (:illustration tech)
   :lead (to-html :md (:description tech))
   :body (list
          (when-let [xs (:recommendations tech)]
            (render-recommendations xs)))})

(defn tech-pages [techs]
  (into {} (map (juxt :url #(partial tech-page %)) techs)))
