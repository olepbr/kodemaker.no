(ns kodemaker-no.ingestion-watcher
  (:require [clojure.java.io :as io]
            [hawk.core :as hawk]
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
             (println "[watcher]"
                      (case (:kind e)
                        :create "Ingested"
                        :modify "Updated"
                        :delete "Removed")
                      file-path))))}])))

(defn stop! [watcher]
  (hawk/stop! watcher))
