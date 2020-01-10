(ns kodemaker-no.ingestion.tech
  (:require [kodemaker-no.homeless :as h]))

(def tech-keys
  {:db/ident :id
   :tech/name :name
   :tech/description :description
   :tech/type :type
   :tech/illustration :illustration
   :tech/site :site
   :tech/ad :ad})

(def qualify-tech-kw (partial h/qualify "tech"))

(defn create-tx [file-name tech]
  [(-> tech
       (h/keep-vals tech-keys)
       (update :db/ident qualify-tech-kw)
       (merge (when (:description tech)
                {:page/uri (str (second (re-find #"tech(.*).edn" file-name)) "/")
                 :page/kind :page.kind/tech})))])

(defn create-tech-type-tx [file-name id->type]
  (for [[id type] id->type]
    {:db/ident (qualify-tech-kw id)
     :tech/type type}))

(defn create-tech-name-tx [file-name id->name]
  (for [[id tech-name] id->name]
    {:db/ident (qualify-tech-kw id)
     :tech/name tech-name}))
