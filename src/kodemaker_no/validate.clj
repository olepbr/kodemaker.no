(ns kodemaker-no.validate
  (:require [schema.core :refer [optional-key validate either Str Keyword Num pred]]))

(def Path (pred (fn [^String s] (re-find #"^(/[a-zA-Z0-9_\-.]+)+/?$" s)) 'path))

(def Person
  {:id Keyword
   :name [Str]
   :title Str
   :start-date Str
   :description Str ;; Skrives i tredjeperson, alt annet i førsteperson
   (optional-key :administration?) Boolean

   :phone-number Str
   :email-address Str

   :presence {(optional-key :cv) Str ;; Kodemaker cv id
              (optional-key :twitter) Str ;; brukernavn
              (optional-key :linkedin) Path ;; path til din offentlige side
              (optional-key :stackoverflow) Path ;; path til din offentlige side
              (optional-key :github) Str ;; brukernavn
              (optional-key :coderwall) Str} ;; brukernavn

   (optional-key :tech) {:favorites-at-the-moment [Keyword]
                         (optional-key :want-to-learn-more) [Keyword]}

   (optional-key :recommendations) [{:link {:url Str :text Str} ;; lenketekst av typen "Se foredraget" og "Les artikkelen"
                                     :title Str ;; Samme som tittel på det du lenker til
                                     :blurb Str ;; Litt om hvorfor du anbefaler
                                     :tech [Keyword]}]

   (optional-key :hobbies) [{:title Str
                             :description Str
                             (optional-key :illustration) Path
                             (optional-key :url) Str}]

   (optional-key :side-projects) [{:title Str
                                   :description Str
                                   :illustration Path
                                   (optional-key :link) {:url Str :text Str}
                                   (optional-key :tech) [Keyword]}]

   (optional-key :blog-posts) [{:url Str
                                :title Str
                                :blurb Str
                                (optional-key :tech) [Keyword]}]

   (optional-key :presentations) [{:title Str ;; foredrag som du selv har holdt
                                   :blurb Str
                                   :tech [Keyword]
                                   :urls {(optional-key :video) Str
                                          (optional-key :slides) Str
                                          (optional-key :source) Str} ;; må ha minst en av disse URLene
                                   :thumb Path}]

   (optional-key :upcoming) [{:title Str ;; Kommende kurs eller presentasjoner
                              :description Str
                              :url Str
                              :tech [Keyword]
                              :date Str}] ;; iso-8601

   (optional-key :open-source-projects) [{:url Str
                                          :name Str
                                          :description Str
                                          :tech [Keyword]}] ;; sortert under første tech

   (optional-key :open-source-contributions) [{:url Str
                                               :name Str
                                               :tech [Keyword]}] ;; sortert under første tech

   (optional-key :projects) [{:id Keyword ;; prosjekter du har deltatt i med Kodemaker
                              :customer Str
                              :description Str
                              :years [Num] ;; årstallene du jobbet der, typ [2013 2014]
                              :tech [Keyword]}] ;; hvilke tech jobbet du med? viktigst først

   (optional-key :endorsements) [{:author Str ;; anbefalinger, gjerne fra linkedin
                                  :quote Str
                                  (optional-key :title) Str
                                  (optional-key :project) Keyword
                                  (optional-key :photo) Path}]})

(def Tech
  {:id Keyword
   :name Str
   :description Str
   (optional-key :illustration) Str
   (optional-key :site) Str})

(def Project
  {:id Keyword
   :name Str
   :logo Str
   :description Str
   :awesomeness Num ;; brukes for sortering - kule prosjekter på toppen
   (optional-key :illustration) Str
   (optional-key :site) Str})

(def BlogPost
  {:title Str
   :published java.util.Date
   (optional-key :illustration) Str
   :body Str})

(def Article
  {:title Str
   (optional-key :illustration) Str
   (optional-key :aside) Str
   :lead Str
   (optional-key :body) Str})

(defn validate-content [content]
  (validate {:people {Keyword Person}
             :tech {Keyword Tech}
             :projects {Keyword Project}
             :articles {Path Article}
             :tech-names {Keyword Str}
             :blog-posts {Path BlogPost}}
            content))
