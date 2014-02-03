(ns kodemaker-no.cultivate.blog-posts
  (:require [kodemaker-no.structured-document :refer [read-doc]]
            [kodemaker-no.homeless :refer [update-vals update-in-existing]]
            [clojure.string :as str])
  (:import java.text.SimpleDateFormat))

(def date-format (java.text.SimpleDateFormat. "yyyy-MM-dd"))

(defn- to-date [date-str]
  (.parse date-format date-str))

(defn- cultivate-blog-post [blog-post-str]
  (-> (read-doc blog-post-str)
      (update-in-existing [:published] to-date)))

(defn cultivate-blog-posts [content]
  (update-vals (:blog-posts content) cultivate-blog-post))
