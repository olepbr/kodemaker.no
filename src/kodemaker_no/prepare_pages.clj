(ns kodemaker-no.prepare-pages
  (:require [kodemaker-no.render-page :as layout]
            [kodemaker-no.homeless :refer [update-vals]]))

(defn prepare-page [get-page request]
  {:body (layout/render-page (get-page) request)})

(defn prepare-pages [pages]
  (update-vals pages #(partial prepare-page %)))
