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

(defn remove-nil-vals [m]
  (apply dissoc m
         (for [[k v] m :when (nil? v)] k)))

(defn nil-if-blank [s]
  (if (empty? s) nil s))
