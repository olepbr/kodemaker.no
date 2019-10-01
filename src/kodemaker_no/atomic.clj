(ns kodemaker-no.atomic
  (:require [kodemaker-no.images :as images]
            [kodemaker-no.render-new-page :refer [render-page]]))

(def frontpage
  {:sections [{:kind :bruce
               :title "Et unikt team av señiorutviklere"
               :text "Vi håndplukker erfarne konsulenter som er selvgående og
                      trygge på sine meninger. Våre folk har sterkt fokus på
                      kompetanseoverføring i team og tar komplekse oppgaver med
                      tunge integrasjoner."
               :link {:text "Jobb med oss"
                      :href "/jobb/"}
               :image-top (images/url-to :bruce-top "foto/alf-kristian-confirmation-bias.jpg")
               :image-right (images/url-to :bruce-right "foto/alf-kristian-confirmation-bias.jpg")}
              {:kind :seymour
               :color :blanc-rose
               :seymours
               [{:icon {:type :science/chemical :height 79}
                 :title "Referanser"
                 :text "Det er fleske meg ikke dårlig hvor mange artige prosjekter vi har fått være med på."
                 :link {:text "Se referanser"
                        :href "/referanser/"}}
                {:icon {:type :science/robot-1 :height 79}
                 :title "Sjekk ut hvem vi har på laget 'æ, guttær!"
                 :text "Vi har kun erfarne konsulenter med oss som liker å bryne seg på vanskelig oppgaver."
                 :link {:text "Våre ansatte"
                        :href "/folk/"}}
                {:icon {:type :computer/laptop-1 :height 79}
                 :title "Kurs og workshops"
                 :text "Her er en ganske kort tekst."
                 :link {:text "Vi kan tilby"
                        :href "/kurs/"}}]}
              {:kind :footer}]})

(defn serve-pages []
  (fn [request]
    {:status 200
     :body (render-page frontpage request)
     :headers {"Content-Type" "text/html"}}))
