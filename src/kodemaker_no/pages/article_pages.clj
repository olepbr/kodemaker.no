(ns kodemaker-no.pages.article-pages
  (:require [kodemaker-no.asciidoc :as adoc]
            [kodemaker-no.homeless :refer [update-vals rename-keys]]
            [clojure.string :as str]))

(defn- article-page [article]
  (adoc/parse-article article))

(defn article-pages [articles]
  (-> articles
      (rename-keys #(str/replace % #"\.adoc$" ".html"))
      (update-vals #(partial article-page %))))
