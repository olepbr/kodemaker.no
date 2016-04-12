:title Kodemaker PULS #3
:published 2015-12-02
:author august
:body

I serien "August forsøker å bli mellomleder" fortsetter jeg å produsere PULS. Jeg tegner organisasjonskartet opp-ned. Jeg er her for å gjøre deres jobb enklere å gjennomføre. Fasilitere produktivitet. Spolsky style, baby!  Jeg siterer Ed Catmull:

> We start  from the presumption that our people are talented and want to contribute. We accept that, without meaning to, our company is stifling that talent in myriad unseen ways. Finally, we try to identify those impediments and fix them

Nok floskler for denne gang, over til PULS #3!

**Sinon.JS 2.0.0 preview ute**

Når noen [lanserer et nytt mocke-rammeverk til JavaScript](https://twitter.com/searls/status/671366116502069248), så finner de det naturlig å si "try using it in place of Sinon.JS". Det er stilig, Sinon.JS har lisso blitt standarden.

Som nevnt i tidligere utgaver, er Sinon.JS laget av Christian. Hvis jeg ikke husker feil, ble det laget i forbindelse med boka hans, da det ikke fantes noe brukbart mocke-opplegg for JavaScript på den tiden. Og Sinon.JS er og blir *the shit*. Som også tidligere nevnt, har Sinon.JS fått bein å gå på uten at Christian er noe særlig innvolvert lengere. I [denne tweeten](https://twitter.com/mantoni/status/671953733333446656) ser vi at det er to personer innvolvert, tweeteren selv og @kopseng, men @cjno er ikke med. Kult at andre folk synes Sinon.JS er verdt å bruke tia si på og ta over vedlikehold og utvikling av!

**Kodemaker-rekord i antall rewteets**

Magnar [fortsetter sin jobb som Kodemaker Resident Thought Leader](https://twitter.com/magnars/status/666961875683405824) og ender opp med hele 81 retweets på sine tanker om future proofing. Noen som husker hva Kodemaker sin retweet-bonus ligger på?

**Christian snakker om ES6 på Framsia**

Selv med et såpass rett frem emne som ES6, forteller folk at Christian sitt foredrag var [rather amusing](https://twitter.com/jenschr/status/669587478970548224).

**Buster.JS**

Det er på mystisk vis fortsatt noen der ute som [viser interesse for Buster.JS](https://github.com/busterjs/buster/issues/462). Også litt aktivitet i IRC-kanalen for tiden. Jeg vil jo si at Buster fortsatt har en del fete ting som ingen andre har, og usabilityen til Buster er veldig høy. Men når begge de opprinnlige utviklerne har gitt opp, og det ikke har vært noe særlig moment på mange år, er det vel på tide å ta hintet og kalle det ABANDONWARE? Tydligvis ikke!

**Parens of the Dead, ep8**

S01e08 ligger ute, og med det er Magnar ferdig med sesong 1! http://www.parens-of-the-dead.com/e8.html

**Open source-bidrag**

Alf Kristian fortsetter i samme ånd som forrige måned og pusher grensen på hvor liten en filleting kan være. Men noen må jo [gjøre det](https://github.com/http4s/http4s/pull/444/files)?

Finn [jobber iherdig med jme-dyn4j](https://github.com/finnjohnsen/jme-dyn4j). Sexy README, ta en titt davel! jme-dyn4j er Finn sin integrering av 2d-fysikkmotoren dyn4j i 3d- og 2d-spillbiblioteket jMonkeyEngine. Usikker på hvorfor Finn ikke heller kalte prosjektet sitt for dyn4j4jme. Det er ikke for sent, Finn!

Christian rapporterte inn et par bugs, [den ene ble fikset samme dag](https://github.com/mantoni/mochify.js/issues/120), mens [den andre er åpen og ubesvart](https://github.com/npm/npm/issues/10316), til tross for enkel reproduserbarhet og generelt ryddig bugrapport. Christian burde kanskje få vunnet seg en Emmy eller noe, spellemannsprisen har tydligvis ikke stor nok effekt i det internasjonale utviklermiljøet. Han har også [koden for connect-four](https://github.com/cjohansen/connect-four) og [minesweeper-ui](https://github.com/cjohansen/minesweeper-ui/commits?author=cjohansen) som jeg antar at er det som ble demonstrert på Framsia når han snakket der i midten av november. Dette er forøvrig ting han har brukt som eksempler på mange av presentasjonene sine, inkl. JavaZone tidligere i år.

Kjetil [oppdaterer sitt lurhus](https://github.com/akafred/lurhus) med en monstercommit med opprydding og snacks. Hans [dotfiles](https://github.com/akafred/dotfiles) har også spor etter seg av Kotlin-arbeider på Geekend.

August [installerte Discourse denne måneden](https://www.youtube.com/watch?v=dQw4w9WgXcQ), og benyttet anledningen til [å klage og sutre litt](https://meta.discourse.org/t/script-error-on-account-confirmation-page-in-self-hosted-install/36012). Intenting Funket (tm) etter installasjon, som følge av en god gammeldags tabbe som heldigvis ble fikset bare få minutter etter den ble rapportert inn.

Magnar: samma som forrige måned! Som vil si en hel bråte med commits spredt over ymse prosjekter, denne måneden string-edit.el, annoying-arrows-mode.el, hardcore-mode.el, buster-snippets.el og clj-refactor.el. Ser ut som Magnar bruker Emacs, men har ikke spurt. Issues er også rapportert inn, liker særlig [denne](https://github.com/clojure-emacs/refactor-nrepl/issues/127) hvor Magnar tydligvis var veldig klar og tydlig i bugrapporten sin siden den har fyrt av en hel masse diskusjon uten at Magnar har trengt å komme med ytterligere forklaringer. Det er bra, for han trengte å veie opp for [denne](https://github.com/clojure-emacs/clj-refactor.el/issues/263).

Magnus gjør som alle andre i Kodemaker med fornavn på M ser ut til å gjøre: en hel masse teksteditor-greier! [ltplanck](https://github.com/rundis/ltplanck) er Light Table-plugin for [planck](http://planck.fikesfarm.com/). [Forbedringer til Light Table-kjernen](https://github.com/LightTable/LightTable/pull/2042). Og [en hel del endringer på Elm-pluginen](https://github.com/rundis/elm-light). Dessuten er Magnus tungt inne i Elm for tiden, og har åpnet en bråte med issues, noen også relatert til hans Light Table-plugin ([elm-repl #95](https://github.com/elm-lang/elm-repl/issues/95), [elm-reactor #166](https://github.com/elm-lang/elm-reactor/issues/166), [elm-package #169](https://github.com/elm-lang/elm-package/issues/169), [elm-reactor #152](https://github.com/elm-lang/elm-reactor/issues/152), [elm-format #65](https://github.com/avh4/elm-format/issues/65) og [package.elm-lang.org #137](https://github.com/elm-lang/package.elm-lang.org/issues/137)).

Stian har laget [noe mystiske greier med navnet bookit](https://github.com/stiancor/bookit). Sikkert noe styreleder-greier. PULS-forfatter benytter anledningen til å klage på tynn README. No wireless. Less space than a Nomad. Lame.

Stein Tore jobber med [et fjongt touch-panel](https://github.com/steintore/openhabTouchPanel) til smarthuset sitt.

Kristoffer gjør fortsatt open source på jobb, du kan stalke ham og lese koden han skriver her: https://github.com/digibib/ls.ext/commits?author=kristom

Odin har lagt ut [threedee-fn](https://github.com/Odinodin/threedee-fn), som er kildekoden til bloggposten nevnt nedenfor.

Ronny [roper ut i det store intet](https://github.com/arialdomartini/oh-my-git/issues/89) og får ikke svar på bugrapporten sin. Dårlig gjort, for det var jo en helt fin og ryddig bugrapport!

**Bloggposter**

Magnus [blogger om å lage Minesveiper i Elm](http://rundis.github.io/blog/2015/elm_sweeper.html), inspirert av Christian sin React-utgave. Edwin Brady kaller språket hans Idris for "pacman complete" - du kan implementere Pacman i språket. Kanskje det nye begrepet blir "Minesweeper Complete"?

Odin skriver om [printing med React](https://www.youtube.com/watch?v=dQw4w9WgXcQ) og [3D-tegning med ClojureScript](http://odinodin.no/2015-11-14-three-cljs/).