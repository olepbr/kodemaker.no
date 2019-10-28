(ns ui.sections.banner-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [logo text]}]
  [:div.section
   {:style {:background-color (str "var(--blanc)")}}
   [:div.content.tac.whitespaceorama
    [:img.banner-logo {:src logo}]
    (e/h1 {} text)]])
