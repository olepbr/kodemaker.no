(ns kodemaker-no.render-new-page
  (:require [clojure.java.io :as io]
            [dumdom.string :as dumdom]
            [optimus.link :as link]
            [ui.layout :as layout]
            [ui.sections.bruce-section :as bruce-section]
            [ui.sections.seymour-section :as seymour-section]))

(defn render-section [section]
  (case (:kind section)
    :bruce (bruce-section/render section)
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
