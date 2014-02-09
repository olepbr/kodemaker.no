(ns kodemaker-no.pages.index-page
  (:require [kodemaker-no.formatting :refer [to-html]]))

(defn- render-form [{:keys [title address subject button action]}]
  (list
   [:h2 title]
   [:form.form.mod {:action (:url action)
                    :method (:method action)}
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

(defn- render-face [{:keys [url name photo]}]
  [:a.linkBlock {:href url}
   [:span.inverse.fpt.linkish name]
   [:img.fpf {:src photo}]])

(defn index-page [data]
  {:body (list
          [:div.line
           [:div.unitRight.r-2of3
            [:div.bd.rel
             (render-face (first (shuffle (:faces data))))]]
           [:div.lastUnit
            (render-form (:form data))]]
          (render-intro (:intro data)))})
