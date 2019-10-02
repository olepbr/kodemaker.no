(ns kodemaker-no.atomic
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [datomic-type-extensions.api :as d]
            [java-time-dte.install :refer [define-dte]]
            [kodemaker-no.new-pages.frontpage :as frontpage]
            [kodemaker-no.render-new-page :refer [render-page]]))

(define-dte :data/edn :db.type/string
  [this] (pr-str this)
  [^String s] (edn/read-string s))

(defn create-database [uri]
  (d/create-database uri)
  (let [conn (d/connect uri)]
    @(d/transact conn (edn/read-string (slurp (io/resource "db-schema.edn"))))

    conn))

(defn serve-pages []
  (fn [request]
    (if (= "/" (:uri request))
      {:status 200
       :body (render-page (frontpage/create-page) request)
       :headers {"Content-Type" "text/html"}}
      {:status 404
       :body "Eg fann han ikkje"
       :headers {"Content-Type" "text/html"}})))
