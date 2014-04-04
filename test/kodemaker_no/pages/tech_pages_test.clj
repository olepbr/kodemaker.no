(ns kodemaker-no.pages.tech-pages-test
  (:require [kodemaker-no.pages.tech-pages :refer :all]
            [midje.sweet :refer :all]
            [hiccup.core :refer [html]]))

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

(fact (->> (page :recommendations [{:title "Anbefaling 1"
                                    :blurb "Denne er bra."
                                    :link {:url "http://example.com" :text "Xzample"}
                                    :by [{:name "Magnar",
                                          :url "/magnar/"}
                                         {:name "Finn"
                                          :url "/finnjoh/"}]}])
           :body html)

      => (html [:h2 "Våre anbefalinger"]
               [:h3 [:a {:href "http://example.com"} "Anbefaling 1"]]
               [:p.near.cookie-w [:span.cookie "Anbefalt av "
                                  [:a {:href "/magnar/"} "Magnar"] " og "
                                  [:a {:href "/finnjoh/"} "Finn"]]]
               [:p "Denne er bra. "
                [:a.nowrap {:href "http://example.com"} "Xzample"]]))
