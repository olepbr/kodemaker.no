:title Datomic: Fem fete fordeler, fort fortalt
:published 2023-01-18
:author magnar
:tech [:database :datomic :datascript :clojure]

:blurb

Det er ikke ofte at en database gjør meg direkte begeistret, men Datomic har
klart kunststykket. Til tross for at det er proprietær software man må betale
for, har databasen så mange fete fordeler at det er vanskelig å velge bare fem
av dem ... men fem må det bli. Hvordan skulle jeg ellers fått fikset en så feiende
flott tittel?

:body

Det er ikke ofte at en database gjør meg direkte begeistret, men Datomic har
klart kunststykket. Til tross for at det er proprietær software man må betale
for, har databasen så mange fete fordeler at det er vanskelig å velge bare fem
av dem ... men fem må det bli. Hvordan skulle jeg ellers fått fikset en så feiende
flott tittel?

## 1. Ta vare på all historikk, alltid.

Som utviklere er vi opptatt av historikk på kodebasen vår. Vi bruker
selvfølgelig [git](/git/), som lar oss spore alle endringer som er gjort. Det er
nyttig -- særlig når ting går galt. Hvorfor er vi da så villige til å skrive
over dataene til kundene våre?

Tenk deg at vi har sendt en pakke til en kundes hjemstedsadresse. Det har gått
en uke, to uker, men pakken kom aldri frem. Kundesenteret går inn i systemet og
finner ut at, joda, det er riktig adresse på kunden.

Hva kan ha skjedd?

Du sier "Kanskje adressen var feil når pakken ble sendt, men har blitt rettet
opp etterpå?" Folk humrer litt usikkert. Det finner vi nemlig ikke ut av, for
den "Ta vare på historiske adresser"-lappen ligger fortsatt i backloggen.

Dette scenarioet er null stress med Datomic. Okay, dumt at kunden ikke fikk
pakken sin, men det til side: All historikk er alltid med og lett tilgjengelig.
Du trenger ikke planlegge for det på forhånd. Alt som kommer inn av data ligger
i transaksjonslogg og indekser. Dataene kan markeres som utdatert, men blir
aldri borte.

Vi kan når som helst spørre "Hvordan så databasen ut på dette tidspunktet?" --
og få svar.

### Bughunt med Datomic

Det er mandag, og det ligger en kjip exception i prod-loggen fra lørdag. Vi
fyrer opp [REPL-et](/blogg/2022-10-repl/) og kjører den kule funksjonen jeg
dyttet til prod på fredag. Vi sender inn et snapshot av databasen slik den er
nå:

```clj
(my-cool-function db params) ;; => 👍
```

Hmm, alt fungerer som det skal ... Kanskje det har blitt bedre i mellomtiden?
Kan det ha noe med datoen å gjøre? At det var helg? Eller fungerer det nå på
grunn av endringer i databasen?

La oss prøve:

```clj
(my-cool-function (d/as-of db #inst "2023-01-14T17:13:11") params) ;; => 💥
```

Se, der var feilen!

I første eksempel sendte vi et snapshot av databasen til funksjonen min. Det
gjør vi i andre eksempel også, men denne gangen fra det nøyaktige tidspunktet
feilen skjedde. Stilig?

Legg merke til at jeg bruker akkurat samme underliggende kode. Jeg trenger ikke
skrive om en eneste query for å hente ut historiske data.

Det er bra greier.

## 2. Databasen som immutable verdi

Etter å ha hentet et snapshot av databasen kan man gjøre så mange spørringer man
vil, over så lang tid man trenger, mot samme uforanderlige blikk på verden.

Det betyr at du slipper å bekymre deg for at databasen endrer seg under føttene
dine mens du prøver å beregne noe. Du trenger ikke skrive megaspørringer for å
hente absolutt alle dataene du trenger i ett stort jafs, i frykt for at
påfølgende spørringer vil være ute av sync. Du slipper bugs som er umulige å
reprodusere, fordi de skjer på grunn av timingen av spørringer og skriving til
databasen.

Det betyr også at funksjoner kan jobbe med hele databasen, og fortsatt være
*pure functions*.

