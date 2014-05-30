(ns kodemaker-no.pages.video-pages)

(defn- create-video-page [video]
  (fn []
    {:title (:title video)
     :body
     (list
      [:div.mod (:embed-code video)]
      [:p (:blurb video)])}))

(defn video-pages [videos]
  (into {} (map (juxt :url create-video-page) videos)))
