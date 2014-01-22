(ns kodemaker-no.cultivate.tech
  (:require [kodemaker-no.homeless :refer [update-vals]]))

(defn- add-url [tech]
  (assoc tech :url
         (str "/" (subs (str (:id tech)) 1) "/")))

(defn- cultivate-tech [content tech]
  (->> tech
       add-url))

(defn cultivate-techs [content]
  (update-in content [:tech] #(update-vals % (partial cultivate-tech content))))

(defn- look-up-tech-1 [content id]
  (if-let [tech (get-in content [:tech id])]
    (-> tech (select-keys #{:id, :name}) add-url)
    {:id id, :name (subs (str id) 1)}))

(defn look-up-tech [content techs]
  (map #(look-up-tech-1 content %) techs))
