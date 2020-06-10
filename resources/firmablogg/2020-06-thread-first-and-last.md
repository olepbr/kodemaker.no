:title Sy sammen s-expressions med thread-first og -last
:author magnar
:tech [:clojure :clojurescript]
:published 2020-06-10

:blurb

Når man er ny til Clojure er macro-ene `->` og `->>` temmelig forvirrende. Ikke
bare er de vanskelige å søke etter på nett, men koden ser også helt mystisk ut.
Heldigvis er det ikke lenge til du blir nyforelska og bruker dem overalt.
Kanskje etter denne bloggposten?

:body

Når man er ny til Clojure er macro-ene `->` og `->>` temmelig forvirrende. Ikke
bare er de vanskelige å søke etter på nett, men koden ser også helt mystisk ut.
Heldigvis er det ikke lenge til du blir nyforelska og bruker dem overalt.
Kanskje etter denne bloggposten?

## Hva og hvordan ser de ut

Macroene `->` og `->>` lar deg skrive om dypt nøsta kode, til noe som ser mer ut
som imperative statements.

Ta en titt på disse fire kodesnuttene:

```clj
(-> player
    (update :health dec)
    (dissoc :happy?)
    (assoc :bloodied? true))
```
```clj
(-> (update player :health dec)
    (dissoc :happy?)
    (assoc :bloodied? true))
```
```clj
(-> (dissoc (update player :health dec) :happy?)
    (assoc :bloodied? true))
```
```clj
(assoc (dissoc (update player :health dec) :happy?) :bloodied? true)
```

Alle disse uttrykkene evaluerer til det samme, men de fleste vil mene at den
første er lettere å lese. Det er også hele poenget. Thread first `->` lar oss
skrive koden på en lettere lesbar måte, uten at det påvirker hva compileren ser.

Hvis du studerer eksemplene nøyere, så vil du se at `->` syr sammen uttrykket
ved å plassere forrige element inn i neste. Hvor? Jo, i første
parameterposisjon. Thread first.

Hva da med thread last `->>`? Ja, nå kan du lure.

## Thread last

Her er noen nye kodesnutter:

```clj
(->> zombies
     (filter :aware-of-player?)
     (remove :dead?)
     (map move-towards-player))
```
```clj
(->> (filter :aware-of-player? zombies)
     (remove :dead?)
     (map move-towards-player))
```
```clj
(->> (remove :dead? (filter :aware-of-player? zombies))
     (map move-towards-player))
```
```clj
(map move-towards-player (remove :dead? (filter :aware-of-player? zombies)))
```

Igjen er det lettere å lese den første. Operasjonene står å lese i samme
rekkefølge som de utføres. Parentesene føles ikke like overveldende.

Du kan se at `->>` også syr sammen uttrykket ved å plassere forrige element inn i neste,
men denne gangen i siste parameterposisjon.

Og det er egentlig alt du trenger å vite. `->` og `->>` gjør koden din mer
lesbar ved å omgå dyp nøsting. Men hvis du slutter å lese her, så får du jo ikke
vite om hemmeligheten i clojure.core.

## Hemmeligheten i clojure.core

Da Rich Hickey skulle lage Clojure, lå han mye i hengekøya si og latet som han
sov. Der grublet han mye, og mye godt kom ut av tankearbeidet. En slik godsak er
hvordan parameterlistene til Clojure sine kjernefunksjoner i all hemmelighet er
laget med threading i mente:

- Funksjoner som jobber med et map tar det som første argument.
- Funksjoner som jobber med en seq tar den som siste argument.

Tenk tilbake på eksemplene over. Når vi jobbet med `player` (et map) så var det
lett å bruke `assoc`, `dissoc` og `update` med thread first `->`. Hvorfor? Fordi
alle disse tar map-et som første argument.

Når vi jobbet med `zombies` (en liste) så var det lett å bruke `filter`,
`remove` og `map` med thread last `->>`. Igjen, fordi alle disse tar seq-en som
siste argument.

Jeg kaller det en hemmelighet, for jeg har ikke sett det skrevet eksplisitt i
dokumentasjonen til Clojure noe sted, men alle funksjonene fungerer etter det
prinsippet.

Men hva betyr det egentlig?

## Clojure er full av affordances

En av fordelene med å lære seg funksjonell programmering med Clojure er at du
blir tvunget til det. Kotlin, Scala og Groovy er alle fine språk, men det er
bare å gasse på videre med gamle vaner. Når du kommer til Clojure er det
bråstopp. Du *må* lære deg å skrive funksjonell kode for å komme noen vei.

Clojure mener altså noe om hvordan du skal skrive kode. Den gjør det hyggelig å
gjøre det 'riktig', og vondt å gjøre det 'feil'.

Min påstand er at thread first `->` og last `->>` er lagt opp som en slik
affordance: Flere operasjoner på samme datastruktur er lett å komponere sammen,
men det øyeblikket du skifter mellom map og seq, så blir det vondt. Du må bytte
threading. Det er klossete.

Les: Ikke gjør det.

Istedet bryter du opp koden. Når du går fra et map til en seq, bryt opp
threadingen. Når du går fra en seq til et map, bryt opp. Det er lov å koste på
seg et navn. [Point free
programming](https://en.wikipedia.org/wiki/Tacit_programming) er kult det, men
ikke dra det for langt da.

Da jobber du med Clojure medhårs.

## Bli med på leken, du også

Det er en annen veldig vanlig grunn til at threading blir klossete: når dine
egne funksjoner ikke følger samme prinsipp. Ikke så rart at det skjer, egentlig.
Det var jo en hemmelighet. Men nå vet du det.

Følg samme prinsipp selv. Ta imot maps som første parameter og seqs som siste,
så flyter det bedre.

Se så fint:

```clj
(defn loot-bodies [player zombies]
  ...)

(-> player
    (update :health dec)
    (dissoc :happy?)
    (assoc :bloodied? true)
    (loot-bodies zombies))
```

Her har vi sendt inn `player` først, og den spiller på lag med `->`. Omvendt så
hadde det blitt klossete:

```clj
(defn loot-bodies [zombies player]
  ...)

(loot-bodies zombies
             (-> player
                 (update :health dec)
                 (dissoc :happy?)
                 (assoc :bloodied? true)))
```

Grøss.

## Til slutt, hvorfor trenger vi disse?

Det er et interessant spørsmål, fordi det er lett å tenke at det handler om
lisp - og alle parentesene. Det er ikke tilfelle. Når man skriver Emacs Lisp,
for eksempel, så er det god gammeldags imperativ kode: En lang rekke med
statements etter hverandre.

Årsaken er at Clojure-kode er bygget opp av [expressions - ikke
statements](https://fsharpforfunandprofit.com/posts/expressions-vs-statements/).
Årsaken er immutability. Uten et sted å mellomlagre state, så er man i større
grad tvunget til å nøste uttrykk. Det kan fort gå ut over lesbarheten.

Thread first `->` og last `->>` gir oss mye av den lesbarheten tilbake.
