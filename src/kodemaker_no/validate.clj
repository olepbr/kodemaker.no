(ns kodemaker-no.validate
  (:require [schema.core :refer [optional-key validate either Str Keyword Num]]))

(def Person
  {:id Keyword
   :name [Str]
   :title Str
   :start-date Str
   :description Str
   (optional-key :administration?) Boolean

   :phone-number Str
   :email-address Str

   :presence {(optional-key :cv) Str ;; Kodemaker cv id
              (optional-key :twitter) Str  ;; username
              (optional-key :linkedin) Str ;; path to public profile
              (optional-key :stackoverflow) Str ;; path to public profile
              (optional-key :github) Str ;; username
              (optional-key :coderwall) Str} ;; username

   (optional-key :tech) {:favorites-at-the-moment [Keyword]
                         (optional-key :want-to-learn-more) [Keyword]}

   (optional-key :recommendations) [{:link {:url Str :text Str}
                                     :title Str
                                     :blurb Str
                                     :tech [Keyword]}]

   (optional-key :hobbies) [{:title Str
                             :description Str
                             :illustration Str
                             (optional-key :url) Str}]

   (optional-key :blogs) [{:id Keyword
                           :name Str
                           :url Str
                           :theme Str ;; very short
                           :tech [Keyword]}]

   (optional-key :blog-posts) [{:url Str
                                :title Str
                                :blurb Str
                                :tech [Keyword]
                                :blog (either Keyword ;; :id from :blogs
                                              {:name Str
                                               :url Str})}]

   (optional-key :presentations) [{:title Str
                                   :blurb Str
                                   :tech [Keyword]
                                   :urls {(optional-key :video) Str
                                          (optional-key :slides) Str
                                          (optional-key :source) Str}
                                   :thumb Str}]

   (optional-key :upcoming) [{:title Str ;; Upcoming courses or presentations
                              :description Str
                              :url Str
                              :tech [Keyword]
                              :date Str}] ;; iso-8601

   (optional-key :open-source-projects) [{:url Str
                                          :name Str
                                          :description Str
                                          :tech [Keyword]}] ;; sorted under first tech

   (optional-key :open-source-contributions) [{:url Str
                                               :name Str
                                               :tech [Keyword]}] ;; sorted under first tech

   (optional-key :projects) [{:id Keyword
                              :customer Str
                              :description Str
                              :tech [Keyword]}]

   (optional-key :endorsements) [{:author Str
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
   (optional-key :illustration) Str})

(defn validate-content [content]
  (validate {:people {Keyword Person}
             :tech {Keyword Tech}
             :projects {Keyword Project}
             :articles {Str Str}}
            content))
