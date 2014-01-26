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
          :lead "<div class=\"paragraph\">\n<p>Vi er et fokusert selskap.</p>\n</div>"
          :body "<h2>Teknologisk i front</h2><div class=\"paragraph\">\n<p>Svært god teknologikunnskap.</p>\n</div><h2>Konsulenttjenester innen systemutvikling</h2><div class=\"paragraph\">\n<p>Våre konsulenter.</p>\n</div>\n<div class=\"paragraph\">\n<p>Med lang erfaring.</p>\n</div><h2>Språk, plattformer og utviklingsmiljøer</h2><div class=\"paragraph\">\n<p>Vi har både små og store.</p>\n</div>"
          :aside "<div class=\"paragraph\">\n<p><a href=\"/kolbjorn.html\">Kolbjørn Jetne</a></p>\n</div>\n<div class=\"paragraph\">\n<p>&#8220;Erfaringer fra tøffere tider&#8221;</p>\n</div>"})

(fact "It handles minimal articles."
      (article-page "
= Referanser

== :lead

Ingressen er her.
")
      => {:title "Referanser"
          :lead "<div class=\"paragraph\">\n<p>Ingressen er her.</p>\n</div>"})

(fact "It handles articles with passthrough sections containing markup."
      (article-page "
= Referanser

== :lead

++++
<div>Hupp</div>
++++

Mer, mer, mer!

== :aside

Punkt 1

=== Heading

Punkt 2

")
      => {:title "Referanser"
          :lead "<div>Hupp</div>\n<div class=\"paragraph\">\n<p>Mer, mer, mer!</p>\n</div>"
          :aside "<div class=\"paragraph\">\n<p>Punkt 1</p>\n</div>\n<div class=\"sect2\">\n<h3 id=\"_heading\">Heading</h3>\n<div class=\"paragraph\">\n<p>Punkt 2</p>\n</div>\n</div>"})
