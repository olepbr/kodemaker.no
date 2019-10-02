(ns kodemaker-no.ingestion-watcher
  (:require [clojure.java.io :as io]
            [juxt.dirwatch :as dirwatch]
            [kodemaker-no.ingest :as ingest]))

(defn start! [directory conn]
  (let [file (io/file directory)
        chop-length (inc (count (.getAbsolutePath file)))]
    (dirwatch/watch-dir
     #(let [file-path (subs (.getAbsolutePath (:file %)) chop-length)]
        (when (ingest/ingest conn file-path)
          (println "[watcher]"
                   (case (:action %)
                     :create "Ingested"
                     :modify "Updated"
                     :delete "Removed")
                   file-path)))
     file)))

(defn stop! [watcher]
  (dirwatch/close-watcher watcher))
