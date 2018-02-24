(ns kodemaker-no.cultivate
  (:require [kodemaker-no.cultivate.blog-posts :refer [cultivate-blog-posts]]
            [kodemaker-no.cultivate.people :refer [cultivate-people]]
            [kodemaker-no.cultivate.screencasts :refer [cultivate-screencasts]]
            [kodemaker-no.cultivate.tech :refer [cultivate-techs]]
            [kodemaker-no.cultivate.videos :refer [cultivate-videos]]))

(defn cultivate-content [raw-content]
  (let [people (cultivate-people raw-content)]
    (assoc raw-content
      :people people
      :tech (cultivate-techs raw-content)
      :blog-posts (cultivate-blog-posts (:blog-posts raw-content) people)
      :videos (cultivate-videos raw-content)
      :screencasts (cultivate-screencasts raw-content))))
