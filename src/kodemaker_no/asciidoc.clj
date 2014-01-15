(ns kodemaker-no.asciidoc
  (:require [asciidoclj.core :as adoc]
            [clojure.string :as str]
            [kodemaker-no.homeless :refer [nil-if-blank remove-nil-vals]]))

(def adoc-parse (memoize adoc/parse))

(defn- find-part [doc title]
  (first (filter #(= title (:title %)) (:parts doc))))

(defn- content [part]
  (when part
    (-> part :content
        (str/replace #"<div class=\"[^\"]+\">\n" "")
        (str/replace #"\n</div>\n?" ""))))

(defn- htmlize-part [part]
  (str "<h2>" (:title part) "</h2>" (content part)))

(defn- patch-together-article-body [doc]
  (->> doc :parts
       (remove #(-> % :title #{":lead" ":aside" ":ignore"}))
       (map htmlize-part)
       (str/join)
       (nil-if-blank)))

(defn parse-article [s]
  (-> (let [doc (adoc-parse (str s "\n\n== :ignore"))]
        {:title (-> doc :header :document-title)
         :url (-> doc :header :attributes :url)
         :illustration (-> doc :header :attributes :illustration)
         :lead (-> doc (find-part ":lead") content)
         :body (-> doc patch-together-article-body)
         :aside (-> doc (find-part ":aside") content)})
      remove-nil-vals))
