(ns kodemaker-no.pages.blog-post-pages
  (:require [kodemaker-no.structured-document :refer [read-doc]]
            [kodemaker-no.homeless :refer [update-vals rename-keys update-in-existing]]
            [kodemaker-no.formatting :refer [to-html]]
            [clojure.string :as str])
  (:import java.text.SimpleDateFormat))

(def date-format (java.text.SimpleDateFormat. "yyyy-MM-dd"))

(defn- to-date [date-str]
  (.parse date-format date-str))

(defn get-blog-post [str]
  (-> (read-doc str)
      (update-in-existing [:title] to-html)
      (update-in-existing [:published] to-date)
      (update-in-existing [:body] to-html)))

(defn blog-post-page [blog-post]
  {:title (:title blog-post)
   :illustration (:illustration blog-post)
   :lead (.format (java.text.SimpleDateFormat. "dd.MM.yyyy") (:published blog-post))
   :body (:body blog-post)})

(defn- blog-post-url [path]
  (str/replace path #"\.md$" "/"))

(defn blog-post-pages [blog-posts]
  (-> blog-posts
      (rename-keys blog-post-url)
      (update-vals #(partial blog-post-page %))))
