(ns ui.sections.hyrule-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard hyrule-section
  (sections/hyrule-section
   {:contents [[:p "Litt tekst her"]
               [:p "Litt tekst her også"]
               [:p "Mer tekst nedentil"]]}))
