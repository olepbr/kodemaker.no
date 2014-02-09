(ns kodemaker-no.pages.index-page
  (:require [kodemaker-no.formatting :refer [to-html]]))

(defn- render-form [{:keys [title address subject button action]}]
  (list
   [:h2 title]
   [:form.form.mod {:action action}
    [:label address]
    [:input.input {:type "text"}]
    [:label subject]
    [:textarea.input {:rows 4}]
    [:div
     [:button.btn {:type "submit"} button]]]))

(defn- render-intro [{:keys [title text]}]
  (list
   [:h2 title]
   (to-html text)))

(defn index-page [data]
  {:body (list
          [:div.line
           [:div.unitRight.r-2of3
            [:div.bd.rel
             [:a.linkBlock {:href "/andre/"}
              [:span.inverse.fpt.linkish "André Bonkowski"]
              [:img.fpf {:src "/photos/people/andre/side-profile-cropped.jpg"}]]]]
           [:div.lastUnit
            (render-form (:form data))]]
          (render-intro (:intro data)))})
