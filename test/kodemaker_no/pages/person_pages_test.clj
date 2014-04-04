(ns kodemaker-no.pages.person-pages-test
  (:require [kodemaker-no.pages.person-pages :refer :all]
            [midje.sweet :refer :all]
            [hiccup.core :refer [html]]
            [clj-time.core :as time]))

(def magnar
  {:url "/magnar/"
   :full-name "Magnar Sveen"
   :genitive "Magnars"
   :title "Framsieutvikler"
   :photos {:half-figure "/photos/magnars/half-figure.jpg"}
   :description "The *description*"
   :phone-number "+47 918 56 425"
   :email-address "magnar@kodemaker.no"})

(defn page [& {:as extras}]
  (((person-pages [(merge magnar extras)]) "/magnar/")))

(defn page-at [date & {:as extras}]
  (((person-pages [(merge magnar extras)] date) "/magnar/")))

(fact (-> (page) :title) => "Magnar Sveen")
(fact (-> (page) :illustration) => "/photos/magnars/half-figure.jpg")
(fact (-> (page) :lead) => "<p>The <em>description</em></p>")

(fact (-> (page) :aside html) => (html [:div.tight
                                        [:h4 "Magnar Sveen"]
                                        [:p
                                         "Framsieutvikler" "<br>"
                                         [:span.nowrap "+47 918 56 425"] "<br>"
                                         [:a {:href "mailto:magnar@kodemaker.no"}
                                          "magnar@kodemaker.no"]]]))

(fact (->> (page :recommendations [{:title "Anbefaling 1"
                                    :blurb "Denne er **bra**."
                                    :link {:url "http://example.com" :text "Les detta"}
                                    :tech [{:name "Clojure", :url "/clojure/"}]}])
           :body html)

      => (html [:h2 "Magnars anbefalinger"]
               [:h3 "Anbefaling 1"]
               [:p.near.cookie-w [:span.cookie [:a {:href "/clojure/"} "Clojure"]]]
               [:p "Denne er <strong>bra</strong>. "
                [:a.nowrap {:href "http://example.com"} "Les detta"]]))

(fact (->> (page :hobbies [{:title "Brettspill"
                            :description "Det er mer enn Monopol og Ludo i verden."
                            :illustration "/photos/hobbies/brettspill.jpg"}
                           {:title "Adventur"
                            :description "Hjemmesnekra spill."
                            :url "http://www.adventur.no"
                            :illustration "/photos/hobbies/adventur.jpg"}])
           :body html)

      => (html [:h2 "Snakker gjerne om"]
               [:div.bd
                [:h3.mtn "Brettspill"]
                [:p
                 [:img.illu {:src "/photos/hobbies/brettspill.jpg"}]
                 "Det er mer enn Monopol og Ludo i verden."]]
               [:div.bd
                [:h3.mtn "Adventur"]
                [:p
                 [:a.illu {:href "http://www.adventur.no"}
                  [:img {:src "/photos/hobbies/adventur.jpg"}]]
                 "Hjemmesnekra spill."]]))

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

(fact (->> (page :presentations [{:title "Lyntale: Wrap Ajax'en din"
                                  :blurb "Jeg tegner og forteller."
                                  :tech [{:name "JavaScript", :url "/javascript/"}]
                                  :urls {:video "http://vimeo.com/28764670"
                                         :source "https://github.com/magnars/server-facade"}}])
           :body html)

      => (html [:h2 "Magnars foredrag"]
               [:h3.mtn "Lyntale: Wrap Ajax'en din"]
               [:p.near.cookie-w [:span.cookie [:a {:href "/javascript/"} "JavaScript"]]]
               [:p "Jeg tegner og forteller. "
                [:a.nowrap {:href "http://vimeo.com/28764670"} "Se video"] " "
                [:a.nowrap {:href "https://github.com/magnars/server-facade"} "Se koden"]]))

(fact (->> (page :presence {:twitter "magnars"})
           :aside last html)

      => (html [:div.mod
                [:div.presence
                 [:a {:href "http://www.twitter.com/magnars"}
                  [:img {:src "/icons/twitter.png" :title "Twitter"}]]]]))

(defn upcoming [title date]
  {:title title
   :date date
   :url "http://vg.no"
   :tech [:javascript]
   :location {:title "I stua" :url "http://127.0.0.1"}
   :description "Something"})

(let [events [(upcoming "Februarkurs" (time/local-date 2013 2 7))
              (upcoming "Marskurs" (time/local-date 2013 3 1))]]

  (fact "Includes upcoming events six weeks from render date"
        (with-redefs [time/today (constantly (time/local-date 2013 1 1))]
          (let [body (->> (page :upcoming events) :body html)]
            body => #(.contains % "Magnars kommende foredrag/kurs")
            body => #(.contains % "Februarkurs")
            body => #(not (.contains % "Marskurs"))))

        (with-redefs [time/today (constantly (time/local-date 2013 2 1))]
          (let [body (->> (page :upcoming events) :body html)]
            body => #(.contains % "Februarkurs")
            body => #(.contains % "Marskurs")))))
