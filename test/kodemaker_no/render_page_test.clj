(ns kodemaker-no.render-page-test
  (:require [kodemaker-no.render-page :refer :all]
            [midje.sweet :refer :all]
            [net.cgrand.enlive-html :refer [select html-resource]]
            [hiccup.core :refer [html]]))

(defn parse [s]
  (html-resource (java.io.StringReader. s)))

(def request {})

(fact
 "Title is postfixed with | Kodemaker"
 (-> {:title "Title"} (render-page request) parse
     (select [:title]) first :content) => '("Title | Kodemaker"))

(fact
 "Unless it's not given, then it's just: Kodemaker"
 (-> {:title nil} (render-page request) parse
     (select [:title]) first :content) => '("Kodemaker"))

(fact
 "The title is also present in a big old h1 at the top."

 (-> {:title "Title"} (render-page request) parse
     (select [:h1]) second :content) => '("Title"))

(fact
 "Again, unless it's not given: In which case there is no header."

 (-> {:title nil} (render-page request) parse
     (select [:h1]) second) => nil)

(fact
 "Uses specific title in head"
 (-> {:title {:head "In head" :h1 "In h1"}} (render-page request) parse
     (select [:title]) first :content) => '("In head | Kodemaker"))

(fact
 "Uses default title in head, even if there is a specific in h1"
 (-> {:title {:h1 "In h1"}} (render-page request) parse
     (select [:title]) first :content) => '("Kodemaker"))

(fact
 "Uses h1 title in body"
 (-> {:title {:head "In head" :h1 "h1"}} (render-page request) parse
     (select [:h1]) second :content first) => "h1")

(fact
 "Adds top margin to body and aside when no h1"
 (let [markup (-> {:title {:head "In head"}
                   :aside "Hmm"
                   :body "Body"} (render-page request) parse)]
   (-> markup (select [:div.aside.mtm]) first :content first :content first) => "Hmm"
   (-> markup (select [:div.body.mtm]) first :content first :content first) => "Body"))

(fact
 "Adds custom meta tags"
 (let [markup (-> {:title {:head "In head"}
                   :meta [{:name "robots", :content "noindex"}]
                   :body "Body"} (render-page request) parse)]
   (-> markup (select [:meta]) last :attrs) => {:name "robots", :content "noindex"}))
