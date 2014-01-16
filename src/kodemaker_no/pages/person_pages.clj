(ns kodemaker-no.pages.person-pages
  (:require [kodemaker-no.people :as people]
            [kodemaker-no.layout :refer [render-page]]
            [optimus.link :as link]))

(defn- person-page [person request]
  (render-page
   {:title (people/full-name person)
    :illustration (link/file-path request (str "/photos/" (people/id person) "/half-figure.jpg")
                                  :fallback "/photos/unknown/half-figure.jpg")
    :lead (str "<p>" (:description person) "</p>")}
   request))

(defn person-pages []
  (->> people/everyone
       (map (juxt :url #(partial person-page %)))
       (into {})))
