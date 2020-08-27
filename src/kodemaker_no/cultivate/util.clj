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
  (or (-> content :tech id :name)
      (-> content :tech-names id)
      (str-for-humans id)))

(defn tech-type [content id]
  (or (-> content :tech id :type)))

(defn look-up-tech [content id]
  (if-let [tech (get-in content [:tech id])]
    (-> tech (select-keys #{:id :name :type}) add-url)
    {:id id
     :name (tech-name content id)
     :type (tech-type content id)}))

(defn get-with-byline [key]
  (fn [person]
    (->> (key person)
         (map #(assoc % :by {:name (first (:name person))
                             :url (url person)})))))
