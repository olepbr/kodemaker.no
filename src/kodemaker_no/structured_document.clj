(ns kodemaker-no.structured-document
  (:require [clojure.string :as str]))

(defn- third [coll]
  (nth coll 2))

(def var-declaration-re
  #"^:([^ ]+) (.+)$")

(defn- find-var-declaration [s]
  (when-let [match (re-find var-declaration-re s)]
    [(keyword (second match)) (third match)]))

(defn- find-sections [lines]
  (->> lines
       (drop-while #(not (.startsWith % ":::")))
       (remove #(re-find var-declaration-re %))
       (partition-by #(.startsWith % ":::"))
       (partition 2)
       (map (juxt #(keyword (subs (ffirst %) 3))
                  #(str/trim (str/join "\n" (second %)))))))

(defn read-doc [s]
  (let [lines (str/split (str/trim s) #"\n")]
    (into {} (concat
              (keep find-var-declaration lines)
              (find-sections lines)))))
