(ns kodemaker-no.web
  (:require [clojure.core.memoize]
            [config :refer [export-directory]]
            [kodemaker-no.content :refer [load-content]]
            [kodemaker-no.cultivate :refer [cultivate-content]]
            [kodemaker-no.homeless :refer [wrap-content-type-utf-8]]
            [kodemaker-no.pages :as pages]
            [kodemaker-no.prepare-pages :refer [prepare-pages]]
            [kodemaker-no.validate :refer [validate-content]]
            [optimus.assets :as assets]
            [optimus.export]
            [optimus-img-transform.core :refer [transform-images]]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [stasis.core :as stasis]))

(defn get-assets []
  (assets/load-assets "public" ["/styles/responsive.css"
                                "/styles/unresponsive.css"
                                #"/illustrations/.*\.jpg"
                                #"/thumbs/.*\.jpg"
                                #"/references/.*\.jpg"
                                #"/photos/.*\.jpg"
                                #"/photos/.*\.svg"
                                #"/photos/.*\.png"
                                #"/logos/.*\.png"
                                #"/icons/.*\.png"
                                #"/images/.*\.png"]))

(defn get-pages []
  (-> (load-content)
      validate-content
      cultivate-content
      pages/create-pages
      prepare-pages))

(def optimize
  (-> (fn [assets options]
        (-> assets
            (transform-images {:regexp #"/photos/.+/side-profile\.jpg"
                               :quality 0.3
                               :crop {:offset [150 100]
                                      :size [400 300]}
                               :progressive true
                               :prefix "cropped/"})
            (transform-images {:regexp #"/photos/.*\.jpg"
                               :quality 0.3
                               :width (* 290 2)
                               :progressive true})
            (transform-images {:regexp #"/references/.*\.jpg"
                               :quality 0.3
                               :width (* 680 2)
                               :progressive true})
            (transform-images {:regexp #"/illustrations/.*\.jpg"
                               :quality 0.3
                               :width (* 210 2)
                               :progressive true})
            (transform-images {:regexp #"/thumbs/.*\.jpg"
                               :quality 0.3
                               :width (* 100 2)
                               :progressive false}) ; too small, will be > kb
            (optimizations/all options)))
      (clojure.core.memoize/lru {} :lru/threshold 3)))

(def app (-> (stasis/serve-pages get-pages)
             (optimus/wrap get-assets optimize serve-live-assets)
             wrap-content-type
             wrap-content-type-utf-8))

(defn export []
  (let [assets (optimize (get-assets) {})]
    (stasis/empty-directory! export-directory)
    (optimus.export/save-assets assets export-directory)
    (stasis/export-pages (get-pages) export-directory {:optimus-assets assets})))
