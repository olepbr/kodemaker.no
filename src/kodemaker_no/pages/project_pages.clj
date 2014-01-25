(ns kodemaker-no.pages.project-pages)

(defn- render-person [person]
  [:div.media
   [:a.img.thumb.mts {:href (:url person)}
    [:img {:src (:thumb person)}]]
   [:div.bd
    [:h4.mtn (:name person)]
    [:p (:description person)]]])

(defn- project-page [project]
  {:title (:name project)
   :illustration (:logo project)
   :lead [:p (:description project)]
   :body (list
          [:h2 "Våre folk på saken"]
          (map render-person (:people project)))})

(defn project-pages [projects]
  (into {} (map (juxt :url #(partial project-page %)) projects)))
