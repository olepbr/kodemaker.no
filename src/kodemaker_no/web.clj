(ns kodemaker-no.web
  (:require [ring.middleware.content-type :refer [wrap-content-type]]
            [kodemaker-no.pages :refer [pages]]
            [stasis.core :as stasis]
            [optimus.assets :as assets]
            [optimus.prime :as optimus]
            [optimus.optimizations :as optimizations]
            [optimus.strategies :refer [serve-live-assets]]
            [optimus.export]))

(defn get-assets []
  (assets/load-assets "public" ["/styles/responsive.css"
                                "/styles/unresponsive.css"
                                #"/photos/.*\.jpg"]))

(def app (-> (stasis/serve-pages pages)
             (optimus/wrap get-assets
                           optimizations/all
                           serve-live-assets)
             wrap-content-type))

(def target-dir "./build/")

(defn export []
  (let [assets (optimizations/all (get-assets) {})]
    (stasis/delete-directory! target-dir)
    (optimus.export/save-assets assets target-dir)
    (stasis/export-pages pages target-dir {:optimus-assets assets})))
