(ns kodemaker-no.pages.article-pages-test
  (:require [kodemaker-no.pages.article-pages :refer :all]
            [midje.sweet :refer :all]))

(fact "It renders markdown for body, lead and aside."
      (article-page {:title "Systemutvikling på høyt nivå"
                     :illustration "/photos/kolbjorn/side-profile-cropped.jpg"
                     :lead "Vi er et fokusert selskap."
                     :body "## Teknologisk i front

Svært god teknologikunnskap.

## Konsulenttjenester innen systemutvikling

Våre konsulenter.

Med lang erfaring.

## Språk, plattformer og utviklingsmiljøer

Vi har både små og store."
                     :aside "[Kolbjørn Jetne](/kolbjorn.html)

\"Erfaringer fra tøffere tider\""})

      => {:title "Systemutvikling på høyt nivå"
          :illustration "/photos/kolbjorn/side-profile-cropped.jpg"
          :lead "<p>Vi er et fokusert selskap.</p>"
          :body "<h2>Teknologisk i front</h2><p>Svært god teknologikunnskap.</p><h2>Konsulenttjenester innen systemutvikling</h2><p>Våre konsulenter.</p><p>Med lang erfaring.</p><h2>Språk, plattformer og utviklingsmiljøer</h2><p>Vi har både små og store.</p>"
          :aside "<p><a href=\"/kolbjorn.html\">Kolbjørn Jetne</a></p><p>&ldquo;Erfaringer fra tøffere tider&rdquo;</p>"})

(fact "It handles articles with passthrough sections containing markup."
      (article-page {:title "Referanser"
                     :lead "<div>Hupp</div>

Mer, mer, mer!"
                     :aside "Punkt 1

### Heading

Punkt 2"})
      => {:title "Referanser"
          :lead "<div>Hupp</div><p>Mer, mer, mer!</p>"
          :aside "<p>Punkt 1</p><h3>Heading</h3><p>Punkt 2</p>"})
