(ns kodemaker-no.pages.blog-pages
  (:require [kodemaker-no.homeless :refer [update-vals rename-keys]]
            [clojure.string :as str]
            [kodemaker-no.formatting :refer [to-html]]
            [kodemaker-no.cultivate.blog-posts :refer [blog-post-path]]
            [kodemaker-no.date :refer [format-dmy]]))

(defn- published [blog-post]
  (format-dmy (:published blog-post)))

(defn- by-published [blog-posts]
  (->> blog-posts (sort-by :published) reverse))

(defn- blog-post-li [blog-post]
  [:li.small (list [:a {:href (:path blog-post)} (:title blog-post)]
                   [:br]
                   [:span.shy (published blog-post)])])

(defn- blog-post-list [blog-posts]
  [:ul (map blog-post-li blog-posts)])

(defn- blog-post-lead [blog-post]
  (list [:h2 [:a {:href (:path blog-post)} (:title blog-post)]]
        [:p.shy (published blog-post)]))

(defn- blog-post-body [blog-post]
  (to-html (:body blog-post)))

(defn- blog-post-aside [blog-post blog-posts]
  (list [:h3 "Mer fra bloggen"]
        (->> blog-posts
             (remove #{blog-post})
             by-published
             blog-post-list)))

(defn- render-blog-post [blog-post]
  [:div.line
   [:div.unit.s-1of3
    [:div.bd
     (when-let [img (:illustration blog-post)]
       [:a.block.mod {:href (:path blog-post)} [:img {:src img}]])]]
   [:div.lastUnit
    [:div.bd
     (list
      (blog-post-lead blog-post)
      (blog-post-body blog-post))]]])

(defn- disqus-script []
  (slurp (clojure.java.io/resource "public/scripts/blog-post.js")))

(defn blog-post-page [blog-post blog-posts]
  {:title (:title blog-post)
   :illustration (:illustration blog-post)
   :aside (list
           [:p.shy (published blog-post)]
           (blog-post-aside blog-post blog-posts))
   :body (list (blog-post-body blog-post)
               [:div#disqus_thread.mod]
               [:script (str "var disqus_identifier='" (:path blog-post) "';"
                             (disqus-script))])})

(defn blog-post-pages [blog-posts]
  (-> blog-posts
      (rename-keys blog-post-path)
      (update-vals #(partial blog-post-page % (vals blog-posts)))))

(defn blog-page [blog-posts]
  {:title "Kodemakerbloggen"
   :body (map render-blog-post (by-published blog-posts))})
