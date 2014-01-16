(ns kodemaker-no.pages
  (:require [kodemaker-no.pages.people-page :refer [all-people]]
            [kodemaker-no.pages.person-pages :refer [person-pages]]
            [kodemaker-no.pages.article-pages :refer [article-pages]]))

(defn general-pages [content]
  {"/mennesker.html" (partial all-people (:people content))})

(defn get-pages [content]
  (merge (person-pages (:people content))
         (article-pages (:articles content))
         (general-pages content)))
