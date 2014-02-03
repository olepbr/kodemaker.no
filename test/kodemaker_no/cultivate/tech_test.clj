(ns kodemaker-no.cultivate.tech-test
  (:require [kodemaker-no.cultivate.tech :refer :all]
            [midje.sweet :refer :all]
            [kodemaker-no.validate :refer [validate-content]]
            [kodemaker-no.cultivate.content-shells :as c]))

(def content
  (c/content
   {:people {:magnar (c/person {:id :magnar
                                :name ["Magnar" "Sveen"]})
             :finnjoh (c/person {:id :finnjoh
                                 :name ["Finn" "J" "Johnsen"]})
             :christian (c/person {:id :christian
                                   :name ["Christian" "Johansen"]})}
    :tech {:react (c/tech {:id :react})
           :javascript (c/tech {:id :javascript})}}))

(defn cultivate [content]
  (cultivate-techs (validate-content content)))

(fact (-> content cultivate :react :url) => "/react/")

(fact
 "Anbefalinger blir overført til sine techs. Hvis flere anbefaler
  samme, så blir de slått sammen."

 (-> content
     (assoc-in [:people :magnar :recommendations]
               [(c/recommendation {:title "A post on React"
                                   :blurb "Den er bra."
                                   :link {:url "http://example.com", :text "!"}
                                   :tech [:react :web-performance]})])
     (assoc-in [:people :finnjoh :recommendations]
               [(c/recommendation {:title "A post on React"
                                   :blurb "Den er knall."
                                   :link {:url "http://example.com", :text "!"}
                                   :tech [:react :javascript]})
                (c/recommendation {:title "Another post"
                                   :blurb "Også bra"
                                   :link {:url "http://not-react.com", :text "!"}
                                   :tech [:javascript]})])
     cultivate :react :recommendations)

 => [{:title "A post on React"
      :blurb "Den er bra."
      :link {:url "http://example.com", :text "!"}
      :tech [:react :web-performance :javascript]
      :by [{:name "Magnar", :url "/magnar/"}
           {:name "Finn", :url "/finnjoh/"}]}])

(fact
 "Foredrag blir overført til sine techs. Hvis flere har holdt samme
  foredrag, så blir det slått sammen."

 (-> content
     (assoc-in [:people :christian :presentations]
               [{:title "Pure JavaScript"
                 :blurb "Kast de objekt-orienterte krykkene."
                 :urls {:video "http://vimeo.com/43808808"}
                 :thumb "/thumbs/presentations/functional-js.jpg"
                 :tech [:javascript]}
                {:title "Zombie TDD: Live parprogrammering"
                 :blurb "Vi setter oss ned med emacsen."
                 :tech [:javascript :tdd]
                 :urls {:video "http://vimeo.com/49485653"}
                 :thumb "/thumbs/presentations/zombie-tdd-live.jpg"}])
     (assoc-in [:people :magnar :presentations]
               [{:title "Zombie TDD: Live parprogrammering"
                 :blurb "Vi setter oss ned med emacsen."
                 :tech [:javascript :tdd]
                 :urls {:video "http://vimeo.com/49485653"}
                 :thumb "/thumbs/presentations/zombie-tdd-live.jpg"}])

     cultivate :javascript :presentations)

 => [{:title "Zombie TDD: Live parprogrammering"
      :blurb "Vi setter oss ned med emacsen."
      :tech [:javascript :tdd]
      :urls {:video "http://vimeo.com/49485653"}
      :thumb "/thumbs/presentations/zombie-tdd-live.jpg"
      :by [{:name "Magnar", :url "/magnar/"}
           {:name "Christian", :url "/christian/"}]}
     {:title "Pure JavaScript"
      :blurb "Kast de objekt-orienterte krykkene."
      :urls {:video "http://vimeo.com/43808808"}
      :thumb "/thumbs/presentations/functional-js.jpg"
      :tech [:javascript]
      :by [{:name "Christian", :url "/christian/"}]}])

(fact
 "Bloggposter blir overført til tech."

 (-> content
     (assoc-in [:people :magnar :blog-posts]
               [{:url "http://framsieutvikling.no/post/753317476"
                 :title "5 JavaScript-uvaner du må legge av deg"
                 :blurb "Jeg tør påstå at JavaScript er et av språkene
                       som er mest utsatt for cargo culting. For noen år siden
                       var det utstrakt klipping og liming, og uvanene spredte
                       seg fortere enn du kunne si globalt navnerom. Her er noen saker du må slutte med."
                 :tech [:javascript]}])

     cultivate :javascript :blog-posts)

 => [{:url "http://framsieutvikling.no/post/753317476"
      :title "5 JavaScript-uvaner du må legge av deg"
      :blurb "Jeg tør påstå at JavaScript er et av språkene
                       som er mest utsatt for cargo culting. For noen år siden
                       var det utstrakt klipping og liming, og uvanene spredte
                       seg fortere enn du kunne si globalt navnerom. Her er noen saker du må slutte med."
      :tech [:javascript]
      :by {:name "Magnar", :url "/magnar/"}}])
