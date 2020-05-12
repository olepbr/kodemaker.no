(ns kodemaker-no.export.cvpartner-test
  (:require [kodemaker-no.export.cvpartner :refer :all]
            [midje.sweet :refer :all]))

;(fact "Generates a simple cv"
;      (generate-cv nil
;        {:person/email-address "trygve@kodemaker.no"
;         :person/projects      [{:project/customer    "Forsvaret"
;                                 :project/summary     "Pif"
;                                 :project/description "Paff"
;                                 :project/years       [2019 2010]
;                                 }]
;
;         })
;      =>
;           {:project_experiences [{:customer         {:no "Forsvaret"}
;                                   :description      {:no "Pif"}
;                                   :long_description {:no "Paff"}
;                                   :year_from        2019
;                                   :year_to          2010
;                                   :disabled         false
;                                   :project_experience_skills anything}]
;            })

