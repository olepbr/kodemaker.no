(ns kodemaker-no.cultivate
  (:require [kodemaker-no.cultivate.people :refer [cultivate-people]]
            [kodemaker-no.cultivate.tech :refer [cultivate-techs]]))

(defn cultivate-content [raw-content]
  (-> raw-content
      cultivate-people
      cultivate-techs))
