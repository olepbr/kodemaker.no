(ns kodemaker-no.prepare-pages
  (:require [kodemaker-no.render-page :refer [render-page]]
            [kodemaker-no.homeless :refer [update-vals with-html-transform]]
            [optimus.link :as link]))

(defn- optimize-path-fn [request]
  (fn [src]
    (or (not-empty (link/file-path request src))
        (throw (Exception. (str "Asset not loaded: " src))))))

(defn- use-optimized-images [html request]
  (with-html-transform html
    [:img] #(update-in % [:attrs :src] (optimize-path-fn request))))

(defn prepare-page [get-page request]
  {:body (-> (get-page)
             (render-page request)
             (use-optimized-images request))})

(defn prepare-pages [pages]
  (update-vals pages #(partial prepare-page %)))
