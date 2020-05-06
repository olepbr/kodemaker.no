:title Partisjonering av data
:published 2020-04-29
:author christian
:tech [:funksjonell-programmering :clojure]

:blurb
`map`, `filter` og `reduce` er nyttige verktøy som mange har i verktøykassa si. Oppi der burde også `partition` være, og jeg skal vise deg hvorfor.

:body

Lister er en utrolig anvendelig datastruktur. Veldig mye data vi forholder oss
til er naturlig en samling av ting, og ved å representere dem likt kan vi også
løse problemer med de samme verktøyene om og om igjen. Tenk bare så anvendelig
`map` er. Jo mer du får den under huden, jo flere use caser ser du for den.

- Skal ha e-postadressen til en gruppe brukere: `(map :email users)`
- Skal vise alle tilgjengelige actions som en knapp: `(map Button actions)`
- Har en mengde strenger som egentlig er tall: `(map parse-int strs)`

`filter` er ikke noe mindre anvendelig akkurat.

På samme måte er det ofte nyttig å partisjonere elementene. La oss si at du har
9 mennesker, og du ønsker å vise dem i en 3x3 grid. Da hadde det vært utrolig
praktisk å kunne dele lista opp i 3 lister med 3 elementer. Og det er nettopp
det `partition` gjør, dersom du gir den `3` som argument:

```clj
(def people
  ["Anne"
   "Arnold"
   "Are"
   "Berit"
   "Belinda"
   "Bjørn"
   "Carl"
   "Celine"
   "Ingrid"])

(partition 3 people)
;;=> [["Anne" "Arnold" "Are"]
;;    ["Berit" "Belinda" "Bjørn"]
;;    ["Carl" "Celine" "Ingrid"]]
```

Perfekt! Da er det bare å sende resultatet til `(map Row partitioned)`. Med
Clojure sin hendige _threading macro_ blir det en søt liten pipeline:

```clj
(->> people
     (partition 3)
     (map Row))
```

## Partisjonering med predikat

Noen ganger er det fint å partisjonere en liste ved å sammenligne elementene. Ta
for eksempel denne spennende listen med events:

```clj
(def events
  [{:date "2020-04-29"
    :event "Woke up"}
   {:date "2020-04-29"
    :event "Made coffee"}
   {:date "2020-04-30"
    :event "Slept in"}
   {:date "2020-04-30"
    :event "Took a shower"}
   {:date "2020-04-31"
    :event "Day off"}])
```

For å dele denne opp i én liste med events per dag kan vi bruke `partition-by`,
og bruke `:date` som predikatet. Da får vi en ny sekvens hver gang `:date` gir
en ny verdi:

```clj
(partition-by :date events)

;;=> [[{:date "2020-04-29", :event "Woke up"}
;;     {:date "2020-04-29", :event "Made coffee"}]
;;
;;    [{:date "2020-04-30", :event "Slept in"}
;;     {:date "2020-04-30", :event "Took a shower"}]
;;
;;    [{:date "2020-04-31", :event "Day off"}]]
```

I stedet for `:date` kunne vi også ha sendt inn en hvilken som helst funksjon,
for å partisjonere etter vilkårlige regler.

## Forrige og neste

`partition` støtter også å skille på `step` og `n`, altså størrelsen på
gruppene. Dette kan være nyttig for å inkludere det samme elementet i flere
grupper.

Ofte trenger vi å vite hva som er det forrige og neste elementet for et gitt
stykke data. Det er lett å ty til løkker med intern tilstand for å løse dette,
men det er ikke nødvendig.

Ved å partisjonere med `n` 3, men `step` kun på 1 - altså, gå kun ett element
til høyre for å lage hver gruppe - så får vi alle elementene i lista, sammen med
naboene dens:

```clj
(partition 3 1 (range 10))

;;=> [[0 1 2]
;;    [1 2 3]
;;    [2 3 4]
;;    [3 4 5]
;;    [4 5 6]
;;    [5 6 7]
;;    [6 7 8]
;;    [7 8 9]]
```

Dvs, vi fikk _nesten_ det jeg ba om. Denne lista har bare 8 elementer, jeg hadde
ønsket meg 10. Vel, det er sånn at `partition` kun lager "fulle grupper". Så når
det kun er to elementer igjen vil den ikke lage en ufullstendig treer-gruppe.
`partition-all` til redningen!

```clj
(partition-all 3 1 (range 10))

;;=> [[0 1 2]
;;    [1 2 3]
;;    [2 3 4]
;;    [3 4 5]
;;    [4 5 6]
;;    [5 6 7]
;;    [6 7 8]
;;    [7 8 9]
;;    [8 9]
;;    [9]
```

Dette ble heller ikke helt riktig. Dersom vi skal kunne traversere denne og
forvente å finne `[forrige x neste]`, så vil det siste elementet lyve for oss.
Det er to måter å løse dette på, avhengig av hva vi ønsker.

### Ensrettet forrige og neste

Dersom "forrige" fra første element ikke er noe, og "neste" fra siste element
heller ikke er noe kan vi padde collectionen vår med en `nil` på hver ende og
kalle det en dag:

```clj
(->> (concat [nil] (range 10) [nil])
     (partition 3 1))

;;=> [[nil 0 1]
;;    [0 1 2]
;;    [1 2 3]
;;    [2 3 4]
;;    [3 4 5]
;;    [4 5 6]
;;    [5 6 7]
;;    [6 7 8]
;;    [7 8 9]
;;    [8 9 nil]]
```

### Sirkulær forrige og neste

I en sirkulær struktur er det siste elementet det forrige fra det første
(puh!). Tilsvarende er det første elementet det neste fra det siste. Dermed må
vi dytte på litt data før vi partisjonerer.

Det er flere måter å padde ut en liste på måten beskrevet over. Her er én:

```clj
(let [xs (range 4)
      n (count xs)]
  (->> (cycle xs)
       (drop (- n 1))
       (take (+ n 2))))

;;=> [3 0 1 2 3 0]
```

Med andre ord:

1. Repeter elementene i lista etter hverandre:
   ```clj
   ;;=> [0 1 2 3 0 1 2 3 0 1 2 3 ...]
   ```
2. Dropp hele den første sekvensen, unntatt siste element:
   ```clj
   ;;=> [0 1 2 3 0 1 2 3 0 1 2 3 ...]
   ;;          ^
   ;;=> [3 0 1 2 3 0 1 2 3 ...]
   ```
3. Ta med like mange elementer som det var i den opprinnelige lista, pluss to
   (én ekstra i hver ende):
   ```clj
   ;;=> [3 0 1 2 3 0]
   ```

Denne kan vi partisjonere med 3 og 1:

```clj
(let [xs (range 10)
      n (count xs)]
  (->> (cycle xs)
       (drop (- n 1))
       (take (+ n 2))
       (partition 3 1)))

;;=> [[9 0 1]
;;    [0 1 2]
;;    [1 2 3]
;;    [2 3 4]
;;    [3 4 5]
;;    [4 5 6]
;;    [5 6 7]
;;    [6 7 8]
;;    [7 8 9]
;;    [8 9 0]]
```

Og vips! Så har vi hele lista på den midtre posisjonen, med forrige til venstre
og neste til høyre. Med dette trikset i ermet kan du nå bytte ut enda flere
imperative løkker med flotte funksjonelle pipelines.
