(ns kodemaker-no.pages.blog-pages-test
  (:require [kodemaker-no.pages.blog-pages :refer :all]
            [kodemaker-no.homeless :refer [hiccup-find]]
            [midje.sweet :refer :all]
            [hiccup.core :refer [html]]))

(def blog-post
  {:title "Nice blog post"
   :published (java.util.Date. 113, 0, 1)
   :illustration "/photos/sexy.jpg"
   :body "This is nice, right?"})

(fact "Gets blog post pages"
      (let [blog-posts {"/nice-blog-post.md" blog-post}
            page-content (((blog-post-pages blog-posts) "/blogg/nice-blog-post/"))
            body (:body page-content)]
        (:title page-content) => {:head "Nice blog post"}
        (:illustration page-content) => "/photos/sexy.jpg"
        (:lead page-content) => '([:h2 "Nice blog post"]
                                  [:p.shy "01.01.2013"])
        (html (first body)) => (html [:p "This is nice, right?"])
        (html (second body)) => (html [:div#disqus_thread.mod])))
