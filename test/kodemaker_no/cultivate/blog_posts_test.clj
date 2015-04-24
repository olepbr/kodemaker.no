(ns kodemaker-no.cultivate.blog-posts-test
  (:require [kodemaker-no.cultivate.blog-posts :refer [cultivate-blog-posts]]
            [kodemaker-no.validate :refer [validate-content]]
            [kodemaker-no.cultivate.content-shells :as c]
            [clj-time.core :refer [local-date]]
            [midje.sweet :refer :all]))

(def blog-posts {"/post.md" {:title "Kommende Kodemaker"
                             :published "2013-06-28"
                             :illustration "/photos/blog/alf-kristian-stoyle.jpg"
                             :presence "twitter stoyle, github stoyle"
                             :body "Velkommen til Kodemaker!"}})

(def cultivated-posts (cultivate-blog-posts blog-posts))

(let [post (cultivated-posts "/post.md")]
  (fact "Parses published date"
        (:published post) => (local-date 2013 6 28))

  (fact "Includes path to blog post"
        (:path post) => "/blogg/post/")

  (fact "Parses presence"
        (:presence post) => {:twitter "stoyle"
                             :github "stoyle"}))
