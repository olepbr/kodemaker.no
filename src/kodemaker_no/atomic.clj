(ns kodemaker-no.atomic
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [datomic-type-extensions.api :as d]
            [java-time-dte.install :refer [define-dte]]
            [kodemaker-no.images :as images]
            [kodemaker-no.new-pages.article-page :as article-page]
            [kodemaker-no.new-pages.blog-post :as blog-post]
            [kodemaker-no.new-pages.frontpage :as frontpage]
            [kodemaker-no.new-pages.profile-page :as profile-page]
            [kodemaker-no.new-pages.reference-page :as reference-page]
            [kodemaker-no.new-pages.people-page :as people-page]
            [kodemaker-no.new-pages.tech-page :as tech-page]
            [kodemaker-no.prepare-pages :refer [post-process-page]]
            [kodemaker-no.render-new-page :refer [render-page]]))

(define-dte :data/edn :db.type/string
  [this] (pr-str this)
  [^String s] (edn/read-string s))

(defn create-database [uri]
  (d/create-database uri)
  (let [conn (d/connect uri)]
    @(d/transact conn (edn/read-string (slurp (io/resource "db-schema.edn"))))

    conn))

(defn serve-page [page request]
  {:status 200
   :body (-> page
             (render-page request)
             (post-process-page images/image-asset-config request))
   :headers {"Content-Type" "text/html"}})

(defn handle-request [conn request]
  (when-let [e (d/entity (d/db conn) [:page/uri (:uri request)])]
    (serve-page (case (:page/kind e)
                  :page.kind/article (article-page/create-page e)
                  :page.kind/blog-post (blog-post/create-page e)
                  :page.kind/frontpage (frontpage/create-page)
                  :page.kind/profile (profile-page/create-page e)
                  :page.kind/reference (reference-page/create-page e)
                  :page.kind/tech (tech-page/create-page e)
                  :page.kind/people (people-page/create-page e))
                request)))

(defn serve-pages [conn]
  (fn [request]
    (or (handle-request conn request)
        {:status 404
         :body "Eg fann han ikkje"
         :headers {"Content-Type" "text/html"}})))

(comment
  (def conn (d/connect "datomic:mem://kodemaker"))

  (->> conn
       d/db
       (d/q '[:find ?kind
              :in $
              :where
              [?e :page/kind ?kind]])
       (map first)
       set)

  (def reference
    (->> (d/q '[:find ?e
                :in $ ?uri
                :where
                [?e :page/uri ?uri]]
              (d/db conn)
              "/referanser/oche-dart/")
         ffirst
         (d/entity (d/db conn))))

  (into {} reference)

)
