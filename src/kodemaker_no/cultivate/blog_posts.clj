(ns kodemaker-no.cultivate.blog-posts
  (:require [clojure.string :as str]
            [clojure.walk :as w]
            [kodemaker-no.date :as d]
            [kodemaker-no.homeless :as h]))

(defn blog-post-path [path]
  (str "/blogg" (str/replace path #"\.md$" "/")))

(defn blog-post-person [blog-post people]
  (get people (keyword (:author blog-post))))

(defn- parse-presence [s]
  (->> (str/split s #"[\s,]+")
       (apply hash-map)
       (w/keywordize-keys)))

(defn load-blog-post [path blog-post people]
  (-> blog-post
      (h/update-in-existing [:published] d/parse-ymd)
      (h/update-in-existing [:presence] parse-presence)
      (assoc :path (blog-post-path path))
      (assoc :author-person (blog-post-person blog-post people))))

(defn cultivate-blog-posts [blog-posts people]
  (into {} (map (fn [[path blog-post]]
                  [path (load-blog-post path blog-post people)]) blog-posts)))
