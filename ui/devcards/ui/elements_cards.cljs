(ns ui.elements-cards
  (:require [devcards.core :refer-macros [defcard]]
            [sablono.core :refer [html]]
            [ui.elements :as e]))

(defcard arrow-link
  (html (e/arrow-link {:text "Se mer"
                       :href "https://wwww.kodemaker.no"})))

(defcard big-arrow-link
  (html (e/arrow-link {:text "Se mer"
                       :size :large
                       :href "https://wwww.kodemaker.no"})))

(defcard icon
  (html
   (e/icon {:type :computer/laptop-1 :width 100})))

(defcard seymour
  (html
   (e/seymour {:icon {:type :science/robot-2 :height 79}
               :title "Referanser"
               :text "Vi har kun erfarne konsulenter med oss, som liker å bryne seg på vanskelige oppgaver."
               :link {:text "Se referanser"
                      :href "/referanser/"}})))
