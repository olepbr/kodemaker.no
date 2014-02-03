(ns kodemaker-no.pages.references-page-test
  (:require [kodemaker-no.pages.references-page :refer :all]
            [kodemaker-no.homeless :refer [hiccup-find]]
            [midje.sweet :refer :all]))

(def projects [{:url "/nsb-enka/"
                :name "NSB Enka"
                :logo "/logos/nsb.png"
                :description "Energiforbruk på tog."
                :awesomeness 11}
               {:url "/finn-oppdrag/"
                :name "FINN oppdrag"
                :logo "/logos/finn.png"
                :description "FINN.no besluttet å lage FINN oppdrag."
                :awesomeness 10}
               {:url "/finn-surf-sammen/"
                :name "FINN surf sammen"
                :logo "/logos/finn.png"
                :description "Konsulenter fra Kodemaker."
                :awesomeness 12}])

(fact (-> (references-page projects) :title) => "Referanser")

(fact (->> (references-page projects) :body (hiccup-find :h3))
      => (list
          [:h3 [:a {:href "/finn-surf-sammen/"} "FINN surf sammen"]]
          [:h3 [:a {:href "/finn-oppdrag/"} "FINN oppdrag"]]
          [:h3 [:a {:href "/nsb-enka/"} "NSB Enka"]]))

(fact (->> (references-page projects) :body (hiccup-find :img))
      => (list
          [:img {:src "/logos/finn.png"}]
          [:img {:src "/logos/nsb.png"}]))

