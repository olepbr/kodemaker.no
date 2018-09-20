(ns kodemaker-no.cultivate.sellable
  (:require [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.homeless :as h]))

(defn- cultivate-item [raw-content item]
  (assoc item :tech (map (partial util/look-up-tech raw-content) (:tech item))))

(defn combine-items [items]
  (as-> items x
    (map #(h/remove-vals % nil?) x)
    (reverse x)
    (apply merge x)
    (assoc x :by (map :by items))))

(defn compare-by-title [a b]
  (or (h/compare* (:title a)
                  (:title b))
      0))

(defn ensure-price [default-price sellable]
  (if (:price sellable)
    sellable
    (assoc sellable :price default-price)))

(defn cultivate-sellable [key raw-content default-price]
  (->> raw-content :people vals
       (mapcat (util/get-with-byline key))
       (map (partial cultivate-item raw-content))
       (group-by :title)
       vals
       (map combine-items)
       (sort compare-by-title)
       (map (partial ensure-price default-price))))
