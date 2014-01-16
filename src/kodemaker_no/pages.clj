(ns kodemaker-no.pages
  (:require [optimus.link :as link]
            [stasis.core :refer [slurp-files]]
            [kodemaker-no.layout :refer [with-layout render-page]]
            [kodemaker-no.asciidoc :as adoc]
            [kodemaker-no.people :as people]
            [kodemaker-no.pages.people-page :refer [all-people]]
            [clojure.java.io :as io]))

(defn- person-page [person request]
  (render-page
   {:title (people/full-name person)
    :illustration (link/file-path request (str "/photos/" (people/id person) "/half-figure.jpg")
                                  :fallback "/photos/unknown/half-figure.jpg")
    :lead (str "<p>" (:description person) "</p>")}
   request))

(defn- article-page [article request]
  (render-page
   (update-in article [:illustration] #(link/file-path request %))
   request))

(defn article-pages []
  (->> (slurp-files "resources/articles/" #"\.adoc$")
       (map adoc/parse-article)
       (map (juxt :url #(partial article-page %)))
       (into {})))

(defn people-pages []
  (->> people/everyone
       (map (juxt :url #(partial person-page %)))
       (into {})))

(defn general-pages []
  {"/mennesker.html" all-people})

(defn get-pages []
  (merge (people-pages)
         (article-pages)
         (general-pages)))
