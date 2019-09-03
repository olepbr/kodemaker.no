(ns ui.elements)

(defn arrow [{:keys [width]}]
  [:div.arrow [:div.arrow-head]])

(defn arrow-link [{:keys [text url] :as params}]
  [:a.ib {:href url} text (arrow params)])
