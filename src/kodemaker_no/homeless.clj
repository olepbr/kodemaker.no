(ns kodemaker-no.homeless
  (:require [clojure.java.io :as io]))

(defn slurp-files [dir regexp]
  (->> (.listFiles (io/as-file dir))
       (filter #(re-find regexp (.getName %)))
       (map slurp)))
