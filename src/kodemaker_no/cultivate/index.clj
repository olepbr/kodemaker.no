(ns kodemaker-no.cultivate.index
  (:require [clojure.string :as str]))

(defn- update-ref [content ref]
  (-> content :projects ref
      (assoc :url (str "/" (subs (str ref) 1) "/"))))

(defn cultivate-index [content]
  (-> (:index content)
      (update-in [:references] #(map (partial update-ref content) %))))
