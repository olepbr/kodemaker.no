(ns kodemaker-no.cultivate.cvs
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [kodemaker-no.cultivate.people :as people]
            [kodemaker-no.cultivate.util :as util]))

(defn cv-url [person]
  (when-let [cv-id (-> person :presence :cv)]
    (format "/cv/%s/" cv-id)))

(defn override [m ns]
  (merge m
         (->> (keys m)
              (filter keyword?)
              (filter #(and (= (namespace %) ns) (contains? m (keyword (name %)))))
              (map (fn [k] [(-> k name keyword) (k m)]))
              (into {}))))

(defn apply-cv-overrides [person]
  (walk/postwalk #(if (map? %) (override % "cv") %) person))

(defn compare-preferred-techs [preferred {:keys [id]}]
  (let [idx (if (seq preferred)
              (.indexOf preferred id)
              -1)]
    (if (< idx 0)
      (count preferred)
      idx)))

(defn cultivate-techs [person content]
  (let [preferred (-> person :cv :default :preferred-techs)
        exclude-techs (->> person :cv :default :exclude-techs (into #{}))]
    (->> (concat (->> person :tech :using-at-work)
                 (->> person :innate-skills)
                 (->> (select-keys person [:side-projects
                                           :blog-posts
                                           :screencasts
                                           :presentations
                                           :appearances
                                           :open-source-projects
                                           :open-source-contributions
                                           :projects])
                      vals
                      (apply concat)
                      (mapcat :tech)
                      (remove exclude-techs)))
         flatten
         (group-by identity)
         (map (fn [[k ks]] (merge (util/look-up-tech content k) {:count (count ks)})))
         (sort-by :count)
         reverse
         (sort-by #(compare-preferred-techs preferred %))
         (group-by :type))))

(defn lookup-employer [employers project]
  (assoc project :employer {:name (employers (:employer project))
                            :id (:employer project)}))

(def months ["Januar" "Februar" "Mars" "April" "Mai" "Juni" "Juli"
             "August" "September" "Oktober" "November" "Desember"])

(defn- date->ym [appearance]
  (if (string? (:date appearance))
    (let [[y m] (str/split (:date appearance) #"-")]
      (-> appearance
          (assoc :year-month (format "%s %s" (months (dec (Integer/parseInt m))) y))
          (assoc :year-month-numeric (format "%02d.%s" (Integer/parseInt m) y))))
    appearance))

(defn- cultivate-appearances [{:keys [presentations appearances]}]
  (->> (concat presentations appearances)
       (sort-by :date)
       reverse
       (map date->ym)))

(defn- main-proglang [x]
  (->> x
       :tech
       (filter #(= :proglang (:type %)))
       first
       :name))

(defn- cultivate-open-source-contributions [{:keys [open-source-projects open-source-contributions]}]
  (->> (concat (map #(assoc % :role :developer) open-source-projects)
               (map #(assoc % :role :contributer) open-source-contributions))
       (group-by main-proglang)))

(defn cultivate-side-project [side-project]
  {:title (or (:cv/title side-project) (:title side-project))
   :url (or (-> side-project :link :url) (:url side-project))
   :summary (or (:cv/blurb side-project) (:cv/description side-project)
                (:blurb side-project) (:description side-project))})

(defn prefix-title [prefix item]
  (update-in item [:title] #(str prefix %)))

(defn cultivate-others [{:keys [blog-posts screencasts side-projects]}]
  (concat (map cultivate-side-project screencasts)
          (map cultivate-side-project side-projects)
          (map (comp cultivate-side-project #(prefix-title "Artikkel: " %)) blog-posts)))

(defn- add-link-to-next-cv [person content]
  (assoc person :next-person-cv-url (people/next-person-link content person cv-url)))

(defn cultivate-cv [person tech content]
  (let [data ((:id person) (:people content))]
    (-> person
        (assoc :years-experience (when-let [xp (:experience-since person)]
                                   (- (+ 1900 (.getYear (java.util.Date.))) xp)))
        (assoc :techs (cultivate-techs data content))
        (update-in [:projects] #(map (partial lookup-employer (:employers content)) %))
        (assoc :appearances (cultivate-appearances person))
        (assoc :open-source-contributions (cultivate-open-source-contributions person))
        (assoc :url (cv-url person))
        (assoc :other (cultivate-others person))
        apply-cv-overrides
        (add-link-to-next-cv content))))

(defn cultivate-cvs [raw-content people tech]
  (->> people
       (filter #(-> % second :cv))
       (map (fn [[id person]] [id (cultivate-cv person tech raw-content)]))
       (into {})))
