(ns kodemaker-no.pages.index-page-test
  (:require [kodemaker-no.homeless :refer [hiccup-find]]
            [kodemaker-no.pages.index-page :refer :all]
            [midje.sweet :refer :all]))

(fact "Administration doesn't count for number of consultants."

      (-> (index-page [{:administration? true}
                       {:administration? false}
                       {:administration? false}] {})
          :title) => {:h1 "2 kvasse konsulenter"})

(fact "Only consultants are included, sorted by reverse order."

      (->> (index-page [{:full-name "Magnar Sveen",   :administration? false, :start-date "2007-03-01"}
                        {:full-name "Finn J Johnsen", :administration? false, :start-date "2008-10-01"}
                        {:full-name "Kolbjørn Jetne", :administration? true,  :start-date "2007-04-01"}] {})
           :body (hiccup-find :.linkish) (map second))
      => ["Finn J Johnsen" "Magnar Sveen"])
