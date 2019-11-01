(ns kodemaker-no.ingestion.tech
  (:require [kodemaker-no.homeless :refer [select-renamed-keys qualify]]))

(def tech-keys
  {:id :db/ident
   :name :tech/name
   :description :tech/description
   :type :tech/type
   :illustration :tech/illustration
   :site :tech/site
   :ad :tech/ad})

(def qualify-tech-kw (partial qualify "tech"))

(defn create-tx [file-name tech]
  [(-> tech
       (select-renamed-keys tech-keys)
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
