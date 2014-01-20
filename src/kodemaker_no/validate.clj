(ns kodemaker-no.validate
  (:require [schema.core :refer [optional-key validate Str Keyword Num]]))

(def Person
  {:id Keyword
   :first-name Str
   :last-name Str
   (optional-key :middle-name) Str
   :title Str
   :order Num
   :description Str
   (optional-key :administration?) Boolean

   (optional-key :blogs) [{:name Str
                           :url Str
                           :posts [{:url Str
                                    :title Str
                                    :blurb Str
                                    :tech [Keyword]}]}]

   (optional-key :recommendations) [{:url Str
                                     :title Str
                                     :blurb Str
                                     :tech [Keyword]}]

   (optional-key :projects) [{:id Keyword
                              :customer Str
                              :description Str
                              :tech [Keyword]}]

   (optional-key :references) [{:author Str
                                :quote Str
                                (optional-key :project) Keyword
                                (optional-key :photo) Str}]})

(defn validate-content [content]
  (validate {:people [Person]
             :articles {Str Str}}
            content))
