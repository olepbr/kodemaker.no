(ns kodemaker-no.cultivate.util)

(defn url [entity]
  (str "/" (name (:id entity)) "/"))
