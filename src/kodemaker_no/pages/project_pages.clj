(ns kodemaker-no.pages.project-pages)

(defn- render-person [person]
  [:div.media
   [:a.img.thumb.mts {:href (:url person)}
    [:img {:src (:thumb person)}]]
   [:div.bd
    [:h4.mtn (:full-name person)]
    [:p (:description person)]]])

(defn- render-people [people project]
  (list [:h2 "Våre folk på saken"]
        (map render-person (:people project))))

(defn- render-endorsement [endo]
  [:div.media
   (when (:photo endo)
     [:img.img.thumb.mts {:src (:photo endo)}])
   [:div.bd
    [:h4.mtn (:author endo)
     [:span.tiny " om "
      [:a {:href (-> endo :person :url)} (-> endo :person :first-name)]]]
    (when (:title endo)
      [:p.near (:title endo)])
    [:p [:q (:quote endo)]]]])

(defn- render-endorsements [endorsements project]
  (list [:h2 "Referanser"]
        (map render-endorsement endorsements)))

(defn- maybe-include [project kw f]
  (when (kw project)
    (f (kw project) project)))

(defn- project-page [project]
  {:title (:name project)
   :illustration (:logo project)
   :lead [:p (:description project)]
   :body (list
          (maybe-include project :people render-people)
          (maybe-include project :endorsements render-endorsements))})

(defn project-pages [projects]
  (into {} (map (juxt :url #(partial project-page %)) projects)))
