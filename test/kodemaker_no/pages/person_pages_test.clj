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
                                    :tech [{:name "Clojure", :url "/clojure/"}]}])
           :body html)

      => (html [:h2 "Magnars Anbefalinger"]
               [:h3 "Anbefaling 1"]
               [:p.near.cookie-w [:span.cookie [:a {:href "/clojure/"} "Clojure"]]]
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

(fact (->> (page :presentations [{:title "Lyntale: Wrap Ajax'en din"
                                  :blurb "Jeg tegner og forteller."
                                  :tech [:javascript :tdd :ajax]
                                  :urls {:video "http://vimeo.com/28764670"
                                         :source "https://github.com/magnars/server-facade"}
                                  :thumb "/thumbs/videos/trivelig-testing.jpg"}])
           :body html)

      => (html [:h2 "Magnars Foredrag"]
               [:div.media
                [:a.img.thumb.mts {:href "http://vimeo.com/28764670"}
                 [:img {:src "/thumbs/videos/trivelig-testing.jpg"}]]
                [:div.bd
                 [:h4.mtn "Lyntale: Wrap Ajax'en din"]
                 [:p "Jeg tegner og forteller. "
                  [:a.nowrap {:href "http://vimeo.com/28764670"} "Se video"] " "
                  [:a.nowrap {:href "https://github.com/magnars/server-facade"} "Se koden"]]]]))
