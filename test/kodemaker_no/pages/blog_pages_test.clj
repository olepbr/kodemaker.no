(ns kodemaker-no.pages.blog-pages-test
  (:require [kodemaker-no.pages.blog-pages :refer :all]
            [kodemaker-no.homeless :refer [hiccup-find]]
            [midje.sweet :refer :all]
            [hiccup.core :refer [html]]
            [net.cgrand.enlive-html :refer [select html-resource]]
            [clj-time.format :refer [parse formatters]]))

(def blog-posts
  {"/nice-blog-post.md" {:title "Nice blog post"
                         :published (parse (formatters :year-month-day) "2013-01-01")
                         :illustration "/photos/sexy.jpg"
                         :body "This is nice, right?"
                         :path "/blogg/nice-blog-post/"}
   "/ausam-artikkel.md" {:title "Ausam artikkel"
                         :published (parse (formatters :year-month-day) "2012-12-01")
                         :body "This is nice, right?"
                         :path "/blogg/ausam-artikkel/"}
   "/annen-artikkel.md" {:title "Annen artikkel"
                         :published (parse (formatters :year-month-day) "2012-12-15")
                         :body "This is nice, right?"
                         :path "/blogg/annen-artikkel/"}})

(defn parse-html [v]
  (html-resource (java.io.StringReader. (html v))))

(fact "Gets blog post pages"
      (let [page-content (((blog-post-pages blog-posts) "/blogg/nice-blog-post/"))
            body (:body page-content)]
        (:title page-content) => "Nice blog post"
        (:illustration page-content) => "/photos/sexy.jpg"
        (html (first body)) => (html [:p "This is nice, right?"])
        (html (second body)) => (html [:div#disqus_thread.mod])))

(fact "Blog post page lists other posts in aside"
      (let [page-content (((blog-post-pages blog-posts) "/blogg/nice-blog-post/"))
            aside (parse-html (:aside page-content))]
        (-> aside (select [:li]) count) => 2
        (-> aside (select [:li]) first str) => #(.contains % "Annen artikkel")
        (-> aside (select [:li]) second str) => #(.contains % "Ausam artikkel")))

(fact "Blog index sorts posts by published date"
      (let [page-content (blog-page (vals blog-posts))
            body (parse-html (:body page-content))]
        (:title page-content) => "Kodemakerbloggen"
        (-> body (select [:h2]) (nth 0) str) => #(.contains % "Nice blog post")
        (-> body (select [:h2]) (nth 1) str) => #(.contains % "Annen artikkel")
        (-> body (select [:h2]) (nth 2) str) => #(.contains % "Ausam artikkel")))
