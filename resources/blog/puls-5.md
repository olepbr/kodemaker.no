:title Kodemaker PULS #5
:published 2016-02-01
:author august
:body

Hele PULS-prosjektet holdt på å gå dukken, da Firefox plutslig fjernet tab groups-funksjonaliteten som jeg er avhengig av for å ha alle i Kodemaker sin github-side liggende klar når jeg skal skrive PULS. Tror ikke jeg hadde orket å hver eneste gang bruke de 5 minuttene det tar å finne frem disse. Heldigvis reddet jeg dem med en addon.

<img src="/photos/blog/puls-5-tab-groups.png" width="690" height="87">

Dessuten har mangelen på et draft-system i Discourse *[red.anm: PULS publiseres først internt i Discourse-forumet vårt]* gjort at jeg bruker icloud.com til å skrive notater, som med jevne mellomrom kræsjer hele Firefoxen min. Er sikkert minst to ting jeg potensielt ikke har fått med meg denne måneden.

Veien til mellomleder-nivå er tung, men det er i motbakke det går oppover, som vi mellomledere liker å si.

**Christin tar over som Thought Leader**
Magnar må desverre stige ned fra tronen som *Kodemaker Resident Thought Leader*. Jeg har nettopp vært på telefon med ham mange timer, og klart å overbevise ham om at livet fortsatt er verdt å leve. Jeg vet at Magnar satt  veldig pris på tittelen Thought Leader.

