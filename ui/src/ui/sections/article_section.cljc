(ns ui.sections.article-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [article background]}]
  [:div.section.article-section {:style {:background background}}
   [:div.content
    (e/article article)]])
