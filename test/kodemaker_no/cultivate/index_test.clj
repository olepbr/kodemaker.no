(ns kodemaker-no.cultivate.index-test
  (:require [kodemaker-no.cultivate.content-shells :as c]
            [kodemaker-no.cultivate.index :refer :all]
            [kodemaker-no.validate :refer [validate-content]]
            [midje.sweet :refer :all]))

(def content
  (c/content
   {:people {:magnar (c/person {:id :magnar
                                :name ["Magnar" "Sveen"]})
             :anders (c/person {:id :anders
                                :name ["Anders" "Furseth"]})}
    :projects {:finn-oppdrag (c/project {:id :finn-oppdrag
                                         :name "FINN oppdrag"})}}))

(defn cultivate [content]
  (cultivate-index (validate-content content)))

(fact
 (-> content
     (assoc-in [:index :faces] [:anders])
     cultivate :faces)
 => [{:name "Anders Furseth"
      :url "/anders/"
      :photo "/photos/people/anders/side-profile-cropped.jpg"}])

(fact
 (-> content
     (assoc-in [:index :references] [:finn-oppdrag])
     cultivate :references first (select-keys [:name :url]))
 => {:name "FINN oppdrag"
     :url "/finn-oppdrag/"})
