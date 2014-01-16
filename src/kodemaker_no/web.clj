(ns kodemaker-no.web
  (:require [kodemaker-no.pages :as pages]
            [kodemaker-no.cultivate :refer [cultivate-content]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [stasis.core :as stasis :refer [slurp-directory]]
            [optimus.assets :as assets]
            [optimus.prime :as optimus]
            [optimus.optimizations :as optimizations]
            [optimus.strategies :refer [serve-live-assets]]
            [optimus-img-transform.core :refer [transform-images]]
            [optimus.export]
            [kodemaker-no.homeless :refer [wrap-content-type-utf-8]]
            [config :refer [export-directory]]))

(defn get-assets []
  (assets/load-assets "public" ["/styles/responsive.css"
                                "/styles/unresponsive.css"
                                #"/photos/.*\.jpg"]))

(defn load-content []
  {:people (->> (slurp-directory "resources/people/" #"\.edn$")
                (vals)
                (map read-string))
   :articles (slurp-directory "resources/articles/" #"\.adoc$")})

(defn get-pages []
  (pages/get-pages (cultivate-content (load-content))))

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
