(ns kodemaker-no.pages.article-pages
  (:require [clojure.string :as str]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h]))

(defn article-page [article]
  (-> article
      (h/update-in-existing [:lead] f/to-html)
      (h/update-in-existing [:aside] f/to-html)
      (h/update-in-existing [:body] f/to-html)
      (h/update-in-existing [:meta] read-string)))

(defn- article-url [path]
  (if (= path "/index.md")
    "/index.html"
    (str/replace path #"\.md$" "/")))

(defn article-pages [articles]
  (-> articles
      (h/rename-keys article-url)
      (h/update-vals #(partial article-page %))))
