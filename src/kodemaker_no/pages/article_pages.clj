(ns kodemaker-no.pages.article-pages
  (:require [optimus.link :as link]
            [stasis.core :refer [slurp-files]]
            [kodemaker-no.layout :refer [render-page]]
            [kodemaker-no.asciidoc :as adoc]))

(defn- article-page [article request]
  (render-page
   (update-in article [:illustration] #(link/file-path request %))
   request))

(defn article-pages []
  (->> (slurp-files "resources/articles/" #"\.adoc$")
       (map adoc/parse-article)
       (map (juxt :url #(partial article-page %)))
       (into {})))
