(ns kodemaker-no.cultivate.people-test
  (:require [kodemaker-no.cultivate.people :refer :all]
            [midje.sweet :refer :all]))

(let [magnars {:id :magnars
               :first-name "Magnar"
               :last-name "Sveen"}
      finnjoh {:id :finnjoh
               :first-name "Finn"
               :middle-name "J"
               :last-name "Johnsen"}
      content (cultivate-people {:people [magnars finnjoh]})
      [magnars finnjoh] (:people content)]

  (fact (-> magnars :full-name) => "Magnar Sveen"
        (-> finnjoh :full-name) => "Finn J Johnsen")

  (fact (-> magnars :str) => "magnars"
        (-> finnjoh :str) => "finnjoh")

  (fact (-> magnars :url) => "/magnars.html"
        (-> finnjoh :url) => "/finnjoh.html")

  (fact (-> magnars :photos) => {:side-profile "/photos/magnars/side-profile.jpg"
                                 :half-figure "/photos/magnars/half-figure.jpg"}
        (-> finnjoh :photos) => {:side-profile "/photos/finnjoh/side-profile.jpg"
                                 :half-figure "/photos/finnjoh/half-figure.jpg"})

  )
