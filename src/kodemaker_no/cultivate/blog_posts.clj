(ns kodemaker-no.cultivate.blog-posts
  (:require [clojure.string :as str]
            [clojure.walk :as w]
            [kodemaker-no.date :as d]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.cultivate.util :as util]))

(defn blog-post-path [path]
  (str "/blogg" (str/replace path #"\.md$" "/")))

(defn blog-post-person [blog-post people]
  (get people (keyword (:author blog-post))))

(defn- parse-presence [s]
  (->> (str/split s #"[\s,]+")
       (apply hash-map)
       (w/keywordize-keys)))

(defn- parse-tech [path content s]
  (try
    (map #(util/look-up-tech content %) (read-string s))
    (catch Throwable e
      (throw (Exception. (str "Unable to parse blog post techs" path s) e)))))

(defn load-blog-post [path blog-post people content]
  (-> blog-post
      (h/update-in-existing [:published] d/parse-ymd)
      (h/update-in-existing [:updated] d/parse-ymd)
      (h/update-in-existing [:presence] parse-presence)
      (h/update-in-existing [:tech] #(parse-tech path content %))
      (assoc :path (blog-post-path path))
      (assoc :file-path path)
      (assoc :author-person (blog-post-person blog-post people))))

(defn cultivate-blog-posts [content blog-posts people]
  (into {} (map (fn [[path blog-post]]
                  [path (load-blog-post path blog-post people content)]) blog-posts)))
