(ns kodemaker-no.ingestion-watcher
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [kodemaker-no.files :as files]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.ingest :as ingest]
            [nextjournal.beholder :as beholder]))

(defn start! [directory conn]
  (let [file (io/file directory)
        chop-length (inc (count (.getAbsolutePath file)))]
    (beholder/watch
     (fn [{:keys [type path]}]
       (let [file-path (subs (.getAbsolutePath (.toFile path)) chop-length)]
         (when (ingest/ingest conn (files/normalize-path file-path))
           (ingest/perform-last-minute-changes conn)
           (when-let [techs (seq (ingest/find-uncategorized-techs (d/db conn)))]
             (println
              (format "[watcher] Det har sneket seg inn techs som ikke er kategorisert! Sørg for å enten fikse stavingen av %s (fra %s) eller legg %s til i resources/tech-categories."
                      (str/join (f/comma-separated (map first techs)))
                      (str/join (f/comma-separated (set (map second techs))))
                      (if (< 1 (count techs)) "dem" "den"))))
           (println "[watcher]"
                    (case type
                      :create "Ingested"
                      :modify "Updated"
                      :delete "Removed"
                      :overflow "Overflowed(?)")
                    file-path)))) directory)))

(defn stop! [watcher]
  (beholder/stop watcher))
