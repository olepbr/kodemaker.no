(ns kodemaker-no.validate
  (:require [schema.core :refer [optional-key validate either Str Keyword Num Any pred both]]
            [clj-time.format :refer [parse formatters]]))

(def Path (pred (fn [^String s] (re-find #"^(/[a-zA-Z0-9_\-.]+)+/?$" s)) 'simple-slash-prefixed-path))
(def URL (pred (fn [^String s] (re-find #"^(?i)\b(https?(?:[^\s()<>]+|\(([^\s()<>]+|(\([^\s()<>]+\)))*\))+(?:\(([^\s()<>]+|(\([^\s()<>]+\)))*\)|[^\s`!()\[\]{};:'\".,<>?«»“”‘’]))$" s)) 'url))
(def ID (both Keyword (pred (fn [kw] (re-find #"^:[a-z0-9-]+$" (str kw))) 'simple-lowercase-keyword)))
(def Date (pred (fn [^String s] (try (parse (formatters :year-month-day) s) true (catch Exception e false)))))

(def Person
  {:id ID
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

   (optional-key :tech) {(optional-key :using-at-work) [ID]
                         (optional-key :favorites-at-the-moment) [ID]
                         (optional-key :want-to-learn-more) [ID]}

   (optional-key :recommendations) [{:title Str ;; Samme som tittel på det du lenker til
                                     :blurb Str ;; Litt om hvorfor du anbefaler
                                     :link {:url URL :text Str} ;; lenketekst av typen "Se foredraget" og "Les artikkelen"
                                     :tech [ID]}]

   (optional-key :hobbies) [{:title Str
                             :description Str
                             :illustration Path
                             (optional-key :url) URL}]

   (optional-key :side-projects) [{:title Str
                                   :description Str
                                   :illustration Path
                                   (optional-key :link) {:url URL :text Str}
                                   (optional-key :tech) [ID]}]

   (optional-key :blog-posts) [{:url URL
                                :title Str
                                :blurb Str
                                (optional-key :tech) [ID]}]

   (optional-key :presentations) [{:title Str ;; foredrag som du selv har holdt
                                   (optional-key :id) ID ;; brukes til å generere URL for video-presentasjoner
                                   :blurb Str
                                   :tech [ID]
                                   :urls {(optional-key :video) URL
                                          (optional-key :slides) URL
                                          (optional-key :source) URL}}] ;; må ha minst en av disse URLene

   (optional-key :upcoming) [{:title Str ;; Kommende kurs eller presentasjoner
                              :description Str
                              :url URL ;; Link til feks din abstract hos konferansen
                              (optional-key :call-to-action) {:url URL :text Str} ;; Bruk denne til "Meld deg på kurs" o.l.
                              :tech [ID]
                              :location {:title Str :url URL} ;; Eks {:title "JavaZone (Oslo)", :url "http://javazone.no"}
                              :date Date}] ;; iso-8601 yyyy-mm-dd

   (optional-key :open-source-projects) [{:url URL
                                          :name Str
                                          :description Str
                                          :tech [ID]}] ;; sortert under første tech

   (optional-key :open-source-contributions) [{:url URL
                                               :name Str
                                               :tech [ID]}] ;; sortert under første tech

   (optional-key :projects) [{:id ID ;; prosjekter du har deltatt i med Kodemaker
                              :customer Str
                              :description Str
                              :years [Num] ;; årstallene du jobbet der, typ [2013 2014]
                              :tech [ID]}] ;; hvilke tech jobbet du med? viktigst først

   (optional-key :endorsements) [{:author Str ;; anbefalinger, gjerne fra linkedin
                                  :quote Str
                                  (optional-key :title) Str
                                  (optional-key :project) ID
                                  (optional-key :photo) Path}]})

(def Tech
  {:id ID
   :name Str
   :description Str
   (optional-key :illustration) Str
   (optional-key :site) URL
   (optional-key :mail-form) {:list-id Str :heading Str :button-text Str :blurb Str}})

(def Project
  {:id ID
   :name Str
   :logo Str
   :description Str
   :awesomeness Num ;; brukes for sortering - kule prosjekter på toppen
   (optional-key :illustration) Path
   (optional-key :site) URL
   (optional-key :reference) {:author Str ;; Kodemakers prosjektreferanse
                              :quote Str
                              :title Str
                              (optional-key :email) Str
                              (optional-key :phone) Str
                              (optional-key :photo) Path}})

(def BlogPost
  {:title Str
   :published Date ;; iso-8601 yyyy-mm-dd
   (optional-key :illustration) Path
   :body Str})

(def Article
  {:title Str
   (optional-key :illustration) Path
   (optional-key :aside) Str
   :lead Str
   (optional-key :body) Str})

(def VideoOverride
  {(optional-key :blurb) Str
   (optional-key :call-to-action) {:seconds-to-delay Num
                                   :content Any}})

(defn validate-content [content]
  (validate {:people {ID Person}
             :tech {ID Tech}
             :projects {ID Project}
             :articles {Path Article}
             :tech-names {ID Str}
             :blog-posts {Path BlogPost}
             :video-overrides {ID VideoOverride}}
            content))
