(ns kodemaker-no.cultivate.people-test
  (:require [kodemaker-no.cultivate.people :refer :all]
            [midje.sweet :refer :all]))

(def content
  {:people {:magnars {:id :magnars
                      :name ["Magnar" "Sveen"]}
            :finnjoh {:id :finnjoh
                      :name ["Finn" "J" "Johnsen"]}
            :andersf {:id :andersf
                      :name ["Anders" "Furseth"]}}})

(let [people (-> content cultivate-people :people)]

  (fact (-> people :magnars :full-name) => "Magnar Sveen"
        (-> people :finnjoh :full-name) => "Finn J Johnsen")

  (fact (-> people :magnars :first-name) => "Magnar"
        (-> people :finnjoh :first-name) => "Finn")

  (fact (-> people :magnars :genitive) => "Magnars"
        (-> people :andersf :genitive) => "Anders'")

  (fact (-> people :magnars :str) => "magnars"
        (-> people :finnjoh :str) => "finnjoh")

  (fact (-> people :magnars :url) => "/magnars/"
        (-> people :finnjoh :url) => "/finnjoh/")

  (fact (-> people :magnars :photos) => {:side-profile "/photos/people/magnars/side-profile.jpg"
                                         :half-figure "/photos/people/magnars/half-figure.jpg"}
        (-> people :finnjoh :photos) => {:side-profile "/photos/people/finnjoh/side-profile.jpg"
                                         :half-figure "/photos/people/finnjoh/half-figure.jpg"}))

(let [people (-> content
                 (assoc-in [:people :magnars :tech]
                           {:favorites-at-the-moment [:clojure]
                            :want-to-learn-more [:react]})
                 (assoc-in [:people :magnars :recommendations]
                           [{:tech [:ansible]}])
                 (assoc-in [:tech :react]
                           {:id :react
                            :name "React"
                            :site "http://react.js"
                            :description "Blah!"})
                 cultivate-people :people)]

  (fact
   "Tech that isn't present in the content is given a name based
    on its :id."
   (-> people :magnars :tech :favorites-at-the-moment)
   => [{:id :clojure, :name "clojure"}]

   (-> people :magnars :recommendations first :tech)
   => [{:id :ansible, :name "ansible"}])

  (fact
   "Tech that is present, uses the :name in the tech, and adds
    a :url based on the :id."
   (-> people :magnars :tech :want-to-learn-more)
   => [{:id :react, :name "React", :url "/react/"}]))
