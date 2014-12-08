(ns kodemaker-no.render-page
  (:require [kodemaker-no.render-old-page :as old]))

(defn render-page [page request]
  (old/render-page page request))
