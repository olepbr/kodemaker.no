(ns kodemaker-no.cultivate
  (:require [kodemaker-no.cultivate.people :refer [cultivate-people]]))

(defn cultivate-content [raw-content]
  (-> raw-content
      cultivate-people))
