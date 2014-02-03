(ns kodemaker-no.pages.blog-post-pages
  (:require [kodemaker-no.homeless :refer [update-vals rename-keys]]
            [clojure.string :as str]
            [kodemaker-no.formatting :refer [to-html]])
  (:import java.text.SimpleDateFormat))

(defn- published [blog-post]
  (.format (java.text.SimpleDateFormat. "dd.MM.yyyy") (:published blog-post)))

(defn- blog-post-url [path]
  (str/replace path #"\.md$" "/"))

(defn blog-post-page [blog-post]
  {:title {:head (:title blog-post)}
   :illustration (:illustration blog-post)
   :lead (list [:h2 (:title blog-post)]
               [:p.shy (published blog-post)])
   :body (to-html (:body blog-post))})

(defn blog-post-pages [blog-posts]
  (-> blog-posts
      (rename-keys blog-post-url)
      (update-vals #(partial blog-post-page %))))
