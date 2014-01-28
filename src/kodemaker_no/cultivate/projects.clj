(ns kodemaker-no.cultivate.projects
  (:require [clojure.string :as str]
            [kodemaker-no.cultivate.tech :as tech]
            [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.homeless :refer [update-vals assoc-in-unless interleave-all]]))

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

(defn- add-tech [content project]
  (assoc-in-unless project [:tech] empty?
                   (->> (:people content)
                        vals
                        (mapcat :projects)
                        (filter #(= (:id project) (:id %)))
                        (map :tech)
                        (apply interleave-all)
                        (distinct)
                        (map (partial tech/look-up-tech-1 content)))))

(defn- cultivate-project [content project]
  (->> project
       add-url
       (add-people content)
       (add-endorsements content)
       (add-tech content)))

(defn cultivate-projects [content]
  (update-vals (:projects content) (partial cultivate-project content)))
