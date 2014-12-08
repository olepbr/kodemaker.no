(ns kodemaker-no.pages.index-page-test
  (:require [kodemaker-no.homeless :refer [hiccup-find]]
            [kodemaker-no.pages.index-page :refer :all]
            [midje.sweet :refer :all]))

(fact "Administration doesn't count for number of consultants."

      (-> (index-page [{:administration? true}
                       {:administration? false}
                       {:administration? false}])
          :title :h1) => "2 kvasse konsulenter")