Dette siste punktet løser det store problemet til [Imperative shell, functional
core](https://kennethlange.com/functional-core-imperative-shell/), nemlig
"Hvordan kan vi vite hvilken informasjon som er relevant før vi dykker ned i den
funksjonelle kjernen?" Jeg snakker en del om både arkitekturen og dette
problemet i [denne videoen](/16-minutter-om-pure-functions/).

Med et uforanderlig snapshot av databasen, så har vi alle dataene for hånden. Problem løst.

## 3. Query som skalerer inn i himmelen

Datomic sine data er *append only*. Vi kan bare legge til ny informasjon. Denne
informasjonen kan handle om noe helt nytt, men den kan også erstatte eller
invalidere gammel informasjon. Uansett så skriver vi ikke over noe. Vi sletter
ingenting.

Dermed kan Datomic data virkelig caches. Grundig. Permanent. Det fører igjen til
at all query i Datomic kan foregå i klienten. Man sender ikke en spørring over
vaieren til databaseserveren, som så må håndtere alle spørringer på toppen av
alle mulige transaksjoner. Du gjør spørringene selv, rett mot logg og indekser.

Det betyr at du kan sette av en egen server til gjøre analyse for bisnissen, for
eksempel. Den kan stå og kverne, kjøre de tyngste spørringene du kan tenke deg,
uten å påvirke databasens ytelse *overhodet*.

Trenger du mer ytelse på spørringer? Fyr opp så mange servere du vil. Ingen
connection pools å konfigurere. Ingen ytelsesutfordringer for databasen
bare fordi en spørring tar lang tid et eller annet sted. Det er bare å gønne på.

## 4. Datamodellering som sitter

Datomic har unngått den berømte [Object-relational impedance
mismatch](https://en.wikipedia.org/wiki/Object–relational_impedance_mismatch)
hvor tradisjonelle, relasjonelle databaser sliter med å modellere trestrukturer.
I stedet er Datomic inspirert av idéer fra universell datamodellering (UDM):
Dataene trenger ikke være firkantede, fordi det er ikke rader og kolonner, men
entiteter og attributter.

Det minner om [RDF-tripler](https://en.wikipedia.org/wiki/Semantic_triple), og ser slik ut:

```clj
[entitet, attributt, verdi]
```

For eksempel:

```clj
[123, :author/first-name, "Magnar"]
[123, :author/last-name, "Sveen"]
```

Datomic sin nyvinning er at de også har med tid, slik at man alltid vet
tidspunkt for faktaet:

```clj
[entitet, attributt, verdi, tidspunkt]
```

Slik kan man sile ut fakta som kommer etter et visst tidspunkt, og dermed gi et
eldre blikk på databasen.

### Førsteklasses relasjoner

Ettersom Datomic også har førsteklasses støtte for relasjoner mellom entiteter,
så fungerer den godt som graf-database. Spesielt husker jeg hvor overrasket
og glad jeg ble da jeg oppdaget at relasjoner kan følges begge veier.

La oss si at vi modellerer at en bloggpost har en forfatter:

```clj
{:db/ident :blog-post/author
 :db/valueType :db.type/ref
 :db/cardinality :db.cardinality/one}
```

Her sier vi at en `blog-post` har en `author` og at den er en referanse til en
annen entitet. Vi kan da navigere den vanlige veien for å slå opp forfatteren av
en bloggpost:

```clj
(:blog-post/author blog-post) ;; => en author
```

Det kule her er at relasjonene blir indeksert i begge retninger. Vi kan også
navigere andre veien. Dersom jeg har en forfatter, kan jeg slå opp alle dens
blogposter:

```clj
(:blog-post/_author author) ;; => en liste med bloggposter
```

Underscoren forteller Datomic at jeg vil navigere referansen baklengs. Stilig.

## 5. Clojure og Datomic, ClojureScript og DataScript

[Odin](/odin/) sa det best: [Data hører hjemme i en database, også på
framsiden](/blogg/2019-06-datascript/). Les den!

Kort fortalt er DataScript en open source implementasjon av Datomic sitt API som
også kan kjøre i nettleseren. Den har en del begrensninger som gjør den lite
egnet som database på backend, men fungerer fortreffelig som datalager i en
Single Page Application.

Den nydeligste arkitekturen for utveksling av data mellom backend og frontend
som jeg har hatt gleden av å jobbe med, baserte seg på å sende slike RDF-tripler
fra Datomic på backend til DataScript på frontend. Idéene og lærdommen fra den
arkitekturen kan du høre meg prate mer om i [dette
JavaZone-foredraget](https://www.kodemaker.no/strom-data-til-nettleseren-uten-a-lage-det-pa-nytt-hver-gang/).

Ikke undervurder fordelene som kommer av en god database, både på baksia og framsia.

## Til slutt

Det er mulig du sitter igjen med følelsen "Jøss, her var det mye superlativer."
Det stemmer: Jeg er begeistret. Det er sjeldent at en database eller teknologi
har gitt meg så mye å tenke på, og så mye ny innsikt, som Datomic.

Datomic har naturligvis også ulemper. Jeg nevnte et par av dem i
åpningsavsnittet, men la meg avslutte med noen flere:

- Datomic er ikke så godt egnet som tidsseriedatabase der man skriver
  kontinuerlige måleverdier og er mest interessert i aggregater. Det kan vokse
  seg for stort.

- Datomic sin historikk er naturligvis begrenset til dataene som ligger i
  Datomic. Dersom man har et konglomerat av datakilder, reduseres verdien av
  historiske data for debugging tilsvarende.

- Datomic har ingen tradisjonell databaseserver, men den har en *transactor*, som
  alene kjører alle nye transaksjoner. Dermed blir den et Single Point of
  Failure for skriv, selv om lesing fortsetter å fungere mens transactoren er
  nede.

- Selv om Datomic har APIer for flere språk, så er det klart enklest brukt fra
  Clojure. (Visste du at [Nubank](https://building.nubank.com.br/working-with-clojure-at-nubank/) valgte Clojure fordi de ville bruke Datomic?)

Når det er sagt: Hvis jeg får jobbe med Datomic, så blir jeg oppriktig glad.
Modellen sitter som et skudd, og det er fint å vite at jeg alltid kan finne frem
til gammel tilstand og få fikset den buggen, selv om den skjedde på lørdag og
det nå er mandag.
