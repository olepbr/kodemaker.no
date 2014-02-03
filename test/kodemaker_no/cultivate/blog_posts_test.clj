(ns kodemaker-no.cultivate.blog-posts-test
  (:require [kodemaker-no.cultivate.blog-posts :refer [cultivate-blog-posts]]
            [kodemaker-no.validate :refer [validate-content]]
            [kodemaker-no.cultivate.content-shells :as c]
            [midje.sweet :refer :all])
  (:import java.text.SimpleDateFormat))

(def content
  (c/content
   {:blog-posts {"post.md"
                 ":title Kommende Kodemaker
:published 2013-06-28
:illustration /photos/blog/alf-kristian-stoyle.jpg

:::body

Velkommen til Kodemaker!"}}))

(defn cultivate [content]
  (cultivate-blog-posts (validate-content content)))

(defn- date-str [date]
  (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") date))

(let [blog-post ((cultivate content) "post.md")]
  (fact "Builds blog post map"
        (:title blog-post) => "Kommende Kodemaker"
        (date-str (:published blog-post)) => "2013-06-28"
        (:body blog-post) => "Velkommen til Kodemaker!"
        (:illustration blog-post) "/photos/blog/alf-kristian-stoyle.jpg"))
