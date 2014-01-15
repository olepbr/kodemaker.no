(ns kodemaker-no.homeless
  (:require [clojure.java.io :as io]))

(defn slurp-files [dir regexp]
  (->> (.listFiles (io/as-file dir))
       (filter #(re-find regexp (.getName %)))
       (map slurp)))

(defn wrap-content-type-utf-8 [handler]
  (fn [request]
    (when-let [response (handler request)]
      (if (.contains (get-in response [:headers "Content-Type"]) ";")
        response
        (if (string? (:body response))
          (update-in response [:headers "Content-Type"] #(str % "; charset=utf-8"))
          response)))))
