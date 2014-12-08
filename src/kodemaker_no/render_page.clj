(ns kodemaker-no.render-page
  (:require [clojure.string :as str]
            [kodemaker-no.formatting :refer [to-html]]
            [kodemaker-no.render-old-page :as old]))

(defn- render-illustrated-column [section]
  (list (when (:title section)
          [:h2.offset [:span.offset-content (:title section)]])
        [:div.line
         [:div.unit.text-adornment
          [:p (when (:illustration section)
                [:img {:src (:illustration section)}])]]
         [:div.lastUnit
          (to-html (:body section))]]))

(defn- render-reference [{:keys [img url logo name phone title body]}]
  (let [quote (str "«" (str/trim body) "»")]
    [:div.ref.mod
     [:div.ref-w
      [:div.ref-img [:img {:src img}]]
      [:div.ref-txt [:p (to-html quote)]]
      [:div.ref-card
       [:div.ref-logo [:img {:src logo}]]
       [:div.ref-info.tight
        [:h4 name]
        [:p title [:br] phone]]]]
     [:div.ref-txt-2 [:p (to-html quote)]]]))

(defn render-section [section]
  [:div.body
   [:div.bd
    (case (:type section)
      "illustrated-column" (render-illustrated-column section)
      "mega-heading" [:h1.hn.pth (:title section)]
      "reference" (render-reference section)
      nil (:body section)

      (throw (ex-info (str "Unknown section type " (:type section)) section)))]])

(defn render-page [page request]
  (if (:sections page)
    (old/with-layout request page
      (map render-section (:sections page)))
    (old/render-page page request)))
