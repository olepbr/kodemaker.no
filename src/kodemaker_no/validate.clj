(ns kodemaker-no.validate
  (:require [clj-time.format :refer [parse formatters]]
            [schema.core :refer [optional-key validate either Str Keyword Num Any pred both enum conditional]]))

(def Path (pred (fn [^String s] (re-find #"^(/[a-zA-Z0-9_\-.]+)+/?$" s)) 'simple-slash-prefixed-path))
(def URL (pred (fn [^String s] (re-find #"^(?i)\b(https?(?:[^\s()<>]+|\(([^\s()<>]+|(\([^\s()<>]+\)))*\))+(?:\(([^\s()<>]+|(\([^\s()<>]+\)))*\)|[^\s`!()\[\]{};:'\".,<>?«»“”‘’]))$" s)) 'url))
(def ID (both Keyword (pred (fn [kw] (re-find #"^:[a-z0-9-]+$" (str kw))) 'simple-lowercase-keyword)))
(def Date (pred (fn [^String s] (try (parse (formatters :year-month-day) s) true (catch Exception e false)))))
(def YearMonth (pred (fn [^String s] (try (parse (formatters :year-month) s) true (catch Exception e false)))))
(def YearRange [(conditional number? Num keyword? (enum :ongoing))])
(def UrlOrPath (conditional #(re-find #"^/" %) Path identity URL))

(def Person
  {:id ID
   :name [Str]
   :title Str
   :start-date Str
   :description Str ;; Skrives i tredjeperson, alt annet i førsteperson
   (optional-key :cv/description) Str ;; Hvis du ønsker en annen/lengre beskrivelse på CV-en
   (optional-key :administration?) Boolean
   (optional-key :quit?) Boolean
   (optional-key :profile-active?) Boolean

   :phone-number Str
   :email-address Str

   :presence {(optional-key :cv) Str ;; Kodemaker cv id
              (optional-key :twitter) Str ;; brukernavn
              (optional-key :linkedin) Path ;; path til din offentlige side
              (optional-key :stackoverflow) Path ;; path til din offentlige side
              (optional-key :github) Str ;; brukernavn
              (optional-key :coderwall) Str} ;; brukernavn

   (optional-key :profile-overview-picture) Path ;; Hvis du ikke ønsker tilfeldig bilde på profiloversikten
   (optional-key :profile-page-picture) Path ;; Hvis du ikke ønsker tilfeldig bilde på profilsiden
   (optional-key :cv-picture) Path ;; Hvis du ikke ønsker tilfeldig bilde på CV-en

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
                                :published Date ;; iso-8601 yyyy-mm-dd
                                (optional-key :cv/blurb) Str
                                (optional-key :tech) [ID]}]

   (optional-key :presentations) [{:title Str ;; foredrag som du selv har holdt
                                   (optional-key :id) ID ;; brukes til å generere URL for video-presentasjoner
                                   :blurb Str
                                   :tech [ID]
                                   (optional-key :event) Str ;; Konferansenavn etc
                                   :date Date ;; iso-8601 yyyy-mm-dd
                                   :urls {(optional-key :video) URL
                                          (optional-key :slides) URL
                                          (optional-key :source) URL} ;; må ha minst en av disse URLene
                                   (optional-key :thumb) Str
                                   (optional-key :direct-link?) Boolean}] ;; true hvis det ikke skal embeddes video på kodemaker-sidene

   (optional-key :upcoming) [{:title Str ;; Kommende kurs eller presentasjoner
                              :description Str
                              :url URL ;; Link til feks din abstract hos konferansen
                              (optional-key :call-to-action) {:url URL :text Str} ;; Bruk denne til "Meld deg på kurs" o.l.
                              :tech [ID]
                              :location {:title Str :url URL} ;; Eks {:title "JavaZone (Oslo)", :url "http://javazone.no"}
                              :date Date}] ;; iso-8601 yyyy-mm-dd

   (optional-key :appearances) [{:title Str ;; Mindre profilerte kurs/workshops/foredrag til CV-en
                                 :event Str
                                 :date Date ;; iso-8601 yyyy-mm-dd
                                 :tech [ID]
                                 (optional-key :urls) {(optional-key :video) URL
                                                       (optional-key :slides) URL
                                                       (optional-key :source) URL}}]

   (optional-key :screencasts) [{:title Str ;; screencasts du selv har laget
                                 (optional-key :blurb) Str
                                 :description Str
                                 :illustration Path
                                 (optional-key :cv/blurb) Str
                                 :tech [ID]
                                 (optional-key :launch-date) Date;; iso-8601 yyyy-mm-dd
                                 :url URL}]

   (optional-key :open-source-projects) [{:url URL
                                          :name Str
                                          :description Str
                                          :tech [ID]}] ;; sortert under første tech

   (optional-key :open-source-contributions) [{:url URL
                                               :name Str
                                               :tech [ID]}] ;; sortert under første tech

   (optional-key :projects) [{:customer Str
                              (optional-key :cv/customer) Str
                              (optional-key :summary) Str
                              (optional-key :employer) ID
                              (optional-key :description) Str
                              (optional-key :cv/description) Str
                              (optional-key :exclude-from-profile?) Boolean
                              (optional-key :years) YearRange ;; årstallene du jobbet der, typ [2013 2014]. [2018 :ongoing] for å beskrive et pågående prosjekt
                              (optional-key :start) YearMonth ;; ...eller år/måned du startet
                              (optional-key :end) YearMonth   ;; og sluttet
                              :tech [ID]}] ;; hvilke tech jobbet du med? viktigst først

   (optional-key :endorsements) [{:author Str ;; anbefalinger, gjerne fra linkedin
                                  :quote Str
                                  (optional-key :title) Str ;; tittel, firma
                                  (optional-key :photo) Path}]

   ;; For CV-er
   (optional-key :born) Num
   (optional-key :education-summary) Str
   (optional-key :experience-since) Num
   (optional-key :qualifications) [Str]
   (optional-key :innate-skills) [ID] ;; Techs du vil ha lista på CV-en men som du ikke har tatt deg
                                      ;; bryet å knytte til et prosjekt av noe slag
   (optional-key :employments) {ID {:description Str}}

   (optional-key :education) [{:institution Str ;; Utdanning
                               :years YearRange
                               :subject Str}]

   (optional-key :languages) [{:language Str
                               :orally (enum "Grunnleggende" "God" "Meget god" "Flytende" "Morsmål")
                               :written (enum "Grunnleggende" "God" "Meget god" "Flytende" "Morsmål")}]

   (optional-key :project-highlights) [{:customer Str
                                        :blurb Str
                                        (optional-key :link) Str}]

   (optional-key :endorsement-highlight) {:author Str ;; Kort utsnitt fra den fineste anbefalingen din
                                          :quote Str  ;; Brukes øverst på CV-en
                                          :title Str}

   (optional-key :certifications) [{:name Str
                                    :year Num
                                    (optional-key :institution) Str
                                    (optional-key :url) URL
                                    (optional-key :certificate) 
                                      {:url UrlOrPath
                                       (optional-key :text) Str}}]

   (optional-key :domain-skills) [{:title Str
                                   :description Str}]

   (optional-key :cv) {ID {(optional-key :preferred-techs) [ID]
                           (optional-key :exclude-techs) [ID]}}

   (optional-key :business-presentations) [{:title Str ;; foredrag du selger til kunder
                                            :blurb Str
                                            :tech [ID]
                                            :duration Str}]

   (optional-key :workshops) [{:title Str ;; workshop eller kurs til salgs
                               :blurb Str
                               :tech [ID]
                               :duration Str
                               :participants {:min Num
                                              :max Num}}]})

(def Tech
  {:id ID
   :name Str
   :description Str
   :type (enum :proglang
               :vcs
               :methodology
               :devtools
               :library
               :framework
               :server
               :database
               :devops
               :os
               :frontend
               :specification
               :tool
               :other)
   (optional-key :illustration) Str
   (optional-key :site) URL
   (optional-key :ad) {:heading Str
                       :blurb Str
                       :link-text Str}})

(def BlogPost
  {:title Str
   (optional-key :published) Date ;; iso-8601 yyyy-mm-dd
   (optional-key :updated) Date ;; iso-8601 yyyy-mm-dd
   (optional-key :illustration) Path
   (optional-key :presence) Str
   (optional-key :contact-form) Str
   (optional-key :contact-form-button) Str
   (optional-key :author) Str
   (optional-key :blurb) Str
   (optional-key :tech) Str
   (optional-key :discussion) Str
   :body Str})

(def Article
  {:title Str
   (optional-key :illustration) Path
   (optional-key :aside) Str
   :lead Str
   (optional-key :meta) Str
   (optional-key :body) Str
   (optional-key :layout) Str})

(def VideoOverride
  {(optional-key :blurb) Str
   (optional-key :call-to-action) {:seconds-to-delay Num
                                   :content Any}})

(def Section
  {Keyword Str})

(defn validate-content [content]
  (validate {:people {ID Person}
             :tech {ID Tech}
             :articles {Path Article}
             :references {Path [Section]}
             :raw-pages {Path Str}
             :raw-css {Path Str}
             :tech-names {ID Str}
             :tech-types {ID ID}
             (optional-key :legacy-blog-posts) {Path BlogPost}
             :blog-posts {Path BlogPost}
             :video-overrides {ID VideoOverride}
             :employers {ID Str}}
            content))
