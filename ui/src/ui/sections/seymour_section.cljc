(ns ui.sections.seymour-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [color seymours]}]
  [:div.section {:style (-> {:background-color (str "var(--" (name (or color :white)) ")")}
                            (l/add-pønt [{:kind :greater-than
                                          :position "bottom -550px left -310px"}
                                         {:kind :ascending-line
                                          :position "top -500px right -440px"}]))}
   [:div.content.whitespaceorama
    [:div.trigrid
     (for [seymour seymours]
       [:div
        (e/seymour seymour)])]]])
