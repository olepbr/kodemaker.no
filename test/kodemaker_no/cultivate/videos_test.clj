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
                        :date "2019-10-01"
                        :blurb "Progger på JavaZone"
                        :tech [:javascript]
                        :urls {:video "http://vimeo.com/49485653"}}]})

     :christian
     (c/person
      {:id :christian
       :name ["Christian" "Johansen"]
       :presentations [{:title "Zombie TDD: Live parprogrammering"
                        :id :zombie-tdd-live-at-javazone
                        :date "2019-10-01"
                        :blurb "Progger på JavaZone"
                        :tech [:javascript]
                        :urls {:video "http://vimeo.com/49485653"}}]})

     :sten-morten
     (c/person
      {:id :sten-morten
       :name ["Sten Morten" "MA"]
       :presentations [{:title "En deilig implementert AND (Teknologihuset)"
                        :blurb "Et dypdykk inn i clojure.core"
                        :date "2019-10-01"
                        :tech [:clojure]
                        :urls {:video "http://programmerer.com/2013/06/en-deilig-implementer-and-video/"}}
                       {:title "Programmeringsspråket betyr alt!"
                        :blurb "Hovedverktøyet til programmerere"
                        :date "2019-09-01"
                        :tech [:clojure]
                        :urls {:video "http://www.youtube.com/watch?v=y5PSRn56ZWo"}}]})}}))

(defn cultivate [content]
  (cultivate-videos (validate-content content)))

(let [videos (cultivate content)]
  (fact
   "It includes the title."
   (map :title videos) => (just #{"Zombie TDD: Live parprogrammering"
                                  "En deilig implementert AND (Teknologihuset)"
                                  "Programmeringsspråket betyr alt!"}))

  (fact
   "It includes name and url to the people."

   (map :by videos) => (just [{:name "Magnar", :url "/magnar/"}
                              {:name "Christian", :url "/christian/"}]
                             [{:name "Sten Morten", :url "/sten-morten/"}]
                             [{:name "Sten Morten", :url "/sten-morten/"}]
                             :in-any-order))

  (fact
   "It includes the blurb."
   (map :blurb videos) => (just #{"Progger på JavaZone"
                                  "Et dypdykk inn i clojure.core"
                                  "Hovedverktøyet til programmerere"}))

  (fact
   "It includes the tech."
   (map :tech videos) => (just [{:id :javascript, :name "Javascript", :type nil}]
                               [{:id :clojure, :name "Clojure", :type nil}]
                               [{:id :clojure, :name "Clojure", :type nil}]
                               :in-any-order))

  (fact
   "It uses the given :id as URL, otherwise it uses the title to generate one.
    Then there's the videos we can't embed on our site. Those are left alone."

   (map :url videos) => (just #{"/zombie-tdd-live-at-javazone/"
                                "http://programmerer.com/2013/06/en-deilig-implementer-and-video/"
                                "/programmeringsspraket-betyr-alt/"}))

  (fact
   "It creates embed-code"

   (->> videos (remove :direct-link?) (map :embed-code)) =>
    (just #{[:div.video-embed
             [:iframe {:src             "//player.vimeo.com/video/49485653?title=0&amp;byline=0&amp;portrait=0"
                       :frameborder     "0"
                       :allowfullscreen true}]]
            [:div.video-embed
             [:iframe {:src             "//www.youtube.com/embed/y5PSRn56ZWo"
                       :frameborder     "0"
                       :allowfullscreen true}]]})))

(fact
 "It recognizes urls"
 (find-video "http://www.youtube.com/watch?v=y5PSRn56ZWo") => {:type :youtube, :id "y5PSRn56ZWo"}
 (find-video "http://vimeo.com/49324971") => {:type :vimeo, :id "49324971"}
 (find-video "https://vimeo.com/28765670") => {:type :vimeo, :id "28765670"}
 (find-video "http://vimeo.com/album/2525252/video/74401304") => {:type :vimeo, :id "74401304"}
 (find-video "http://vimeo.com/user18356272/review/96634125/3419ad5e0a") => {:type :vimeo, :id "96634125"})


(fact
 "It merges in video overrides based on id."

 (let [org-blurb (get-in content [:people :magnar :presentations 0 :blurb])
       videos
       (as-> content x
        (assoc x :video-overrides
                 {:zombie-tdd-live-at-javazone
                  {:blurb "Overridden"}})
        (cultivate x)
        (map :blurb x))]
  org-blurb =not=> nil?
  videos => (contains "Overridden")
  videos =not=> (contains org-blurb)))

(fact "Videos should be sorted by date and then name"
 (let [f (partial sort compare-by-date-and-title)]
  (f []) => []
  (f [{:foo 1} {:bar 2}]) => (just {:foo 1} {:bar 2} :in-any-order)

  (f [{:title "Ola"} {:title "Per"}])
  => [{:title "Ola"} {:title "Per"}]

  (f [{:title "Per"} {:title "Ola"}])
  => [{:title "Ola"} {:title "Per"}]

  (f [{:date #inst"2014-01-01" :title "Per"} {:date #inst"2014-01-02" :title "Ola"}])
  => [{:date #inst"2014-01-02" :title "Ola"} {:date #inst"2014-01-01" :title "Per"}]

  (f [{:date #inst"2014-01-02" :title "Ola"} {:date #inst"2014-01-02" :title "Per"}])
  => [{:date #inst"2014-01-02" :title "Ola"} {:date #inst"2014-01-02" :title "Per"}]

  (f [{:date #inst"2014-01-02" :title "Per"} {:date #inst"2014-01-02" :title "Ola"}])
  => [{:date #inst"2014-01-02" :title "Ola"} {:date #inst"2014-01-02" :title "Per"}]

  (f [{:date #inst"2014-01-01" :title "Per"} {:title "Ola"}])
  => [{:date #inst"2014-01-01" :title "Per"} {:title "Ola"}]

  (f [{:title "Ola"} {:date #inst"2014-01-01" :title "Per"}])
  => [{:date #inst"2014-01-01" :title "Per"} {:title "Ola"}]))

(fact "Nil values from videos, should not override other non-nil values"
 (let [f combine-videos]
  (f [{:by "Christian" :k 2} {:by "Magnar" :k 1}])
  => {:by ["Christian" "Magnar"] :k 2}

  (f [{:by "Christian" :k nil} {:by "Magnar" :k 1}])
  => {:by ["Christian" "Magnar"] :k 1}))

#_(fact
   "It does not mess with videos with :direct-link? set to true."

   (-> content
       (assoc-in [:people :magnar :presentations 0 :direct-link?] true)
       cultivate
       count) => 1)
