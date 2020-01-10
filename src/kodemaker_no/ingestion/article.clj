(ns kodemaker-no.ingestion.article
  (:require [kodemaker-no.homeless :as h]))

(def article-keys
  {:article/title :title
   :article/illustration :illustration
   :article/layout :layout
   :article/aside :aside
   :article/lead :lead
   :article/meta :meta
   :article/body :body})

(defn create-tx [file-name article]
  [(-> article
       (h/keep-vals article-keys)
       (assoc :page/uri (str (second (re-find #"articles(.*).md" file-name)) "/"))
       (assoc :page/kind :page.kind/article)
       (h/update-in-existing [:article/layout] keyword))])

(comment
  (defn ingest-article [file-name]
    (create-tx file-name (h/slurp-mapdown-resource file-name)))

  (ingest-article "articles/jobbe-hos-oss.md")
  )
