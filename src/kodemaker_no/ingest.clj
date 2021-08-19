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
            [kodemaker-no.ingestion.tech :as tech]
            [mapdown.core :as mapdown]))

(defn find-create-tx-fn [file-name]
  (cond
    (= "weird-tech-names.edn" file-name)
    kodemaker-no.ingestion.tech/create-tech-name-tx

    (= "tech-categories.edn" file-name)
    kodemaker-no.ingestion.tech/create-tech-category-tx

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
   "/blogg/" :page.kind/blog
   "/kontakt/" :page.kind/contact
   "/referanser/" :page.kind/references
   "/om-oss/" :page.kind/whoami})

(defn techs [db]
  (->> (d/q '[:find [?v ...] :where [_ :db/ident ?v]] db)
       (filter #(= "tech" (namespace %)))
       (map #(d/entity db %))))

(defn techs-without-name [db]
  (->> (techs db)
       (remove :tech/name)
       (map :db/ident)))

(defn find-uncategorized-techs [db]
  (->> (d/q '[:find ?e ?file
              :in $
              :where
              [?e :tech/name]
              [?e :db/ident _ ?t]
              [?t :tx-source/file-name ?file]]
            db)
       (map #(vector (d/entity db (first %)) (second %)))
       (remove (comp :tech/type first))
       (map (fn [[tech file]]
              [(:db/ident tech) file]))))

(defn perform-last-minute-changes [conn]
  @(datomic.api/transact conn (for [tech-id (techs-without-name (d/db conn))]
                                [:db/add tech-id :tech/name (homeless/str-for-humans tech-id)]))
  @(datomic.api/transact conn (for [[post-id picture] (blog/blog-post-author-images (d/db conn))]
                                [:db/add post-id :blog-post/author-picture picture]))
  @(datomic.api/transact conn (->> (techs (d/db conn))
                                   (filter tech/is-page?)
                                   (map tech/page)))
  @(datomic.api/transact conn (for [tech (blog/blogged-techs (d/db conn))]
                                {:page/uri (format "/blogg/%s/" (name tech))
                                 :page/kind :page.kind/blog-category
                                 :blog-category/tech tech})))

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
  (def file-name "tech/aws.edn")
  (def file-name "tech/clojure.edn")

  (ingest conn "weird-tech-names.edn")
  (ingest conn "firmablogg/2019-06-datascript.md")
  (ingest conn "people/christian.edn")

  (kodemaker-no.ingestion.tech/create-tech-category-tx
   "tech-categories.edn"
   (read-string (slurp (io/resource "tech-categories.edn"))))

  (def db (d/db conn))

  (d/q '[:find ?uri ?tech
         :in $
         :where
         [?e :page/kind :page/blog-category]
         [?e :page/uri ?uri]
         [?e :blog-category/tech ?tech]]
       db)

  (:blog-post/published (d/entity db [:page/uri "/blogg/2019-06-datascript/"]))

  )
