(ns kodemaker-no.pages.person-pages
  (:require [kodemaker-no.layout :refer [render-page]]
            [optimus.link :as link]))

(defn- person-page [person request]
  (render-page
   {:title (:full-name person)
    :illustration (link/file-path request (-> person :photos :half-figure)
                                  :fallback "/photos/unknown/half-figure.jpg")
    :lead (str "<p>" (:description person) "</p>")}
   request))

(defn person-pages [people]
  (->> people
       (map (juxt :url #(partial person-page %)))
       (into {})))
