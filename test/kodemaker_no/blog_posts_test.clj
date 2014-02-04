(ns kodemaker-no.blog-posts-test
  (:require [kodemaker-no.blog-posts :refer [load-blog-posts]]
            [kodemaker-no.validate :refer [validate-content]]
            [kodemaker-no.cultivate.content-shells :as c]
            [midje.sweet :refer :all])
  (:import java.text.SimpleDateFormat))

(defn- date-str [date]
  (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") date))

(def blog-posts {"post.md" {:title "Kommende Kodemaker"
                            :published "2013-06-28"
                            :illustration "/photos/blog/alf-kristian-stoyle.jpg"
                            :body "Velkommen til Kodemaker!"}})

(def loaded-posts (load-blog-posts blog-posts))

(let [post (loaded-posts "post.md")]
  (fact "Parses published date"
        (date-str (:published post)) "2013-06-28")

  (fact "Validates blog-post map"
        (validate-content (c/content {:blog-posts loaded-posts}))))
