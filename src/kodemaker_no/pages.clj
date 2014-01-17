(ns kodemaker-no.pages
  (:require [kodemaker-no.pages.people-page :refer [all-people]]
            [kodemaker-no.pages.person-pages :refer [person-pages]]
            [kodemaker-no.pages.article-pages :refer [article-pages]]
            [clojure.set :as set]))

(defn general-pages [content]
  {"/mennesker.html" (partial all-people (:people content))})

(defn- guard-against-collisions [pages]
  (doseq [k1 (keys pages)
          k2 (keys pages)]
    (when-not (= k1 k2)
      (let [collisions (set/intersection (set (keys (k1 pages)))
                                         (set (keys (k2 pages))))]
        (when-not (empty? collisions)
          (throw (Exception. (str "URL conflicts between " k1 " and " k2 ": " collisions)))))))
  pages)

(defn get-pages [content]
  (->> {:person-pages (person-pages (:people content))
        :article-pages (article-pages (:articles content))
        :general-pages (general-pages content)}
       (guard-against-collisions)
       (vals)
       (apply merge)))
