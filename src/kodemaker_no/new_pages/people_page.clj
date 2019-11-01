(ns kodemaker-no.new-pages.people-page
  (:require [datomic-type-extensions.api :as d]
            [ui.elements :as e]))

(defn person-card [idx {:person/keys [full-name title phone-number email-address] :as person}]
  {:content
   (e/illustrated
    {:image (str "/profile-medium/foto/profiles/" (name (:db/ident person)) ".jpg")
     :title full-name
     :curtain (let [modifier (mod idx 7)]
                (cond
                  (= 0 modifier) :left
                  (= 5 modifier) :right))
     :lines [title phone-number email-address]})})

(defn create-page [page]
  (let [db (d/entity-db page)]
    {:title "Folk"
     :sections
     [{:kind :header}
      {:kind :grid
       :grid-type :card-grid
       :items (->> (d/q '[:find ?e
                          :in $
                          :where
                          [?e :person/full-name]]
                        db)
                   (map #(d/entity db (first %)))
                   (sort-by :person/start-date)
                   reverse
                   (map-indexed person-card))}
      {:kind :footer}]}))
