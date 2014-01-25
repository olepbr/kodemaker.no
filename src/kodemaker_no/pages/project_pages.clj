(ns kodemaker-no.pages.project-pages)

(defn- project-page [project]
  {:title (:name project)
   :illustration (:logo project)
   :lead [:p (:description project)]})

(defn project-pages [projects]
  (into {} (map (juxt :url #(partial project-page %)) projects)))
