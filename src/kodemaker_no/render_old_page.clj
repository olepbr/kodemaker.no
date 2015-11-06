(ns kodemaker-no.render-old-page
  (:require [kodemaker-no.layout :refer [with-layout]]))

(defn- render-single-column [page]
  [:div.body
   [:div.bd.iw
    (:lead page)
    (:body page)]])

(defn- render-two-column [page]
  [:div.iw
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
        (if (:site page)
          [:a {:href (:site page)}
            [:img {:src (:illustration page)}]]
          [:img {:src (:illustration page)}])])
     (:aside page)]]])

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
