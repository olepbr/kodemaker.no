(ns ui.sections.pønt-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [portrait-1 portrait-2 top-triangle bottom-triangle top-circle bottom-circle]}]
  [:div.section.pønt-section {:style {:background-color (str "var(--blanc-rose)")}}
   [:div.content
    [:div.gutter.gutter-xl
     [:div.pønt-item.portrait-1
      [:img.img.image-style-chocolate-triangle-pønt {:src portrait-1}]]

     [:div.pønt-item.top-triangle
      [:img.img.image-style-rouge-triangle-pønt {:src top-triangle}]]

     [:div.pønt-item.bottom-circle
      [:img.img.image-style-chocolate-circle-pønt {:src bottom-circle}]]

     [:div.pønt-item.portrait-2
      [:img.img.image-style-rouge-triangle-pønt {:src portrait-2}]]

     [:div.pønt-item.top-circle
      [:img.img.image-style-rouge-circle-pønt {:src top-circle}]]

     [:div.pønt-item.bottom-triangle
      [:img.img.image-style-chocolate-triangle-pønt {:src bottom-triangle}]]]

    ]])
