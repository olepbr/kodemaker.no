(ns ui.sections.contact-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard contact-section
  (sections/contact-section
   {:email "kontakt@kodemaker.no"
    :phone "+47 22 82 20 80"
    :address "Universitetsgata 2, 0164 Oslo"
    :contacts [{:name "Kolbjørn Jetne"
                :title "Daglig leder"
                :phone "+47 957 45 096"
                :email "kolbjorn@kodemaker.no"
                :image-round "/devcard_images/person.png"
                :image-tall "/devcard_images/profile.jpg"}
               {:name "Gry Gautier Dale"
                :title "Lederassistent"
                :phone "+47 228 22 080"
                :email "gry@kodemaker.no"
                :image-round "/devcard_images/person.png"
                :image-tall "/devcard_images/profile.jpg"
                :curtain :right}]
    :map {:zoom 15
          :lat 59.914432
          :lon 10.731476
          :title "Universitetsgata 2, 0164 Oslo"
          :api-key "add-your-own-google-maps-api-key-for-localhost"
          :map-marker-url "/devcard_images/map-marker.png"}
    :link {:text "Alle ansatte"
           :href "/folk/"}
    :pønt [{:kind :greater-than
            :position "top -250px right 90%"}]}))
