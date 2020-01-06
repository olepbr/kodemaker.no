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
          :lead "<p>Vi er et fokusert selskap.</p>\n"
          :body "<h2>Teknologisk i front</h2>\n<p>Svært god teknologikunnskap.</p>\n<h2>Konsulenttjenester innen systemutvikling</h2>\n<p>Våre konsulenter.</p>\n<p>Med lang erfaring.</p>\n<h2>Språk, plattformer og utviklingsmiljøer</h2>\n<p>Vi har både små og store.</p>\n"
          :aside "<p><a href=\"/kolbjorn.html\">Kolbjørn Jetne</a></p>\n<p>&ldquo;Erfaringer fra tøffere tider&rdquo;</p>\n"})

(fact "It handles articles with passthrough sections containing markup."
      (article-page {:title "Referanser"
                     :lead "<div>Hupp</div>

Mer, mer, mer!"
                     :aside "Punkt 1

### Heading

Punkt 2"})
      => {:title "Referanser"
          :lead "<div>Hupp</div>\n<p>Mer, mer, mer!</p>\n"
          :aside "<p>Punkt 1</p>\n<h3>Heading</h3>\n<p>Punkt 2</p>\n"})

(fact "It allows articles to have meta."
      (article-page {:title "Referanser"
                     :meta "[{:name \"robots\", :content \"noindex\"}]"})
      => {:title "Referanser"
          :meta [{:name "robots", :content "noindex"}]})
