(ns kodemaker-no.layout
  (:require [clojure.java.io :as io]
            [hiccup.page :refer [html5]]
            [kodemaker-no.formatting :refer [no-widows]]
            [optimus.link :as link]
            [optimus.optimizations.minify :refer [minify-js]]))

(defn- serve-to-media-query-capable-browsers [tag]
  (list "<!--[if (gt IE 8) | (IEMobile)]><!-->" tag "<!--<![endif]-->"))

(defn- serve-to-media-query-clueless-browsers [tag]
  (list "<!--[if (lte IE 8) & (!IEMobile)]>" tag "<![endif]-->"))

(defn- head-title [title]
  (if-let [title-str (or (:head title) (and (string? title) title))]
    (str title-str " | Kodemaker")
    "Kodemaker"))

(defn- h1-title [title]
  (when-let [title-str (or (:h1 title) (and (string? title) title))]
    [:div.iw
     [:h1.hn.mbn
      (when (:arrow title)
        [:a.arrow {:href (:arrow title)} [:span.arrow-body] [:span.arrow-head]])
      (no-widows title-str)]]))

(defn- brick
  ([] [:div.brick "&nbsp;"])
  ([{:keys [url text]}] [:a.brick {:href url} text]))

(defn- brick-collapsing
  [] [:div.brick.collapsing-brick "&nbsp;"])

(defn- meta-tag [attrs]
  [:meta attrs])

(defn with-layout [request page content]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    (when (:meta page)
      (map meta-tag (:meta page)))
    (serve-to-media-query-capable-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/responsive.css")}])
    (serve-to-media-query-clueless-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/unresponsive.css")}])
    [:title (head-title (:title page))]]
   [:body
    [:script (slurp (io/resource "public/scripts/google-tag-manager.js"))]
    [:div#ow ;; outer-wrapper for off-canvas menu
     [:div#ocm ;; off-canvas menu
      [:div.bd
       [:div.ocm-item [:a {:href "/folk/"} "Folkene i Kodemaker"]]
       [:div.ocm-item [:a {:href "/kurs/"} "Foredrag og slikt"]]
       [:div.ocm-item [:a {:href "/kontakt/"} "Ta kontakt"]]]]
     [:div#iw ;; inner-wrapper for off-canvas menu
      [:div#main
       [:div#header
        [:div.bd.iw
         [:div#ocb.mod [:span] [:span] [:span]]
         [:div.mod.menu
          [:a {:href "/kurs/"} "Lær"]
          [:a {:href "/folk/"} "Folk"]
          [:a {:href "/kontakt/"} "Ta kontakt"]]
         [:h1#logo.hn
          [:a.linkBlock {:href "/"} "Kodemaker"]]]]
       (h1-title (:title page))
       [:div.bd
        content]
       [:div.footer-spacing]
       (when-let [bricks (:bricks page)]
         [:div.wall
          [:div.bricks
           (brick (nth bricks 0))
           (brick)
           (brick (nth bricks 1))
           (brick (nth bricks 2))
           (brick-collapsing)]
          [:div.bricks
           (brick (nth bricks 3))
           (brick)
           (brick-collapsing)
           (brick (nth bricks 4))
           (brick)
           (brick (nth bricks 5))]
          [:div.bricks
           (brick)
           (brick (nth bricks 6))
           (brick (nth bricks 7))
           (brick-collapsing)
           (brick)]])
       [:div#footer-dec.bd.iw
        [:div.mod.mvn.rel
         [:div.ft-dec-0] ;; footer decorations
         [:div.ft-dec-1]
         [:div.ft-dec-2]
         [:div.ft-dec-3]
         [:div.ft-dec-4]]]
       [:div#footer
        [:div.bd.iw
         [:div.mod
          [:strong.mrl (no-widows "Kodemaker Systemutvikling AS")] " "
          [:span.nowrap "Orgnr. 982099595 "]
          [:div
           [:span.nowrap "Munkedamsveien 3b,"] " "
           [:span.nowrap "Kommunenes hus,"] " "
           [:span.nowrap "0161 OSLO"]]
          [:div
           [:span.nowrap.mrl "+47 22 82 20 80"] " "
           [:span.nowrap "<a href='mailto:kontakt@kodemaker.no'>kontakt@kodemaker.no</a>"]
           [:div [:span.nowrap "<a href='/personvern/'>Personvern</a>"]]]]
         [:div.ft-dec-5]]]]]]
    [:script
     (minify-js (slurp (io/resource "public/scripts/off-canvas-menu.js")))]]))
