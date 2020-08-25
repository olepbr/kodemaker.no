(ns kodemaker-no.new-pages.open-source
  (:require [datomic-type-extensions.api :as d]))

(defn proglang [project]
  (let [db (d/entity-db project)]
    (->> (:oss-project/techs project)
         (map #(d/entity db [:db/ident %]))
         (filter (comp #{:proglang} :tech/type))
         first)))

(defn significant-tech [project]
  (or (proglang project)
      (->> [:db/ident (first (:oss-project/techs project))]
           (d/entity (d/entity-db project)))))

(comment

  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))
  (def magnar (d/entity db [:db/ident :person/magnar]))

  (into {} (first (:person/open-source-contributions magnar)))
  (into {} (significant-tech (first (:person/open-source-contributions magnar))))
  (into {} (proglang (first (:person/open-source-contributions magnar))))

)
