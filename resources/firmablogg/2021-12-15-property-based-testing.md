:title Property-based testing i Clojure
:author anders
:tech [:clojure :testing]
:published 2021-12-15

:blurb

Er du lei av å skrive tester? Hva om jeg fortalte deg at det finnes
verktøy som genererer testene for deg? For godt til å være sant?

:body

For noen år tilbake dro flere kollegaer og jeg til en konferanse for faglig
påfyll og inspirasjon. Et av foredragene bar tittelen "Don't write tests!
Generate them". Nysgjerrigheten ble umiddelbart pirret selv om dette hørtes for
godt ut til å være sant. Foredragsholderen proklamerte videre at han ved denne
formen for testing hadde avdekket dype, intrikate feil i alt fra
telefonsystemer, styringssystemer til biler, samt databaseimplementasjoner. Feil
som nær sagt hadde vært umulig å avdekke i form av tradisjonelle tester.

Foredragsholderen var John Hughes og er mannen bak
[QuickCheck](https://hackage.haskell.org/package/QuickCheck), det opprinnelige
verktøyet for å skrive Property-Based tester.

Denne bloggposten er den første i en serie av flere. I første omgang vil jeg gi
deg en kort innføring i Property-Based Testing (PBT). Vi skal skrive et par
tester mot en triviell, velkjent funksjon og ta et aldri så lite dykk nedi
[test.check](https://github.com/clojure/test.check), et PBT-verktøy for
[Clojure](/clojure). Dersom Clojure er fremmed for deg anbefaler jeg
deg likevel fortsatt til å lese videre. Anatomien og prinsippene bak PBT er
universelle på tvers av teknologi.

Støtte for PBT finnes for et bredt utvalg av språk. Et raskt søk på nett vil
gi deg pekere til testverktøy for eksempelvis
[JavaScript](https://github.com/dubzzz/fast-check),
[Ruby](https://github.com/hayeah/rantly),
[Java](http://java.net/projects/quickcheck/pages/Home),
[Go](https://github.com/flyingmutant/rapid), [Erlang /
Elixir](https://github.com/pragdave/quixir) og
[Python](https://github.com/DRMacIver/hypothesis).

## Anatomi

Den _tradisjonelle_ måten å skrive tester av en funksjon går som følger:

1. Vi formulerer et konkret eksempel på input til funksjonen.
2. Vi kaller på funksjonen med det konkrete eksempelet som input.
3. Vi verifiserer at resultatet stemmer med våre forventninger.

En property-based test genererer testene for deg. Dette skjer ved at:

1. Vi formulerer en mer _generell_ beskrivelse av input ved hjelp av
   testverktøyet.
2. Testverktøyet genererer et _konkret og tilfeldig_ eksempel som passer
   beskrivelsen.
3. Vi kaller på funksjonen med det konkrete eksempelet som input.
4. Vi verifiserer at resultatet stemmer med våre forventninger.

Ettersom det er testverktøyet, og ikke du, som genererer input til funksjonen
din vil testverktøyet kunne generere "uendelig" antall unike tester mot
funksjonen din. Når jeg sier "funksjon", så mener jeg ikke en nødvendigvis en
funksjon som er ["pure"](https://en.wikipedia.org/wiki/Pure_function). Tvert
imot; PBT er like, om ikke mer, nyttig til testing av metoder eller systemer med
[sideeffekter](https://en.wikipedia.org/wiki/Side_effect_(computer_science)). I
min erfaring er det gjerne nettopp der de fleste feilene oppstår og gjerne også
hvor de er mest vanskelig å avdekke. "Funksjonen" har i så tilfelle ansvar for å
sette opp systemet i en tilstand som lar seg teste.

La oss konkretisere disse stegene i litt kode ved å skrive en test for noe så
trivielt som en funksjon som sorterer en liste av heltall.

## Formulere beskrivelse av input

Testverktøyet vårt, `test.check`, tilbyr et bredt utvalg av funksjoner (kalt
generatorer) for å beskrive input. La oss finne beskrivelsen av heltall. Jeg
bruker hjelpefunksjonen `sample` for å trekke ut 20 konkrete eksempler fra
heltall-generatoren:

```clj
(ns sorter-heltall-test
  (:require [clojure.test.check.generators :as gen]))

(gen/sample gen/int 20)

=> (0 0 1 -2 -4 1 -3 7 8 8 -3 -2 7 13 0 11 -9 17 14 -18)
```

Sweet! Vi trenger dog å beskrive en liste av heltall som input til
sorteringsfunksjonen vår. Generatoren `gen/list` hjelper oss med dette. Den tar
en vilkårlig generator som argument og genererer lister med elementer generert
fra denne:

```clj
(gen/sample (gen/list gen/int) 10)

=> (() (-1) (1) (-2 -3 -1) (0 3 1) (-1 -2 -1 1 -1) (1 -3 -1 6 0 -3) (2 -4) (-2) (-3 2 5 0 -5 3))
```

Flotters. Da var det første steget i orden. Vi har nå en generell beskrivelse av
input til funksjonen vår: `(gen/list gen/int)`.

## Formidle beskrivelsen til testverktøyet

De neste par stegene er å overlate denne beskrivelsen til testverktøyet (som
tross alt skal generere testene), samt kalle på sorteringsfunksjonen vår.

```clj
(ns sorter-heltall-test
  (:require [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]))

(def sorter-heltall sort)

(tc/quick-check
 1000
 (prop/for-all
  [input (gen/list gen/int)]
  (let [output (sorter-heltall input)]
    ;; TODO: steg 4, verifisere output
    )))
```

Vi forteller `quick-check` at vi ønsker å generere og kjøre `1000` tester.
Beskrivelsen av input gir vi til testverktøyets `for-all`, som i tur vil
generere det ene konkrete eksempel etter det andre. Hvert konkret eksempel blir
bundet til symbolet vi har navngitt `input`. Videre forventer `for-all` at vi
signaliserer at en test feiler ved å enten returnere `false` eller kaste en
exception.

## Verifisering

Det siste og fjerde steget er uten tvil det vanskeligste i PBT. Hvordan skal vi
få verifisert output'n fra sorteringsfunksjonen vår når vi ikke vet hva den har
fått som input? Ettersom vi har beskrevet input'n i generelle termer blir vi
pent nødt til å gjøre det samme i verifiseringssteget.

Hva er det sorteringsfunksjonen vår _gjør_? Hvilke _egenskaper_ (derav navnet
"property-based", forresten) har funksjonen vår? Dette høres ut som banale
spørsmål, men det er spørsmål som må besvares i kode. La oss bryte det ned.
Output fra sorteringsfunksjonen er:

1. en liste som inneholder (og kun inneholder) alle elementene som ble gitt som input
2. en liste hvis elementer er i stigende rekkefølge

## En kjørende test!

La oss komplettere testen vår med det fjerde og siste steget. For å lette
eventuell feilsøking velger jeg å lage egne tester for hver egenskap.

```clj
(ns sorter-heltall-test
  (:require [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]))

(def sorter-heltall sort)


;; Egenskap 1: Inneholder (og kun inneholder) alle
;; elementene som ble gitt som input

(tc/quick-check
 1000
 (prop/for-all
  [input (gen/list gen/int)]
  (let [output (sorter-heltall input)]
    (= (frequencies input)
       (frequencies output)))))


;; Egenskap 2: Elementene er i stigende rekkefølge

(tc/quick-check
 1000
 (prop/for-all
  [input (gen/list gen/int)]
  (let [output (sorter-heltall input)]
    (or (and (empty? input) (empty? output))
        (apply < output)))))
```

Vi bruker Clojures basale og innebygde funksjoner for å implementere våre
forventninger.

Nå er vi endelig klar til å sette maskinen i arbeid og nyte synet av
mangfoldige tester suse avgårde i blindende glimt av betryggende grønnfarge!

🥁

```clj
...

(tc/quick-check
 1000
 (prop/for-all
  [input (gen/list gen/int)]
  (let [output (sorter-heltall input)]
    (= (frequencies input)
       (frequencies output)))))

=> {:num-tests 1000
    :pass? true
    :seed 1639440034364
    :time-elapsed-ms 45}

...
```

✅ Herlig! 1000 grønne tester generert og kjørt på et blunk.

La oss teste egenskap #2:

```clj
(tc/quick-check
 1000
 (prop/for-all
  [input (gen/list gen/int)]
  (let [output (sorter-heltall input)]
    (or (and (empty? input) (empty? output))
        (apply < output)))))

=> {:fail [(7 -3 5 -7 5 -5 -2 -2)]
    :num-tests 9
    :pass? false
    :seed 1639435177764
    :shrunk {:depth 2
             :pass? false
             :smallest [(-2 -2)]}}

```

😱 What!?

Etter 9 iterasjoner har testen vår feilet. Funksjonen vår ble fôret `(7 -3 5 -7
5 -5 -2 -2)`. Testverktøyet har dog, på nesten magisk vis, krympet input'n og
funnet ut at funksjonen vår faktisk allerede feiler når den blir gitt `(-2 -2)`!

Jeg var litt slurvete i sjekken av egenskapen; med `(apply < output)` tar vi jo
ikke høyde for at listen kan inneholde identiske elementer. Vi bytter `<` med
`<=` og fyrer opp på nytt.

```clj
{:result true, :pass? true, :num-tests 1000, :time-elapsed-ms 53, :seed 1639436350880}
```

Sååååååååånn! 🥳

## Krymping og tilfeldigheter

Dette med krympingen av input kan sikkert oppfattes som et nærmest ubetydelig
bidrag i dette konkrete eksempelet. Ved testing av funksjoner med mer kompleks
logikk og mistenkelig mange parametre, feilende input som inneholder hundrevis
av elementer og du i tillegg ikke ser bunnen på nøstingen av hvert enkelt
element... da kan jeg love deg at denne krympingen er helt og fullstendig
uvurderlig!

En annen ting: Settet av testdata som genereres ved kjøring av testen vår vil
variere. Den er jo tross alt tilfeldig generert. Dette er noe av styrken til
property-based testing. Selv om vi i dissee testen har sagt vi kun ønsker å
utføre 2000 tester vil de i stor grad være unike mellom hver testkjøring. Når
testen først feiler kan det være fordelaktig å kunne kjøre påfølgende tester med
tilsvarende generert input. Spesielt dersom du tester funksjoner som ikke er
pure! Testrapporten oppgir derfor hvilket `:seed` som er benyttet i genereringen
av input. Denne verdien kan du angi til `quick-check` for å enkelt reprodusere
feilen.

Generatorene i `test.check` (og trolig tilsvarende konsepter i ditt fremtidige
favorittverktøy for PBT) er dog ikke helt tilfeldig. De har alle en formening om
relativ "størrelse" eller "kompleksitet" i bestanddelene som blir generert.
Testverktøyet vil stadig generere mer og mer komplekse permutasjoner av
beskrivelsen du har formidlet, både for å tidlig kunne detektere elementære feil
og gjøre en god jobb med å krympe datasettet, men samtidig også øke testflaten
til funksjonene dine.


## Dette var jo interessant, meeeeen...

Du tenker sikkert: "Å skrive property-based tester for konseptuelt enkle
sorteringsalgoritmer er én ting, men hvordan kan jeg skrive tilsvarende tester i
mitt langt mer komplekse og mindre generelle domene?". Det er et godt og
betimelig spørsmål.

I neste innlegg i denne serien skal vi gjøre nettopp dét!
