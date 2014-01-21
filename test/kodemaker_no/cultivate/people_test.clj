(ns kodemaker-no.cultivate.people-test
  (:require [kodemaker-no.cultivate.people :refer :all]
            [midje.sweet :refer :all]))

(let [magnars {:id :magnars
               :name ["Magnar" "Sveen"]}
      finnjoh {:id :finnjoh
               :name ["Finn" "J" "Johnsen"]}
      content (cultivate-people {:people {:magnars magnars
                                          :finnjoh finnjoh}})
      people (:people content)]

  (fact (-> people :magnars :full-name) => "Magnar Sveen"
        (-> people :finnjoh :full-name) => "Finn J Johnsen")

  (fact (-> people :magnars :first-name) => "Magnar"
        (-> people :finnjoh :first-name) => "Finn")

  (fact (-> people :magnars :str) => "magnars"
        (-> people :finnjoh :str) => "finnjoh")

  (fact (-> people :magnars :url) => "/magnars/"
        (-> people :finnjoh :url) => "/finnjoh/")

  (fact (-> people :magnars :photos) => {:side-profile "/photos/people/magnars/side-profile.jpg"
                                         :half-figure "/photos/people/magnars/half-figure.jpg"}
        (-> people :finnjoh :photos) => {:side-profile "/photos/people/finnjoh/side-profile.jpg"
                                         :half-figure "/photos/people/finnjoh/half-figure.jpg"})

  )
