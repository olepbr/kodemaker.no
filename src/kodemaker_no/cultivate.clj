(ns kodemaker-no.cultivate
  (:require [kodemaker-no.cultivate.people :refer [cultivate-people]]
            [kodemaker-no.cultivate.tech :refer [cultivate-techs]]
            [kodemaker-no.cultivate.projects :refer [cultivate-projects]]
            [kodemaker-no.cultivate.blog-posts :refer [cultivate-blog-posts]]))

(defn cultivate-content [raw-content]
  (assoc raw-content
    :people (cultivate-people raw-content)
    :tech (cultivate-techs raw-content)
    :projects (cultivate-projects raw-content)
    :blog-posts (cultivate-blog-posts (:blog-posts raw-content))))
