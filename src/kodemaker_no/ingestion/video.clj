(ns kodemaker-no.ingestion.video
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h]))

(defn find-video [^String url]
  (when url
    (cond
      (.startsWith url "http://www.youtube.com/watch?v=") {:type :youtube, :id (subs url 31)}
      (.startsWith url "http://vimeo.com/album/") {:type :vimeo, :id (second (re-find #"http://vimeo.com/album/\d+/video/(\d+)" url))}
      (re-find #"https?://vimeo.com/\d+" url) {:type :vimeo, :id (second (re-find #"https?://vimeo.com/(\d+)" url))}
      (re-find #"https?://vimeo.com/user\d+/review/\d+/\S+" url) {:type :vimeo, :id (second (re-find #"https?://vimeo.com/user\d+/review/(\d+)/\S+" url))}
      :else nil)))

(defn- create-video-page-for-presentation? [presentation]
  (and (not (:presentation/direct-link? presentation))
       (find-video (:presentation/video-url presentation))))

(defn- video-id [presentation]
  (some-> presentation
          :presentation/title
          f/to-id-str
          keyword))

(defn- video-url [presentation]
  (if (create-video-page-for-presentation? presentation)
    (str "/" (name (video-id presentation)) "/")
    (:presentation/video-url presentation)))

(def video-keys
  {:video/title :presentation/title
   :video/blurb :presentation/description
   :video/date :presentation/date
   :video/url :presentation/video-url
   :video/techs :presentation/techs
   :video/tech-list :presentation/tech-list})

(defn video-data [person-ident presentation]
  (when-let [url (video-url presentation)]
    (cond-> (h/keep-vals presentation video-keys)
      (create-video-page-for-presentation? presentation)
      (merge {:page/uri url
              :page/kind :page.kind/video})

      :always (merge {:db/ident (video-id presentation)
                      :video/by [{:db/ident person-ident}]}))))

(comment
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  (d/touch (d/entity db :kosetime-live-parprogrammering-og-zombier))

  )
