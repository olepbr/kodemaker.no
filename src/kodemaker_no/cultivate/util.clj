(ns kodemaker-no.cultivate.util)

(defn url [entity]
  (str "/" (subs (str (:id entity)) 1) "/"))
