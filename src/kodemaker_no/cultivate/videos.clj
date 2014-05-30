(ns kodemaker-no.cultivate.videos
  (:require [kodemaker-no.cultivate.tech :as tech]
            [kodemaker-no.cultivate.util :as util]
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

(defn- cultivate-video [raw-content {:keys [blurb title urls by id tech]}]
  (let [id (or id (keyword (to-id-str title)))
        override (-> raw-content :video-overrides (get id))]
    (merge
     {:title title
      :by by
      :blurb blurb
      :tech (map (partial tech/look-up-tech-1 raw-content) tech)
      :url (str "/" (name id) "/")
      :embed-code (create-embed-code (:video urls))}
     override)))

(defn- get-with-byline [key]
  (fn [person]
    (->> (key person)
         (map #(assoc % :by {:name (first (:name person))
                             :url (util/url person)})))))

(defn cultivate-videos [raw-content]
  (->> raw-content :people vals
       (mapcat (get-with-byline :presentations))
       (filter (comp find-video :video :urls))
       (remove :direct-link?)
       (map (partial cultivate-video raw-content))))
