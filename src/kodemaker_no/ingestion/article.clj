(ns kodemaker-no.ingestion.article
  (:require [kodemaker-no.homeless :as h]))

(def article-keys
  {:title :article/title
   :illustration :article/illustration
   :aside :article/aside
   :lead :article/lead
   :meta :article/meta
   :body :article/body})

(defn create-tx [file-name article]
  [(-> article
       (h/select-renamed-keys article-keys)
       (assoc :page/uri (str (second (re-find #"articles(.*).md" file-name)) "/"))
       (assoc :page/kind :page.kind/article))])

(comment
  (defn ingest-article [file-name]
    (create-tx file-name (h/slurp-mapdown-resource file-name)))

  (ingest-article "articles/kompetanse.md")
  )
