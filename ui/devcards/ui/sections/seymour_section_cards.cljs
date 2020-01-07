(ns ui.sections.seymour-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard seymour-section
  (sections/seymour-section
   {:pønt [{:kind :greater-than
            :position "bottom -550px left -310px"}
           {:kind :ascending-line
            :position "top -500px right -440px"}]
    :seymours
    [{:icon {:type :science/chemical :height 79}
      :title "Referanser"
      :text "Det er fleske meg ikke dårlig hvor mange artige prosjekter vi har fått være med på."
      :link {:text "Se referanser"
             :href "/referanser/"}}
     {:icon {:type :science/robot-1 :height 79}
      :title "Sjekk ut hvem vi har på laget 'æ, guttær!"
      :text "Vi har kun erfarne konsulenter med oss som liker å bryne seg på vanskelig oppgaver."
      :link {:text "Våre ansatte"
             :href "/folk/"}}
     {:icon {:type :computer/laptop-1 :height 79}
      :title "Kurs og workshops"
      :text "Her er en ganske kort tekst."
      :link {:text "Vi kan tilby"
             :href "/kurs/"}}]}))
