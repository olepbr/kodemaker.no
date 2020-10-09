(ns kodemaker-no.ingest-test
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.atomic :as atomic]
            [kodemaker-no.ingest :as sut]
            [midje.sweet :refer [fact]]))

(def conn (atomic/create-database (str "datomic:mem://" (d/squuid))))
(sut/ingest-all conn "resources")

(fact "All techs have categories, if not they should be added to resources/tech-categories.edn"
      (seq (sut/find-uncategorized-techs (d/db conn))) => nil)
