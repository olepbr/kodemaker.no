(ns kodemaker-no.articles-test
  (:require [kodemaker-no.articles :refer [load-articles]]
            [kodemaker-no.validate :refer [validate-content]]
            [kodemaker-no.cultivate.content-shells :as c]
            [midje.sweet :refer :all]))

(let [article-strings {"/page.md" ":title Systemutvikling
:illustration /photos/kolbjorn/side-profile-cropped.jpg
:::lead

Vi er et fokusert selskap.

:::body

## Teknologisk i front

:::aside

Erfaringer fra tøffere tider"
                       "/minimal.md" ":title Referanser
:::lead
Ingressen er her."}
      articles (load-articles article-strings)
      page (articles "/page.md")]

  (fact "It generates map from structured markdown"
        (:title page) => "Systemutvikling"
        (:illustration page) => "/photos/kolbjorn/side-profile-cropped.jpg"
        (:lead page) => "Vi er et fokusert selskap."
        (:body page) => "## Teknologisk i front"
        (:aside page) => "Erfaringer fra tøffere tider")

  (fact "It handles minimal pages"
        (articles "/minimal.md") => {:title "Referanser"
                                     :lead "Ingressen er her."})

  (fact "It validates articles"
        (validate-content (c/content {:articles articles}))))
