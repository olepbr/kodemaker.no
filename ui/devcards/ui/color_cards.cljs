(ns ui.color-cards
  (:require [dumdom.devcards :refer-macros [defcard]]))

(def color-names
  ["rouge"
   "taupe-foncé"
   "chocolat-au-lait"
   "puits-de-mine"
   "chablis"
   "blanc-rose"
   "blanc"
   "saumon"])

(def fg-colors
  {"puits-de-mine" "var(--blanc)"
   "taupe-foncé" "var(--blanc)"
   "chocolat-au-lait" "var(--blanc)"})

(defn color [name]
  [:div {:style {:border "1px solid #ddd"
                 :background (str "var(--" name ")")
                 :padding-bottom "100%"
                 :border-radius "2px"
                 :position "relative"}}
   [:div {:style {:position "absolute"
                  :top "50%"
                  :left "50%"
                  :font-size "18px"
                  :transform "translate(-50%, -50%)"
                  :color (get fg-colors name "var(--puits-de-mine)")}}
    name]])

(defcard colors
  [:div {:style {:display "grid"
                 :grid-template-columns "1fr 1fr 1fr 1fr"
                 :grid-gap "10px"}}
   (map color color-names)])
