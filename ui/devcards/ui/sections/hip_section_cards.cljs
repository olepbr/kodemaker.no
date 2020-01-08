(ns ui.sections.hip-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard hip-section
  (sections/hip-section
   {:title "Eivind snakker gjerne om"
    :single {:title "Fløyte"
             :content "Eivind spilte fløyte i skolekorps i ungdommen. Dette er en hobby han har tatt opp igjen for noen år siden. Spiller i Grav Musikkorps i Bærum."
             :image "/devcard_images/eivind-flute.png"}}))

(defcard hip-section
  (sections/hip-section
   {:title "Eivind snakker gjerne om"
    :left {:title "Fløyte"
           :content "Eivind spilte fløyte i skolekorps i ungdommen. Dette er en hobby han har tatt opp igjen for noen år siden. Spiller i Grav Musikkorps i Bærum."
           :image "/devcard_images/eivind-flute.png"}
    :right {:title "Snowboard"
            :content "Snowboard har vært en hobby siden videregående, og det blir fortsatt noen turer hvert år. De siste årene har barna også blitt med, noe som gjør det ekstra stas :)"
            :image "/devcard_images/eivind_snowboard.jpg"}}))
