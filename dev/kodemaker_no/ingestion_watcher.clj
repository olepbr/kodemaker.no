(ns kodemaker-no.ingestion-watcher
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [hawk.core :as hawk]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.ingest :as ingest]))

(defn start! [directory conn]
  (let [file (io/file directory)
        chop-length (inc (count (.getAbsolutePath file)))]
    (hawk/watch!
     [{:paths [directory]
       :filter hawk/file?
       :handler
       (fn [_ e]
         (let [file-path (subs (.getAbsolutePath (:file e)) chop-length)]
           (when (ingest/ingest conn file-path)
             (ingest/perform-last-minute-changes conn)
             (when-let [techs (seq (ingest/find-uncategorized-techs (d/db conn)))]
               (println
                (format "[watcher] Det har sneket seg inn techs som ikke er kategorisert! Sørg for å enten fikse stavingen av %s (fra %s) eller legg %s til i resources/tech-categories."
                        (str/join (f/comma-separated (map first techs)))
                        (str/join (f/comma-separated (set (map second techs))))
                        (if (< 1 (count techs)) "dem" "den"))))
             (println "[watcher]"
                      (case (:kind e)
                        :create "Ingested"
                        :modify "Updated"
                        :delete "Removed")
                      file-path))))}])))

(defn stop! [watcher]
  (hawk/stop! watcher))
