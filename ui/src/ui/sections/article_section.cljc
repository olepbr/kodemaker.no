(ns ui.sections.article-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [article video]}]
  [:div.section
   [:div.content
    (e/article article)]])
