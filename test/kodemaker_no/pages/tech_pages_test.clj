(ns kodemaker-no.pages.tech-pages-test
  (:require [kodemaker-no.pages.tech-pages :refer :all]
            [midje.sweet :refer :all]))

(def react
  {:url "/react/"
   :name "React"
   :illustration "/photos/tech/react.jpg"
   :description "The description"})

(defn page [& {:as extras}]
  (((tech-pages [(merge react extras)]) "/react/")))

(fact (-> (page) :title) => "React")
(fact (-> (page) :illustration) => "/photos/tech/react.jpg")
(fact (-> (page) :lead) => "<p>The description</p>")
