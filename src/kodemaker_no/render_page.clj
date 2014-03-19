(ns kodemaker-no.render-page
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.page :refer [html5]]
            [kodemaker-no.formatting :refer [no-widows]]
            [optimus.link :as link]
            [optimus.optimizations.minify :refer [minify-js]]))

(defn- serve-to-media-query-capable-browsers [tag]
  (list "<!--[if (gt IE 8) | (IEMobile)]><!-->" tag "<!--<![endif]-->"))

(defn- serve-to-media-query-clueless-browsers [tag]
  (list "<!--[if (lte IE 8) & (!IEMobile)]>" tag "<![endif]-->"))

(defn- head-title [title]
  (if-let [title-str (or (:head title) (:h1 title) title)]
    (str title-str " | Kodemaker")
    "Kodemaker"))

(defn- h1-title [title]
  (when-let [title-str (or (:h1 title) (and (string? title) title))]
    [:h1.hn.mbn (no-widows title-str)]))

(defn- with-layout [request title content]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    (serve-to-media-query-capable-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/responsive.css")}])
    (serve-to-media-query-clueless-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/unresponsive.css")}])
    [:title (head-title title)]]
   [:body
    [:div#ow ;; outer-wrapper for off-canvas menu
     [:div#ocm ;; off-canvas menu
      [:div.bd
       [:div.ocm-item [:a {:href "/mennesker/"} "Mennesker"]]
       [:div.ocm-item [:a {:href "/kurs/"} "Kurs"]]
       [:div.ocm-item [:a {:href "/referanser/"} "Referanser"]]
       [:div.ocm-item [:a {:href "/kontakt/"} "Ta kontakt"]]]]
     [:div#iw ;; inner-wrapper for off-canvas menu
      [:div#main
       [:div#header
        [:div.bd
         [:div#ocb.mod [:span] [:span] [:span]]
         [:div.mod.menu
          [:a {:href "/mennesker/"} "Mennesker"]
          [:a {:href "/kurs/"} "Kurs"]
          [:a {:href "/referanser/"} "Referanser"]
          [:a {:href "/kontakt/"} "Ta kontakt"]]
         [:h1#logo.hn
          [:a.linkBlock {:href "/"} "Kodemaker"]]]]
       (h1-title title)
       content
       [:div#footer
        [:div.mod
         [:strong (no-widows "Kodemaker Systemutvikling AS")] " "
         [:span.nowrap "Orgnr. 982099595 "]
         [:div
          [:span.nowrap "Dronning Eufemias gate 16,"] " "
          [:span.nowrap "Visma-bygget,"] " "
          [:span.nowrap "0191 Oslo"]]
         [:div
          [:span.nowrap "Telefon: +47 22 82 20 80."] " "
          [:span.nowrap "Telefaks: +47 22 82 20 88"] " "
          [:span.nowrap "E-post: <a href='mailto:kontakt@kodemaker.no'>kontakt@kodemaker.no</a>"]]]]
       ]]]
    [:script
     (minify-js (slurp (io/resource "public/scripts/off-canvas-menu.js")))]]))

(defn- render-single-column [page]
  [:div.body
   [:div.bd
    (:lead page)
    (:body page)]])

(defn- render-two-column [page]
  (list
   [(if (-> page :title :h1)
      :div.body.unitRight.r-2of3
      :div.body.unitRight.r-2of3.mtm)
    [:div.bd
     (:lead page)
     (:body page)]]
   [(if (-> page :title :h1)
      :div.aside.lastUnit
      :div.aside.lastUnit.mtm)
    [:div.bd
     (when (:illustration page)
       [:div.illustration
        [:img {:src (:illustration page)}]])
     (:aside page)]]))

(defn- two-column-page? [page]
  (or (:illustration page)
      (:aside page)))

(defn render-page [page request]
  (with-layout request (:title page)
    (if (two-column-page? page)
      (render-two-column page)
      (render-single-column page))))
