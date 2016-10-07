:title Kodemaker PULS #9
:published 2016-10-07
:author august
:body

Det er allerede ett år siden PULS #1, og vi feirer dette med å lansere utgave #9 av det månedlige nyhetsbrevet vårt.

Den tradisjonelle sommerpausen til PULS er dermed over, og vi fortsetter som før med _nesten helt nøyaktig_ månedlige publikasjoner. Alle som er utviklere vet hvor vansklig det kan være å jobbe med datoer og tidssoner og sånn, og har full forståelse for at å treffe nøyaktig på måneden kan være vanskelig/umulig.

**Kodemaker på JavaZone**

Kodemaker var så fornøyde med fjorårets stand at vi valgte å bruke den om igjen i år. Det var vondt å skrote de titalls andre gode idéene vi selvfølgelig hadde, for vi ville aldri funnet på å vente helt til siste liten. Men vi syntes "Kodemaker er for kode det Carlsberg er for øl", slik enkelte øl-interesserte JZ-deltagere som besøkte standen vår kommenterte, klinger såpass godt at det ble vansklig å velge den bort.

Det ble holdt foredrag:

* Odin: [3 grunner til at ClojureScript gir deg superkrefter](https://2016.javazone.no/program/3-grunner-til-at-clojure-script-gir-deg-superkrefter)
* Magnus: [Elm - Awesomesauce or just yet another language for the Front-end?](https://2016.javazone.no/program/elm-awesomesauce-or-just-yet-another-language-for-the-front-end)
* Christin/Eivind: [5 approaches to concurrency in Java - which one is the best?](https://2016.javazone.no/program/5-approaches-to-concurrency-in-java-which-one-is-the-best) ([Demoer mm. på Github](https://github.com/ChristinGorman/javazone2016))

**Magnus snakker på flatMap**

<img src="/photos/blog/puls-9-magnus-flatmap.png" width="690" height="318">

flatMap har utviklet seg til å bli "typekonferansen" i Oslo, og Magnus [snakker om Elm](http://2016.flatmap.no/rundberget.html) ([video](https://vimeo.com/165928866)). Slides og kode er [på Github](https://github.com/rundis/elm-flatmap2016). Heldigvis var FRP bare en av mange ting Magnus var innom, da det viste seg at Elm-teamet bare noen dager etter Magnus sin talk fant ut at [FRP skulle ut](http://elm-lang.org/blog/farewell-to-frp)


**Kjetil snakker på DevOpsDays 2016**

<img src="/photos/blog/puls-9-kjetil-devopsdays.png" width="690" height="497">

Kjetil benytter anledningen til å fortelle om ["How to kill DevOps in 5 minutes"](https://youtu.be/BTTqGoLdRJM?t=8192). (Linken skal ta deg rett til Kjetil sin presentasjon på 2:16:32)

Det er faktisk ikke lyden som er ute av sync med videoen, Kjetil er bare en glimrende buktaler.

**Magnar og Christian sitt Clojure-bibliotek "prone" anses som "worth mentioning"**

I en Github-analyse av hvilke Clojure-prosjekter som er hyppigst dependet på i open source-land, finner vi at Magnar og Christian sin "prone" akkurat klarer å havne innenfor beste 2/3, på en 64-plass. Hyggelig nok nevnes "prone" som "a few more top 100 entries worth mentioning".

http://blog.takipi.com/the-top-100-clojure-libraries-in-2016-after-analyzing-30000-dependencies/

**Eivind og Christin snakker om Concurrency**

Eller: Eivind om snakker Christin. og Concurrency.

Eivind og Christin snakket om dette på JavaZone, som nevnt i eget avsnitt. Men det ble også [holdt en slags preview-variant på javaBin](http://www.meetup.com/javaBin/events/231265930/).

I bildet under ser vi et glimrende eksempel på concurrency: Eivind er både smørblid og holder mikrofon på en gang.

<img src="/photos/blog/puls-9-christin-eivind-concurrency.jpg" width="666" height="500">

**Christin krangler på internett, på den _fine_ måten**

Christin er krangle-ekspert, og viser at det går helt fint an å slippe unna med å være kranglete på internett - bare sørg for at det er en tredjepart som publiserer deg!

Christin leverer, sammen med Geir Amsjø i Lean Venture, [et innlegg på digi.no](http://www.digi.no/artikler/brsys-en-paminnelse-om-et-utdatert-anskaffelsesregime/349759) som svarer på sjefen til brreg [sine uttalelser](http://www.digi.no/artikler/bronnoysundregistrene-er-en-sarbar-spagetti-ma-bygge-nytt-for-1-2-milliarder/349633) om at brreg må skrives om til en prislapp på 1.2 mrd NOK. Ikke nok med det, sjefen i brreg [svarer også](http://www.digi.no/artikler/ny-registerplattform-er-avgjorende-for-sikkerheten-til-bronnoysundregistrene/349782#cxrecs_s) på innlegget til Christin og Geir.

**Open source-bidrag**

Stig kan skilte med [sitt første pull request](https://github.com/graemerocher/gdoc-to-asciidoc/pull/1) på Github! Merget og greier. Stig sørget for at Grails sin asciidoc-snurre spytter ut bilder/grafikk som den skal. Stig har også [laget en mapdemo](https://github.com/stigmelling/mapdemo). Ser ut som en slags demo over maps. Usikker på nøyaktig hva den gjør, siden Stig ikke har orientert meg om dette. (Den setningen der ble rasende festlig, i og med at Stig driver aktivt med idretten orientering).

Alf Kristian har fått [økt kompetanse](https://github.com/nathanmarz/specter/issues/110) i Github-søk, og åpnet et par pull requests. [Det ene](https://github.com/laurentj/slimerjs/issues/495) viser at unit tests ikke er garantert å avdekke 100% av alle problemer som kan oppstå, mens den andre viser at [selv om man sier](https://github.com/bensu/doo/pull/104) "versågod, ta denne awesome koden", risikere man å møte på open source SLA-en "døve ører".

Magnus er som vanlig busy. Han hjelper med å reprodusere bugs i elm-hot-loader ([#15](https://github.com/fluxxu/elm-hot-loader/issues/15)), peker ut "off by one"-dilldall (lt-jshint [#2](https://github.com/bbbates/lt-jshint/issues/2)), foreslår tweaks i lt-lint ([#7](https://github.com/bbbates/lt-lint/issues/7), [#8](https://github.com/bbbates/lt-lint/issues/8)), fikser så Lighttable kan rendre inline-docs i markdown-format ([PR #2250](https://github.com/LightTable/LightTable/issues/2250)), og sist men ikke minst bygger broer mellom Atom og Lightable sine elm-plugins (atom-eljutsu [#14](https://github.com/halohalospecial/atom-elmjutsu/issues/14)). Elm core team demonstrerer også at de liker å holde kortene tett til brystet (elm-test [#9](https://github.com/elm-community/elm-test/issues/9), [#10](https://github.com/elm-community/elm-test/issues/10)). Et spennende teaser-repo er å finne på Magnus sin Github: [euro-2016-predictions](https://github.com/rundis/euro-2016-predictions). Magnus har obfuskert koden sin med statisk typing, som en stakkars dynamic-head som meg ikke klarer å lese, så jeg tar ikke sjansen på å komme med noen predictions på hva dette prosjektet går ut på.

En generell observasjon her er at Magnus har mange lave issue-tall, som tyder på at han tar i et tak og hjelper til med å få i gang unge prosjekter. Bra!

Magnus har også fått til noen releaser siden sist: [elm-lighttable](https://github.com/rundis/elm-light) 0.4.2 er ute, og 0.4 er endret nok til at han har [blogget om hva som er nytt](http://rundis.github.io/blog/2016/elm_light_ast.html). Også [ rewrite-cljs](https://github.com/rundis/rewrite-cljs) har tilfeldigvis (eller?) også havnet på versjon 0.4.2 i skrivende stund. Bra timing!

Trygve har satt seg opp en aldri så liten [react-redux-starter](https://github.com/trygvea/react-redux-starter).

Kjetil hjelper til med å fikse ZWave-integrasjonen i OpenHab ([#4345](https://github.com/openhab/openhab/issues/4345)), ordner opp i Homebrew sin Vitamin-R-pakke ([#21988](https://github.com/caskroom/homebrew-cask/pull/21988)) og rydder opp en helg haug i [dotfiles-ene sine](https://github.com/akafred/dotfiles).

Christian har lagt ut [loose-server](https://github.com/cjohansen/loose-server) og [loose-client](https://github.com/cjohansen/loose-client), som ser ut til å være et demo-oppsett hvor en chat-applikasjon lages ved bruk av ES2016.

August oppdaterer bloggen sin med ["add annoying page title"](https://github.com/augustl/augustl.com/commit/51ad2d36e86538cbc05dcfabb87f56bc31f0b705) og ["make everything considerably more ugly"](https://github.com/augustl/augustl.com/commit/bd404f7e609ddcff64f893a406906da47625ef29). Websiden har nå endt opp med å være [en slags blanding av brutalisme og postmodernisme](http://augustl.com).

Stian fortseter kampanjen å gjøre Twitter sosialt ([twittosocial](https://github.com/stiancor/twittosocial)). Vi får bl.a. ta del i[ gledene ved implisitt logikk](https://github.com/stiancor/twittosocial/commit/4c1d66b87791d4ec9c16af0fd6c828ce93cc06a0).

Ronny sliter med kosmetiske feil i grails-cache-ehcache og tar i et tak ([#23](https://github.com/grails-plugins/grails-cache-ehcache/pull/23), [#25](https://github.com/grails-plugins/grails-cache-ehcache/issues/25), [#27](https://github.com/grails-plugins/grails-cache-ehcache/issues/27)), fikser en dokumentasjonsbug i jQuery UI ([#151](https://github.com/jquery/jqueryui.com/pull/151)) og Geb ([#17](https://github.com/geb/geb-example-grails/pull/17))

Odin [kvitter seg](https://github.com/Odinodin/cashflow/commit/7aeb3e033fd1d8e27d94d20808868ca152e59619) med kommentarer á la `/* Add numbers */ const addTwoNumbers = (a,b) => a+b`, og fikser opp i lein-git-info-edn ([#1](https://github.com/noisesmith/lein-git-info-edn/issues/1), [#2](https://github.com/noisesmith/lein-git-info-edn/pull/2)). Odin jobber som en helt med [data-frisk-reagent](https://github.com/Odinodin/data-frisk-reagent) og ser ut til å være fornøyd etter [even more tweaking](https://github.com/Odinodin/data-frisk-reagent/commit/b39870b35b5c5292c040c1a3df536b00e5ab78cd). Dessuten har han tatt imot pull requests og issues ([#1](https://github.com/Odinodin/data-frisk-reagent/issues/1), [#4](https://github.com/Odinodin/data-frisk-reagent/pull/4), [#5](https://github.com/Odinodin/data-frisk-reagent/pull/5)) fra _ekte_ amerikanere, så dette begynner etter hvert å se ut som et ganske levende prosjekt.

Anders [tar imot pull-request fra Magnus](https://github.com/andersfurseth/rubberlike/pull/4), og sier for over en måned siden at "I'll cut a release within a few days". Dokumentasjonsbug i lein-s3-repo fikses også ([#1](https://github.com/briprowe/lein-s3-repo/pull/1)).

Selveste Kristian gjør også litt open source på jobb og deler Animalia sin [kafa-consumer](https://github.com/animalia/kafka-consumer), samt ordner det slik at jOOλ får klassen `Sneaky` ([#260](https://github.com/jOOQ/jOOL/issues/260)).

Magnar ordner React 15 i quiescent ([#58](https://github.com/levand/quiescent/pull/58)), tenker høyt om støtte for Cache-Control immutable i optimus ([#53](https://github.com/magnars/optimus/issues/53)), dabbler med edge caser i Cordova ([#1018](https://github.com/katzer/cordova-plugin-local-notifications/issues/1018)), bæsjer på leggen (js2-refactor.el [#68](https://github.com/magnars/js2-refactor.el/issues/68)), og fikser og merger en helg haug med ting i [cljs-styles](https://github.com/magnars/cljs-styles), [s.el](https://github.com/magnars/s.el), [stubadub](https://github.com/magnars/stubadub), [tagedit](https://github.com/magnars/tagedit), [multiple-cursors.el](https://github.com/magnars/multiple-cursors.el), [expand-region.el](https://github.com/magnars/expand-region.el), [stasis](https://github.com/magnars/stasis), [prone](https://github.com/magnars/prone) og [dash.el](https://github.com/magnars/dash.el). Et nytt prosjekt er osgå lansert, [optimus-autoprefixer](https://github.com/magnars/optimus-autoprefixer), for å legge på vendor prefixer automatisk i CSS-en din.

Kristoffer gjør fortsatt open source på jobb i [digibib/ls.ext](https://github.com/digibib/ls.ext), og vi koser oss med å lære om bibliotek-domenespråk som [katalogisator](https://github.com/digibib/ls.ext/issues/46).

**Blogginnlegg**

Kjetil blogger om Git, og gir oss en oversikt over ting som kan være greit å få med seg dersom man ikke har brukt Git så mye før. http://www.akafred.com/2016/05/git-introduction-part-1/

Magnus fortsetter bloggserien sin med CRUD basert på Elm: http://rundis.github.io/blog/2016/haskel_elm_spa_part5.html

Magnus har også hoppet i det, og blogget om statisk vs. dynamisk: http://rundis.github.io/blog/2016/type_confused.html. Både på [Twitter](https://twitter.com/mrundberget/status/742463559116988416), [Reddit](https://www.reddit.com/r/elm/comments/4nxwns/oh_no_learning_elm_has_gotten_me_confused_about/) og [Hacker News](https://news.ycombinator.com/item?id=11897906) viser det seg at folk er både enige og uenige i ting som blir sagt om statisk vs. dynamisk.
Static/dynamic bloggpost
