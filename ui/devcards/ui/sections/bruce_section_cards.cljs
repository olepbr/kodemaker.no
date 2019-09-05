(ns ui.sections.bruce-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.sections.bruce-section :as section]))

(defcard
  (section/render
   {:title "Et unikt team av señiorutviklere"
    :text "Vi håndplukker erfarne konsulenter som er selvgående og trygge på
    sine meninger. Våre folk har sterkt fokus på kompetanseoverføring i team og
    tar komplekse oppgaver med tunge integrasjoner."
    :link {:text "Jobb med oss"
           :href "/jobb/"}
    :image-top "/devcard_images/bruce-top.png"
    :image-right "/devcard_images/bruce-right.png"}))
