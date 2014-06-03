(ns kodemaker-no.pages.course-page
  (:require [clojure.java.io :as io]
            [kodemaker-no.formatting :refer [to-html comma-separated]]
            [kodemaker-no.markup :as markup]))

(defn- link-to-person [person]
  [:a {:href (:url person)} (:name person)])

(defn render-tech-bubble [tech person]
  (when-not (empty? tech)
    [:p.near.cookie-w
     [:span.cookie
      (link-to-person person) " om "
      (comma-separated (map markup/link-if-url tech))]]))

(defn- render-video [video]
  (list
   [:h3 [:a {:href (:url video)} (:title video)]]
   (render-tech-bubble (:tech video) (:by video))
   [:p (:blurb video)]))

(defn course-page [videos]
  {:title "Lærelysten? Vi deler gjerne!"
   :lead [:p "Bli med på foredrag, kurs, eller workshop, eller len deg tilbake og se en screencast."]
   :body (list
          [:h2 "Screencasts"]
          (to-html (slurp (io/resource "screencasts.md")))
          [:h2 "Foredrag"]
          (map render-video videos))})
