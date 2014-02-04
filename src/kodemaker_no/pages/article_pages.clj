(ns kodemaker-no.pages.article-pages
  (:require [kodemaker-no.homeless :refer [nil-if-blank remove-vals update-vals rename-keys update-in-existing]]
            [kodemaker-no.formatting :refer [to-html]]
            [clojure.string :as str]))

(defn article-page [article]
  (-> article
      (update-in-existing [:lead] to-html)
      (update-in-existing [:aside] to-html)
      (update-in-existing [:body] to-html)))

(defn- article-url [path]
  (if (= path "/index.md")
    "/index.html"
    (str/replace path #"\.md$" "/")))

(defn article-pages [articles]
  (-> articles
      (rename-keys article-url)
      (update-vals #(partial article-page %))))
