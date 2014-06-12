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
  (if-let [title-str (or (:head title) (and (string? title) title))]
    (str title-str " | Kodemaker")
    "Kodemaker"))

(defn- h1-title [title]
  (when-let [title-str (or (:h1 title) (and (string? title) title))]
    [:h1.hn.mbn
     (when (:arrow title)
       [:a.arrow {:href (:arrow title)} [:span.arrow-body] [:span.arrow-head]])
     (no-widows title-str)]))

(defn- brick
  ([] [:div.brick "&nbsp;"])
  ([{:keys [url text]}] [:a.brick {:href url} text]))

(defn- brick-collapsing
  [] [:div.brick.collapsing-brick "&nbsp;"])

(defn- with-layout [request page content]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    (serve-to-media-query-capable-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/responsive.css")}])
    (serve-to-media-query-clueless-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/unresponsive.css")}])
    [:title (head-title (:title page))]]
   [:body
    [:script {:data-no-instant ""} (slurp (io/resource "public/scripts/ga.js"))]
    [:div#ow ;; outer-wrapper for off-canvas menu
     [:div#ocm ;; off-canvas menu
      [:div.bd
       [:div.ocm-item [:a {:href "/kurs/"} "Lær"]]
       [:div.ocm-item [:a {:href "/skjema/"} "Ta kontakt"]]]]
     [:div#iw ;; inner-wrapper for off-canvas menu
      [:div#main
       [:div#header
        [:div.bd
         [:div#ocb.mod [:span] [:span] [:span]]
         [:div.mod.menu
          [:a {:href "/kurs/"} "Lær"]
          [:a {:href "/skjema/"} "Ta kontakt"]]
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
       [:div#footer
        [:div.bd
         [:div.mod
          [:strong.mrl (no-widows "Kodemaker Systemutvikling AS")] " "
          [:span.nowrap "Orgnr. 982099595 "]
          [:div
           [:span.nowrap "Dronning Eufemias gate 16,"] " "
           [:span.nowrap "Visma-bygget,"] " "
           [:span.nowrap "0191 Oslo"]]
          [:div
           [:span.nowrap.mrl "+47 22 82 20 80"] " "
           [:span.nowrap "<a href='mailto:kontakt@kodemaker.no'>kontakt@kodemaker.no</a>"]]]]]]]]
    [:script {:data-no-instant "",
              :src (link/file-path request "/scripts/instantclick.js")}]
    [:script {:data-no-instant ""} "InstantClick.init(50);"]]))

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

(defn render-body [page]
  (if (two-column-page? page)
    (render-two-column page)
    (render-single-column page)))

(defn render-page [page request]
  (with-layout request page
    (render-body page)))
