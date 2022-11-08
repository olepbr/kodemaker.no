(ns kodemaker-no.web-test
  (:require [kodemaker-no.web :as web]
            [midje.sweet :refer :all]
            [net.cgrand.enlive-html :refer [select html-resource]]))

(defn parse [s]
  (html-resource (java.io.StringReader. s)))

(fact
 (set (web/extract-images "
<html>
<head>
<meta property=\"og:image\" content=\"meta.jpg\">
<meta property=\"og:site\" content=\"Kodemaker Test\">
</head>
<body>
<div>
  <img src=\"foo.png\"/>
  <div class=\"foo w-style-img\" style=\"background: url(bar.jpg)\"></div>
  <div class=\"bar w-style-img\"></div>
</div></body></html>")) => #{"bar.jpg" "foo.png", "meta.jpg"}
 )

#_(def app (web/create-app))

#_(fact
 "En helt enkel ende-til-ende test for å fange opp dusterier."

 (let [result (app {:uri "/magnar/"})]
   (-> result :status) => 200
   (-> result :body parse (select [:title]) first :content)
   => '("Magnar Sveen | Kodemaker"))

 (let [result (app {:uri "/oocss/"})]
   (-> result :status) => 200
   (-> result :body parse (select [:title]) first :content)
   => '("OOCSS | Kodemaker")))

(def split-index 75)

#_(fact
 "Får vi 200 på hele siten? (del 1)" :slow :slow-1

 (let [urls (take split-index (keys (web/get-pages)))]
   (doseq [url urls]
     {:status (-> (app {:uri url}) :status)
      :uri url} => {:status 200 :uri url})))

#_(fact
 "Får vi 200 på hele siten? (del 2)" :slow :slow-2

 (let [urls (drop split-index (keys (web/get-pages)))]
   (doseq [url urls]
     {:status (-> (app {:uri url}) :status)
      :uri url} => {:status 200 :uri url})))
