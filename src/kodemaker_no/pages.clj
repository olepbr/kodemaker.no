(ns kodemaker-no.pages
  (:require [kodemaker-no.pages.people-page :refer [all-people]]
            [kodemaker-no.pages.person-pages :refer [person-pages]]
            [kodemaker-no.pages.article-pages :refer [article-pages]]))

(defn general-pages []
  {"/mennesker.html" all-people})

(defn get-pages []
  (merge (person-pages)
         (article-pages)
         (general-pages)))
