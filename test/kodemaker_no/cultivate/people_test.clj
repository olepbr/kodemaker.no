(ns kodemaker-no.cultivate.people-test
  (:require [kodemaker-no.cultivate.people :refer :all]
            [midje.sweet :refer :all]))

(let [magnars {:id :magnars
               :name ["Magnar" "Sveen"]}
      finnjoh {:id :finnjoh
               :name ["Finn" "J" "Johnsen"]}
      content (cultivate-people {:people [magnars finnjoh]})
      [magnars finnjoh] (:people content)]

  (fact (-> magnars :full-name) => "Magnar Sveen"
        (-> finnjoh :full-name) => "Finn J Johnsen")

  (fact (-> magnars :first-name) => "Magnar"
        (-> finnjoh :first-name) => "Finn")

  (fact (-> magnars :str) => "magnars"
        (-> finnjoh :str) => "finnjoh")

  (fact (-> magnars :url) => "/magnars/"
        (-> finnjoh :url) => "/finnjoh/")

  (fact (-> magnars :photos) => {:side-profile "/photos/people/magnars/side-profile.jpg"
                                 :half-figure "/photos/people/magnars/half-figure.jpg"}
        (-> finnjoh :photos) => {:side-profile "/photos/people/finnjoh/side-profile.jpg"
                                 :half-figure "/photos/people/finnjoh/half-figure.jpg"})

  )
