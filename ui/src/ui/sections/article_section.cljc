(ns ui.sections.article-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [article articles pønt class background]}]
  [:div.section.article-section
   {:className class
    :style (cond-> {:background-color (when background (str "var(--" (name background) ")"))}
             pønt (l/add-pønt pønt))}
   [:div.content
    (e/article article)
    (map e/article articles)]])
