(ns kodemaker-no.ingestion.firmablogg
  (:require [kodemaker-no.homeless :refer [update-in-existing parse-local-date select-renamed-keys]]))

(def blog-post-keys
  {:title :blog-post/title
   :published :blog-post/published
   :updated :blog-post/updated
   :illustration :blog-post/illustration
   :author :blog-post/author
   :blurb :blog-post/blurb
   :tech :blog-post/tech
   :body :blog-post/body})

(defn create-tx [file-name blog-post]
  [(-> blog-post
       (update-in-existing [:published] parse-local-date)
       (update-in-existing [:updated] parse-local-date)
       (update-in-existing [:author] (fn [s] {:db/ident (keyword "person" s)}))
       (update-in-existing [:tech] #(for [s (read-string %)]
                                      {:db/ident (keyword "tech" (name s))}))
       (select-renamed-keys (keys blog-post-keys))
       (assoc :page/uri (str "/blogg" (second (re-find #"firmablogg(.*).md" file-name)) "/"))
       (assoc :page/kind :blog-post))])
