(ns kodemaker-no.homeless
  (:require [clojure.set :as set]
            [clojure.string :as str])
  (:import java.time.LocalDate
           java.time.LocalDateTime))

(defn wrap-content-type-utf-8 [handler]
  (fn [request]
    (when-let [response (handler request)]
      (if (.contains (get-in response [:headers "Content-Type"]) ";")
        response
        (if (string? (:body response))
          (update-in response [:headers "Content-Type"] #(str % "; charset=utf-8"))
          response)))))

(defn remove-vals [m pred]
  (apply dissoc m
         (for [[k v] m :when (pred v)] k)))

(defn update-vals [m f]
  (into {} (for [[k v] m] [k (f v)])))

(defn update-vals-with-key [m f]
  (into {} (for [[k v] m] [k (f k v)])))

(defn rename-keys [m f]
  (into {} (map (fn [[key val]] [(f key) val]) m)))

(defn nil-if-blank [s]
  (if (empty? s) nil s))

(defn assoc-in-unless [m path pred v]
  (if (not (pred v))
    (assoc-in m path v)
    m))

(defn update-in-existing [m path f]
  (if-not (nil? (get-in m path))
    (update-in m path f)
    m))

(defn update-in* [m path f]
  "Like update-in, but can map over lists by nesting paths."
  (if (vector? (last path))
    (let [nested-path (last path)
          this-path (drop-last path)]
      (if (empty? nested-path)
        (update-in m this-path (partial map f))
        (update-in m this-path (partial map #(update-in* % nested-path f)))))
    (update-in m path f)))

(defn interleave-all
  "Returns a lazy seq of the first item in each coll, then the second etc.
   Once a coll is empty, it keeps going for the others."
  ([] nil)
  ([c1]
     c1)
  ([c1 c2]
     (lazy-seq
      (let [s1 (seq c1) s2 (seq c2)]
        (if (and s1 s2)
          (cons (first s1) (cons (first s2)
                                 (interleave-all (rest s1) (rest s2))))
          (or s1 s2)))))
  ([c1 c2 & colls]
     (lazy-seq
      (let [ss (keep seq (conj colls c2 c1))]
        (when-not (empty? ss)
          (concat (map first ss) (apply interleave-all (map rest ss))))))))

(defn compare* [a b]
  "Like compare, but returns nil if they're equal, so you can chain
   compares with or."
  (let [result (compare a b)]
    (if (zero? result)
      nil
      result)))

;; create project hiccup-find for this?

(defn hiccup-nodes [root]
  (->> root
       (tree-seq #(or (vector? %) (seq? %)) seq)
       (filter vector?)))

(defn split-hiccup-symbol [symbol]
  (re-seq #"[:.#][^:.#]+" (str symbol)))

(defn hiccup-symbol-matches? [q symbol]
  (set/subset? (set (split-hiccup-symbol q))
               (set (split-hiccup-symbol symbol))))

(defn hiccup-find [q root]
  (->> root
       (hiccup-nodes)
       (filter #(hiccup-symbol-matches? q (first %)))))

(defn select-renamed-keys [m ks]
  (-> m
      (select-keys (keys ks))
      (set/rename-keys ks)))

(defn parse-local-date [date-str]
  (LocalDate/parse date-str))

(defn- ensure-clock [ts]
  (if (re-find #"T\d\d:\d\d" ts)
    ts
    (str ts "T00:00")))

(defn parse-local-date-time [datetime-str]
  (-> datetime-str
      (str/replace #" " "T")
      ensure-clock
      LocalDateTime/parse))

(defn map-vals [f m]
  (->> m
       (map (fn [[k v]] [k (f v)]))
       (into {})))
