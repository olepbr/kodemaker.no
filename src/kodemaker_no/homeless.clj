(ns kodemaker-no.homeless
  (:require [clojure.java.io :as io]))

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