Følgende tweet linker til [Christin sin bloggpost](http://kranglefant.tumblr.com/post/136874082265/tear-down-this-wall), og er skrevet av en hot shot, og havnet i min twitter-tidslinje via en retweet fra en annen hot shot (Martin Thompson, Disruptor, Aeron, ...). Den kan skilte med hele 134 retweets, som er en hel størrelsesorden mere enn Magnar fikk (82) på [sin tweet om future proofing](https://twitter.com/magnars/status/666961875683405824).

https://twitter.com/jessitron/status/685835979148533760

[Nailed it](https://twitter.com/slipset/status/685573904090447872)!

Det blir spennende å følge med på hva Magnar gjør for å eventuelt ta tilbake tronen. Magnar er allerede på god vei ved å ha fått [prone](https://github.com/magnars/prone) (sammen med Christian) og [clj-refactor](https://github.com/clojure-emacs/clj-refactor.el) inn på [en tech radar](https://juxt.pro/radar.html).

**Open source-bidrag**

Alf driver med et eller annet mystisk. Han har laget et prosjekt som heter "bam" som ser ut til å ha som mål å få [`true` til å være det samme som `false`](https://github.com/aesolbakken/bam/blob/c9d6c58c19ae5c69853ef91bb82f6e30e3217a40/src/test/groovy/bam/BokSpec.groovy). Kan man sparke inn dører, klarer man sikkert  det og!

Christian sin rolle i Sinon er omtrent som en romersk keiser på gladiator-arenaen. [En enkel thumbs up eller thumbs down](https://github.com/sinonjs/lolex/pull/34#issuecomment-169926357) bestemmer utfallet av andres blodslit. Noen ganger greier han også ut, og deklarerer ["there should be no singleton"](https://github.com/sinonjs/sinon/pull/936#issuecomment-172175661).

Christian fortsetter også å [flikke på spasm](https://github.com/cjohansen/spasm). Virker som flere og flere er enige om at React er en plattform, og at det absolutt gir mening at teams rundt om kring lager sine egne små struktur-lag på toppen, bestående av noen få hundre linjer.

Fredrik og Stein Tore lever i en fantastisk symbiose og har begynt å sende [pull requests på hverandres smarthus](https://github.com/steintore/openhabTouchPanel/pull/1). Slå den! Stein Tore [fortsetter også utviklingen](https://github.com/steintore/openhabTouchPanel) på touch-panelet til smarthuset sitt.

Stian gjør et par [kosmetiske tweaks på twittosocial](https://github.com/stiancor/twittosocial), som basert på navnet ser ut til å være et prosjekt som endelig skal gjøre Twitter litt sosialt. Unique selling point? Han sitter også, akkurat nå mens jeg skriver denne utgavn ag PULS, og pusher til booking-systemet [bookit](https://github.com/stiancor/bookit).

Magnus har fyrt av ny release av [asciidoctor for lighttable](https://twitter.com/mrundberget/status/684987598276816896), [elm-pluginen til lighttable](https://twitter.com/mrundberget/status/685179009370615812), og [rewrite-cljs](https://twitter.com/mrundberget/status/689712581108469760). Ser også ut til at å få repl-connections til å dø skikkelig i Lighttable krever at man har tunga rett i munnen, men [det har Magnus](https://github.com/LightTable/Clojure/pull/74).

August har vært innom Buster.JS igjen denne måneden, og [fikset](https://github.com/busterjs/buster-docs/commit/26bd025ab100982441f2393016f5ed82642b140e) noen overdrevne styrker som var tilstede i dokumentasjonen. Han (Han? Jeg? Tredjeperson, ass...) har også lagt ned [js-epub](https://github.com/augustl/js-epub), [js-unzip](https://github.com/augustl/js-unzip) og [js-inflate](https://github.com/augustl/js-inflate) - dette var laget for en epub-leser-POC, og tilsvarende biblioteker fantes ikke den gangen. Men nå finnes det gode alternativer, og da er det ikke noe vits å kaste bort folks tid på biblioteker som ikke vedlikeholdes. Det ble også gjort en release av [ruby-prowl](https://github.com/augustl/ruby-prowl), en liten wrapper-gem som fortsatt ser ut til å være i bruk. Til sist, asset-pipeline har fått [påbegynt servlets til bruk helt stand-alone](https://github.com/bertramdev/asset-pipeline/pull/70).

Finn fortsetter å jobbe med [jme4j-jdyn4j-4j](https://github.com/finnjohnsen/jme-dyn4j), og har også tweaket litt på [POC-en](https://github.com/finnjohnsen/remindwear) for at konduktørene til NSB skal få risting i klokka si når toget skal ha avgang.

Magnar har [merget et pull request (og releaset) s.el](https://github.com/magnars/s.el/pull/87), fikset noen småting i [optimus-angular](https://github.com/magnars/optimus-angular) (skulle nesten tro Magnar var på Angular-prosjekt, det skulle tatt seg ut!), [fått inn](https://github.com/clojure/clojure-site/pull/35) parens of the dead på clojure.org, fikset context path-problem (juhuu) og en haug andre ting i [prone](https://github.com/magnars/prone), gjort [første endring siden i fjor høst](https://github.com/magnars/optimus/pull/49) på Optimus, og møtt døve ører i quiescent, en React-wrapper for ClojureScript, i pull request [#53](https://github.com/levand/quiescent/issues/53) og [#54](https://github.com/levand/quiescent/issues/54).

Magnar har også kommet med et helt nytt prosjekt, [stubadub](https://github.com/magnars/stubadub). Sjekk README-en, hvor det kommer frem at Magnar er en slags pragmatisk masochist. På Twitter kalles det også for [wise words](https://twitter.com/philjackson/status/684292187052490753).

Kristoffer jobber som kjent på Dechman, og hans output kan [stalkes i detalj på Github](https://github.com/digibib/ls.ext), da prosjektet han jobber på er open source.

Odin har [fått merget en pull request](https://github.com/comoyo/clj-oauth2/pull/2) i clj-oauth2, og dermed gjort det mulig å bestemme hva som skal skje når noen forsøker å gjøre noe de ikke får lov til.

Ronny har fått merget et par pull requests ([#7](https://github.com/sdkman/sdkman-extensions/pull/7), [#8](https://github.com/sdkman/sdkman-extensions/pull/8)) til sdkman, pakkesystemet til Groovy-verden. Nå kan Ronny definitivt putte shellscript på CV-en! Et par mindre tweaks ([#54](https://github.com/grails/grails-profile-repository/pull/54), [#56](https://github.com/grails/grails-profile-repository/pull/56)) ble også gjort på default genererte Grails-apper.

**Bloggposter**

Magnus fortsetter bloggserien sin om å kombinere det beste fra to verdener - kategoriteori og CRUD - og har publisert [del 2](http://rundis.github.io/blog/2016/haskel_elm_spa_part2.html) og [del 3](http://rundis.github.io/blog/2016/haskel_elm_spa_part3.html).

August [blogger om Datomic](http://augustl.com/blog/2016/datomic_the_most_innovative_db_youve_never_heard_of/). Dette er en slags variant av lyntalen om samme emne som kommer på Software 2016 senere denne måneden.

Eivind [blogger](http://eivindw.github.io/2016/01/08/comparing-gc-collectors.html) om de ulike GC-valgene man har på JVM-en, med [tilhørende kodeeksempel](https://github.com/eivindw/mem-gc-test).