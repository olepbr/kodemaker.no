(ns ui.sections.bruce-section
  "The Bruce section. Bruce? Yes, Bruce. Bruce BANNER, that's who."
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [title text link image-top image-right]}]
  [:div.section.bruce
   {:style {:background-color (str "var(--blanc)")}}
   [:div.content
    [:div.gutter.gutter-l
     {:style (-> {}
                 (l/add-pønt
                  [{:kind :dotgrid
                    :position "bottom -320px left 155px"}
                   {:kind :ascending-line
                    :position "left -325px bottom -140px"}
                   {:kind :descending-line
                    :position "left 220px top -240px"}
                   {:kind :descending-line
                    :position "left 880px top -240px"}
                   {:kind :greater-than-small
                    :position "left 700px top -40px"}]))}
     [:div.bruce-header (l/header)]
     [:div.bruce-content
      (e/h1 {} title)
      [:p.text text]
      (e/arrow-link link)]
     [:img.bruce-image-top.image-style-bruce-top {:src image-top}]
     [:img.bruce-image-right.image-style-bruce-right {:src image-right}]]]])
