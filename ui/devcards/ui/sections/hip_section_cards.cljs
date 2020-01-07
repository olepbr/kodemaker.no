(ns ui.sections.hip-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard hip-section
  (sections/hip-section
   {:title "Eivind snakker gjerne om"
    :single {:title "Fløyte"
             :content "Eivind spilte fløyte i skolekorps i ungdommen. Dette er en hobby han har tatt opp igjen for noen år siden. Spiller i Grav Musikkorps i Bærum."
             :image "/devcard_images/eivind_snowboard.png"}}))
