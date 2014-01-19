(ns kodemaker-no.pages.person-pages-test
  (:require [kodemaker-no.pages.person-pages :refer :all]
            [midje.sweet :refer :all]))

(def pages (person-pages [{:url "/magnar/"
                           :full-name "Magnar Sveen"
                           :photos {:half-figure "/photos/magnars/half-figure.jpg"}
                           :description "The description"}]))

(fact ((pages "/magnar/")) => {:title "Magnar Sveen"
                                   :illustration "/photos/magnars/half-figure.jpg"
                                   :lead [:p "The description"]})
