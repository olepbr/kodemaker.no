(ns ui.sections.widescreen-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections.widescreen-section :as section]))

(defcard
  (section/render
   {:image "/devcard_images/geir.jpg"
    :alt "Geir Oterhals på Oche"}))
