(ns kodemaker-no.layout
  (:require [optimus.link :as link]
            [hiccup.page :refer [html5]]
            [clojure.string :as str]
            [kodemaker-no.formatting :refer [no-widows]]))

(defn- serve-to-media-query-capable-browsers [tag]
  (list "<!--[if (gt IE 8) | (IEMobile)]><!-->" tag "<!--<![endif]-->"))

(defn- serve-to-media-query-clueless-browsers [tag]
  (list "<!--[if (lte IE 8) & (!IEMobile)]>" tag "<![endif]-->"))

(defn with-layout [request title content]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    (serve-to-media-query-capable-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/responsive.css")}])
    (serve-to-media-query-clueless-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/unresponsive.css")}])
    [:title (str title " | Kodemaker")]]
   [:body
    [:div#main
     [:div#header
      [:div.bd
       [:div.mod.menu
        [:a {:href "#"} "Referanser"]
        [:a {:href "/mennesker.html"} "Mennesker"]
        [:a {:href "#"} "Ta kontakt"]]
       [:h1#logo.hn
        [:a.linkBlock {:href "/"} "Kodemaker"]]]]
     [:h1.hn.mbn (no-widows title)]
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
        [:span.nowrap "E-post: <a href='mailto:kontakt@kodemaker.no'>kontakt@kodemaker.no</a>"]]]]]]))

(defn- render-single-column [page]
  [:div.body
   [:div.bd
    (:lead page)
    (:article page)]])

(defn- render-two-column [page]
  (list
   [:div.body.unitRight.r-2of3
    [:div.bd
     (:lead page)
     (:body page)]]
   [:div.aside.lastUnit
    [:div.bd
     (when (:illustration page)
       [:div.illustration
        [:img {:src (:illustration page)}]])
     (:aside page)]]))

(defn- two-column-page? [page]
  (or (:illustration page)
      (:aside page)))

(defn render-page [page request]
  {:body
   (with-layout request (:title page)
     (if (two-column-page? page)
       (render-two-column page)
       (render-single-column page)))})
