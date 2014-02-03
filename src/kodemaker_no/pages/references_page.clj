(ns kodemaker-no.pages.references-page)

(defn- render-reference [{:keys [url logo name description]}]
  (list
   [:h3 [:a {:href url} name]]
   [:p description " "
    [:a.nowrap {:href url} "Se referansen"]]))

(defn- render-reference-group [[logo references]]
  [:div.line
   [:div.unit.s-1of3
    [:div.bd
     [:a.block.mod {:href (-> references first :url)} [:img {:src logo}]]]]
   [:div.lastUnit
    [:div.bd
     (->> references
          (sort-by :awesomeness)
          (reverse)
          (map render-reference))]]])

(defn- max-awesomeness [[_ references]]
  (apply max (map :awesomeness references)))

(defn references-page [projects]
  {:title "Referanser"
   :body (->> projects
              (group-by :logo)
              (sort-by max-awesomeness)
              (reverse)
              (map render-reference-group)
              (interpose [:hr]))})
