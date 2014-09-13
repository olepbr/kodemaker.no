(ns kodemaker-no.pages.video-pages
  (:require [kodemaker-no.formatting :refer [comma-separated link-to-person]]
            [kodemaker-no.markup :as markup]))

(defn render-tech-bubble [tech by]
  (when-not (empty? tech)
    [:p.near.cookie-w
     [:span.cookie
      (comma-separated (map link-to-person by)) " om "
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
