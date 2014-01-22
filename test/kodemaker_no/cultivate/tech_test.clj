(ns kodemaker-no.cultivate.tech-test
  (:require [kodemaker-no.cultivate.tech :refer :all]
            [midje.sweet :refer :all]))

(def content
  {:people {:magnars {:id :magnars
                      :name ["Magnar" "Sveen"]}
            :finnjoh {:id :finnjoh
                      :name ["Finn" "J" "Johnsen"]}}
   :tech {:react {:id :react}}})

(fact (-> content cultivate-techs :react :url) => "/react/")

(fact
 "Anbefalinger blir overført til sine techs. Hvis flere anbefaler
  samme, så blir de slått sammen."

 (-> content
     (assoc-in [:people :magnars :recommendations]
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
      :recommended-by [{:name "Magnar", :url "/magnars/"}
                       {:name "Finn", :url "/finnjoh/"}]}])
