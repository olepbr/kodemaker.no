(ns kodemaker-no.ingestion.blog
  (:require [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [kodemaker-no.homeless :as h]))

(def blog-post-keys
  {:blog-post/title :title
   :blog-post/published :published
   :blog-post/updated :updated
   :blog-post/illustration :illustration
   :blog-post/author :author
   :blog-post/blurb :blurb
   :blog-post/techs :tech
   :blog-post/tech-list :tech
   :blog-post/body :body
   :blog-post/discussion-links :discussion})

(def host-text
  {"twitter.com" "Twitter"
   "www.linkedin.com" "LinkedIn"
   "www.kode24.no" "Kode24"
   "www.facebook.com" "Facebook"
   "stackoverflow.com" "Stack Overflow"
   "github.com" "GitHub"})

(def host-icon
  {"twitter.com" :twitter
   "www.linkedin.com" :linkedin
   "stackoverflow.com" :stack-overflow
   "github.com" :github})

(defn- parse-discussion-links [s]
  (->> (str/split s #"\n")
       (map-indexed #(let [[url text icon] (str/split %2 #" ")
                           [_ host] (re-find #"https?://([^/]+)" url)]
                       {:list/idx %1
                        :url url
                        :text (or text (host-text host) host)
                        :icon (or icon (host-icon host) :external-url)}))
       (into [])))

(defn blog-post-tx [file-name blog-post]
  (-> blog-post
      (h/update-in-existing [:published] h/parse-local-date)
      (h/update-in-existing [:updated] h/parse-local-date)
      (h/update-in-existing [:author] (fn [s] {:db/ident (keyword "person" s)}))
      (h/update-in-existing [:tech] read-string)
      (h/update-in-existing [:discussion] parse-discussion-links)
      (h/keep-vals blog-post-keys)
      (h/update-in-existing [:blog-post/techs] h/prep-techs)
      (h/update-in-existing [:blog-post/tech-list] h/prep-tech-list)
      (assoc :page/uri (str "/blogg" (second (re-find #"(?:firmablogg|blog)(.*).md" file-name)) "/"))
      (assoc :page/kind :page.kind/blog-post)))

(defn create-tx [file-name blog-post]
  [(blog-post-tx file-name blog-post)])

(defn create-legacy-tx [file-name blog-post]
  [(assoc (blog-post-tx file-name blog-post) :blog-post/archived? true)])

(defn shuffle-author-pictures [author]
  (let [pictures (-> (or (seq (:person/portraits author)) [(:person/cv-picture author)])
                     shuffle
                     cycle)]
    (->> (:blog-post/_author author)
         (filter :blog-post/published)
         (sort-by :blog-post/published)
         (map-indexed (fn [idx post]
                        [(:db/id post) (nth pictures idx)]))
         vec)))

(defn blog-post-author-images [db]
  (->> (d/q '[:find [?e ...]
              :in $
              :where
              [?p :blog-post/published]
              [?p :blog-post/author ?e]]
            db)
       (map #(d/entity db %))
       (mapcat shuffle-author-pictures)))

(defn blogged-techs [db]
  (->> (d/q '[:find ?tech
              :in $
              :where
              [_ :blog-post/techs ?t]
              [?t :db/ident ?tech]]
            db)
       (map first)))

(comment
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  (blog-post-author-images db)

  (defn ingest-blog-post [file-name & [f]]
    (->> file-name
         clojure.java.io/resource
         slurp
         mapdown.core/parse
         ((or f create-tx) file-name)))

  (ingest-blog-post "firmablogg/2019-06-datascript.md")
  (ingest-blog-post "blog/hostens-andre-nykommer-christian-johansen.md" create-legacy-tx)

  )
