(ns kodemaker-no.render-page-test
  (:require [kodemaker-no.render-page :refer :all]
            [midje.sweet :refer :all]
            [net.cgrand.enlive-html :refer [select html-resource]]))

(defn parse [s]
  (html-resource (java.io.StringReader. s)))

(def request {})

(fact
 "Title is postfixed with | Kodemaker"
 (-> {:title "Title"} (render-page request) parse
     (select [:title]) first :content) => '("Title | Kodemaker"))

(fact
 "Unless it's not given."
 (-> {:title nil} (render-page request) parse
     (select [:title]) first :content) => '("Kodemaker"))

(fact
 "The title is also present in a big old h1 at the top."

 (-> {:title "Title"} (render-page request) parse
     (select [:h1]) second :content) => '("Title"))

(fact
 "Unless it's not given - again."

 (-> {:title nil} (render-page request) parse
     (select [:h1]) second) => nil)

