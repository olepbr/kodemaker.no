(ns kodemaker-no.pages.video-pages
  (:require [kodemaker-no.formatting :refer [comma-separated]]
            [kodemaker-no.markup :as markup]))

(defn- link-to-person [person]
  [:a {:href (:url person)} (:name person)])

(defn render-tech-bubble [tech person]
  (when-not (empty? tech)
    [:p.near.cookie-w
     [:span.cookie
      (link-to-person person) " om "
      (comma-separated (map markup/link-if-url tech))]]))

(defn- create-video-page [video]
  (fn []
    {:title (:title video)
     :body
     (list
      (render-tech-bubble (:tech video) (:by video))
      [:div.mod (:embed-code video)]
      [:p (:blurb video)])}))

(defn video-pages [videos]
  (into {} (map (juxt :url create-video-page) videos)))
