(ns kodemaker-no.cultivate.blog-posts
  (:require [kodemaker-no.date :refer [parse-ymd]]
            [clojure.string :as str]
            [kodemaker-no.homeless :refer [update-in-existing]]))

(defn blog-post-path [path]
  (str "/blogg" (str/replace path #"\.md$" "/")))

(defn load-blog-post [path blog-post]
  (-> blog-post
      (update-in-existing [:published] parse-ymd)
      (assoc :path (blog-post-path path))))

(defn cultivate-blog-posts [blog-posts]
  (into {} (map (fn [[path blog-post]]
                  [path (load-blog-post path blog-post)]) blog-posts)))
