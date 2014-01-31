(ns kodemaker-no.validate
  (:require [schema.core :refer [optional-key validate either Str Keyword Num]]))

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
              (optional-key :twitter) Str  ;; brukernavn
              (optional-key :linkedin) Str ;; path til din offentlige side
              (optional-key :stackoverflow) Str ;; path til din offentlige side
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
                             (optional-key :illustration) Str
                             (optional-key :url) Str}]

   (optional-key :side-projects) [{:title Str
                                   :description Str
                                   :illustration Str
                                   (optional-key :link) {:url Str :text Str}
                                   (optional-key :tech) [Keyword]}]

   (optional-key :blogs) [{:id Keyword ;; brukes bare for å referere til i :blog-posts
                           :name Str
                           :url Str
                           :theme Str ;; veldig kort, typ "teknisk frontend" eller "groovy"
                           :tech [Keyword]}]

   (optional-key :blog-posts) [{:url Str
                                :title Str
                                :blurb Str
                                (optional-key :tech) [Keyword]
                                :blog (either Keyword ;; :id fra :blogs
                                              {:name Str ;; eller rett inline
                                               :url Str})}]

   (optional-key :presentations) [{:title Str ;; foredrag som du selv har holdt
                                   :blurb Str
                                   :tech [Keyword]
                                   :urls {(optional-key :video) Str
                                          (optional-key :slides) Str
                                          (optional-key :source) Str} ;; må ha minst en av disse URLene
                                   :thumb Str}]

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
                              :tech [Keyword]}]

   (optional-key :endorsements) [{:author Str ;; anbefalinger, gjerne fra linkedin
                                  :quote Str
                                  (optional-key :title) Str
                                  (optional-key :project) Keyword
                                  (optional-key :photo) Str}]})

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
   (optional-key :illustration) Str
   (optional-key :site) Str})

(defn validate-content [content]
  (validate {:people {Keyword Person}
             :tech {Keyword Tech}
             :projects {Keyword Project}
             :articles {Str Str}
             :tech-names {Keyword Str}}
            content))
