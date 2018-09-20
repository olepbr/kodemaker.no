(ns kodemaker-no.cultivate.screencasts
  (:require [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.homeless :as h]))

(defn- cultivate-screencast [raw-content screencast]
  (assoc screencast :tech (map (partial util/look-up-tech raw-content) (:tech screencast))))

(defn combine-screencasts [screencasts]
  (as-> screencasts x
    (map #(h/remove-vals % nil?) x)
    (reverse x)
    (apply merge x)
    (assoc x :by (map :by screencasts))))

(defn compare-by-date-and-title [a b]
  (or (h/compare* (:launch-date b)
                  (:launch-date a))
      (h/compare* (:title a)
                  (:title b))
      0))

(defn cultivate-screencasts [raw-content]
  (->> raw-content :people vals
       (mapcat (util/get-with-byline :screencasts))
       (map (partial cultivate-screencast raw-content))
       (group-by :url)
       vals
       (map combine-screencasts)
       (sort compare-by-date-and-title)))
