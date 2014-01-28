(ns kodemaker-no.cultivate.projects-test
  (:require [kodemaker-no.cultivate.projects :refer :all]
            [midje.sweet :refer :all]
            [kodemaker-no.validate :refer [validate-content]]
            [kodemaker-no.cultivate.content-shells :as c]))

(def content
  (c/content
   {:people {:magnar (c/person {:id :magnar
                                :name ["Magnar" "Sveen"]})
             :andersf (c/person {:id :andersf
                                 :name ["Anders" "Furseth"]})}
    :projects {:finn-reise (c/project {:id :finn-reise})}}))

(defn cultivate [content]
  (cultivate-projects (validate-content content)))

(fact (-> content cultivate :finn-reise :url) => "/finn-reise/")

(fact
 "Personer som deltok på prosjektet blir lagt til."

 (-> content
     (assoc-in [:people :magnar :projects]
               [{:id :finn-reise
                 :customer "FINN Reise"
                 :description "Gjorde bra ting."
                 :tech [:javascript]}])
     cultivate :finn-reise :people)

 => [{:url "/magnar/"
      :first-name "Magnar"
      :full-name "Magnar Sveen"
      :thumb "/photos/people/magnar/side-profile.jpg"
      :description "Gjorde bra ting."}])

(fact
 "Personlige referanser blir lagt til."

 (-> content
     (assoc-in [:people :magnar :endorsements]
               [{:project :finn-reise
                 :author "Geir Pettersen"
                 :photo "/thumbs/faces/geir-pettersen.jpg"
                 :quote "Jeg liker brettspill, og det gjør Magnar og."}])
     cultivate :finn-reise :endorsements)

 => [{:project :finn-reise
      :person {:first-name "Magnar"
               :full-name "Magnar Sveen"
               :url "/magnar/"
               :thumb "/photos/people/magnar/side-profile.jpg"}
      :author "Geir Pettersen"
      :photo "/thumbs/faces/geir-pettersen.jpg"
      :quote "Jeg liker brettspill, og det gjør Magnar og."}])
