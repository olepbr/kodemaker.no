(ns kodemaker-no.pages.blog-post-pages-test
  (:require [kodemaker-no.pages.blog-post-pages :refer :all]
            [kodemaker-no.homeless :refer [hiccup-find]]
            [midje.sweet :refer :all])
  (:import java.text.SimpleDateFormat))

(fact "Parses blog post document"
      (let [blog-post (get-blog-post ":title Nice blog post
:published 2013-01-01
:illustration /photos/sexy.jpg
:::body
This is nice, right?")]
        (:title blog-post) => "<p>Nice blog post</p>"
        (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") (:published blog-post)) => "2013-01-01"
        (:body blog-post) => "<p>This is nice, right?</p>"
        (:illustration blog-post) => "/photos/sexy.jpg"))
