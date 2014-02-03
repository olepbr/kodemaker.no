(ns kodemaker-no.pages.references-page)

(defn- render-reference [{:keys [url logo name description]}]
  [:div.line
   [:div.unit.s-1of3
    [:div.bd
     [:a.block.mod {:href url} [:img {:src logo}]]]]
   [:div.lastUnit
    [:div.bd
     [:h3 [:a {:href url} name]]
     [:p description " "
      [:a.nowrap {:href url} "Se referansen"]]]]])

(defn references-page [projects]
  {:title "Referanser"
   :body (->> projects
              (map render-reference)
              (interpose [:hr]))})
