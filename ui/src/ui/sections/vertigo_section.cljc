(ns ui.sections.vertigo-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [title text link image]}]
  [:div.section.vertigo
   {:style {:background-color (str "var(--blanc)")}}
   [:div.content
    [:div.gutter.gutter-l.grid
     {:style (-> {}
                 (l/add-pønt
                  [{:kind :less-than
                    :position "right -300px top -410px"}]))}
     [:div.vertigo-media
      [:div.inner-media
       [:img.img {:src image}]]]
     [:div.vertigo-content
      [:div.inner-content
       (e/h2 {} title)
       [:p.text text]
       (e/arrow-link link)]]]]])
