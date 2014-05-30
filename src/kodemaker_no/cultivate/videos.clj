(ns kodemaker-no.cultivate.videos
  (:require [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.formatting :refer [to-id-str]]))

(defn find-video [^String url]
  (when url
    (cond
     (.startsWith url "http://www.youtube.com/watch?v=") {:type :youtube, :id (subs url 31)}
     (.startsWith url "http://vimeo.com/album/") {:type :vimeo, :id (second (re-find #"http://vimeo.com/album/\d+/video/(\d+)" url))}
     (.startsWith url "http://vimeo.com/") {:type :vimeo, :id (subs url 17)}
     (.startsWith url "https://vimeo.com/") {:type :vimeo, :id (subs url 18)}
     :else nil)))

(defn- create-embed-code [url]
  (let [{:keys [type id]} (find-video url)]
    (case type
      :youtube [:div.video-embed
                [:iframe {:src (str "http://www.youtube.com/embed/" id)
                          :frameborder "0"
                          :allowfullscreen true}]]
      :vimeo [:div.video-embed
              [:iframe {:src (str "//player.vimeo.com/video/" id "?title=0&amp;byline=0&amp;portrait=0")
                        :frameborder "0"
                        :allowfullscreen true}]])))

(defn- cultivate-video [{:keys [blurb title urls by id]}]
  {:title title
   :by by
   :blurb blurb
   :url (str "/" (if id (name id) (to-id-str title)) "/")
   :embed-code (create-embed-code (:video urls))})

(defn- get-with-byline [key]
  (fn [person]
    (->> (key person)
         (map #(assoc % :by {:name (first (:name person))
                             :url (util/url person)})))))

(defn cultivate-videos [raw-content]
  (->> raw-content :people vals
       (mapcat (get-with-byline :presentations))
       (filter (comp find-video :video :urls))
       (map cultivate-video)))
