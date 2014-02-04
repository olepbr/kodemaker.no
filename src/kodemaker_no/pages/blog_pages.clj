(ns kodemaker-no.pages.blog-pages
  (:require [kodemaker-no.homeless :refer [update-vals-with-key rename-keys]]
            [clojure.string :as str]
            [kodemaker-no.formatting :refer [to-html]]
            [kodemaker-no.blog-posts :refer [blog-post-path]])
  (:import java.text.SimpleDateFormat))

(defn- published [blog-post]
  (.format (java.text.SimpleDateFormat. "dd.MM.yyyy") (:published blog-post)))

(defn blog-post-page [path blog-post]
  {:title {:head (:title blog-post)}
   :illustration (:illustration blog-post)
   :lead (list [:h2 (:title blog-post)]
               [:p.shy (published blog-post)])
   :body (list (to-html (:body blog-post))
               [:div#disqus_thread.mod]
               [:script (str "var disqus_identifier='" path "';"
                             (slurp (clojure.java.io/resource "public/scripts/blog-post.js")))])})

(defn blog-post-pages [blog-posts]
  (-> blog-posts
      (rename-keys blog-post-path)
      (update-vals-with-key #(partial blog-post-page %1 %2))))

(defn blog-page [blog-posts]
  {:title "Blogg"
   :body (str "Det er bloggen vår da" (keys blog-posts))})
