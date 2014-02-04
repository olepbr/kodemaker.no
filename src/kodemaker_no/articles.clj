(ns kodemaker-no.articles
  (:require [kodemaker-no.structured-document :refer [read-doc]]
            [kodemaker-no.homeless :refer [update-vals]]))

(defn load-articles [articles]
  (update-vals articles read-doc))
