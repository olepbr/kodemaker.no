(ns kodemaker-no.pages
  (:require [optimus.link :as link]
            [kodemaker-no.layout :refer [with-layout]]
            [kodemaker-no.formatting :refer [to-html]]
            [kodemaker-no.people :as people]))

(defn index [request]
  (with-layout request "Systemutvikling på høyt nivå"
    (list
     [:div.body.unitRight.r-2of3
      [:div.bd
       [:p "Vi er et fokusert selskap. Fokusert på kompetanse. Erfaringer fra tøffere tider har lært oss at vi må være helt i front teknologisk for å være attraktive som konsulenter. Vi setter vår ære i å holde oss oppdatert på nye teknologier og trender innen våre fagfelt. Kundene opplever oss som medspillere, og vi er flinke til å formidle og dele vår kunnskap."]
       (to-html :md "
## Teknologisk i front

Svært god teknologikunnskap er bare én brikke i det å drive vellykket
konsulentvirksomhet. Våre gode kommunikasjonsevner, evne til å
samarbeide samt at vi har et våkent øye for nye og bedre løsninger
gjør at kundene opplever oss som viktige støttespillere. Som regel
ønsker kundene å beholde våre konsulenter etter det første prosjektet,
og vi blir da ofte sentrale aktører i de neste.

I motsetning til større aktører har vi ingen intensjon om å pådytte
kundene våre egne metodikker, men setter oss i stedet i kundens
posisjon og bidrar med vår kunnskap der hvor dette er ønskelig. Som
representanter for en uavhengig aktør slipper våre konsulenter den
”tvangstrøyen” mange opplever ved å jobbe i et større konsulenthus
eller hos en leverandør. Vi tror at kundene opplever konsulenter fra
mindre selskaper som minst like dyktige og kanskje mer opptatt av
kundens problemstillinger enn sin egen arbeidsgivers prosjektpolitikk.
Vi har også samarbeidspartnere med lignende og utfyllende kompetanse.
Vi er derfor istand til påta oss større prosjekter. Totalt råder vi
over bortimot 200 konsulenter via våre allianser i inn- og utland.

## Konsulenttjenester innen systemutvikling

Våre konsulenter har lang og god erfaring med utvikling av komplekse
og virksomhetskritiske systemer. Vi bruker mye ressurser på å holde
oss oppdatert på ny teknologi innen våre fagfelt, og dette gjør at vi
har dyp kjennskap til en lang rekke teknologier og verktøy som vi
benytter for raskere å løse kundens problemer. Vi har evnen til ta i
bruk riktige teknologiske nyvinninger når tiden er moden, og en av
våre målsetninger er at våre konsulenter i størst mulig grad skal
delta i prosjekter med ’state-of-the-art’-teknologi.

Med lang erfaring og varierte oppgaver underveis har våre konsulenter
tilegnet seg kunnskap om de fleste aspekter av systemutvikling.Vi kan
fungere som prosjektledere, arktitekter og utviklere med
spesialkompetanse på alt fra brukeropplevelser til håndtering av store
transaksjonsmengder. Vi definerer oss selv som systemutviklere, men
har helt klart gode forutsetninger for å mene noe om og jobbe med alt
fra forretningsutvikling til brukerinteraksjon også.

## Språk, plattformer og utviklingsmiljøer

Vi har både små og store, men heldigvis teknologisk utfordrende
kunder. Disse kundene ønsker gjerne å være teknologisk i front, og vi
forsøker å bistå i å gjøre de riktige valgene til riktig tid. Vi
behersker mange teknologier og plattformer, og dette framkommer best
via den enkeltes CV.")]]
     [:div.aside.lastUnit
      [:div.bd
       [:p
        [:img.top-img {:src (link/file-path request "/photos/KolbjornJetne.jpg")}]
        "<br><cite><a href='http://www.kodemaker.no/mennesker/kolbjorn/'>Kolbjørn Jetne</a></cite><br>
          <q>Erfaringer fra tøffere tider har lært oss at vi må være helt i front teknologisk for å være attraktive som konsulenter. Vi setter vår ære i å holde oss oppdatert på nye teknologier og trender innen våre fagfelt. Kundene opplever oss som medspillere, og vi er flinke til å formidle og dele vår kunnskap.</q>"]]])))

(defn render-person [person]
  [:p (people/full-name person)
   [:span.title (:title person)]])

(defn all-people [request]
  (with-layout request (str (count people/consultants) " kvasse konsulenter")
    [:div.body
     [:div.bd
      (map render-person people/everyone)]]))

(def pages {"/index.html" index
            "/mennesker.html" all-people})
