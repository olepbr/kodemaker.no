(ns kodemaker-no.web-test
  (:require [kodemaker-no.web :refer :all]
            [midje.sweet :refer :all]
            [net.cgrand.enlive-html :refer [select html-resource]]))

(defn parse [s]
  (html-resource (java.io.StringReader. s)))

(fact
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

(fact
 "Får vi 200 på hele siten? (del 1)" :slow :slow-1

 (let [urls (take split-index (keys (get-pages)))]
   (doseq [url urls]
     (-> (app {:uri url}) :status) => 200)))

(fact
 "Får vi 200 på hele siten? (del 2)" :slow :slow-2

 (let [urls (drop split-index (keys (get-pages)))]
   (doseq [url urls]
     (-> (app {:uri url}) :status) => 200)))
