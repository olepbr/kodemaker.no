(ns kodemaker-no.pages.article-pages
  (:require [optimus.link :as link]
            [kodemaker-no.layout :refer [render-page]]
            [kodemaker-no.asciidoc :as adoc]
            [kodemaker-no.homeless :refer [update-vals rename-keys]]
            [clojure.string :as str]))

(defn- article-page [article request]
  (render-page
   (-> article
       (adoc/parse-article)
       (update-in [:illustration] #(link/file-path request %)))
   request))

(defn article-pages [articles]
  (-> articles
      (rename-keys #(str/replace % #"\.adoc$" ".html"))
      (update-vals #(partial article-page %))))
