(ns kodemaker-no.pages.article-pages-test
  (:require [kodemaker-no.pages.article-pages :refer :all]
            [midje.sweet :refer :all]
            [net.cgrand.enlive-html :refer [select html-resource]]))

(def articles {"/kompetanse.adoc" "
= Systemutvikling på høyt nivå

:illustration: /photos/kolbjorn/side-profile-cropped.jpg

== :lead

Vi er et fokusert selskap.
"})

(def assets
  [{:path "/pk-spc-1f832dasf8.jpg"
    :original-path "/photos/kolbjorn/side-profile-cropped.jpg"}])

(defn parse [s]
  (html-resource (java.io.StringReader. s)))

(fact
 (let [pages (article-pages articles)
       page (parse (:body ((pages "/kompetanse.html") {:optimus-assets assets})))]

   (-> page (select [:title]) first :content)
   => '("Systemutvikling på høyt nivå | Kodemaker")

   (-> page (select [:.illustration :img]) first :attrs :src)
   => "/pk-spc-1f832dasf8.jpg"

   (-> page (select [:.unitRight :.bd :p]) first :content)
   => '("Vi er et fokusert selskap.")))
