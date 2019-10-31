(ns ui.sections.article-section
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn render [{:keys [article pønt background]}]
  [:div.section.article-section
   {:style (cond-> {:background-color (when background (str "var(--" (name background) ")"))}
             pønt (l/add-pønt pønt))}
   [:div.content
    (e/article article)]])
