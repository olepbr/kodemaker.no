(ns ui.sections.bruce-section
  "The Bruce section. Bruce? Yes, Bruce. Bruce BANNER, that's who."
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [title text link image-front image-back]}]
  [:div.section.bruce
   {:style {:background-color (str "var(--blanc)")}}
   [:div.content
    [:div.gutter.gutter-l
     [:div.bruce-header (l/header)]
     [:div.bruce-content
      (e/h0 {} title)
      [:p.text text]
      (e/arrow-link link)]
     [:div.bruce-image-front
      [:img {:src image-front}]]
     [:div.bruce-image-back
      [:img {:src image-back}]]]]])
