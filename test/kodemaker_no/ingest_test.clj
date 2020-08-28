(ns kodemaker-no.ingest-test
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.atomic :as atomic]
            [kodemaker-no.ingest :as sut]
            [midje.sweet :refer [fact]]))

(def conn (atomic/create-database (str "datomic:mem://" (d/squuid))))
(sut/ingest-all conn "resources")

(fact "All techs have categories"
      (seq (sut/find-uncategorized-techs (d/db conn))) => nil)
