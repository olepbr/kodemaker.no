:title Kodemaker PULS #6
:published 2016-03-04
:author august
:body

Jeg og Kolbjørn ble enige om at PULS var noe som gjerne måtte publiseres ut i offentligheten. Dette fører til at jeg ikke tør å være morsom på mine kollegers bekostning lengere, hverken her i introen min eller nedenfor i brødteksten. Sånn kan det gå, PULS har blitt firmabloggifisert. Herfra og ut blir alt pragmatisk, ting vil bli sett på fra begge sider, vi tar høyde for _reelle_ behov, og alt det der.

Vi minnes dette ved å finne frem mine favoritt-harselleringer fra tidligere utgaver av PULS:

* Det viser seg at Magnar har nådd en posisjon hvor "[the magic of his voice](https://twitter.com/sickill/status/653849906944299008)" får folk til å gå med på omtrent hva som helst.
* Magnar [fortsetter sin jobb som Kodemaker Resident Thought Leader](https://twitter.com/magnars/status/666961875683405824), og ender opp med hele 81 retweets på sine tanker om future proofing.
* Magnar må desverre stige ned fra tronen som Kodemaker Resident Thought Leader. Jeg har nettopp vært på telefon med ham mange timer, og klart å overbevise ham om at livet fortsatt er verdt å leve.
* [...] det kommer frem at Magnar er en slags pragmatisk masochist.

Hmm, kanskje det var like greit å bli firmabloggifisert, siden det meste var på _Magnar_ sin bekostning.

**Christin i podcast**

Christin er subjektet i episode 101 av Developer on Fire. 101 er altså episode nummer 101, ikke S01E01 (i TV-serienotasjon). En podcast med noen episoder på baken, med andre ord.

Episoden heter [Making the world a better place](http://developeronfire.com/episode-101-christin-gorman-making-the-world-a-better-place), og med en slik tittel er Christin fortsatt soleklart Kodemaker Resident Thought Leader, en (gjev!) tittel hun tok før hun en gang har startet opp med konsulentarbeid for oss, og har holdt siden.

[She talks so fast](https://twitter.com/dashorst/status/701838079406055424), men det kan vi leve med.

**Anders bygger viktig Angular-kompetanse**

Snappet opp et par commit-meldinger fra prosjektet Anders er på. Lar de tale for seg selv.


> **Fix 'create view' checkbox when importing**
>
> Angular does not like binding to boolean directly on scope. Kinda funny
> considering the endless love and mutilation of booleans permeating this
> fine framework.
>
> Then again, I guess it makes perfect fucking sense.

Og:

> **Correctly display history of changes. FML.**
>
> Yeah. So, the saga continues.
>
> Angular's view of booleans is known to be epic. By now, that is an
> esablished truth. Wait, terrible wording! Established fact.
>
> ng-show and ng-hide certainly does not disappoint in this regard. Not at
> all. A boolean expression resolving to a string of, let's say 'No' or
> '0', is certainly falsy. True, right? No? Yes!
>
> Oh no!

**Open source-bidrag**

Kjetil har tatt opp igjen blogging, og Jekyll-oppsettet for bloggen hans finner man [her](https://github.com/akafred/akafred.github.io).

Kristoffer fortsetter med kontrakfestet open source på dagtid: https://github.com/digibib/ls.ext

Anders, vår nybakte og alltid elastiske styremedlem, lanserer [rubberlike](https://github.com/andersfurseth/rubberlike). Det er et Clojure-bibliotek for å jobbe med embedda elasticsearch-servere. Det står i README-en at det er "Copyright © 2014 Anders Furseth" så dette er åpenbart et prosjekt han har holdt på med i noen år. Anders har også sendt inn to pull requests ([#1](https://github.com/magnars/stubadub/pull/1), [#2](https://github.com/magnars/stubadub/pull/2)) til Magnar sin [stubadub](https://github.com/magnars/stubadub), hvor du kan se Anders og Magnar kommunisere med hverandre på Github som om de ikke kjenner hverandre. Open Source bringer folk nærmere hverandre, og venner/kolleger lengere fra hverandre.

Kristian, derimot, gjør som seg hør og bør, og [sender pull requests til Anders](https://github.com/andersfurseth/rubberlike/pull/3) som om de _faktisk_ kjenner hverandre.

Finn hygger seg med [java-jme4j-dyn4j4j](https://github.com/finnjohnsen/jme-dyn4j), og imponerer alle med [commit-meldinger som inneholder ord som "ray cast"](https://github.com/finnjohnsen/jme-dyn4j/commit/9fb3224e4925d497c899e597317ca36e2bb7f141). Hvis ikke det bidrar til økt timespris, vet ikke jeg. Som dreven Android-utvikler har han også [lagt ut en POC](https://github.com/finnjohnsen/drivermodepoc) på noe Android intent-interaksjon mellom to apper, slik at alle apper på NSB sine Android-devicer kan vite om telefonen er i "førermodus" eller ikke. Les: vennligst ikke forstyrr føreren, rødlyspassering suger bare sånn at dere vet det.

Magnus har ikke sluppet helt taket på LightTable til tross for nyoppdaget iver for Elm. Det er godt å se! Alt for ofte dør open source-prosjekter når dets foreldre får teften av noe annet. Magnus startet prosjektet [LightTable autocompleter](https://github.com/LightTable/autocompleter), som er en work-in-progress-prototype for å erstatte eksisterende autocompleter-implementasjon i LightTable. På Elm-fronten, møter Magnus desverre øredøvende stillhet i issue [#1](https://github.com/maxgurewitz/elm-server/issues/1) og [#2](https://github.com/maxgurewitz/elm-server/pull/2) i elm-server. Magnus har også bidratt til rewrite-clj, på en tilfredsstillende nok måte at han [blir spurt av eksisterende maintainer om å ta over hele prosjektet](https://github.com/xsc/rewrite-clj/issues/4#issuecomment-189284923)!

Ronny har sendt inn 3 pull requests ([#1](https://github.com/rundis/Dumpling/pull/1), [#2](https://github.com/rundis/Dumpling/pull/2), [#3](https://github.com/rundis/Dumpling/pull/3)) med tweaks til Magnus sin dumpling. I tillegg har dokumentasjon blitt ryddet opp i [her](https://github.com/jbake-org/jbake/issues/265), [her](https://github.com/qos-ch/logback/pull/301) og [her](https://github.com/Grails-Plugin-Consortium/grails-jesque/pull/1), og issues med Grails 3 har blitt rapportert og fikset [her](https://github.com/grails/grails-core/issues/9706) og [her](https://github.com/grails/grails-core/issues/9689).

Odin diller videre med oauth2, og sender inn patcher til clj-oauth2 med buzzwords som [concurrency](https://github.com/comoyo/clj-oauth2/pull/6) og [access tokens](https://github.com/comoyo/clj-oauth2/pull/5).

Christian hacker fortsatt på [spasm](https://github.com/cjohansen/spasm), React-toolinga han bruker på oppdrag hos NRK. F.eks driver han med spennende ting som [korrekt URL-encoding](https://github.com/cjohansen/spasm/commit/3ef4d35f0d759ce55628e3f91d1b477ec6c4f873). Noen av Buster.JS sine moduler er helt frittstående og lever i beste velgående, og [samsam](https://github.com/busterjs/samsam) har fått [et pull request som Christian merget](https://github.com/busterjs/samsam/pull/14). Dessuten har Christian blitt funksjonell programmerer på sine eldre år, og har dermed [fått set-datastrukturen i sitt hjerte](https://github.com/busterjs/formatio/commit/055a641371fc47b59804fd1d96a1dc2b364e9bf1).

August dytter litt borti [gradle-warlike-plugin](https://github.com/augustl/gradle-warlike-plugin) og fikser småting, samt [driter seg ut](https://github.com/augustl/gradle-warlike-plugin/commit/87328dcaf79d3934c458801ad2c1d27b85a60a09). Typisk folk i 20-åra. [net-http-cheat-sheet](https://github.com/augustl/net-http-cheat-sheet), som forøvrig har bikka 1000 watchers på Github, har også blitt tweaka litt på. Det kan også se ut som August [forsøker å lære seg Rust](https://github.com/phildawes/racer/issues/499). En JVM-utvikler som vil ut av boksen, kanskje? Kritiske bugs har også [blitt avdekket](https://issues.apache.org/jira/browse/HTTPCLIENT-1478?focusedCommentId=15152242&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel) i (heldigvis) eldre versjoner av Java-plattformens mest brukte HTTP-bibliotek.

Stian sitt korstog for å endelig gjøre Twitter litt sosialt har plutslig fått bein og gå på, med en hel haug commits til [twittosocial](https://github.com/stiancor/twittosocial). Særlig [koser han seg](https://github.com/stiancor/twittosocial/commit/2e777a3759c9245910756eb2f4f91fe65c794969) med [sentrering av elementer](https://github.com/stiancor/twittosocial/commit/936d2f70cef40b72881f203d256a6f1839763a2d) i HTML/CSS.

Magnar har [merget pull requests](https://github.com/magnars/prone/pull/35) i prone, [fått inn pull requests](https://github.com/levand/quiescent/pull/56) i quiescent, og [cider](https://github.com/clojure-emacs/cider/pull/1581), og to ([#144](https://github.com/clojure-emacs/refactor-nrepl/pull/144), [#147](https://github.com/clojure-emacs/refactor-nrepl/pull/147)) i refactor-nrepl. Han har startet et nytt Clojure-bibliotek, [naive-xml-reader](https://github.com/magnars/naive-xml-reader), idéelt for "simple XML documents where pulling out the big guns is just too much work." Og til slutt, har multiple-cursors.el fått merget noen pull requests ([#239](https://github.com/magnars/multiple-cursors.el/pull/239), [#240](https://github.com/magnars/multiple-cursors.el/pull/240)).

Stein Tore jobber fortsatt med [smarthuset sitt](https://github.com/steintore/ansible_openhab_linux). Ser ut til at han ender opp med å [ta noen "pragmatiske" løsninger](https://github.com/steintore/ansible_openhab_linux/commit/0ed6bddf3cae363b93e849c7d0f01138641c48fb) for å få ymse proprietære bokser til å spille på lag. I tillegg tar han i et tak for at [OpenHAB skal kunne unngå man-in-the-middle-angrep](https://github.com/openhab/openhab/pull/4067/files) når man fra sitt eget hus snakker med sin egen varmepumpe. Rett skal være rett!

Alf Kristian har gjort [lein-autoexpect mindre irriterende](https://github.com/jakemcc/lein-autoexpect/pull/18), sørget for at Scala sin json-parser har fått [fikset... øøh.. noe typesystem-greier](https://github.com/argonaut-io/argonaut/pull/213). (Må lære meg et typesystem snart.)

**Bloggposter**

Magnus er CRUD'in it up, og fortsetter bloggserien sin med en single page app hvor Elm brukes på begge sider av TCP-connectionen: http://rundis.github.io/blog/2016/haskel_elm_spa_part4.html

Kjetil lærer oss en ting eller to om `apt`. Kjært barn har mange navn, og de fleste kjenner kanskje til `apt-get`, `dpkg` og `aptitude`. Men at `apt` i seg selv var en greie var helt nytt for meg! http://www.akafred.com/2016/02/apt-1-0-simpler-package-mgmt/

Kjetil lærer også å sette opp servere, da han endelig kom seg vekk fra en gammeldags shared host webhotell og over på en VPS: http://www.akafred.com/2016/03/my-budget-virtual-server-in-the-cloud/
