(ns kodemaker-no.cultivate.tech
  (:require [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.cultivate.videos :refer [replace-video-urls]]
            [kodemaker-no.date :as d]
            [kodemaker-no.homeless :refer [update-vals assoc-in-unless]]))

(defn look-up-tech [content techs]
  (map #(util/look-up-tech content %) techs))

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
      (select-keys #{:title :blurb :direct-link?})
      (assoc
          :by (map :by presentations)
          :tech (distinct (mapcat :tech presentations))
          :urls (apply merge (map :urls presentations)))))

(defn- combine-side-projects [side-projects]
  (-> (first side-projects)
      (select-keys #{:title :description :illustration :link})
      (assoc
          :by (map :by side-projects)
          :tech (distinct (mapcat :tech side-projects)))))

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
                        (group-by :link)
                        vals
                        (map combine-side-projects)
                        (filter #(is-about tech %)))))

(defn- cultivate-tech [content tech]
  (->> tech
       util/add-url
       (add-recommendations content)
       (add-presentations content)
       replace-video-urls
       (add-blog-posts content)
       (add-upcoming content)
       (add-side-projects content)
       (add-open-source-projects content)))

(defn cultivate-techs [content]
  (update-vals (:tech content) (partial cultivate-tech content)))
