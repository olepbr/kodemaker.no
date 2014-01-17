(ns kodemaker-no.homeless
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [net.cgrand.enlive-html :as enlive]))

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

(defn rename-keys [m f]
  (into {} (map (fn [[key val]] [(f key) val]) m)))

(defn nil-if-blank [s]
  (if (empty? s) nil s))

(defmacro with-html-transform [html & body]
  `(apply str ((enlive/template (java.io.StringReader. ~html) []
                                ~@body))))

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
