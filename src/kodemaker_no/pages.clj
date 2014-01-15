(ns kodemaker-no.pages
  (:require [optimus.link :as link]
            [kodemaker-no.layout :refer [with-layout render-page]]
            [kodemaker-no.asciidoc :as adoc]
            [kodemaker-no.people :as people]
            [kodemaker-no.homeless :refer [slurp-files]]
            [kodemaker-no.pages.people-page :refer [all-people]]
            [clojure.java.io :as io]))

(defn- person-page [person request]
  {:body (with-layout request (people/full-name person)
           [:div.body])})

(defn article-pages []
  (->> (slurp-files "resources/articles/" #"\.adoc$")
       (map adoc/parse-article)
       (map (juxt :url #(partial render-page %)))
       (into {})))

(defn general-pages []
  {"/mennesker.html" all-people})

(defn people-pages []
  (into {} (map (juxt :url #(partial person-page %)) people/everyone)))

(defn get-pages []
  (merge (people-pages)
         (article-pages)
         (general-pages)))
