(ns kodemaker-no.web
  (:require [ring.middleware.content-type :refer [wrap-content-type]]
            [kodemaker-no.pages :refer [get-pages]]
            [stasis.core :as stasis]
            [optimus.assets :as assets]
            [optimus.prime :as optimus]
            [optimus.optimizations :as optimizations]
            [optimus.strategies :refer [serve-live-assets]]
            [optimus-img-transform.core :refer [transform-images]]
            [optimus.export]
            [kodemaker-no.homeless :refer [wrap-content-type-utf-8]]
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

(def app (-> (stasis/serve-pages get-pages)
             (optimus/wrap get-assets optimize serve-live-assets)
             wrap-content-type
             wrap-content-type-utf-8))

(defn export []
  (let [assets (optimize (get-assets) {})]
    (stasis/delete-directory! export-directory)
    (optimus.export/save-assets assets export-directory)
    (stasis/export-pages (get-pages) export-directory {:optimus-assets assets})))
