(ns kodemaker-no.cultivate.tech-test
  (:require [kodemaker-no.cultivate.tech :refer :all]
            [midje.sweet :refer :all]))

(def content
  {:people {}
   :tech {:react {:id :react}}})

(let [tech (-> content cultivate-techs :tech)]

  (fact (-> tech :react :url) => "/react/"))
