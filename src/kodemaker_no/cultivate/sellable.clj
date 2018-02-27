(ns kodemaker-no.cultivate.sellable
  (:require [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.homeless :refer [compare* remove-vals]]))

(defn- cultivate-item [raw-content item]
  (assoc item :tech (map (partial util/look-up-tech raw-content) (:tech item))))

(defn combine-items [items]
  (as-> items x
    (map #(remove-vals % nil?) x)
    (reverse x)
    (apply merge x)
    (assoc x :by (map :by items))))

(defn compare-by-title [a b]
  (or (compare* (:title a)
                (:title b))
      0))

(defn cultivate-sellable [key raw-content]
  (->> raw-content :people vals
       (mapcat (util/get-with-byline key))
       (map (partial cultivate-item raw-content))
       (group-by :title)
       vals
       (map combine-items)
       (sort compare-by-title)))
