(ns ui.sections.intro-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [article video]}]
  [:div.section
   [:div.content
    [:h3.h3.mbl (:title article)]
    [:div.text (:text article)]]])
