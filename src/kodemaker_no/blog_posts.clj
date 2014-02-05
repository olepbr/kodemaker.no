(ns kodemaker-no.blog-posts
  (:require [kodemaker-no.homeless :refer [update-vals update-in-existing]]
            [clojure.string :as str])
  (:import java.text.SimpleDateFormat))

(def date-format (java.text.SimpleDateFormat. "yyyy-MM-dd"))

(defn- to-date [date-str]
  (.parse date-format date-str))

(defn blog-post-path [path]
  (str "/blogg" (str/replace path #"\.md$" "/")))

(defn load-blog-post [path blog-post]
  (-> blog-post
      (update-in-existing [:published] to-date)
      (assoc :path (blog-post-path path))))

(defn load-blog-posts [blog-posts]
  (into {} (map (fn [[path blog-post]]
                  [path (load-blog-post path blog-post)]) blog-posts)))
