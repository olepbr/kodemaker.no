(ns ^:figwheel-hooks ui.devcards
  (:require [devcards.core :as devcards]
            ui.color-cards
            ui.elements-cards
            ui.layout-cards
            ui.sections.article-section-cards
            ui.sections.banner-section-cards
            ui.sections.bruce-section-cards
            ui.sections.enumeration-section-cards
            ui.sections.grid-section-cards
            ui.sections.hip-section-cards
            ui.sections.profile-section-cards
            ui.sections.pønt-section-cards
            ui.sections.seymour-section-cards
            ui.sections.titled-section-cards
            ui.sections.vertigo-section-cards
            ui.sections.widescreen-section-cards
            ui.typography-cards))

(enable-console-print!)

(defn render []
  (devcards/start-devcard-ui!))

(defn ^:after-load render-on-relaod []
  (render))

(render)
