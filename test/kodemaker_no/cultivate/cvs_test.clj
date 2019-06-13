(ns kodemaker-no.cultivate.cvs-test
  (:require [kodemaker-no.cultivate.content-shells :as c]
            [kodemaker-no.cultivate.cvs :as cvs]
            [kodemaker-no.cultivate.people :as p]
            [kodemaker-no.validate :refer [validate-content]]
            [midje.sweet :refer :all]))

(def content
  (c/content
   {:people
    {:christian
     (c/person
      {:id :christian
       :name ["Christian" "Johansen"]
       :cv {:default {:exclude-techs [:jira]}}
       :tech {:using-at-work [:clojure :java]}
       :innate-skills [:bash]
       :description "Default beskrivelse"
       :cv/description "CV-beskrivelse"
       :recommendations [{:title "Esoteric topic"
                          :blurb "Esoteric"
                          :link {:url "http://lolcathost" :text "Se"}
                          :tech [:haskell]}]
       :side-projects [{:title "Sideprosjekt"
                        :description "Test"
                        :illustration "/prosjekt.png"
                        :tech [:clojurescript :clojure]}]
       :presentations [{:title "Foredrag"
                        :blurb "Foredrag"
                        :tech [:unix]
                        :date "2017-01-02"
                        :urls {:video "http://lolcathost"}}]
       :appearances [{:title "Annet foredrag"
                      :date "2017-02-01"
                      :event "JavaZone"
                      :tech [:java]}]
       :open-source-projects [{:name "Sinon.JS"
                               :url "https://sinonjs.org"
                               :description "Sinon"
                               :tech [:sinon :java]}]
       :open-source-contributions [{:name "Buster.JS"
                                    :url "https://busterjs.org"
                                    :tech [:tdd :jira :clojure]}]
       :projects [{:customer "Bank"
                   :description "Beskrivelse"
                   :years [2020]
                   :tech [:java]
                   :employer :kodemaker}
                  {:customer "Bank"
                   :cv/customer "Bank1"
                   :description "Beskrivelse"
                   :cv/description "Beskrivelse1"
                   :years [2020]
                   :tech [:java]
                   :employer :kodemaker}]})

     :kjetil
     (c/person
      {:id :kjetil
       :name ["Kjetil" "Jørgensen-Dahl"]
       :description "Default beskrivelse"
       :tech {:using-at-work [:kotlin :java]}
       :cv {:default {:preferred-techs [:kotlin :groovy]}}
       :projects [{:customer "Bank"
                   :description "Beskrivelse"
                   :years [2020]
                   :tech [:java :javascript :groovy]
                   :employer :kodemaker}
                  {:customer "Bank 2"
                   :description "Beskrivelse 2"
                   :years [2020]
                   :tech [:java :javascript :groovy]
                   :employer :kodemaker}]})

     :magnar
     (c/person
      {:id :magnar
       :name ["Magnar" "Sveen"]})}

    :tech-types {:java :proglang
                 :jira :methodology
                 :tdd :methodology
                 :bash :proglang
                 :sinon :library
                 :clojure :proglang
                 :clojurescript :proglang
                 :javascript :proglang
                 :groovy :proglang
                 :unix :os
                 :haskell :proglang
                 :kotlin :proglang}

    :employers {:kodemaker "Kodemaker AS"}}))

(defn cultivate [content]
  (let [raw (validate-content content)]
    (cvs/cultivate-cvs raw (p/cultivate-people raw))))

(def cvs (cultivate content))

(let [cvs (cultivate content)]
  (fact
   "New CVs are opt-in"
   (map :id (vals cvs)) => [:christian :kjetil])

  (fact
   "Prefers cv/description when available"
   (map :description (vals cvs)) => ["CV-beskrivelse" "Default beskrivelse"])

  (fact
   "Prefers cv overrides in projects"
   (->> cvs :christian :projects (map #(select-keys % [:customer :description]))) =>
   [{:description "Beskrivelse" :customer "Bank"}
    {:description "Beskrivelse1" :customer "Bank1"}])

  (fact
   "Groups techs by type"
   (->> cvs :christian :techs keys set) => #{:proglang :library :methodology :os})

  (fact
   "Orders techs by most frequently referenced"
   (->> cvs :christian :techs :proglang (map :id)) => [:java :clojure :clojurescript :bash])

  (fact
   "Skews tech ordering by preferred techs"
   (->> cvs :kjetil :techs :proglang (map :id)) => [:kotlin :groovy :java :javascript])

  (fact
   "Excludes some techs"
   (->> cvs :christian :techs :methodology (map :id)) => [:tdd])

  (fact
   "Compiles techs from all self-effort fields"
   (->> cvs :christian :techs vals (apply concat) (map :id) set) =>
   #{:java :clojure :clojurescript :bash :sinon :tdd :unix})

  (fact
   "Resolves project employers"
   (->> cvs :christian :projects (map :employer)) => [{:id :kodemaker :name "Kodemaker AS"}
                                                      {:id :kodemaker :name "Kodemaker AS"}])

  (fact
   "Combines appearances with presentations and sorts by date"
   (->> cvs :christian :appearances (map :title)) => ["Annet foredrag" "Foredrag"])

  (fact
   "Combines open source projects and contributions, and groups by programming language"
   (->> cvs :christian :open-source-contributions keys) => ["Java" "Clojure"]
   (->> cvs :christian :open-source-contributions vals (map #(map :name %))) => [["Sinon.JS"] ["Buster.JS"]]))
