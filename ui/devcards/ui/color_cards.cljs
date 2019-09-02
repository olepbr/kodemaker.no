(ns ui.color-cards
  (:require [devcards.core :refer-macros [defcard]]
            [sablono.core :refer [html]]))

(def color-names ["red" "salmon" "dark-taupe" "milk-chocolate" "mine-shaft" "rose-white" "white"])

(def fg-colors
  {"mine-shaft" "var(--white)"
   "dark-taupe" "var(--white)"
   "milk-chocolate" "var(--white)"})

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
                  :color (get fg-colors name "var(--mine-shaft)")}}
    name]])

(defcard colors
  (html
   [:div {:style {:display "grid"
                  :grid-template-columns "1fr 1fr 1fr 1fr"
                  :grid-gap "10px"}}
    (map color color-names)]))
