(ns kodemaker-no.cultivate
  (:require [kodemaker-no.cultivate.people :refer [cultivate-people]]
            [kodemaker-no.cultivate.tech :refer [cultivate-techs]]
            [kodemaker-no.cultivate.projects :refer [cultivate-projects]]))

(defn cultivate-content [raw-content]
  (assoc raw-content
    :people (cultivate-people raw-content)
    :tech (cultivate-techs raw-content)
    :projects (cultivate-projects raw-content)))
