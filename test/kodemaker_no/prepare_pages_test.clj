(ns kodemaker-no.prepare-pages-test
  (:require [kodemaker-no.prepare-pages :refer :all]
            [midje.sweet :refer :all]
            [net.cgrand.enlive-html :refer [select html-resource]]))

(defn get-page []
  {:title "Systemutvikling på høyt nivå"
   :illustration "/photos/kolbjorn/side-profile-cropped.jpg"
   :lead [:p "Vi er et fokusert selskap."]})

(def request
  {:optimus-assets [{:path "/pk-spc-1f832dasf8.jpg"
                     :original-path "/photos/kolbjorn/side-profile-cropped.jpg"}]})

(defn parse [s]
  (html-resource (java.io.StringReader. s)))

(fact
 (let [page (parse (prepare-page get-page request))]

   (-> page (select [:title]) first :content)
   => '("Systemutvikling på høyt nivå | Kodemaker")

   (-> page (select [:.illustration :img]) first :attrs :src)
   => "/pk-spc-1f832dasf8.jpg"

   (-> page (select [:.unitRight :.bd :p]) first :content)
   => '("Vi er et fokusert selskap.")))
