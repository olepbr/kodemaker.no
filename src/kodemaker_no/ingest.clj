(ns kodemaker-no.ingest
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            datomic.api
            [datomic-type-extensions.api :as d]
            [kodemaker-no.files :as files]
            kodemaker-no.ingestion.article
            kodemaker-no.ingestion.firmablogg
            kodemaker-no.ingestion.person
            kodemaker-no.ingestion.tech
            kodemaker-no.ingestion.tech-types
            kodemaker-no.ingestion.weird-tech-names
            kodemaker-no.ingestion.employers
            [mapdown.core :as mapdown]))

(defn find-create-tx-fn [file-name]
  (cond
    (= "weird-tech-names.edn" file-name)
    kodemaker-no.ingestion.weird-tech-names/create-tx

    (= "tech-types.edn" file-name)
    kodemaker-no.ingestion.tech-types/create-tx

    (= "employers.edn" file-name)
    kodemaker-no.ingestion.employers/create-tx

    (re-find #"tech/.+\.edn" file-name)
    kodemaker-no.ingestion.tech/create-tx

    (re-find #"firmablogg/.+\.md" file-name)
    kodemaker-no.ingestion.firmablogg/create-tx

    (re-find #"blog/.+\.md" file-name)
    kodemaker-no.ingestion.firmablogg/create-legacy-tx

    (re-find #"people/.+\.edn" file-name)
    kodemaker-no.ingestion.person/create-tx

    (re-find #"articles/.+\.md" file-name)
    kodemaker-no.ingestion.article/create-tx))

(defn create-tx [file-name]
  (when-let [r (io/resource file-name)]
    (when-let [f (find-create-tx-fn file-name)]
      (println "Ingesting" file-name)
      (f file-name ((cond
                      (str/ends-with? file-name ".edn")
                      edn/read-string

                      (str/ends-with? file-name ".md")
                      mapdown/parse)
                    (slurp r))))))

(def attrs-to-keep #{:db/ident
                     :db/txInstant})

(defn retract-tx [db file-name]
  (when-let [tx-id (datomic.api/q '[:find ?e .
                                :in $ ?file-name
                                :where
                                [?e :tx-source/file-name ?file-name]]
                              db
                              file-name)]
    (keep
     (fn [[e a v t]]
       (when (= tx-id t)
         (let [attr (:ident (datomic.api/attribute db a))]
           (when (not (attrs-to-keep attr))
             [:db/retract e attr v]))))
     (datomic.api/datoms db :eavt))))

(defn ingest [conn file-name]
  (when-let [tx (retract-tx (datomic.api/db conn) file-name)]
    (try
      @(datomic.api/transact conn tx)
      (catch Exception e
        (throw (ex-info "Unable to retract" {:tx tx
                                             :file-name file-name} e)))))
  (when-let [tx (create-tx file-name)]
    (try
      @(d/transact conn (conj tx [:db/add (d/tempid :db.part/tx) :tx-source/file-name file-name]))
      (catch Exception e
        (throw (ex-info "Unable to assert" {:tx tx
                                            :file-name file-name} e))))))

(defn ingest-all [conn directory]
  (doseq [file-name (files/find-file-names directory #"(md|edn)$")]
    (ingest conn file-name)))

(comment
  (require '[kodemaker-no.atomic :as a])

  (a/create-database "datomic:mem://kodemaker")
  (datomic.api/delete-database "datomic:mem://kodemaker")
  (def conn (d/connect "datomic:mem://kodemaker"))

  (def file-name "weird-tech-names.edn")
  (def file-name "tech-types.edn")
  (def file-name "tech/aws.edn")
  (def file-name "tech/clojure.edn")

  (ingest conn "weird-tech-names.edn")
  (ingest conn "tech-types.edn")
  (ingest conn "firmablogg/2019-06-datascript.md")
  (ingest conn "people/christian.edn")
  (ingest-all conn "resources")

  (def db (d/db conn))

  (:blog-post/published (d/entity db [:page/uri "/blogg/2019-06-datascript/"]))

  )
