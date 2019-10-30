(ns ui.sections.widescreen-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [image alt]}]
  [:div.section.widescreen
   [:div.content.content-l
    [:img.img {:src image :alt alt}]]])

