(ns kodemaker-no.people-test
  (:require [kodemaker-no.people :refer :all]
            [midje.sweet :refer :all]))

(fact
 (full-name {:first-name "Magnar" :last-name "Sveen"}) => "Magnar Sveen"
 (full-name {:first-name "Finn" :middle-name "J" :last-name "Johnsen"}) => "Finn J Johnsen")
