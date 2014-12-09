(ns kodemaker-no.pages.reference-pages
  (:require [clojure.string :as str]
            [kodemaker-no.homeless :refer [update-vals rename-keys]]))

(defn- reference-page [sections]
  {:title (:page-title (first sections))
   :sections sections})

(defn- reference-url [path]
  (str "/referanser"
       (if (= path "/index.md")
         "/index.html"
         (str/replace path #"\.md$" "/"))))

(defn reference-pages [references]
  (-> references
      (rename-keys reference-url)
      (update-vals #(partial reference-page %))))
