:title Kodemaker PULS #8
:published 2016-05-02
:author august
:body

Litt forsinket PULS, men jeg kunne jo ikke ha drevet med slike ting på 1. mai! Av en eller annen grunn arbeider vi _ikke_ på arbeidernes dag. Jeg foreslår at vi oppretter en ny dag, hvor det f.eks ikke er noen møter og det er ikke lov til å forstyrre noen av kollegene dine, slik at vi har minst en dag i året hvor vi får jobbe helt i fred.

**Vår ærede daglige leder i Computerworld**

Kolbjørn har meninger om hvordan små selskaper passer inn ved offentlige innkjøp. Som ikke det var nok, presenteres det ikke bare meninger, men også _fakta_, i form av tall fra forskning!

http://www.cw.no/artikkel/offentlig-sektor/etterlyser-smidighet

Vi håper smått og godt blir vanligere i offentlig sektor fremover.

**Stillingsannonsen vår**

Kodemaker ansetter kontinuerlig, men noen ganger slår vi til med stillingsannonse. Annonsen ble publisert for litt over en måned siden. Vi kan avsløre at minst flere enn null personer har søkt på den så langt. Tekst av Christin og Eivind, applaus!

http://m.finn.no/job/fulltime/ad.html?finnkode=72924266

**Open source-bidrag**

Kjetil eksperimenterer med [Octave](https://www.gnu.org/software/octave/), og har både [automatisert installasjonen av det](https://github.com/akafred/dotfiles/commit/128b76466ff0c9bf94037ad0fb183696be4d1292) og [fikset på](https://github.com/adampash/adampash.github.io/pull/6) det vi kan annta er en bloggpost med høy rangering på Google-søk.

Anders lager [noe mystisk](https://github.com/andersfurseth/dialog). Det kan se ut som dette er et eksempel-oppsett av et ClojureScript-prosjekt med hot reloading i netleseren. Det er sikkert bra greier, siden ingen tid er kastet bort på en beskrivende README.

Christian [lar seg forvirre](https://github.com/ramda/ramda/issues/1720) av de ulike navne-konvensjonene man har på funksjoner som gjør akkurat det samme i ulike språk - det som heter `get-in` i Clojure, heter `path` i lodash. [Emacs-configen hans](https://github.com/cjohansen/.emacs.d) har også fått et løft, men det er ingen overraskelse, da en Emacs-bruker sin Emacs-config aldri står stille.

Stein Tore [rydder opp i](https://github.com/openhab/openhab/pull/4263) OpenHAB sin integrasjon mot Samsung-varmepumper, og den kontinuerlige smarthus-tweakingen [fortsetter](https://github.com/steintore/ansible_openhab_linux). Og [kryptert open source](https://github.com/steintore/ansible_openhab_linux/commit/118fa6805c5488448f0b151f7b3cdb97a5fe64ef) er fortsatt open source, sant?

Kristian [møter døve ører i kafka-rest](https://github.com/confluentinc/kafka-rest/issues/188), til tross et tappert forsøk.

August ser ut til å drive README-driven development i [CRUD Life](https://github.com/augustl/crud-life), for alt vi finner der i skrivende stund er dokumentasjon. Det kan spoiles at dette er et ambisiøst prosjekt som forsøker å lette på state-synkronisering mellom (micro)services.

Magnus roper ut og melder inn issue [#6](https://github.com/w0rm/elm-slice-show/issues/6), [#7](https://github.com/w0rm/elm-slice-show/issues/7), [#8](https://github.com/w0rm/elm-slice-show/issues/8) og [#9](https://github.com/w0rm/elm-slice-show/issues/9) til elm-slice-show, et slide/presentasjon-verktøy skrevet i Elm. LightTable sin groovy-plugin [pensjoneres](https://github.com/rundis/LightTable-Groovy/commit/a94fce852b02fa30e07c964cc10baecb0cee5427) også. Et dynamisk typet Java-like er kanskje ikke det mest motiverende for en som er dypt nede i et statisk Haskell-like.

Odin [får smake kjeppen](https://github.com/reagent-project/reagent/issues/228) og lærer seg å ikke komme her og komme her. Han har også lansert [data-frisk-reagent](https://github.com/Odinodin/data-frisk-reagent). Ingen README, men i følge han selv er det for visualisering av innholdet i en trestruktur. Vi stoler på at det stemmer.

Eivind er [ute med nytt prosjekt, clj-date-no](https://github.com/eivindw/clj-date-no), for å gjøre helligdag-hverdagen til Clojure-programmerere litt greiere. I ekte NPM-stil er prosjektet delt opp i mikromoduler, så Eivind har også lansert [det frittstående clj-easter-day](https://github.com/eivindw/clj-easter-day). Savner funksjonen `is-the-easter-late-or-early-this-year`, som er det alle egentlig lurer på. Eivind viser også at han er en dyktig enterprise-utvikler, og [aksepterer sitt eget pull request](https://github.com/eivindw/records/pull/1) til sitt eget prosjekt.

Magnar er på mergern denne måneden: [tagedit](https://github.com/magnars/tagedit) ([#13](https://github.com/magnars/tagedit/pull/13)), [multiple-cursors](https://github.com/magnars/multiple-cursors.el) ([#236](https://github.com/magnars/multiple-cursors.el/pull/236), [#246](https://github.com/magnars/multiple-cursors.el/pull/246)), [s.el](https://github.com/magnars/s.el) ([#91](https://github.com/magnars/s.el/pull/91), [#93](https://github.com/magnars/s.el/pull/93), [#94](https://github.com/magnars/s.el/pull/94)) og [string-edit.el](https://github.com/magnars/string-edit.el) ([#9](https://github.com/magnars/string-edit.el/pull/9)). Han har til og med gjort litt jobb selv, som å [fikse bugs i optimus](https://github.com/magnars/optimus/commit/09e5f73422891c2414156317e0facea7b03093ad) av typen som tyder på at dette er et stabiliserende og langtlevende prosjekt, og [mindre feil](https://github.com/clojure-emacs/clj-refactor.el/commit/b883082128cebd7a3f00f719b588b3da1bcac312) som tilhengere av statiske typesystem sikkert ville mistet en god del nattesøvn av å måtte oppleve. Det er også en ny unge på blokka, [clj-styles](https://github.com/magnars/cljs-styles), for å hjelpe til med vendor prefixing i ClojureScript/React.

Ronny har [ryddet opp i utdaterte URL-er dokumentasjon](https://github.com/gpc/jms/pull/20) og vært enig i at [spaces er bedre enn tabs](https://github.com/mjhugo/grails-build-info/pull/12). [Issue #21](https://github.com/gpc/jms/issues/21) er rapportert inn til Grails sin jms-plugin, og [issue #9870](https://github.com/grails/grails-core/issues/9870) i grails-core viser oss at enkelte former for Groovy goodness kreperer i Grails sitt strippede unit-test-miljø.

**Blogging**

Magnus [blogger om å håndtere tomme verdier/null i Elm](http://rundis.github.io/blog/2016/elm_maybe.html), og ikke overraskende skjer dette med statisk typesjekking.

August har riktignok ikke blogget denne måneden, men å ut av det blå få både [en](https://twitter.com/ingesol/status/717243923945623553) og [to](https://twitter.com/pesterhazy/status/721313654289915904) tweets med bloggpost-ros fra [en noen måneder gammel bloggpost](http://augustl.com/blog/2016/datomic_the_most_innovative_db_youve_never_heard_of/) er nevneverdig!
