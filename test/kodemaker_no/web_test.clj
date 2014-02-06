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

;; (fact
;;  "Får vi 200 på hele siten?"

;;  (let [urls (keys (get-pages))]
;;    (doseq [url urls]
;;      (-> (app {:uri url}) :status) => 200)))
