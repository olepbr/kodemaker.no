(ns kodemaker-no.pages
  (:require [kodemaker-no.pages.people-page :refer [all-people]]
            [kodemaker-no.pages.person-pages :refer [person-pages]]
            [kodemaker-no.pages.tech-pages :refer [tech-pages]]
            [kodemaker-no.pages.article-pages :refer [article-pages]]
            [kodemaker-no.pages.project-pages :refer [project-pages]]
            [stasis.core :as stasis]))

(defn general-pages [content]
  {"/mennesker/" (partial all-people (vals (:people content)))})

(defn create-pages [content]
  (stasis/merge-page-sources
   {:person-pages (person-pages (vals (:people content)))
    :tech-pages (tech-pages (vals (:tech content)))
    :project-pages (project-pages (vals (:projects content)))
    :article-pages (article-pages (:articles content))
    :general-pages (general-pages content)}))
