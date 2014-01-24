(ns kodemaker-no.cultivate.tech-test
  (:require [kodemaker-no.cultivate.tech :refer :all]
            [midje.sweet :refer :all]))

(def content
  {:people {:magnar {:id :magnar
                      :name ["Magnar" "Sveen"]}
            :finnjoh {:id :finnjoh
                      :name ["Finn" "J" "Johnsen"]}
            :christian {:id :christian
                        :name ["Christian" "Johansen"]}}
   :tech {:react {:id :react}
          :javascript {:id :javascript}}})

(fact (-> content cultivate-techs :react :url) => "/react/")

(fact
 "Anbefalinger blir overført til sine techs. Hvis flere anbefaler
  samme, så blir de slått sammen."

 (-> content
     (assoc-in [:people :magnar :recommendations]
               [{:title "A post on React"
                 :blurb "Den er bra."
                 :url "http://example.com"
                 :tech [:react :web-performance]}])
     (assoc-in [:people :finnjoh :recommendations]
               [{:title "A post on React"
                 :blurb "Den er knall."
                 :url "http://example.com"
                 :tech [:react :javascript]}
                {:title "Another post"
                 :blurb "Også bra"
                 :url "http://not-react.com"
                 :tech [:javascript]}])
     cultivate-techs :react :recommendations)

 => [{:title "A post on React"
      :blurb "Den er bra."
      :url "http://example.com"
      :tech [:react :web-performance :javascript]
      :recommended-by [{:name "Magnar", :url "/magnar/"}
                       {:name "Finn", :url "/finnjoh/"}]}])

(fact
 "Foredrag blir overført til sine techs. Hvis flere har holdt samme
  foredrag, så blir det slått sammen."

 (-> content
     (assoc-in [:people :christian :presentations]
               [{:title "Pure JavaScript"
                 :blurb "Kast de objekt-orienterte krykkene."
                 :urls {:video "http://vimeo.com/43808808"}
                 :thumb "/thumbs/videos/functional-js.jpg"
                 :tech [:javascript]}
                {:title "Zombie TDD: Live parprogrammering"
                 :blurb "Vi setter oss ned med emacsen."
                 :tech [:javascript :tdd]
                 :urls {:video "http://vimeo.com/49485653"}
                 :thumb "/thumbs/videos/zombie-tdd-live.jpg"}])
     (assoc-in [:people :magnar :presentations]
               [{:title "Zombie TDD: Live parprogrammering"
                 :blurb "Vi setter oss ned med emacsen."
                 :tech [:javascript :tdd]
                 :urls {:video "http://vimeo.com/49485653"}
                 :thumb "/thumbs/videos/zombie-tdd-live.jpg"}])

     cultivate-techs :javascript :presentations)

 => [{:title "Pure JavaScript"
      :blurb "Kast de objekt-orienterte krykkene."
      :urls {:video "http://vimeo.com/43808808"}
      :thumb "/thumbs/videos/functional-js.jpg"
      :tech [:javascript]
      :by [{:name "Christian", :url "/christian/"}]}
     {:title "Zombie TDD: Live parprogrammering"
      :blurb "Vi setter oss ned med emacsen."
      :tech [:javascript :tdd]
      :urls {:video "http://vimeo.com/49485653"}
      :thumb "/thumbs/videos/zombie-tdd-live.jpg"
      :by [{:name "Christian", :url "/christian/"}
           {:name "Magnar", :url "/magnar/"}]}])
