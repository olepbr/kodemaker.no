(ns kodemaker-no.cultivate
  (:require [kodemaker-no.cultivate.blog-posts :refer [cultivate-blog-posts]]
            [kodemaker-no.cultivate.cvs :refer [cultivate-cvs]]
            [kodemaker-no.cultivate.people :refer [cultivate-people]]
            [kodemaker-no.cultivate.screencasts :refer [cultivate-screencasts]]
            [kodemaker-no.cultivate.sellable :refer [cultivate-sellable]]
            [kodemaker-no.cultivate.tech :refer [cultivate-techs]]
            [kodemaker-no.cultivate.videos :refer [cultivate-videos]]))

(defn cultivate-content [raw-content]
  (let [people (cultivate-people raw-content)
        blog-posts (cultivate-blog-posts raw-content (:blog-posts raw-content) people)]
    (assoc raw-content
      :people people
      :cvs (cultivate-cvs raw-content people)
      :tech (cultivate-techs raw-content {:blog-posts blog-posts})
      :legacy-blog-posts (cultivate-blog-posts raw-content (:legacy-blog-posts raw-content) people)
      :blog-posts blog-posts
      :videos (cultivate-videos raw-content)
      :screencasts (cultivate-screencasts raw-content)
      :business-presentations (cultivate-sellable :business-presentations raw-content)
      :workshops (cultivate-sellable :workshops raw-content))))
