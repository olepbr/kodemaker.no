(ns kodemaker-no.cultivate.screencasts-test
  (:require [kodemaker-no.cultivate.content-shells :as c]
            [kodemaker-no.cultivate.screencasts :refer :all]
            [kodemaker-no.validate :refer [validate-content]]
            [midje.sweet :refer :all]))

(def content
  (c/content
   {:people
    {:magnar
     (c/person
      {:id :magnar
       :name ["Magnar" "Sveen"]
       :screencasts [{:title "Zombie TDD - Testdrevet JavaScript"
                      :url "http://www.zombietdd.com/"
                      :blurb "JavaScript zombies"
                      :tech [:javascript]}
                     {:title "Zombie CLJ - Clojure og ClojureScript"
                      :url "http://www.zombieclj.no/"
                      :blurb "Clojure zombies"
                      :tech [:clojure :clojurescript]}]})

     :christian
     (c/person
      {:id :christian
       :name ["Christian" "Johansen"]
       :screencasts [{:title "Zombie CLJ - Clojure og ClojureScript"
                      :url "http://www.zombieclj.no/"
                      :blurb "Clojure zombies"
                      :tech [:clojure :clojurescript]}]})
     }}))

(defn cultivate [content]
  (cultivate-screencasts (validate-content content)))

(let [screencasts (cultivate content)]
  (fact
   "It includes the title."
   (map :title screencasts) => (just #{"Zombie TDD - Testdrevet JavaScript"
                                       "Zombie CLJ - Clojure og ClojureScript"}))

  (fact
   "It includes name and url to the people."

   (map :by screencasts) => (just [{:name "Magnar", :url "/magnar/"}
                                   {:name "Christian", :url "/christian/"}]
                                  [{:name "Magnar", :url "/magnar/"}]
                                  :in-any-order))

  (fact
   "It includes the blurb."
   (map :blurb screencasts) => (just #{"JavaScript zombies"
                                       "Clojure zombies"}))

  (fact
   "It includes the tech."
   (map :tech screencasts) => (just [{:id :javascript, :name "Javascript"}]
                                    [{:id :clojure, :name "Clojure"}
                                     {:id :clojurescript, :name "Clojurescript"}]
                                    :in-any-order)))

(fact "Screencasts should be sorted by date and then name"
      (let [f (partial sort compare-by-date-and-title)]
        (f []) => []
        (f [{:foo 1} {:bar 2}]) => (just {:foo 1} {:bar 2} :in-any-order)

        (f [{:title "Ola"} {:title "Per"}])
        => [{:title "Ola"} {:title "Per"}]

        (f [{:title "Per"} {:title "Ola"}])
        => [{:title "Ola"} {:title "Per"}]

        (f [{:launch-date #inst"2014-01-01" :title "Per"} {:launch-date #inst"2014-01-02" :title "Ola"}])
        => [{:launch-date #inst"2014-01-02" :title "Ola"} {:launch-date #inst"2014-01-01" :title "Per"}]

        (f [{:launch-date #inst"2014-01-02" :title "Ola"} {:launch-date #inst"2014-01-02" :title "Per"}])
        => [{:launch-date #inst"2014-01-02" :title "Ola"} {:launch-date #inst"2014-01-02" :title "Per"}]

        (f [{:launch-date #inst"2014-01-02" :title "Per"} {:launch-date #inst"2014-01-02" :title "Ola"}])
        => [{:launch-date #inst"2014-01-02" :title "Ola"} {:launch-date #inst"2014-01-02" :title "Per"}]

        (f [{:launch-date #inst"2014-01-01" :title "Per"} {:title "Ola"}])
        => [{:launch-date #inst"2014-01-01" :title "Per"} {:title "Ola"}]

        (f [{:title "Ola"} {:launch-date #inst"2014-01-01" :title "Per"}])
        => [{:launch-date #inst"2014-01-01" :title "Per"} {:title "Ola"}]))
