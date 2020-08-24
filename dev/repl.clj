(ns repl
  (:require [clojure.tools.namespace.repl :as c.t.n.r]
            [integrant.core :as ig]
            integrant.repl
            [kodemaker-no.atomic :as a]
            [kodemaker-no.ingest :as ingest]
            [kodemaker-no.ingestion-watcher :as ingestion-watcher]
            [kodemaker-no.web :as web]
            [ring.adapter.jetty :as jetty]))

(defmacro with-timing-info [name exp]
  `(let [start# (System/currentTimeMillis)
         res# ~exp]
     (println "[app]" ~name "in" (- (System/currentTimeMillis) start#) "ms")
     res#))

(def config
  {:adapter/jetty {:port 3333 :handler (ig/ref :app/handler)}
   :app/handler {:new-site? true :conn (ig/ref :datomic/conn)}
   :datomic/conn {:uri "datomic:mem://kodemaker"}
   :dev/ingestion-watcher {:directory "resources" :conn (ig/ref :datomic/conn)}})

(defmethod ig/init-key :app/handler [_ opts]
  (with-timing-info "Created web app"
    (web/create-app opts)))

(defmethod ig/init-key :adapter/jetty [_ {:keys [handler port] :as opts}]
  (with-timing-info (str "Started jetty on port " port)
    (jetty/run-jetty handler (-> opts (dissoc :handler) (assoc :join? false)))))

(defmethod ig/halt-key! :adapter/jetty [_ server]
  (with-timing-info "Stopped jetty"
    (.stop server)))

(defmethod ig/init-key :datomic/conn [_ {:keys [uri]}]
  (with-timing-info "Created database"
    (a/create-database uri)))

(defmethod ig/init-key :dev/ingestion-watcher [_ {:keys [directory conn]}]
  (with-timing-info "Ingested all data"
    (ingest/ingest-all conn directory))
  (with-timing-info "Started watcher"
   (ingestion-watcher/start! directory conn)))

(defmethod ig/halt-key! :dev/ingestion-watcher [_ watcher]
  (with-timing-info "Stopped watcher"
    (ingestion-watcher/stop! watcher)))

(integrant.repl/set-prep! (constantly config))

(c.t.n.r/set-refresh-dirs "src" "dev" "ui/src")

(defn start []
  (integrant.repl/go))

(defn stop []
  (integrant.repl/halt))

(defn reset []
  (integrant.repl/reset))

(declare ring-app)
(defn init-app-for-ring! []
  (integrant.repl/set-prep! (constantly (dissoc config :adapter/jetty)))
  (start)
  (def ring-app (:app/handler integrant.repl.state/system)))

(comment
  (start)
  )
