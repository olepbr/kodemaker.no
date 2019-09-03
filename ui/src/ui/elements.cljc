(ns ui.elements)

(defn arrow [{:keys [width]}]
  [:div.arrow [:div.arrow-head]])

(defn arrow-link [{:keys [text href] :as params}]
  [:a.ib {:href href} text (arrow params)])
