(ns kodemaker-no.content
  (:require [clojure.string :as str]
            [kodemaker-no.homeless :refer [update-vals]]
            [mapdown.core :as mapdown]
            [stasis.core :as stasis]))

(defn- detonate-the-bom [^String s]
  (if (.startsWith s "\uFEFF") (subs s 1) s))

(defn- trim-properly [s]
  (-> s str/trim detonate-the-bom))

(defn- read-string-strictly [[file s]]
  (let [forms (try
                (read-string (str "[" (trim-properly s) "]"))
                (catch Exception e
                  (throw (Exception. (str "Error in " file ": " (.getMessage e))))))]
    (when (> (count forms) 1)
      (throw (Exception. (str "File " file " should contain only a single map, but had " (count forms) " forms."))))
    (first forms)))

(defn- slurp-edn-maps [directory]
  (->> (stasis/slurp-directory directory #"\.edn$")
       (map read-string-strictly)
       (map (juxt :id identity))
       (into {})))

(defn- slurp-edn-map [file]
  (read-string-strictly [file (slurp file)]))

(defn load-content []
  {:people (slurp-edn-maps "resources/people/")
   :tech (slurp-edn-maps "resources/tech/")
   :articles (mapdown/slurp-directory "resources/articles/" #"\.md$")
   :references (mapdown/slurp-directory "resources/references/" #"\.md$")
   :raw-pages (stasis/slurp-directory "resources/raw" #"\.html$")
   :raw-css (stasis/slurp-directory "resources/public" #"\.css$")
   :legacy-blog-posts (mapdown/slurp-directory "resources/blog/" #"\.md$")
   :blog-posts (mapdown/slurp-directory "resources/firmablogg/" #"\.md$")
   :tech-names (slurp-edn-map "resources/weird-tech-names.edn")
   :tech-types (slurp-edn-map "resources/tech-types.edn")
   :video-overrides (slurp-edn-map "resources/video-overrides.edn")
   :employers (slurp-edn-map "resources/employers.edn")
   })
