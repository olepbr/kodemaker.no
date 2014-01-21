(ns kodemaker-no.pages.person-pages-test
  (:require [kodemaker-no.pages.person-pages :refer :all]
            [midje.sweet :refer :all]))

(def pages (person-pages [{:url "/magnar/"
                           :full-name "Magnar Sveen"
                           :title "Framsieutvikler"
                           :photos {:half-figure "/photos/magnars/half-figure.jpg"}
                           :description "The description"
                           :phone-number "+47 918 56 425"
                           :email-address "magnar@kodemaker.no"}]))

(def page ((pages "/magnar/")))

(fact (-> page :title) => "Magnar Sveen")
(fact (-> page :illustration) => "/photos/magnars/half-figure.jpg")
(fact (-> page :lead) => [:p "The description"])

(fact (-> page :aside) => [:div.tight
                           [:h5 "Magnar Sveen"]
                           [:p
                            "Framsieutvikler" "<br>"
                            [:span.nowrap "+47 918 56 425"] "<br>"
                            [:a {:href "mailto:magnar@kodemaker.no"}
                             "magnar@kodemaker.no"]]])
