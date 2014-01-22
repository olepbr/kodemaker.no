(ns kodemaker-no.cultivate.tech
  (:require [kodemaker-no.homeless :refer [update-vals assoc-in-unless]]
            [kodemaker-no.cultivate.util :as util]))

(defn- add-url [tech]
  (assoc tech :url (util/url tech)))

(defn- look-up-tech-1 [content id]
  (if-let [tech (get-in content [:tech id])]
    (-> tech (select-keys #{:id, :name}) add-url)
    {:id id, :name (subs (str id) 1)}))

(defn look-up-tech [content techs]
  (map #(look-up-tech-1 content %) techs))

(defn- combine-recommendations [recommendations]
  (-> (first recommendations)
      (select-keys #{:title :blurb :url})
      (assoc
          :recommended-by (map :recommended-by recommendations)
          :tech (distinct (mapcat :tech recommendations)))))

(defn- get-recommendations [person]
  (->> (:recommendations person)
       (map #(assoc % :recommended-by {:name (first (:name person))
                                       :url (util/url person)}))))

(defn- is-about [tech recommendation]
  ((set (:tech recommendation)) (:id tech)))

(defn- add-recommendations [content tech]
  (assoc-in-unless tech [:recommendations] empty?
                   (->> (:people content)
                        vals
                        (mapcat get-recommendations)
                        (group-by :url)
                        vals
                        (map combine-recommendations)
                        (filter #(is-about tech %)))))

(defn- cultivate-tech [content tech]
  (->> tech
       add-url
       (add-recommendations content)))

(defn cultivate-techs [content]
  (update-vals (:tech content) (partial cultivate-tech content)))
