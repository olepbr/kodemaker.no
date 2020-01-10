(ns kodemaker-no.homeless
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [mapdown.core :as mapdown])
  (:import [java.time LocalDate LocalDateTime]))

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

(defn update-in-existing [m path & args]
  (if-not (nil? (get-in m path))
    (apply update-in m path args)
    m))

(defn max-key*
  "Returns the x for which (k x) is greatest.
  Differs from `max-key` in that it uses `compare` instead of `>`."
  {:static true}
  ([k x] x)
  ([k x y] (if (pos? (compare (k x) (k y))) x y))
  ([k x y & more]
   (reduce #(max-key* k %1 %2) (max-key* k x y) more)))

(defn min-key*
  "Returns the x for which (k x) is least.
  Differs from `min-key` in that it uses `compare` instead of `<`."
  {:static true}
  ([k x] x)
  ([k x y] (if (neg? (compare (k x) (k y))) x y))
  ([k x y & more]
   (reduce #(min-key* k %1 %2) (min-key* k x y) more)))

(defn max-by [k coll & [default]]
  (if (seq coll)
    (apply max-key* k coll)
    default))

(defn min-by [k coll & [default]]
  (if (seq coll)
    (apply min-key* k coll)
    default))

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

(defn keep-vals [f m]
  (->> m
       (keep (fn [[k v]]
               (when-let [new-v (f v)]
                 [k new-v])))
       (into {})))

(defn slurp-mapdown-resource [file-name]
  (-> file-name
      io/resource
      slurp
      mapdown/parse))

(defn qualify [ns kw]
  (keyword ns (name kw)))

(defn prep-techs [techs]
  (map (fn [tech] {:db/ident (qualify "tech" tech)}) techs))

(defn prep-tech-list [techs]
  (map-indexed (fn [idx tech]
                 {:list/idx idx
                  :list/ref {:db/ident (qualify "tech" tech)}})
               techs))

(defn entity-seq [coll]
  (->> coll
       (sort-by :list/idx)
       (map #(or (:list/ref %) %))))

(defn unwrap-ident-list [entity k]
  (let [db (d/entity-db entity)]
    (->> (entity-seq (k entity))
         (map #(d/entity db %)))))

(defn capitalize [s]
  (str (.toUpperCase (subs s 0 1))
       (subs s 1)))

(defn str-for-humans [id]
  (-> id
      name
      (str/replace "-" " ")
      capitalize))

(defn distinct-by [k xs]
  (loop [used #{}
         res []
         xs xs]
    (let [x (first xs)
          xk (k x)]
      (cond
        (empty? xs) res
        (contains? used xk) (recur used res (rest xs))
        :default (recur (conj used xk) (conj res x) (rest xs))))))

(defn unwrap-idents [entity k]
  (map (partial d/entity (d/entity-db entity)) (k entity)))
