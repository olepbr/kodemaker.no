(ns kodemaker-no.ingestion.tech
  (:require [clojure.set :as set]))

(def tech-keys
  {:id :db/ident
   :name :tech/name
   :description :tech/description
   :type :tech/type
   :illustration :tech/illustration
   :site :tech/site
   :ad :tech/ad})

(defn create-tx [file-name tech]
  [(-> tech
       (select-keys (keys tech-keys))
       (set/rename-keys tech-keys)
       (update :db/ident #(keyword "tech" (name %)))
       (assoc :page/uri (str (second (re-find #"tech(.*).edn" file-name)) "/")))])
