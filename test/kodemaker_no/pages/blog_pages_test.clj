(ns kodemaker-no.pages.blog-pages-test
  (:require [kodemaker-no.pages.blog-pages :refer :all]
            [kodemaker-no.homeless :refer [hiccup-find]]
            [midje.sweet :refer :all]
            [hiccup.core :refer [html]]
            [net.cgrand.enlive-html :refer [select html-resource]]))

(def blog-posts
  {"/nice-blog-post.md" {:title "Nice blog post"
                         :published (java.util.Date. 113, 0, 1) ;; lol
                         :illustration "/photos/sexy.jpg"
                         :body "This is nice, right?"
                         :path "/blogg/nice-blog-post/"}
   "/ausam-artikkel.md" {:title "Ausam artikkel"
                         :published (java.util.Date. 112, 11, 1) ;; lol
                         :body "This is nice, right?"
                         :path "/blogg/ausam-artikkel/"}
   "/annen-artikkel.md" {:title "Annen artikkel"
                         :published (java.util.Date. 112, 11, 15) ;; lol
                         :body "This is nice, right?"
                         :path "/blogg/annen-artikkel/"}})

(defn parse [v]
  (html-resource (java.io.StringReader. (html v))))

(fact "Gets blog post pages"
      (let [page-content (((blog-post-pages blog-posts) "/blogg/nice-blog-post/"))
            body (:body page-content)]
        (:title page-content) => {:head "Nice blog post"}
        (:illustration page-content) => "/photos/sexy.jpg"
        (:lead page-content) => '([:h2 [:a {:href "/blogg/nice-blog-post/"} "Nice blog post"]]
                                    [:p.shy "01.01.2013"])
        (html (first body)) => (html [:p "This is nice, right?"])
        (html (second body)) => (html [:div#disqus_thread.mod])))

(fact "Blog post page lists other posts in aside"
      (let [page-content (((blog-post-pages blog-posts) "/blogg/nice-blog-post/"))
            aside (parse (:aside page-content))]
        (-> aside (select [:li]) count) => 2
        (-> aside (select [:li]) first str) => #(.contains % "Annen artikkel")
        (-> aside (select [:li]) second str) => #(.contains % "Ausam artikkel")))

(fact "Blog index sorts posts by published date"
      (let [page-content (blog-page (vals blog-posts))
            body (parse (:body page-content))]
        (:title page-content) => "Kodemakerbloggen"
        (-> body (select [:h2]) (nth 0) str) => #(.contains % "Nice blog post")
        (-> body (select [:h2]) (nth 1) str) => #(.contains % "Annen artikkel")
        (-> body (select [:h2]) (nth 2) str) => #(.contains % "Ausam artikkel")))
