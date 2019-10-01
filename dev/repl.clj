(ns repl
  (:require [clojure.tools.namespace.repl :as c.t.n.r]
            [integrant.core :as ig]
            [integrant.repl]
            [kodemaker-no.web :as web]
            [ring.adapter.jetty :as jetty]))

(def config
  {:adapter/jetty {:port 3333 :handler (ig/ref :app/handler)}
   :app/handler {:new-site? true}})

(defmethod ig/init-key :app/handler [_ opts]
  (web/create-app))

(defmethod ig/init-key :adapter/jetty [_ {:keys [handler] :as opts}]
  (jetty/run-jetty handler (-> opts (dissoc :handler) (assoc :join? false))))

(defmethod ig/halt-key! :adapter/jetty [_ server]
  (.stop server))

(integrant.repl/set-prep! (constantly config))

(c.t.n.r/set-refresh-dirs "src" "test" "dev")

(defn start []
  (integrant.repl/go))

(defn stop []
  (integrant.repl/halt))

(defn reset []
  (integrant.repl/reset))
