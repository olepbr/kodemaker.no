(ns kodemaker-no.pages
  (:require [kodemaker-no.pages.article-pages :refer [article-pages]]
            [kodemaker-no.pages.blog-pages :refer [blog-post-pages blog-page]]
            [kodemaker-no.pages.index-page :refer [index-page]]
            [kodemaker-no.pages.people-page :refer [all-people]]
            [kodemaker-no.pages.person-pages :refer [person-pages]]
            [kodemaker-no.pages.project-pages :refer [project-pages]]
            [kodemaker-no.pages.references-page :refer [references-page]]
            [kodemaker-no.pages.tech-pages :refer [tech-pages]]
            [stasis.core :as stasis]))

(defn general-pages [content]
  {"/" (partial index-page (:index content))
   "/mennesker/" (partial all-people (vals (:people content)))
   "/referanser/" (partial references-page (vals (:projects content)))
   "/blogg/" (partial blog-page (vals (:blog-posts content)))})

(defn create-pages [content]
  (stasis/merge-page-sources
   {:person-pages (person-pages (vals (:people content)))
    :tech-pages (tech-pages (vals (:tech content)))
    :project-pages (project-pages (vals (:projects content)))
    :article-pages (article-pages (:articles content))
    :blog-post-pages (blog-post-pages (:blog-posts content))
    :general-pages (general-pages content)}))
