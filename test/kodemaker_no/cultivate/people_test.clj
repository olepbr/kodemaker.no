(ns kodemaker-no.cultivate.people-test
  (:require [clj-time.core :refer [local-date]]
            [kodemaker-no.cultivate.content-shells :as c]
            [kodemaker-no.cultivate.people :refer :all]
            [kodemaker-no.validate :refer [validate-content]]
            [midje.sweet :refer :all]))

(def content
  (c/content
   {:people {:magnar (c/person {:id :magnar
                                :name ["Magnar" "Sveen"]
                                :start-date "2007-04-01 00:09"})
             :finnjoh (c/person {:id :finnjoh
                                 :name ["Finn" "J" "Johnsen"]
                                 :start-date "2008-10-01"})
             :andersf (c/person {:id :andersf
                                 :name ["Anders" "Furseth"]
                                 :start-date "2010-03-15"})}}))

(defn cultivate [content]
  (cultivate-people (validate-content content)))

(let [people (cultivate content)]

  (fact (-> people :magnar :full-name) => "Magnar Sveen"
        (-> people :finnjoh :full-name) => "Finn J Johnsen")

  (fact (-> people :magnar :first-name) => "Magnar"
        (-> people :finnjoh :first-name) => "Finn")

  (fact (-> people :magnar :genitive) => "Magnars"
        (-> people :andersf :genitive) => "Anders'")

  (fact (-> people :magnar :str) => "magnar"
        (-> people :finnjoh :str) => "finnjoh")

  (fact (-> people :magnar :url) => "/magnar/"
        (-> people :finnjoh :url) => "/finnjoh/")

  (fact (-> people :andersf :next-person-url) => "/finnjoh/"
        (-> people :finnjoh :next-person-url) => "/magnar/"
        (-> people :magnar :next-person-url) => "/andersf/")

  (fact (-> people :magnar :photos) => {:side-profile "/photos/people/magnar/side-profile.jpg"
                                        :side-profile-near "/photos/people/magnar/side-profile-near.jpg"
                                        :half-figure "/photos/people/magnar/half-figure.jpg"}
        (-> people :finnjoh :photos) => {:side-profile "/photos/people/finnjoh/side-profile.jpg"
                                         :side-profile-near "/photos/people/finnjoh/side-profile-near.jpg"
                                         :half-figure "/photos/people/finnjoh/half-figure.jpg"}))

(let [people (-> content
                 (assoc-in [:people :magnar :tech]
                           {:favorites-at-the-moment [:css]
                            :want-to-learn-more [:react]})
                 (assoc-in [:people :magnar :recommendations]
                           [(c/recommendation {:tech [:ansible]})])
                 (assoc-in [:tech :react]
                           {:id :react
                            :name "React"
                            :site "http://react.js"
                            :description "Blah!"})
                 (assoc-in [:tech-names :css] "CSS")
                 cultivate)]

  (fact
   "Tech that isn't present in the content is given a name based
    on its :id."
   (-> people :magnar :recommendations first :tech)
   => [{:id :ansible, :name "Ansible"}])

  (fact
   "Unless its in the list of weird tech names."
   (-> people :magnar :tech :favorites-at-the-moment)
   => [{:id :css, :name "CSS"}])

  (fact
   "Tech that is present, uses the :name in the tech, and adds
    a :url based on the :id."
   (-> people :magnar :tech :want-to-learn-more)
   => [{:id :react, :name "React", :url "/react/"}])

  (fact
   "It doesn't add empty maps and lists"

   (-> people :finnjoh :tech) => nil))

(let [people (-> content
                 (assoc-in [:people :magnar :upcoming]
                           [{:title "Presentasjon"
                             :date "2013-02-01"
                             :url "http://vg.no"
                             :tech [:javascript]
                             :location {:title "I stua" :url "http://127.0.0.1"}
                             :description "Something"}])
                 cultivate)]
  (fact "It parses dates in upcoming events"
        (-> people :magnar :upcoming first :date) => (local-date 2013 2 1))

  (fact "It cultivates tech tags"
        (-> people :magnar :upcoming first :tech) => (list {:id :javascript :name "Javascript"}))

  (fact "It does not create upcoming when there are none"
        (-> people :finnjoh :upcoming) => nil))

