(ns kodemaker-no.pages.video-pages
  (:require [kodemaker-no.formatting :refer [comma-separated]]
            [kodemaker-no.markup :as markup]))

(defn- link-to-person [person]
  (if (map? person)
    [:a {:href (:url person)} (:name person)]
    (comma-separated (map link-to-person person))))

(defn render-tech-bubble [tech person]
  (when-not (empty? tech)
    [:p.near.cookie-w
     [:span.cookie
      (link-to-person person) " om "
      (comma-separated (map markup/link-if-url tech))]]))

(defn render-call-to-action [cta]
  (when cta
    (list
     [:div#cta.call-to-action.hidden
      [:div.bd [:p (:content cta)]]]
     [:script {:data-no-instant ""}
      "setTimeout(function () {
            document.getElementById('cta').className = 'call-to-action visible';
          }, " (* 1000 (:seconds-to-delay cta)) ");"])))

(defn- create-video-page [video]
  (fn []
    {:title (:title video)
     :body
     (list
      (render-tech-bubble (:tech video) (:by video))
      [:div.mod (:embed-code video)]
      (render-call-to-action (:call-to-action video))
      [:p (:blurb video)])}))

(defn video-pages [videos]
  (->> videos
       (remove :direct-link?)
       (map (juxt :url create-video-page))
       (into {})))
