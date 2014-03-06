(ns kodemaker-no.pages.form-page
  (:require [kodemaker-no.formatting :refer [to-html]]))

(defn- render-form [{:keys [title address subject button action]}]
  (list
   [:form.form.mod {:action (:url action)
                    :method (:method action)}
    [:label subject]
    [:textarea.input {:rows 4}]
    [:label "Omtrentlig omfang:"]
    [:input.input {:type "text"}]
    [:label "Ønsket oppstart:"]
    [:input.input {:type "text"}]
    [:label address]
    [:input.input {:type "text"}]
    [:div
     [:button.btn {:type "submit"} button]]]))

(defn form-page [data]
  {:title "Hva er det du vil?"
   :body (render-form (:form data))})
