(ns kodemaker-no.atomic
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [datomic-type-extensions.api :as d]
            [java-time-dte.install :refer [define-dte]]
            [kodemaker-no.images :as images]
            [kodemaker-no.new-pages.blog-post :as blog-post]
            [kodemaker-no.new-pages.frontpage :as frontpage]
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
  (cond
    (= "/" (:uri request))
    (serve-page (frontpage/create-page) request)

    :else
    (when-let [e (d/entity (d/db conn) [:page/uri (:uri request)])]
      (serve-page (case (:page/kind e)
                    :tech-page (tech-page/create-page e)
                    :blog-post (blog-post/create-page e))
                  request))))

(defn serve-pages [conn]
  (fn [request]
    (or (handle-request conn request)
        {:status 404
         :body "Eg fann han ikkje"
         :headers {"Content-Type" "text/html"}})))
