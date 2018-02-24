(ns kodemaker-no.cultivate.screencasts
  (:require [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.formatting :refer [to-id-str]]
            [kodemaker-no.homeless :refer [update-in-existing update-vals compare* remove-vals]]))

(defn- get-with-byline [key]
  (fn [person]
    (->> (key person)
         (map #(assoc % :by {:name (first (:name person))
                             :url (util/url person)})))))

(defn- cultivate-screencast [raw-content {:keys [title url by tech blurb link-text]}]
  {:title title
   :by by
   :blurb blurb
   :tech (map (partial util/look-up-tech raw-content) tech)
   :url  url})

(defn combine-screencasts [screencasts]
  (as-> screencasts x
    (map #(remove-vals % nil?) x)
    (reverse x)
    (apply merge x)
    (assoc x :by (map :by screencasts))))

(defn compare-by-date-and-title [a b]
  (or (compare* (:launch-date b)
                (:launch-date a))
      (compare* (:title a)
                (:title b))
      0))

(defn cultivate-screencasts [raw-content]
  (->> raw-content :people vals
       (mapcat (get-with-byline :screencasts))
       (map (partial cultivate-screencast raw-content))
       (group-by :url)
       vals
       (map combine-screencasts)
       (sort compare-by-date-and-title)))
