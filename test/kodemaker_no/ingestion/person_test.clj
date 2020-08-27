(ns kodemaker-no.ingestion.person-test
  (:require [clojure.test :refer [deftest is]]
            [kodemaker-no.ingestion.person :as sut]))

(deftest add-pictures-sets-profile-pictures
  (is (= (-> (sut/add-pictures
              {}
              ["/images/a.jpg"
               "/images/b.jpg"
               "/images/c.jpg"])
             first
             (select-keys [:person/profile-overview-picture
                           :person/profile-page-picture
                           :person/cv-picture])
             vals
             set
             count)
         3)))

(comment
  (year-range #time/ld "2019-01-01" nil)
  (year-range nil #time/ld "2019-01-01")
  (year-range #time/ld "2016-01-01" #time/ld "2019-01-01")
  (year-range #time/ld "2016-01-01" :ongoing))
