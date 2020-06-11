(ns kodemaker-no.export.cvpartner-test
  (:require [kodemaker-no.export.cvpartner :refer :all]
            [kodemaker-no.atomic :as atomic]
            [kodemaker-no.content :as content]
            [kodemaker-no.files :as files]
            [kodemaker-no.ingest :as ingest]
            [kodemaker-no.validate :refer [validate-content ID Person]]
            [datomic-type-extensions.api :as d]
            [midje.sweet :refer :all]
            schema.core))

(defn- setup [conn]
  (let [johndoe-filename "people/johndoe.edn"]
    (doseq [file-name (files/find-file-names "resources/tech" #"(md|edn)$")]
      (ingest/ingest conn (str "tech/" file-name)))
    (ingest/ingest conn "tech-types.edn")
    (ingest/ingest conn johndoe-filename)
    (ingest/perform-last-minute-changes conn)
    (let [person (content/slurp-edn-map (str "test/resources" "/" johndoe-filename))]
      (schema.core/validate Person person))))

(let [conn (atomic/create-database (str "datomic:mem://" (d/squuid)))]
  (setup conn)
  (let [db (d/db conn)
        person (d/entity db (first (d/q '[:find [?e ...] :where [?e :person/email-address]] db)))
        cv (generate-cv db person)]

    (fact "Basic structure ok"
          cv
          =>
          (just {:telefon             "+47 9206 1819"
                 :twitter             "johndoe"
                 :certifications      anything
                 :educations          anything
                 :key_qualifications  anything
                 :languages           anything
                 :presentations       anything
                 :project_experiences anything
                 :technologies        anything
                 }))

    (fact "certification ok"
          (:certifications cv)
          =>
          (just [{:long_description {:no "Url: https://www.coursera.org/learn/introduction-tensorflow\nCertificate-name: Statements of Accomplishment\nCertificate-url: https://www.coursera.org/account/accomplishments/verify/ZSEZVPCC2MDS"}
                  :name             {:no "Introduction to TensorFlow for Artificial Intelligence,,,"}
                  :organizer        {:no "Deeplearning.ai / Coursera"}
                  :year             2019}
                 {:long_description {:no ""}
                  :name             {:no "Certified ScrumMaster"}
                  :organizer        {:no nil}
                  :year             2006}])
          :in-any-order)

    (fact "educations ok"
          (:educations cv)
          =>
          [{:degree    {:no "8 vekttall matematikk"}
            :school    {:no "Universitet i Oslo"}
            :year_from 1987
            :year_to   1987}
           {:degree    {:no "Bedriftsøkonomistudiet"}
            :school    {:no "Handelshøyskolen BI"}
            :year_from 1991
            :year_to   1992}]
          )

    (fact "key_qualifications ok"
          (:key_qualifications cv)
          =>
          (just [{:label            {:no "Beskrivelse, person"}
                  :long_description {:no "\nJohn har lang og solid erfaring innen faget systemutvikling."}
                  :tag_line         {:no ""}}
                 {:label            {:no "Beskrivelse, cv"}
                  :long_description {:no "John er entusiastisk opptatt av fagområdet systemutvikling..."}
                  :tag_line         {:no ""}}
                 {:key_points       [{:name {:no "Erfaring med forskjellige former for funksjonell programmering"}}
                                     {:name {:no "Erfaring med moderne frontendutvikling med javascript, Typescript, React, Redux, CSS"}}]
                  :label            {:no "Nøkkelkvalifikasjoner"}
                  :long_description {:no "Systemutvikler"}}
                 {:key_points [{:name {:no "TypeScript"}}
                               {:name {:no "Groovy"}}]
                  :label      {:no "Prefererte teknologier"}}
                 {:key_points [{:name {:no "TypeScript"}}
                               {:name {:no "React"}}
                               {:name {:no "Redux"}}]
                  :label      {:no "Bruker på jobben"}}
                 {:key_points [{:name {:no "Reinforcement learning"}}]
                  :label      {:no "Favoritter for tiden"}}
                 {:key_points [{:name {:no "Tensorflow js"}}]
                  :label      {:no "Vil lære mer av"}}
                 {:key_points [{:long_description {:no "Url: https://github.com/grails/grails-core/commits?author=trygvea\nTechs: clojure.lang.LazySeq@753aa147"}
                                :name             {:no "grails-core"}}]
                  :label      {:no "Open source bidrag"}}
                 ]
                :in-any-order)
          )

    (fact "languages ok"
          (:languages cv)
          =>
          []
          )

    (fact "presentations ok"
          (:presentations cv)
          =>
          (just [{:description      {:no "Deep Learning / dyp læring"}
                  :long_description {:no "Event-name: Kodemaker fagdag"}
                  :month            5
                  :year             2015}
                 {:description      {:no "Groovy collection API"}
                  :long_description {:no "Description: En oversikt over groovy's collection api\nEvent-name: Communities in Action\nSlides-url: http://www.slideshare.net/trygvea/groovy-collection-api-as-held-on-ci-a"}
                  :month            2
                  :year             2012}]
                :in-any-order)
          )

    (fact "project_experiences ok"
          (:project_experiences cv)
          =>
          (just [{:customer                  {:no "Forsvaret"}
                  :description               {:no "PoC for innrapportering av materiell i skyen"}
                  :disabled                  false
                  :long_description          {:no "Laget et _Proof of Concept_..."}
                  :project_experience_skills [{:tags {:no "React"}}
                                              {:tags {:no "Redux"}}]
                  :year_from                 2019
                  :year_to                   2020}
                 {:customer                  {:no "SAS"}
                  :description               {:no "System for flyvedlikehold, m.fl."}
                  :disabled                  false
                  :long_description          {:no "Analyse og utvikling ..."}
                  :project_experience_skills [{:tags {:no "C"}}
                                              {:tags {:no "Plsql"}}]
                  :year_from                 1989
                  :year_to                   1994}
                 {:customer                  {:no "Kolonial.no"}
                  :description               {:no "Nytt terminalverktøy"}
                  :disabled                  false
                  :long_description          {:no "Utvikling av ny løsning..."}
                  :project_experience_skills [{:tags {:no "TypeScript"}}
                                              {:tags {:no "React"}}]
                  :year_from                 2020
                  :year_to                   2020}]
                :in-any-order)
          )

    (fact "technologies ok"
          (:technologies cv)
          =>
          (just [{:category          {:no "Programmeringsspråk"}
                  :technology_skills [{:tags {:no "TypeScript"}}
                                      {:tags {:no "Groovy"}}
                                      {:tags {:no "Python"}}
                                      {:tags {:no "C"}}
                                      {:tags {:no "Prolog"}}
                                      {:tags {:no "Pascal"}}]
                  :uncategorized     false}
                 {:category          {:no "Frontend"}
                  :technology_skills [{:tags {:no "React"}}]
                  :uncategorized     false}
                 {:category          {:no "Metodikk"}
                  :technology_skills [{:tags {:no "Maskinlæring"}}
                                      {:tags {:no "Dyp læring"}}]
                  :uncategorized     false}
                 {:category          {:no "Bibliotek"}
                  :technology_skills [{:tags {:no "Redux"}}]
                  :uncategorized     false}
                 {:category          {:no nil}
                  :technology_skills [{:tags {:no "Plsql"}}
                                      {:tags {:no "Fortran"}}
                                      {:tags {:no "Dlib"}}]
                  :uncategorized     true}]
                :in-any-order)
          )))

