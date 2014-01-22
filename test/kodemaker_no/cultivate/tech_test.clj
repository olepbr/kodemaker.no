(ns kodemaker-no.cultivate.tech-test
  (:require [kodemaker-no.cultivate.tech :refer :all]
            [midje.sweet :refer :all]))

(def content
  {:people {:magnars {:url "/magnar/"
                      :first-name "Magnar"}
            :finnjoh {:url "/finnjoh/"
                      :first-name "Finn"}}
   :tech {:react {:id :react}}})

(fact (-> content cultivate-techs :tech :react :url) => "/react/")

(fact
 "Anbefalinger blir overført til sine techs. Hvis flere anbefaler
  samme, så blir de slått sammen."

 (-> content
     (assoc-in [:people :magnars :recommendations]
               [{:title "A post on React"
                 :blurb "Den er bra."
                 :url "http://example.com"
                 :tech [{:id :react} {:id :web-performance}]}])
     (assoc-in [:people :finnjoh :recommendations]
               [{:title "A post on React"
                 :blurb "Den er knall."
                 :url "http://example.com"
                 :tech [{:id :react} {:id :javascript}]}
                {:title "Another post"
                 :blurb "Også bra"
                 :url "http://not-react.com"
                 :tech [{:id :javascript}]}])
     cultivate-techs :tech :react :recommendations)
 => [{:title "A post on React"
      :blurb "Den er bra."
      :url "http://example.com"
      :tech [{:id :react} {:id :web-performance} {:id :javascript}]
      :recommended-by [{:name "Magnar", :url "/magnar/"}
                       {:name "Finn", :url "/finnjoh/"}]}])
