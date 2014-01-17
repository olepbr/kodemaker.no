(ns kodemaker-no.pages.article-pages-test
  (:require [kodemaker-no.pages.article-pages :refer :all]
            [midje.sweet :refer :all]))

(fact "It generates a page from an asciidoc article."
      (article-page "
= Systemutvikling på høyt nivå

:illustration: /photos/kolbjorn/side-profile-cropped.jpg

== :lead

Vi er et fokusert selskap.

== Teknologisk i front

Svært god teknologikunnskap.

== Konsulenttjenester innen systemutvikling

Våre konsulenter.

Med lang erfaring.

== Språk, plattformer og utviklingsmiljøer

Vi har både små og store.

== :aside

link:/kolbjorn.html[Kolbjørn Jetne]

``Erfaringer fra tøffere tider''
")
      => {:title "Systemutvikling på høyt nivå"
          :illustration "/photos/kolbjorn/side-profile-cropped.jpg"
          :lead "<p>Vi er et fokusert selskap.</p>"
          :body "<h2>Teknologisk i front</h2><p>Svært god teknologikunnskap.</p><h2>Konsulenttjenester innen systemutvikling</h2><p>Våre konsulenter.</p><p>Med lang erfaring.</p><h2>Språk, plattformer og utviklingsmiljøer</h2><p>Vi har både små og store.</p>"
          :aside "<p><a href=\"/kolbjorn.html\">Kolbjørn Jetne</a></p><p>&#8220;Erfaringer fra tøffere tider&#8221;</p>"})

(fact "It handles minimal articles."
      (article-page "
= Referanser

== :lead

Ingressen er her.
")
      => {:title "Referanser"
          :lead "<p>Ingressen er her.</p>"})
