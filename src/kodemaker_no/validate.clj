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

   (optional-key :recommendations) [{:url Str
                                     :title Str
                                     :blurb Str
                                     :tech [Keyword]}]

   (optional-key :projects) [{:id Keyword
                              :customer Str
                              :description Str
                              :tech [Keyword]}]

   (optional-key :endorsements) [{:author Str
                                  :quote Str
                                  (optional-key :title) Str
                                  (optional-key :project) Keyword
                                  (optional-key :photo) Str}]})

(defn validate-content [content]
  (validate {:people [Person]
             :articles {Str Str}}
            content))
