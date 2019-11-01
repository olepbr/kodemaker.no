(ns ^:figwheel-hooks ui.devcards
  (:require [devcards.core :as devcards]
            ui.color-cards
            ui.elements-cards
            ui.layout-cards
            ui.sections-cards
            ui.typography-cards))

(enable-console-print!)

(defn render []
  (devcards/start-devcard-ui!))

(defn ^:after-load render-on-relaod []
  (render))

(render)
