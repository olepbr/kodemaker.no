(ns repl
  (:require [clojure.tools.namespace.repl :as c.t.n.r]
            [integrant.core :as ig]
            integrant.repl
            [kodemaker-no.atomic :as a]
            [kodemaker-no.ingestion-watcher :as ingestion-watcher]
            [kodemaker-no.web :as web]
            [ring.adapter.jetty :as jetty]))

(def config
  {:adapter/jetty {:port 3333 :handler (ig/ref :app/handler)}
   :app/handler {:new-site? true :conn (ig/ref :datomic/conn)}
   :datomic/conn {:uri "datomic:mem://kodemaker"}
   :dev/ingestion-watcher {:directory "resources" :conn (ig/ref :datomic/conn)}})

(defmethod ig/init-key :app/handler [_ opts]
  (web/create-app opts))

(defmethod ig/init-key :adapter/jetty [_ {:keys [handler] :as opts}]
  (jetty/run-jetty handler (-> opts (dissoc :handler) (assoc :join? false))))

(defmethod ig/halt-key! :adapter/jetty [_ server]
  (.stop server))

(defmethod ig/init-key :datomic/conn [_ {:keys [uri]}]
  (a/create-database uri))

(defmethod ig/init-key :dev/ingestion-watcher [_ {:keys [directory conn]}]
  (ingestion-watcher/start! directory conn))

(defmethod ig/halt-key! :dev/ingestion-watcher [_ watcher]
  (ingestion-watcher/stop! watcher))

(integrant.repl/set-prep! (constantly config))

(c.t.n.r/set-refresh-dirs "src" "dev" "ui/src")

(defn start []
  (integrant.repl/go))

(defn stop []
  (integrant.repl/halt))

(defn reset []
  (integrant.repl/reset))
