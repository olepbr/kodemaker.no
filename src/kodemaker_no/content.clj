(ns kodemaker-no.content
  (:require [stasis.core :refer [slurp-directory]]
            [kodemaker-no.blog-posts :refer [load-blog-posts]]
            [kodemaker-no.articles :refer [load-articles]]))

(defn- read-string-strictly [[file s]]
  (let [forms (try
                (read-string (str "[" s "]"))
                (catch Exception e
                  (throw (Exception. (str "Error in " file ": " (.getMessage e))))))]
    (when (> (count forms) 1)
      (throw (Exception. (str "File " file " should contain only a single map, but had " (count forms) " forms."))))
    (first forms)))

(defn- slurp-edn-maps [directory]
  (->> (slurp-directory directory #"\.edn$")
       (map read-string-strictly)
       (map (juxt :id identity))
       (into {})))

(defn- slurp-edn-map [file]
  (read-string-strictly [file (slurp file)]))

(defn load-content []
  {:people (slurp-edn-maps "resources/people/")
   :tech (slurp-edn-maps "resources/tech/")
   :projects (slurp-edn-maps "resources/projects/")
   :articles (load-articles (slurp-directory "resources/articles/" #"\.md$"))
   :blog-posts (load-blog-posts (slurp-directory "resources/blog/" #"\.md$"))
   :tech-names (slurp-edn-map "resources/weird-tech-names.edn")})
