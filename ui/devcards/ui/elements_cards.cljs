(ns ui.elements-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]))

(defcard arrow-link
  (e/arrow-link {:text "Se mer"
                 :href "https://wwww.kodemaker.no"}))

(defcard big-arrow-link
  (e/arrow-link {:text "Se mer"
                 :size :large
                 :href "https://wwww.kodemaker.no"}))

(defcard icon
  (e/icon {:type :computer/laptop-1 :width 100}))

(defcard seymour
  (e/seymour {:icon {:type :science/robot-2 :height 79}
              :title "Referanser"
              :text "Vi har kun erfarne konsulenter med oss, som liker å bryne seg på vanskelige oppgaver."
              :link {:text "Se referanser"
                     :href "/referanser/"}}))
