(ns kodemaker-no.ingest
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [kodemaker-no.ingestion.tech]
            [kodemaker-no.ingestion.tech-types]
            [kodemaker-no.ingestion.weird-tech-names]))

(defn find-create-tx-fn [file-name]
  (cond
    (= "weird-tech-names.edn" file-name)
    kodemaker-no.ingestion.weird-tech-names/create-tx

    (= "tech-types.edn" file-name)
    kodemaker-no.ingestion.tech-types/create-tx

    (re-find #"tech/.+\.edn" file-name)
    kodemaker-no.ingestion.tech/create-tx))

(defn create-tx [file-name]
  (when-let [r (io/resource file-name)]
    (when-let [f (find-create-tx-fn file-name)]
      (f (edn/read-string (slurp r))))))

(def attrs-to-keep #{:db/ident
                     :db/txInstant})

(defn retract-tx [db file-name]
  (when-let [tx-id (d/q '[:find ?e .
                          :in $ ?file-name
                          :where
                          [?e :tx-source/file-name ?file-name]]
                        db
                        file-name)]
    (keep
     (fn [[e a v t]]
       (when (= tx-id t)
         (let [attr (:ident (d/attribute db a))]
           (when (not (attrs-to-keep attr))
             [:db/retract e attr v]))))
     (d/datoms db :eavt))))

(defn ingest [conn file-name]
  (when-let [tx (retract-tx (d/db conn) file-name)]
    @(d/transact conn tx))
  (when-let [tx (create-tx file-name)]
    @(d/transact conn (conj tx [:db/add (d/tempid :db.part/tx) :tx-source/file-name file-name]))))

(comment
  (require '[kodemaker-no.atomic :as a])

  (def conn (d/connect "datomic:mem://kodemaker"))
  (:tech/type (d/entity (d/db conn) :tech/alpine))

  (def file-name "weird-tech-names.edn")
  (def file-name "tech-types.edn")
  (def file-name "tech/aws.edn")
  (def file-name "tech/clojure.edn")

  (ingest conn file-name)

  (def db (d/db conn))

  (d/entity db :tech/actionscript)

  )
