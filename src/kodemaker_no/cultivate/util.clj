(ns kodemaker-no.cultivate.util
  (:require [clojure.string :as str]))

(defn url [entity]
  (str "/" (name (:id entity)) "/"))

(defn add-url [m]
  (assoc m :url (url m)))

(defn capitalize [s]
  (str (.toUpperCase (subs s 0 1))
       (subs s 1)))

(defn str-for-humans [id]
  (-> id
      name
      (str/replace "-" " ")
      capitalize))

(defn tech-name [content id]
  (or (-> content :tech-names id)
      (str-for-humans id)))

(defn look-up-tech [content id]
  (if-let [tech (get-in content [:tech id])]
    (-> tech (select-keys #{:id, :name}) add-url)
    {:id id, :name (tech-name content id)}))
