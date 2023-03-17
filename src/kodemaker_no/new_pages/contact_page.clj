(ns kodemaker-no.new-pages.contact-page
  (:require [datomic-type-extensions.api :as d]))

(defn contact [person]
  {:name (:person/full-name person)
   :title (:person/title person)
   :href (:page/uri person)
   :phone (:person/phone-number person)
   :email (:person/email-address person)
   :image-round (str "/vcard-small" (first (:person/portraits person)))
   :image-tall (first (:person/profile-pictures person))})

(defn create-page [db]
  {:title "Kontakt"
   :sections
   [{:kind :contact
     :email "kontakt@kodemaker.no"
     :phone "+47 22 82 20 80"
     :address "Universitetsgata 2, 0164 Oslo"
     :contacts [(contact (d/entity db :person/kolbjorn))
                (-> (contact (d/entity db :person/marte))
                    (assoc :curtain :right))]
     :map {:zoom 15
           :lat 59.917369
           :lon 10.740605
           :title "Universitetsgata 2, 0164 Oslo"
           :api-key "AIzaSyDi89iBAXS9WK22fa7ua4ruhVssJLpAb9w"
           :map-marker-url "/images/map-marker.png"}
     :link {:text "Alle ansatte"
            :href "/folk/"}
     :pønt [{:kind :greater-than
             :position "top -250px right 90%"}]}]})

(comment
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  (create-page db)

  )
