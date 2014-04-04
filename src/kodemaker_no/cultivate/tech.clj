(ns kodemaker-no.cultivate.tech
  (:require [clojure.string :as str]
            [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.homeless :refer [update-vals assoc-in-unless]]
            [kodemaker-no.date :as d]))

(defn- add-url [tech]
  (assoc tech :url (util/url tech)))

(defn- capitalize [s]
  (str (.toUpperCase (subs s 0 1))
       (subs s 1)))

(defn- str-for-humans [id]
  (-> id
      name
      (str/replace "-" " ")
      capitalize))

(defn look-up-tech-1 [content id]
  (if-let [tech (get-in content [:tech id])]
    (-> tech (select-keys #{:id, :name}) add-url)
    {:id id, :name (or (-> content :tech-names id)
                       (str-for-humans id))}))

(defn look-up-tech [content techs]
  (map #(look-up-tech-1 content %) techs))

(defn- is-about [tech m]
  ((set (:tech m)) (:id tech)))

(defn- get-with-byline [key]
  (fn [person]
    (->> (key person)
         (map #(assoc % :by {:name (first (:name person))
                             :url (util/url person)})))))

(defn- combine-recommendations [recommendations]
  (-> (first recommendations)
      (select-keys #{:title :blurb :link})
      (assoc
          :by (map :by recommendations)
          :tech (distinct (mapcat :tech recommendations)))))

(defn- add-recommendations [content tech]
  (assoc-in-unless tech [:recommendations] empty?
                   (->> (:people content)
                        vals
                        (mapcat (get-with-byline :recommendations))
                        (group-by #(-> % :link :url))
                        vals
                        (map combine-recommendations)
                        (filter #(is-about tech %)))))

(defn- combine-open-source-projects [projects]
  (-> (first projects)
      (select-keys #{:url :name :description})
      (assoc
          :by (map :by projects)
          :tech (distinct (mapcat :tech projects)))))

(defn- normalized-url [{:keys [^String url]}]
  (if (.endsWith url "/")
    (subs url 0 (dec (count url)))
    url))

(defn- add-open-source-projects [content tech]
  (assoc-in-unless tech [:open-source-projects] empty?
                   (->> (:people content)
                        vals
                        (mapcat (get-with-byline :open-source-projects))
                        (group-by normalized-url)
                        vals
                        (map combine-open-source-projects)
                        (filter #(is-about tech %)))))

(defn- combine-presentations [presentations]
  (-> (first presentations)
      (select-keys #{:title :blurb})
      (assoc
          :by (map :by presentations)
          :tech (distinct (mapcat :tech presentations))
          :urls (apply merge (map :urls presentations)))))

(defn- add-presentations [content tech]
  (assoc-in-unless tech [:presentations] empty?
                   (->> (:people content)
                        vals
                        (mapcat (get-with-byline :presentations))
                        (group-by :urls)
                        vals
                        (map combine-presentations)
                        (filter #(is-about tech %)))))

(defn- add-blog-posts [content tech]
  (assoc-in-unless tech [:blog-posts] empty?
                   (->> (:people content)
                        vals
                        (mapcat (get-with-byline :blog-posts))
                        (filter #(is-about tech %)))))

(defn- add-upcoming [content tech]
  (assoc-in-unless tech [:upcoming] empty?
                   (->> (:people content)
                        vals
                        (mapcat (get-with-byline :upcoming))
                        (filter #(is-about tech %))
                        (map #(update-in % [:date] d/parse-ymd)))))

(defn- add-side-projects [content tech]
  (assoc-in-unless tech [:side-projects] empty?
                   (->> (:people content)
                        vals
                        (mapcat (get-with-byline :side-projects))
                        (filter #(is-about tech %)))))

(defn- cultivate-tech [content tech]
  (->> tech
       add-url
       (add-recommendations content)
       (add-presentations content)
       (add-blog-posts content)
       (add-upcoming content)
       (add-side-projects content)
       (add-open-source-projects content)))

(defn cultivate-techs [content]
  (update-vals (:tech content) (partial cultivate-tech content)))
