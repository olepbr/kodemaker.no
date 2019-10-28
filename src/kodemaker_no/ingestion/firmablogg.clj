(ns kodemaker-no.ingestion.firmablogg
  (:require [clojure.set :as set]
            [kodemaker-no.homeless :refer [update-in-existing]])
  (:import java.time.LocalDate))

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
       (select-keys (keys blog-post-keys))
       (update-in-existing [:published] #(LocalDate/parse %))
       (update-in-existing [:updated] #(LocalDate/parse %))
       (update-in-existing [:author] (fn [s] {:db/ident (keyword "person" s)}))
       (update-in-existing [:tech] #(for [s (read-string %)]
                                      {:db/ident (keyword "tech" (name s))}))
       (set/rename-keys blog-post-keys)
       (assoc :page/uri (str "/blogg" (second (re-find #"firmablogg(.*).md" file-name)) "/"))
       (assoc :page/kind :blog-post))])
