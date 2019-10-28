(ns kodemaker-no.ingest
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            datomic.api
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
