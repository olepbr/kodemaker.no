(ns kodemaker-no.pages.references-page)

(defn- render-reference [project]
  [:div.line
   [:div.unit.s-1of3
    [:div.bd
     [:a.block.mod {:href (:url project)} [:img {:src (:logo project)}]]]]
   [:div.lastUnit
    [:div.bd
     [:h3 [:a {:href (:url project)} (:name project)]]
     [:p (:description project) " "
      [:a.nowrap {:href (:url project)} "Se referansen"]]]]])

(defn references-page [projects]
  {:title "Referanser"
   :body (interpose [:hr] (map render-reference projects))})
