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
    :projects {:finn-reise (c/project {:id :finn-reise :logo "finn.png"})
               :finn-oppdrag (c/project {:id :finn-oppdrag :logo "finn.png"})
               :sp1 (c/project {:id :sp1 :logo "sp1.png"})}}))

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
                 :tech [:javascript]
                 :years [2008]}])
     cultivate :finn-reise :people)

 => [{:url "/magnar/"
      :first-name "Magnar"
      :full-name "Magnar Sveen"
      :thumb "/photos/people/magnar/side-profile.jpg"
      :description "Gjorde bra ting."
      :years [2008]}])

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

(fact
 "Tech blir aggregert personlige prosjektoppføringer. Den forsøker å
  ta hensyn til alles rekkefølge ved å flette først, og så fjerne
  duplikater."

 (-> content
     (assoc-in [:people :magnar :projects]
               [{:id :finn-reise
                 :customer "FINN Reise"
                 :description "Gjorde bra ting."
                 :tech [:javascript :testing :design]
                 :years []}])
     (assoc-in [:people :andersf :projects]
               [{:id :finn-reise
                 :customer "FINN Reise"
                 :description "Gjorde også bra ting."
                 :tech [:java :javascript :testing]
                 :years []}])
     cultivate :finn-reise :tech)

 => [{:id :javascript, :name "Javascript"}
     {:id :java, :name "Java"}
     {:id :testing, :name "Testing"}
     {:id :design, :name "Design"}])

(fact
 "Legger til relaterte prosjekter ved å se på logo"

 (-> content cultivate :finn-reise :related-projects)

 => (list {:awesomeness 0,
           :description "!",
           :id :finn-oppdrag,
           :illustration "/path",
           :logo "finn.png",
           :name "!"
           :url "/finn-oppdrag/"}))
