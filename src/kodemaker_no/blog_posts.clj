(ns kodemaker-no.blog-posts
  (:require [kodemaker-no.homeless :refer [update-vals update-in-existing]])
  (:import java.text.SimpleDateFormat))

(def date-format (java.text.SimpleDateFormat. "yyyy-MM-dd"))

(defn- to-date [date-str]
  (.parse date-format date-str))

(defn load-blog-posts [blog-posts]
  (update-vals blog-posts #(update-in-existing % [:published] to-date)))
