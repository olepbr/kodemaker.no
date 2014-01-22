(ns kodemaker-no.pages.person-pages-test
  (:require [kodemaker-no.pages.person-pages :refer :all]
            [midje.sweet :refer :all]
            [hiccup.core :refer [html]]
            [kodemaker-no.homeless :refer [hiccup-find]]))

(def magnar
  {:url "/magnar/"
   :full-name "Magnar Sveen"
   :genitive "Magnars"
   :title "Framsieutvikler"
   :photos {:half-figure "/photos/magnars/half-figure.jpg"}
   :description "The description"
   :phone-number "+47 918 56 425"
   :email-address "magnar@kodemaker.no"})

(defn page [& {:as extras}]
  (((person-pages [(merge magnar extras)]) "/magnar/")))

(fact (-> (page) :title) => "Magnar Sveen")
(fact (-> (page) :illustration) => "/photos/magnars/half-figure.jpg")
(fact (-> (page) :lead) => [:p "The description"])

(fact (-> (page) :aside) => [:div.tight
                             [:h4 "Magnar Sveen"]
                             [:p
                              "Framsieutvikler" "<br>"
                              [:span.nowrap "+47 918 56 425"] "<br>"
                              [:a {:href "mailto:magnar@kodemaker.no"}
                               "magnar@kodemaker.no"]]])

(fact (->> (page :recommendations [{:title "Anbefaling 1"
                                    :blurb "Denne er bra."
                                    :url "http://example.com"
                                    :tech [:clojure]}])
           :body html)

      => (html [:h2 "Magnars Anbefalinger"]
               [:h3 "Anbefaling 1"]
               [:p "Denne er bra. "
                [:a.nowrap {:href "http://example.com"} "Les mer"]]))

(fact (->> (page :hobbies [{:title "Brettspill"
                            :description "Det er mer enn Monopol og Ludo i verden."
                            :illustration "/photos/hobbies/brettspill.jpg"}])
           :body html)

      => (html [:h2 "Snakker gjerne om"]
               [:div.bd
                [:h3.mtn "Brettspill"]
                [:p
                 [:img.right {:src "/photos/hobbies/brettspill.jpg"}]
                 "Det er mer enn Monopol og Ludo i verden."]]))

(fact (->> (page :tech {:favorites-at-the-moment [{:name "clojure"}
                                                  {:name "emacs"}
                                                  {:name "ansible"}]
                        :want-to-learn-more [{:name "React", :url "/react/"}]})
           :body html)

      => (html [:p
                [:strong "Favoritter for tiden: "]
                "clojure, emacs og ansible"
                "<br>"
                [:strong "Vil lære mer: "]
                [:a {:href "/react/"} "React"]
                "<br>"]))
