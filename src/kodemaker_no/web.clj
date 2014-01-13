(ns kodemaker-no.web
  (:require [ring.middleware.content-type :refer [wrap-content-type]]
            [kodemaker-no.pages :refer [pages]]
            [stasis.core :as stasis]
            [optimus.assets :as assets]
            [optimus.prime :as optimus]
            [optimus.optimizations :as optimizations]
            [optimus.strategies :refer [serve-live-assets]]
            [optimus-img-transform.core :refer [transform-images]]
            [optimus.export]
            [clojure.java.io :as io]
            [config :refer [export-directory]]))

(defn get-assets []
  (assets/load-assets "public" ["/styles/responsive.css"
                                "/styles/unresponsive.css"
                                #"/photos/.*\.jpg"]))

(defn optimize [assets options]
  (-> assets
      (transform-images {:regexp #"/photos/.*\.jpg"
                         :quality 0.3
                         :width (* 290 2)
                         :progressive true})
      (optimizations/all options)))

(def app (-> (stasis/serve-pages pages)
             (optimus/wrap get-assets optimize serve-live-assets)
             wrap-content-type))

(defn export []
  (let [assets (optimize (get-assets) {})]
    (stasis/delete-directory! export-directory)
    (optimus.export/save-assets assets export-directory)
    (stasis/export-pages pages export-directory {:optimus-assets assets})))
