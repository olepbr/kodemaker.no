(ns kodemaker-no.pages
  (:require [kodemaker-no.pages.article-pages :refer [article-pages]]
            [kodemaker-no.pages.blog-pages :refer [blog-post-pages blog-page]]
            [kodemaker-no.pages.index-page :refer [index-page]]
            [kodemaker-no.pages.form-page :refer [form-page]]
            [kodemaker-no.pages.person-pages :refer [person-pages]]
            [kodemaker-no.pages.project-pages :refer [project-pages]]
            [kodemaker-no.pages.tech-pages :refer [tech-pages]]
            [kodemaker-no.pages.video-pages :refer [video-pages]]
            [stasis.core :as stasis]))

(defn general-pages [content]
  {"/" (partial index-page (vals (:people content)))
   "/skjema/" form-page
   "/blogg/" (partial blog-page (vals (:blog-posts content)))})

(defn create-pages [content]
  (stasis/merge-page-sources
   {:person-pages (person-pages (vals (:people content)))
    :tech-pages (tech-pages (vals (:tech content)))
    :project-pages (project-pages (vals (:projects content)))
    :article-pages (article-pages (:articles content))
    :blog-post-pages (blog-post-pages (:blog-posts content))
    :general-pages (general-pages content)
    :video-pages (video-pages (:videos content))}))
