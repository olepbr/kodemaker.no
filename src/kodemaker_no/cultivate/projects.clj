(ns kodemaker-no.cultivate.projects
  (:require [clojure.string :as str]
            [kodemaker-no.cultivate.tech :as tech]
            [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.homeless :refer [update-vals assoc-in-unless interleave-all update-in-existing]]))

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
                :thumb (str "/photos/people/" (-> person :id name) "/side-profile.jpg")}]
      (map #(assoc % :person info) (key person)))))

(defn- add-people [content project]
  (assoc-in-unless project [:people] empty?
                   (->> (:people content)
                        vals
                        (mapcat (get-with-person-info :projects))
                        (filter #(= (:id project) (:id %)))
                        (map #(assoc (:person %)
                                :description (:description %)
                                :years (:years %))))))

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

(defn- add-related-projects [content project]
  (let [related (->> (vals (:projects content))
                     (filter #(and (not= (:id project) (:id %))
                                   (= (:logo project) (:logo %))))
                     (map add-url))]
    (if (seq related)
      (assoc project :related-projects related)
      project)))

(defn- remove-duplicate-faces-1 [faces endorsements]
  (map #(if (faces (:photo %))
          (dissoc % :photo :title)
          %) endorsements))

(defn- remove-duplicate-faces [project]
  (let [face (-> project :reference :photo)]
    (update-in-existing project [:endorsements]
                        (partial remove-duplicate-faces-1 #{face}))))

(defn- cultivate-project [content project]
  (->> project
       add-url
       (add-people content)
       (add-endorsements content)
       (add-tech content)
       (add-related-projects content)
       (remove-duplicate-faces)))

(defn cultivate-projects [content]
  (update-vals (:projects content) (partial cultivate-project content)))
