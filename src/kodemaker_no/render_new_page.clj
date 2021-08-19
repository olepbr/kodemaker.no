(ns kodemaker-no.render-new-page
  (:require [clojure.java.io :as io]
            [dumdom.string :as dumdom]
            [kodemaker-no.new-pages.whoami-page :as whoami-page]
            [optimus.link :as link]
            [ui.layout :as layout]
            [ui.sections :as sections]))

(defn render-section [section]
  (try
    ((case (:kind section)
       :article sections/article-section
       :banner sections/banner-section
       :bruce sections/bruce-section
       :contact sections/contact-section
       :content :content
       :container sections/container-section
       :cv-intro sections/cv-intro-section
       :definitions sections/definition-section
       :enumeration sections/enumeration-section
       :footer layout/footer
       :grid sections/grid-section
       :grid-header sections/grid-header-section
       :header layout/header-section
       :hip sections/hip-section
       :hyrule sections/hyrule-section
       :profile sections/profile-section
       :pønt sections/pønt-section
       :seymour sections/seymour-section
       :references-intro sections/references-intro-section
       :tech-intro sections/tech-intro-section
       :titled sections/titled-section
       :vertigo sections/vertigo-section
       :whoami whoami-page/render-section
       :widescreen sections/widescreen-section) section)
    (catch Exception e
      (throw (ex-info (format "Unable to render section: %s" (.getMessage e)) {:section section})))))

(defn- head-title [title]
  (if-let [title-str (or (:head title) (and (string? title) title))]
    (str title-str " | Kodemaker")
    "Kodemaker"))

(defn render-page [page request]
  (str "<!DOCTYPE html>"
   (dumdom/render
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
      (map (fn [url] [:link {:rel "stylesheet" :href url}])
           (link/bundle-paths request ["styles.css"]))
      [:link {:href (link/file-path request "/favicon.ico") :rel "icon" :type "image/x-icon"}]
      [:link {:href (link/file-path request "/favicon.ico") :rel "shortcut icon" :type "image/ico"}]
      [:link {:href (link/file-path request "/favicon.ico") :rel "shortcut icon" :type "image/x-icon"}]
      [:link {:href (link/file-path request "/favicon.ico") :rel "shortcut icon" :type "image/vnd.microsoft.icon"}]
      [:link {:href "/atom.xml" :rel "alternate" :title "Kodemakerblogg" :type "application/atom+xml"}]
      [:title (head-title (:title page))]]
     [:body
      [:script (slurp (io/resource "public/scripts/analytics.js"))]
      (map render-section (:sections page))]])))
