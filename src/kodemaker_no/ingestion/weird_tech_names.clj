(ns kodemaker-no.ingestion.weird-tech-names
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [datomic-type-extensions.api :as d]))

(defn create-tx [id->name]
  (for [[id tech-name] id->name]
    {:db/ident (keyword "tech" (name id))
     :tech/name tech-name}))
