(ns kodemaker-no.pages.person-pages-test
  (:require [kodemaker-no.pages.person-pages :refer :all]
            [midje.sweet :refer :all]
            [hiccup.core :refer [html]]
            [kodemaker-no.homeless :refer [hiccup-find]]))

(def pages
  (person-pages
   [{:url "/magnar/"
     :full-name "Magnar Sveen"
     :genitive "Magnars"
     :title "Framsieutvikler"
     :photos {:half-figure "/photos/magnars/half-figure.jpg"}
     :description "The description"
     :phone-number "+47 918 56 425"
     :email-address "magnar@kodemaker.no"

     :recommendations [{:title "Anbefaling 1"
                        :blurb "Denne er bra."
                        :url "http://example.com"
                        :tech [:clojure]}]}]))

(def page ((pages "/magnar/")))

(fact (-> page :title) => "Magnar Sveen")
(fact (-> page :illustration) => "/photos/magnars/half-figure.jpg")
(fact (-> page :lead) => [:p "The description"])

(fact (-> page :aside) => [:div.tight
                           [:h4 "Magnar Sveen"]
                           [:p
                            "Framsieutvikler" "<br>"
                            [:span.nowrap "+47 918 56 425"] "<br>"
                            [:a {:href "mailto:magnar@kodemaker.no"}
                             "magnar@kodemaker.no"]]])

(fact (->> page :body html) => (html [:h2 "Magnars Anbefalinger"]
                                     [:h3 [:a {:href "http://example.com"} "Anbefaling 1"]]
                                     [:p "Denne er bra."]))
