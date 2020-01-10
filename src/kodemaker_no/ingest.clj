(ns kodemaker-no.ingest
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            datomic.api
            [datomic-type-extensions.api :as d]
            [kodemaker-no.files :as files]
            [kodemaker-no.homeless :as homeless]
            kodemaker-no.ingestion.article
            [kodemaker-no.ingestion.blog :as blog]
            kodemaker-no.ingestion.employers
            kodemaker-no.ingestion.person
            kodemaker-no.ingestion.reference
            kodemaker-no.ingestion.tech
            [mapdown.core :as mapdown]))

(defn find-create-tx-fn [file-name]
  (cond
    (= "weird-tech-names.edn" file-name)
    kodemaker-no.ingestion.tech/create-tech-name-tx

    (= "tech-types.edn" file-name)
    kodemaker-no.ingestion.tech/create-tech-type-tx

    (re-find #"tech-stubs/.+\.edn" file-name)
    kodemaker-no.ingestion.tech/create-tx

    (re-find #"tech/.+\.edn" file-name)
    kodemaker-no.ingestion.tech/create-tx

    (= "employers.edn" file-name)
    kodemaker-no.ingestion.employers/create-tx

    (re-find #"firmablogg/.+\.md" file-name)
    kodemaker-no.ingestion.blog/create-tx

    (re-find #"blog/.+\.md" file-name)
    kodemaker-no.ingestion.blog/create-legacy-tx

    (re-find #"people/.+\.edn" file-name)
    kodemaker-no.ingestion.person/create-tx

    (re-find #"articles/.+\.md" file-name)
    kodemaker-no.ingestion.article/create-tx

    (re-find #"references/.+\.md" file-name)
    kodemaker-no.ingestion.reference/create-tx))

(defn create-tx [file-name]
  (when-let [r (io/resource file-name)]
    (when-let [f (find-create-tx-fn file-name)]
      (try
        (f file-name ((cond
                        (str/ends-with? file-name ".edn")
                        edn/read-string

                        (str/ends-with? file-name ".md")
                        mapdown/parse)
                      (slurp r)))
        (catch Exception e
          (println "Failed to ingest" file-name)
          (prn e))))))

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

(def static-pages
  {"/" :page.kind/frontpage
   "/folk/" :page.kind/people
   "/blogg/" :page.kind/blog})

(defn techs-without-name [db]
  (->> (d/q '[:find [?v ...] :where [_ :db/ident ?v]] db)
       (filter #(= "tech" (namespace %)))
       (remove #(:tech/name (d/entity db %)))))

(defn perform-last-minute-changes [conn]
  @(datomic.api/transact conn (for [tech-id (techs-without-name (d/db conn))]
                                [:db/add tech-id :tech/name (homeless/str-for-humans tech-id)]))
  @(datomic.api/transact conn (for [[post-id picture] (blog/blog-post-author-images (d/db conn))]
                                [:db/add post-id :blog-post/author-picture picture])))

(defn ingest-all [conn directory]
  (doseq [file-name (files/find-file-names directory #"(md|edn)$")]
    (ingest conn file-name))
  @(datomic.api/transact conn (for [[uri kind] static-pages]
                                {:page/uri uri :page/kind kind}))
  (perform-last-minute-changes conn))

(comment
  (require '[kodemaker-no.atomic :as a])

  (datomic.api/delete-database "datomic:mem://kodemaker")
  (a/create-database "datomic:mem://kodemaker")
  (def conn (d/connect "datomic:mem://kodemaker"))
  (ingest-all conn "resources")

  (def file-name "weird-tech-names.edn")
  (def file-name "tech-types.edn")
  (def file-name "tech/aws.edn")
  (def file-name "tech/clojure.edn")

  (ingest conn "weird-tech-names.edn")
  (ingest conn "tech-types.edn")
  (ingest conn "firmablogg/2019-06-datascript.md")
  (ingest conn "people/christian.edn")

  (def db (d/db conn))

  (:blog-post/published (d/entity db [:page/uri "/blogg/2019-06-datascript/"]))

  )
