(ns kodemaker-no.pages.video-pages
  (:require [kodemaker-no.formatting :as f]
            [kodemaker-no.markup :as markup]))

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
      (f/render-tech-bubble (:tech video) (:by video))
      [:div.mod (:embed-code video)]
      (render-call-to-action (:call-to-action video))
      [:p (:blurb video)])}))

(defn video-pages [videos]
  (->> videos
       (remove :direct-link?)
       (map (juxt :url create-video-page))
       (into {})))
