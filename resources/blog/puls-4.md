:title Kodemaker PULS #4
:published 2016-01-06
:author august
:body

Det er tid for den tradisjonelle jule-PULSa, som tradisjon tro lanseres 13. dag jul.

Tidligere har jeg hatt en draft liggende i Gmail som jeg skriver notater i. Discourse *[red.anm: PULS publiseres først internt i Discourse-forumet vårt]* har et rotete draft-system jeg ikke skjønner bæra av, så denne måneden har jeg ikke skrevet noen notater. Tl,dr: har jeg glømt noe, er det 100% teknologien sin feil, og 0% August sin feil.

**Magnar, Thought Leader**

Phil Jackson, en fyr på Twitter med runde briller, [minner oss på at Magnar er en vis mann](https://twitter.com/philjackson/status/684292187052490753).

Magnar minner oss også på dette selv, i [16 minutter om Pure Functions](http://www.kodemaker.no/16-minutter-om-pure-functions/). Kult å lage sånne videoer, low-tech og bare fokus på bra innhold.

**Christin**

Christin har blitt Kodemaker, og tabber seg skikkelig ut ved å [kalle oss "friendly" på Twitter](https://twitter.com/ChristinGorman/status/677498977563058176)! Ha ha.

Men, kult! Og [Erik Assum er misunnelig](https://twitter.com/slipset/status/677872745368199168).

**Christian på podcast-video-ting**

Christian ble invitert til å [snakke om testing](https://www.youtube.com/watch?v=tfkUN8Jr9zY&feature=youtu.be&t=2092) på en podcast som ser ut til å være en Google Hangout? Christian demonstrerer at selv om du skriver bok om TDD og har skjegg, kan tidssoner fortsatt være en utfordring.

**Open source-bidrag**

Her er det skikkelig mye greier! Kom gjerne med innspill på om du setter pris på denne delen, som i bunn og grunn er en oppsummering av folks Github-profiler, og er ganske tidkrevende å sette sammen.

Eivind [committer til et spennende repo](https://github.com/eivindw/eivindw.github.io) som ser ut til å være [en blogg](http://eivindw.github.io/) som jeg tipper ikke er langt unna  å bli lansert.

Desverre møter Eivind en øredøvende stillhet når han [fikser en bug i spring-data-jdbc-ext](https://github.com/spring-projects/spring-data-jdbc-ext/pull/25).

Christian lager spasm, som er en bitteliten sak som ligger på toppen av React og hjelper til med routing og state-håndtering av single page apps. Issue [#2](https://github.com/cjohansen/spasm/issues/2), [#4](https://github.com/cjohansen/spasm/issues/4) og [#5](https://github.com/cjohansen/spasm/issues/5) har blitt fikset og/eller lukket. Jeg antar at Torstein Bjørnstad som melder inn issues er noen han jobber med på NRK? Ikke at jeg ikke har noen tro på at andre enn kolleger er interessert i å bidra til Christian sine prosjekter, altså.

Christian [bidrar](https://github.com/airbnb/enzyme/issues/43#issuecomment-161968155) også til at AirBnB sitt bibliotek "reagent" får nytt navn, "enzyme", siden det kræsjer med et eksisterende prosjekt. Maintaineren sier "please do not offer simple "+1s", as these add no value." så konklusjonen må derfor bli at Christian sin kommentar "adds value" da den ikke ble slettet.

Dessuten har Christian [sørget for at det er en mindre typo på internett](https://github.com/elastic/elasticsearch/pull/15304) nå enn det var før.

Ronny [minner oss på](https://github.com/uberall/grails-asset-autoprefixer/issues/3) at det fortsatt er lett å glømme å skrive `git push`. Han [observerer også snodige feilmeldinger](https://github.com/bertramdev/grails-asset-pipeline/issues/323) i Grails sin asset-pipeline, og som vanlig med snodige feilmeldinger konkluderes det med "The only thing i could think of", "I doubt the latter so i bet your good." og "Hope so". Alltid kjedelig med issues som ikke kan reproduseres. Til slutt har Ronny også [eksperimentert](https://github.com/rlovtangen/profilerefresh) med hvordan en standard Grails-app burde se ut etter du har kjørt "grails create-app".

Finn jobber med [en POC for Android Wear](https://github.com/finnjohnsen/remindwear). IT-sjef for NSB Trafikk IKT hadde noen idéer til noe Finn ikke har noe særlig kompetanse på, nemlig Android Wear, og Finn lærer seg derfor dette på fritiden. Kult, det!

Magnar har som vanlig gjort sinnsykt mye greier. Jeg velger å være lat denne måneden, sjekk sjæl! https://github.com/magnars?tab=activity. Den største nyheten er nok release av [prone](https://github.com/magnars/prone) 0.5.0, med f.eks ganske så sexy håndtering av ajax-requests.

Odin [leker med Kafka](https://github.com/Odinodin/kafka-fn)! Virker som en veldig lite Kafkask prosess (badam-pish). Sikkert lurt også, Kafka virker som det kan løse en hel haug med problemer. I tillegg har Odin [gjort det mulig å logge ut](https://github.com/comoyo/clj-oauth2/pull/1) dersom du bruker clj-oauth2.

August liker å snakke om seg selv i tredjeperson, og nevner at en [bug i ClojureScript ble meldt in](http://dev.clojure.org/jira/browse/CLJS-1524)n. Value semantics er det viktigste med values, men ClojureScript leverte ikke varene. Men [det ble fikset på null komma svisj](https://github.com/clojure/clojurescript/commit/15f9bbe59b47312f451b90972864a37a1c2246d2), også takket være Magnar sitt eminente bidrag med Clojure-eksempelet som demonstrerer bugen. Denne buggen ble forøvrig oppdaget ved bruk av [Mori](http://swannodette.github.io/mori/), som er en ren JS-versjon av ClojureScript sine datastrukturer.

Kjetil tweaker [dotfiles](https://github.com/akafred/dotfiles)!

Magnus [knuser PNG-er](https://github.com/LightTable/lighttable.com/pull/13), men møter desverre en del stillet ([halive #12](https://github.com/lukexi/halive/issues/12), [elm-make #73](https://github.com/elm-lang/elm-make/issues/73)) i issue-trackeren.

Stein Tore [forsøker](https://github.com/steintore/ansible_openhab_linux/commit/f069245979d7981cab5110cd9df785e845a1f7c5) å få smarthuset sitt til å holde litt bedre på varmen i vinterkulda.

Stian [jobber videre med booking-systemet](https://github.com/stiancor/bookit) bygget på Datomic og React.

**Bloggposter**

Stig [blogger](http://stigmelling.github.io/blog/blog/2015/min-blog.html)! Stig [forteller](http://stigmelling.github.io/blog/blog/2015/nmultra.html) b.la. at hvis du trenger et kick, er orientering et godt alternativ. Her fortelles det i detalj om systemet Stig har laget for å spore et løp mens det pågår, med GPS-tracking og det hele. I gamle dager måtte man sette bjelle på løperne, men det slo ikke helt an.

Magnus [demonstrerer](http://rundis.github.io/blog/2015/haskell_elm_spa_part1.html) at funksjonelle språk ikke er begrenset til trauste ting som [spam-håndtering hos Facebook](https://code.facebook.com/posts/745068642270222/fighting-spam-with-haskell/), det kan også brukes til ting som betyr noe for oss industri-utviklere: CRUD! Her er det Haskell på backend, og Elm på front-end.

Hvis man kunne tenke seg å høre Magnus snakke litt engelsk og få en kjapp bekreftelse på at navnet hans faktisk *er* Magnus, [har man muligheten her](http://rundis.github.io/blog/2016/elm_light_package.html). I tillegg får man se hva slags features Magnus har jobbet med i det siste i elm-light.

Hadde vært kult om Magnus får Haskell- eller Elm-oppdrag ved neste korsvei. Det er i alle fall ikke synligheten det skal stå på!

P.S, hvis du lurer på hva "Elm" er, har vi her et bilde av en finfin variant av [Elm](https://en.wikipedia.org/wiki/Elm), nemlig gode gamle Ulmus minor:

<img src="/photos/blog/puls-4-ulmus-minor.jpg" width="220" height="275">