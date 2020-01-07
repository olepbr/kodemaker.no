(ns ui.sections.widescreen-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard widescreen-section
  (sections/widescreen-section
   {:image "/devcard_images/geir.jpg"
    :alt "Geir Oterhals på Oche"}))
