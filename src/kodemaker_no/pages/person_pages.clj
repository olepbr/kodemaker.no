(ns kodemaker-no.pages.person-pages)

(defn- person-page [person]
  {:title (:full-name person)
   :illustration (-> person :photos :half-figure)
   :lead [:p (:description person)]})

(defn person-pages [people]
  (into {} (map (juxt :url #(partial person-page %)) people)))
