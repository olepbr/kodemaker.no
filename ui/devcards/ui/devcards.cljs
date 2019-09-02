(ns ^:figwheel-hooks ui.devcards
  (:require [ui.typo-cards]))

(enable-console-print!)

(defn render []
  (devcards.core/start-devcard-ui!))

(defn ^:after-load render-on-relaod []
  (render))

(render)
