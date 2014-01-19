(ns kodemaker-no.pages.article-pages
  (:require [asciidoclj.core :as adoc]
            [kodemaker-no.homeless :refer [nil-if-blank remove-vals update-vals rename-keys]]
            [clojure.string :as str]))

(def adoc-parse (memoize adoc/parse))

(defn- find-part [doc title]
  (first (filter #(= title (:title %)) (:parts doc))))

(defn- content [part]
  (when part
    (-> part :content
        (str/replace #"\n?<div class=\"[^\"]+\">\n?" "")
        (str/replace #"\n?</div>\n?" ""))))

(defn- htmlize-part [part]
  (str "<h2>" (:title part) "</h2>" (content part)))

(defn- patch-together-article-body [doc]
  (->> doc :parts
       (remove #(-> % :title #{":lead" ":aside" ":ignore"}))
       (map htmlize-part)
       (str/join)
       (nil-if-blank)))

(defn article-page [s]
  (-> (let [doc (adoc-parse (str s "\n\n== :ignore"))]
        {:title (-> doc :header :document-title)
         :illustration (-> doc :header :attributes :illustration)
         :lead (-> doc (find-part ":lead") content)
         :body (-> doc patch-together-article-body)
         :aside (-> doc (find-part ":aside") content)})
      (remove-vals nil?)))

(defn- article-url [path]
  (prn path)
  (if (= path "/index.adoc")
    "/index.html"
    (str/replace path #"\.adoc$" "/")))

(defn article-pages [articles]
  (-> articles
      (rename-keys article-url)
      (update-vals #(partial article-page %))))
