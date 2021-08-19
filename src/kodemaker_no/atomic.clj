(ns kodemaker-no.atomic
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [datomic-type-extensions.api :as d]
            [java-time-dte.install :refer [define-dte]]
            [java-time-literals.core :as jte]
            [kodemaker-no.images :as images]
            [kodemaker-no.new-pages.article-page :as article-page]
            [kodemaker-no.new-pages.blog :as blog]
            [kodemaker-no.new-pages.contact-page :as contact-page]
            [kodemaker-no.new-pages.cv-page :as cv-page]
            [kodemaker-no.new-pages.frontpage :as frontpage]
            [kodemaker-no.new-pages.people-page :as people-page]
            [kodemaker-no.new-pages.profile-page :as profile-page]
            [kodemaker-no.new-pages.reference-page :as reference-page]
            [kodemaker-no.new-pages.references-page :as references-page]
            [kodemaker-no.new-pages.tech-page :as tech-page]
            [kodemaker-no.new-pages.video-page :as video-page]
            [kodemaker-no.new-pages.whoami-page :as whoami-page]
            [kodemaker-no.prepare-pages :refer [post-process-page]]
            [kodemaker-no.render-new-page :refer [render-page]]))

::jte/keep

(define-dte :data/edn :db.type/string
  [this] (pr-str this)
  [^String s] (read-string s))

(defn create-database [uri]
  (d/create-database uri)
  (let [conn (d/connect uri)]
    @(d/transact conn (edn/read-string (slurp (io/resource "db-schema.edn"))))
    conn))

(defn serve-page [page request]
  {:status 200
   :body (-> page
             (render-page request)
             (post-process-page images/image-asset-config request))
   :headers {"Content-Type" "text/html"}})

(defn handle-request [db request]
  (when-let [e (d/entity db [:page/uri (:uri request)])]
    (serve-page (case (:page/kind e)
                  :page.kind/article (article-page/create-page e)
                  :page.kind/blog (blog/create-index-page db)
                  :page.kind/blog-category (blog/create-category-index-page db e)
                  :page.kind/blog-post (blog/create-post-page e)
                  :page.kind/frontpage (frontpage/create-page)
                  :page.kind/contact (contact-page/create-page db)
                  :page.kind/profile (profile-page/create-page e)
                  :page.kind/cv (cv-page/create-page e)
                  :page.kind/reference (reference-page/create-page e)
                  :page.kind/references (references-page/create-page e)
                  :page.kind/tech (tech-page/create-page e)
                  :page.kind/people (people-page/create-page e)
                  :page.kind/video (video-page/create-page e)
                  :page.kind/whoami (whoami-page/create-page e))
                request)))

(defn serve-pages [conn]
  (fn [request]
    (or (handle-request (d/db conn) request)
        {:status 404
         :body "Eg fann han ikkje"
         :headers {"Content-Type" "text/html"}})))

(defn get-pages [db request]
  (into {}
        (for [uri (d/q '[:find [?uri ...] :where [_ :page/uri ?uri]] db)]
          (try
            [uri (:body (handle-request db (assoc request :uri uri)))]
            (catch Exception e
              (throw (ex-info (str "Unable to render page " uri)
                              {:uri uri}
                              e)))))))



(comment
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  (->> "tech-categories.edn"
       io/resource
       slurp
       read-string
       (group-by second)
       (map (fn [[k v]] [k (sort (map first v))]))
       (into {})
       pr-str
       (spit "/tmp/lol.edn"))

  (def reference
    (->> (d/q '[:find ?e
                :in $ ?uri
                :where
                [?e :page/uri ?uri]]
              (d/db conn)
              "/referanser/oche-dart/")
         ffirst
         (d/entity (d/db conn))))

  (into {} reference)

)
