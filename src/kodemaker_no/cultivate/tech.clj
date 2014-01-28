(ns kodemaker-no.cultivate.tech
  (:require [kodemaker-no.homeless :refer [update-vals assoc-in-unless]]
            [kodemaker-no.cultivate.util :as util]))

(defn- add-url [tech]
  (assoc tech :url (util/url tech)))

(defn look-up-tech-1 [content id]
  (if-let [tech (get-in content [:tech id])]
    (-> tech (select-keys #{:id, :name}) add-url)
    {:id id, :name (subs (str id) 1)}))

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

(defn- combine-presentations [presentations]
  (-> (first presentations)
      (select-keys #{:title :blurb :thumb})
      (assoc
          :by (map :by presentations)
          :tech (distinct (mapcat :tech presentations))
          :urls (apply merge (map :urls presentations)))))

(defn- add-presentations [content tech]
  (assoc-in-unless tech [:presentations] empty?
                   (->> (:people content)
                        vals
                        (mapcat (get-with-byline :presentations))
                        (group-by :thumb)
                        vals
                        (map combine-presentations)
                        (filter #(is-about tech %)))))

(defn- cultivate-tech [content tech]
  (->> tech
       add-url
       (add-recommendations content)
       (add-presentations content)))

(defn cultivate-techs [content]
  (update-vals (:tech content) (partial cultivate-tech content)))
