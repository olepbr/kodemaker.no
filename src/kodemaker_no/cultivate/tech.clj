(ns kodemaker-no.cultivate.tech
  (:require [kodemaker-no.homeless :refer [update-vals assoc-in-unless]]))

(defn- add-url [tech]
  (assoc tech :url
         (str "/" (subs (str (:id tech)) 1) "/")))

(defn- combine-recommendations [recommendations]
  (assoc (first recommendations)
    :recommended-by (map :recommended-by recommendations)
    :tech (distinct (mapcat :tech recommendations))))

(defn- get-recommendations [person]
  (->> (:recommendations person)
       (map #(assoc % :recommended-by {:name (:first-name person)
                                       :url (:url person)}))))

(defn- is-about [tech recommendation]
  ((set (map :id (:tech recommendation))) (:id tech)))

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
  (update-in content [:tech] #(update-vals % (partial cultivate-tech content))))

(defn- look-up-tech-1 [content id]
  (if-let [tech (get-in content [:tech id])]
    (-> tech (select-keys #{:id, :name}) add-url)
    {:id id, :name (subs (str id) 1)}))

(defn look-up-tech [content techs]
  (map #(look-up-tech-1 content %) techs))
