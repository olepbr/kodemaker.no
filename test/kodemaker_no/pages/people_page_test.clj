(ns kodemaker-no.pages.people-page-test
  (:require [kodemaker-no.pages.people-page :refer :all]
            [kodemaker-no.homeless :refer [hiccup-find]]
            [midje.sweet :refer :all]))

(fact "Administration doesn't count for number of consultants."

      (-> (all-people [{:administration? true}
                       {:administration? false}
                       {:administration? false}])
          :title) => "2 kvasse konsulenter")

(fact "Everyone is included, sorted by reverse order"

      (->> (all-people [{:full-name "Magnar Sveen",   :administration? false, :order 1}
                        {:full-name "Finn J Johnsen", :administration? false, :order 2}
                        {:full-name "Kolbjørn Jetne", :administration? true,  :order 0}])
           :body (hiccup-find :.linkish) (map second))
      => ["Finn J Johnsen" "Magnar Sveen" "Kolbjørn Jetne"])
