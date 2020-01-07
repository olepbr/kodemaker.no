(ns kodemaker-no.ingestion.blog
  (:require [kodemaker-no.homeless :as h]
            [clojure.string :as str]))

(def blog-post-keys
  {:title :blog-post/title
   :published :blog-post/published
   :updated :blog-post/updated
   :illustration :blog-post/illustration
   :author :blog-post/author
   :blurb :blog-post/blurb
   :tech :blog-post/tech
   :body :blog-post/body
   :discussion :blog-post/discussion-links})

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
      (h/update-in-existing [:tech] #(for [s (read-string %)]
                                     {:db/ident (keyword "tech" (name s))}))
      (h/update-in-existing [:discussion] parse-discussion-links)
      (h/select-renamed-keys blog-post-keys)
      (assoc :page/uri (str "/blogg" (second (re-find #"(?:firmablogg|blog)(.*).md" file-name)) "/"))
      (assoc :page/kind :page.kind/blog-post)))

(defn create-tx [file-name blog-post]
  [(blog-post-tx file-name blog-post)])

(defn create-legacy-tx [file-name blog-post]
  [(assoc (blog-post-tx file-name blog-post) :blog-post/archived? true)])

(comment
  (defn ingest-blog-post [file-name & [f]]
    (->> file-name
         clojure.java.io/resource
         slurp
         mapdown.core/parse
         ((or f create-tx) file-name)))

  (ingest-blog-post "firmablogg/2019-06-datascript.md")
  (ingest-blog-post "blog/hostens-andre-nykommer-christian-johansen.md" create-legacy-tx)

  )
