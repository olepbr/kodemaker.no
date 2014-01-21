(ns kodemaker-no.pages.person-pages)

(defn- person-page [person]
  {:title (:full-name person)
   :illustration (-> person :photos :half-figure)
   :lead [:p (:description person)]
   :aside [:div.tight
           [:h5 (:full-name person)]
           [:p
            (:title person) "<br>"
            [:span.nowrap (:phone-number person)] "<br>"
            [:a {:href (str "mailto:" (:email-address person))}
             (:email-address person)]]]})

(defn person-pages [people]
  (into {} (map (juxt :url #(partial person-page %)) people)))
