(ns kodemaker-no.cultivate.projects
  (:require [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.homeless :refer [update-vals assoc-in-unless]]
            [clojure.string :as str]))

(defn- add-url [project]
  (assoc project :url (util/url project)))

(defn look-up-project [content id]
  (when-let [project (get-in content [:projects id])]
    (-> project (select-keys #{:id, :name}) add-url)))

(defn- get-with-person-info [key]
  (fn [person]
    (let [info {:url (util/url person)
                :full-name (str/join " " (:name person))
                :first-name (first (:name person))
                :thumb (str "/photos/people/" (-> person :id str (subs 1)) "/side-profile.jpg")}]
      (map #(assoc % :person info) (key person)))))

(defn- add-people [content project]
  (assoc-in-unless project [:people] empty?
                   (->> (:people content)
                        vals
                        (mapcat (get-with-person-info :projects))
                        (filter #(= (:id project) (:id %)))
                        (map #(assoc (:person %) :description (:description %))))))

(defn- add-endorsements [content project]
    (assoc-in-unless project [:endorsements] empty?
                     (->> (:people content)
                          vals
                          (mapcat (get-with-person-info :endorsements))
                          (filter #(= (:id project) (:project %))))))

(defn- cultivate-project [content project]
  (->> project
       add-url
       (add-people content)
       (add-endorsements content)))

(defn cultivate-projects [content]
  (update-vals (:projects content) (partial cultivate-project content)))
