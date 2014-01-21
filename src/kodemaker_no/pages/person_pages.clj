(ns kodemaker-no.pages.person-pages)

(defn- render-recommendation [rec]
  (list [:h3 [:a {:href (:url rec)} (:title rec)]]
        [:p (:blurb rec)]))

(defn- render-recommendations [person recs]
  (list [:h2 (str (:genitive person) " Anbefalinger")]
        (map render-recommendation recs)))

(defn- person-page [person]
  {:title (:full-name person)
   :illustration (-> person :photos :half-figure)
   :lead [:p (:description person)]
   :aside [:div.tight
           [:h4 (:full-name person)]
           [:p
            (:title person) "<br>"
            [:span.nowrap (:phone-number person)] "<br>"
            [:a {:href (str "mailto:" (:email-address person))}
             (:email-address person)]]]
   :body (list
          (when-let [recs (:recommendations person)]
            (render-recommendations person recs)))})

(defn person-pages [people]
  (into {} (map (juxt :url #(partial person-page %)) people)))
