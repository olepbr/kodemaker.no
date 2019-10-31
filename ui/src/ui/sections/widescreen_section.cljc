(ns ui.sections.widescreen-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [image alt background]}]
  [:div.section.widescreen
   {:style {:background-color (when background (str "var(--" (name background) ")"))}}
   [:div.content.content-l
    [:img.img {:src image :alt alt}]]])

