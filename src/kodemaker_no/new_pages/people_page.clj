(ns kodemaker-no.new-pages.people-page
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.homeless :as h]
            [ui.elements :as e]))

(defn person-card [idx {:person/keys [full-name title phone-number email-address] :as person}]
  {:content
   (e/illustrated
    {:image (str "/profile-medium" (:person/profile-overview-picture person))
     :title full-name
     :href (:page/uri person)
     :curtain (let [modifier (mod idx 7)]
                (cond
                  (= 0 modifier) :left
                  (= 5 modifier) :right))
     :lines [title phone-number email-address]})})

(defn create-page [page]
  (let [db (d/entity-db page)]
    {:title "Folk"
     :sections
     [{:kind :grid-header
       :pønt [{:kind :greater-than
               :position "left -200px top -400px"}
              {:kind :dotgrid
               :position "right 100px top 200px"}
              {:kind :dotgrid
               :position "right 560px top 200px"}
              {:kind :dotgrid
               :position "left 0 top 1450px"}
              {:kind :dotgrid
               :position "left 460px top 1450px"}
              {:kind :dotgrid
               :position "right 0 top 3000px"}
              {:kind :dotgrid
               :position "right 460px top 3000px"}
              {:kind :greater-than
               :position "left 0 top 5500px"}]
       :background :blanc-rose
       :grid-type :card-grid
       :items (->> (d/q '[:find ?e
                          :in $
                          :where
                          [?e :person/full-name]
                          [?e :person/profile-active? true]
                          [?e :person/quit? false]]
                        db)
                   (map #(d/entity db (first %)))
                   (sort-by :person/start-date)
                   reverse
                   (map-indexed person-card))}
      {:kind :footer}]}))
