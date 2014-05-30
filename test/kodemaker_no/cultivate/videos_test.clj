(ns kodemaker-no.cultivate.videos-test
  (:require [kodemaker-no.cultivate.content-shells :as c]
            [kodemaker-no.cultivate.videos :refer :all]
            [kodemaker-no.validate :refer [validate-content]]
            [midje.sweet :refer :all]))

(def content
  (c/content
   {:people
    {:magnar
     (c/person
      {:id :magnar
       :name ["Magnar" "Sveen"]
       :presentations [{:title "Zombie TDD: Live parprogrammering"
                        :id :zombie-tdd-live-at-javazone
                        :blurb "Progger på JavaZone"
                        :tech [:javascript]
                        :urls {:video "http://vimeo.com/49485653"}}]})

     :sten-morten
     (c/person
      {:id :sten-morten
       :name ["Sten Morten" "MA"]
       :presentations [{:title "En deilig implementert AND (Teknologihuset)"
                        :blurb "Et dypdykk inn i clojure.core"
                        :tech [:hjernen :clojure]
                        :urls {:video "http://programmerer.com/2013/06/en-deilig-implementer-and-video/"}}
                       {:title "Programmeringsspråket betyr alt!"
                        :blurb "Hovedverktøyet til programmerere"
                        :tech [:clojure]
                        :urls {:video "http://www.youtube.com/watch?v=y5PSRn56ZWo"}}]})}}))

(defn cultivate [content]
  (cultivate-videos (validate-content content)))

(let [videos (cultivate content)]
  (fact
   "Videos with known hosts are included."
   (map :title videos) => ["Zombie TDD: Live parprogrammering"
                           "Programmeringsspråket betyr alt!"])

  (fact
   "It includes name and url to the person."

   (map :by videos) => [{:name "Magnar", :url "/magnar/"}
                        {:name "Sten Morten", :url "/sten-morten/"}])

  (fact
   "It includes the blurb."
   (map :blurb videos) => ["Progger på JavaZone"
                           "Hovedverktøyet til programmerere"])

  (fact
   "It includes the tech."
   (map :tech videos) => [[{:id :javascript, :name "Javascript"}]
                          [{:id :clojure, :name "Clojure"}]])

  (fact
   "It uses the given :id as URL, otherwise it uses the title to generate one."

   (map :url videos) => ["/zombie-tdd-live-at-javazone/"
                         "/programmeringsspraket-betyr-alt/"])

  (fact
   "It creates embed-code"

   (map :embed-code videos) =>
   [[:div.video-embed
     [:iframe {:src "//player.vimeo.com/video/49485653?title=0&amp;byline=0&amp;portrait=0"
               :frameborder "0"
               :allowfullscreen true}]]
    [:div.video-embed
     [:iframe {:src "http://www.youtube.com/embed/y5PSRn56ZWo"
               :frameborder "0"
               :allowfullscreen true}]]]))

(fact
 "It recognizes urls"
 (find-video "http://www.youtube.com/watch?v=y5PSRn56ZWo") => {:type :youtube, :id "y5PSRn56ZWo"}
 (find-video "http://vimeo.com/49324971") => {:type :vimeo, :id "49324971"}
 (find-video "https://vimeo.com/28765670") => {:type :vimeo, :id "28765670"}
 (find-video "http://vimeo.com/album/2525252/video/74401304") => {:type :vimeo, :id "74401304"})

(fact
 "It merges in video overrides based on id."

 (-> content
     (assoc :video-overrides
       {:zombie-tdd-live-at-javazone
        {:blurb "Overridden"}})
     cultivate
     first
     :blurb) => "Overridden")

(fact
 "It does not include videos with :direct-link? set to true."

 (-> content
     (assoc-in [:people :magnar :presentations 0 :direct-link?] true)
     cultivate
     count) => 1)
