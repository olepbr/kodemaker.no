(ns kodemaker-no.blog-posts
  (:require [kodemaker-no.structured-document :refer [read-doc]]
            [kodemaker-no.homeless :refer [update-vals update-in-existing]]
            [clojure.string :as str])
  (:import java.text.SimpleDateFormat))

(def date-format (java.text.SimpleDateFormat. "yyyy-MM-dd"))

(defn- to-date [date-str]
  (.parse date-format date-str))

(defn- load-blog-post [blog-post-str]
  (-> (read-doc blog-post-str)
      (update-in-existing [:published] to-date)))

(defn load-blog-posts [blog-posts]
  (update-vals blog-posts load-blog-post))
