(ns kodemaker-no.render-new-page
  (:require [dumdom.string :as dumdom]
            [optimus.link :as link]
            [ui.layout :as layout]
            [ui.sections.seymour-section :as seymour-section]
            [clojure.java.io :as io]))

(defn render-section [section]
  (case (:kind section)
    :seymour (seymour-section/render section)
    :footer (layout/footer)))

(defn- head-title [title]
  (if-let [title-str (or (:head title) (and (string? title) title))]
    (str title-str " | Kodemaker")
    "Kodemaker"))

(defn render-page [page request]
  (str "<!DOCTYPE html>"
   (dumdom/render
    [:html
     [:head
      [:link {:rel "stylesheet" :href (link/file-path request "/css/kodemaker.css")}]
      [:link {:href (link/file-path request "/favicon.ico") :rel "icon" :type "image/x-icon"}]
      [:link {:href (link/file-path request "/favicon.ico") :rel "shortcut icon" :type "image/ico"}]
      [:link {:href (link/file-path request "/favicon.ico") :rel "shortcut icon" :type "image/x-icon"}]
      [:link {:href (link/file-path request "/favicon.ico") :rel "shortcut icon" :type "image/vnd.microsoft.icon"}]
      [:title (head-title (:title page))]]
     [:body
      [:script (slurp (io/resource "public/scripts/analytics.js"))]
      (map render-section (:sections page))]])))

(comment
  (render-page
   {:sections [{:kind :seymour
                :color :white
                :seymours
                [{:icon {:type :science/chemical :height 79}
                  :title "Referanser"
                  :text "Det er fleske meg ikke dårlig hvor mange artige prosjekter vi har fått være med på."
                  :link {:text "Se referanser"
                         :href "/referanser/"}}
                 {:icon {:type :science/robot-1 :height 79}
                  :title "Sjekk ut hvem vi har på laget 'æ, guttær!"
                  :text "Vi har kun erfarne konsulenter med oss som liker å bryne seg på vanskelig oppgaver."
                  :link {:text "Våre ansatte"
                         :href "/folk/"}}
                 {:icon {:type :computer/laptop-1 :height 79}
                  :title "Kurs og workshops"
                  :text "Her er en ganske kort tekst."
                  :link {:text "Vi kan tilby"
                         :href "/kurs/"}}]}
               {:kind :footer}]}
   {:optimus-assets kodemaker-no.web/optimized-assets})


  )
