:title Et gammelt triks i en ny kontekst
:author magnar
:tech [:design :clojure :functional-programming]
:published 2019-07-17

:blurb

Det er lett å ta gamle triks med seg inn i en ny hverdag. Den første
Clojure-koden jeg skrev ligner ikke spesielt mye på den jeg skriver i dag. I
denne bloggposten skriver jeg kort om én av tingene jeg har gradvis lært meg av
med.

:body

Det er lett å ta gamle triks med seg inn i en ny hverdag. Den første
Clojure-koden jeg skrev ligner ikke spesielt mye på den jeg skriver i dag. I
denne bloggposten skriver jeg kort om én av tingene jeg har gradvis lært meg av
med.

### Mange små funksjoner?

Det er blitt noen år siden nå, men jeg lærte av en gammel onkel at man måtte
dele kode opp i mange små funksjoner. Da ble den lettere å skjønne, også fikk
man dokumentert koden ved hjelp av funksjonsnavnene.

Jeg syns ikke det er så dumt tenkt. Jeg bruker fortsatt funksjonsnavn som
dokumentasjon istedet for kommentarer. Samtidig har jeg oppdaget at dette tipset
ikke holder seg like godt i Clojure.

La meg demonstrere. Hva gjør denne kodebiten?

```clj
(decrease-player-health game)
```

Det ser jo vitterlig ut som den reduserer helsen til spilleren i `game`. Det vet
jeg fordi jeg antar at funksjonen er godt navngitt. Den antagelsen kan vi gjerne
holde på. Funksjoner endrer seg, kan være dårlig navngitt, etc, men det er ikke
poenget mitt her.

La oss si at funksjonen er implementert slik:

```clj
(defn decrease-player-health [game]
  (update game :player decrease-health))
```

Jaha, se her, dette ser jo riktig ut. `:player` i `game` blir oppdatert med
funksjonen `decrease-health`. Vi kan anta at den også er godt navngitt, eller vi
kan trykke oss videre til definisjonen:

```clj
(defn decrease-health [player]
  (update player :health dec))
```

Perfekt. Nå vet vi hva `decrease-player-health`-funksjonen vår gjør. Den
oppdaterer `:health` på `:player` ved å kalle `dec`, en funksjon som reduserer
et tall med én.

### Mange små byggeklosser

Eller var det perfekt?

Jeg måtte lese og forstå to funksjonsnavn, og navigere to ganger for å
verifisere at funksjonene gjorde det som stod på boksen. [Navn er
vanskelig](https://martinfowler.com/bliki/TwoHardThings.html).

Hva skjer hvis vi inliner `decrease-health` funksjonen?

```clj
(defn decrease-player-health [game]
  (update game :player #(update % :health dec)))
```

Det ser litt klossete ut, men denne formen for nøstet oppdatering kan i Clojure
skrives som:

```clj
(defn decrease-player-health [game]
  (update-in game [:player :health] dec))
```

Og når det er gjort, trenger vi `decrease-player-health` funksjonen?

Istedet for:

```clj
(decrease-player-health game)
```

er det ikke like lett å lese:

```clj
(update-in game [:player :health] dec)
```

?

Nei! Det er *mye lettere* - gitt at jeg kjenner byggeklossene i clojure.core. Jeg
trenger ikke lese og forstå et navn. Jeg trenger ikke navigere til koden for å
være sikker.

Med andre ord: Når byggeklossene er gode nok, kan man øke lesbarheten ved å
unngå navngitte funksjoner.

Som [Alan Perlis sa](http://www.cs.yale.edu/homes/perlis-alan/quotes.html):

> It is better to have 100 functions operate on one data structure than 10 functions on 10 data structures.
