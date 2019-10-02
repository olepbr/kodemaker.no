:title Et lite Clojure-triks til å bli glad av
:author magnar
:published 2019-10-02
:tech [:clojure :functional-programming]

:blurb

Det viktigste og beste med Clojure er [pure
functions](/16-minutter-om-pure-functions/) og [immutability](/immutability/),
men i dag skal jeg skrive om noe ganske annet. Jeg skal skrive om en småting som
gjør meg glad når jeg koder.

:body

Det viktigste og beste med Clojure er [pure
functions](/16-minutter-om-pure-functions/) og [immutability](/immutability/),
men i dag skal jeg skrive om noe ganske annet. Jeg skal skrive om en småting som
gjør meg glad når jeg koder.

## Det er bare å glede seg

Her er en liten kodesnutt:

```clj
(defn add-new-zombie [zombies]
  (conj zombies (new-zombie)))

(update game :zombies add-new-zombie)
```

Funksjonen `update` tar i mot et map `game`, en nøkkel `:zombies` i det mappet,
og en funksjon `add-new-zombie` som skal brukes til å oppdatere verdien bak den nøkkelen.

(`conj` legger en verdi til i en liste)

Det er naturligvis *ille dust* at det finnes en funksjon `add-new-zombie`. Vi
vil heller ha [små byggeklosser enn små
funksjoner](https://www.kodemaker.no/blogg/2019-07-gammelt-triks-ny-kontekst/).

Dermed kan vi dra koden inn via en anonym funksjon:

```clj
(update game :zombies #(conj % (new-zombie)))
```

Hmfr. "Jeg trodde liksom det skulle være så lite syntax i denne JVM-lispen din,
Magnar?" sier du kanskje. "Hva i alle verdens land og rike er disse skigardene og
prosenttegnene?"

Jeg er enig. Jeg skjemmes litt. Vi kan skrive en skikkelig funksjonsliteral istedet:

```clj
(update game :zombies (fn [zombies] (conj zombies (new-zombie))))
```

Som du kanskje ser så er `%` en slags anaforisk parameter -- spesiell syntaks for
å vinne kodegolfturninger. Det er ikke det som gjør meg glad når jeg koder.

## Her er det

Den vanlige formen av `update` ser slik ut:

```clj
(update map key function)
```

Det som gjør meg glad er den alternative formen. Den tar varargs, og ser slik ut:

```clj
(update map key function args...)
```

Disse ekstra argumentene får bli med inn i funksjonen. Og trikset er: verdien som skal oppdateres sendes inn først! ... og så resten av argumentene etterpå.

Slik blir det:

```clj
(update game :zombies conj (new-zombie))
```

Det leser: "Oppdater spillet sine zombier ved å legge til en ny zombie." Svit.

## Og ikke bare det!

Dette trikset funker også med `update-in` og `swap!`. Sistnevnte oppdaterer
Clojure sine atomer, et slags [fengsel for muterbar
state](https://kodemaker.no/interaktiv-programmering-med-clojurescript/).

Vi trenger altså ikke skrive:

```clj
(swap! game-atom (fn [game] (update game :zombies (fn [zombies] (conj zombies (new-zombie))))))
```

Det skulle tatt seg ut. Så mange parenteser, da gett. Vi vet at vi allerede kan forenkle det til:

```clj
(swap! game-atom (fn [game] (update game :zombies conj (new-zombie))))
```

Alternativt kan vi forenkle det til:

```clj
(swap! game-atom update :zombies (fn [zombies] (conj zombies (new-zombie))))
```

Men her er det aller mest nydelige. Ettersom disse komponerer helt lekkert, koker det hele ned til:

```clj
(swap! game-atom update :zombies conj (new-zombie))
```

Det er nesten så jeg får tårer i øyekroken.
