(ns ui.sections.grid-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [items background]}]
  [:div.section.grid-section
   {:style {:background-color (when background (str "var(--" background ")"))}}
   [:div.content
    (e/grid items)]])
