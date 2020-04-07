(ns kodemaker-no.rss
  (:require [clojure.data.xml :as xml]
            [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [hiccup.core :refer [html]]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.new-pages.blog :as blog]))

(defn to-id-str [str]
  "Replaces all special characters with dashes, avoiding leading,
   trailing and double dashes."
  (-> (.toLowerCase str)
      (str/replace #"[^a-zA-Z0-9]+" "-")
      (str/replace #"-$" "")
      (str/replace #"^-" "")))

(defn- url [post]
  (str "https://www.kodemaker.no" (:page/uri post)))

(defn- entry [post]
  [:entry
   [:title (:blog-post/title post)]
   [:updated (str (:blog-post/published post) "T07:00:00+02:00")]
   [:author [:name (:person/full-name (blog/author post))]]
   [:link {:href (url post)}]
   [:id (str "urn:www.kodemaker.no:feed:post:" (:blog-post/published post))]
   [:content {:type "html"}
    (html
     [:div
      [:div.text.mbm (f/to-html (:blog-post/blurb post))]
      [:p [:a {:href (url post)}
           "Les artikkelen"]]])]])

(defn atom-xml [blog-posts]
  (xml/emit-str
   (xml/sexp-as-element
    [:feed {:xmlns "http://www.w3.org/2005/Atom"
            :xmlns:media "http://search.yahoo.com/mrss/"}
     [:id "urn:www.kodemaker.no:feed"]
     [:updated
      (str (:blog-post/published (first blog-posts)) "T07:00:00+02:00")]
     [:title {:type "text"} "Kodemaker sin blogg"]
     [:link {:rel "self" :href "https://www.kodemaker.no/atom.xml"}]
     (map entry blog-posts)])))

(comment
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  (def posts (blog/blog-posts-by-published db))

  (def post (first posts))

  (entry post)
  (atom-xml posts)

  )
