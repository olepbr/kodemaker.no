:title Litt julekos med Markov-kjeder
:author magnar
:tech [:programming :clojure]
:published 2022-12-19

:blurb

Det har den siste tiden blitt svært tydelig at AI har kommet langt på tekstlig
dialog. Men det er snart jul, og ikke tiden for eksistensielle spørsmål. Nå er
det tid for kos. Nærmere bestemt å produsere latterlig svada med Markov-kjeder.

:body

Det har den siste tiden blitt svært tydelig at AI har [kommet
langt](https://www.kode24.no/artikkel/overvurderer-vi-chatgpt-eller-overvurderer-vi-oss-selv/78067679)
på tekstlig dialog. Men det er snart jul, og ikke tiden for eksistensielle
spørsmål. Nå er det tid for kos. Nærmere bestemt å produsere latterlig svada med
Markov-kjeder.

## Hva?

Markov-kjeder (navngitt etter den russiske matematikeren Andrey Markov) er en
liten algoritme som er lett å forstå, men som gir overraskende resultater.
Tekstproduksjon er nok ikke dens sterkeste side, men det gjør det desto
morsommere.

La oss starte på slutten. Her er noen eksempler fra Markov-generatoren som er
trenet på de 2,4 millioner ordene i det tekstbaserte eventyrspillet [Adventur Delux](https://www.adventur.no):

```
  "Gelenderet er et dusin ølglass i skogen."

  "Pelshandleren gir blaffen i trynet igjen."

  "DVERGEBESTANDEN gauler trollet."

  "Jævla spillforfattere tenker på en drink."
```

Ingen av disse setningene er å finne i spillet, men de ligner veldig på teksten
som er der. De gir nesten mening, også. Særlig den siste.

## Hvordan?

La oss se på litt kode mens jeg forklarer. Dette er [Clojure](/clojure/), men
det kunne selvfølgelig vært hva som helst:

```clj
(def corpus
  ["Julaften er gøy."
   "Nyttårsaften er gøy."
   "Venting er kjedelig."])
```

Her har vi et lite korpus av tekst. La oss dele det opp i ord:

```clj
(def words
  (mapcat #(str/split % #"\s") corpus))

;; => ["Julaften"
;;     "er"
;;     "gøy."
;;     "Nyttårsaften"
;;     "er"
;;     "gøy."
;;     "Venting"
;;     "er"
;;     "kjedelig."]
```

Videre kan vi ta to-og-to ord av gangen, slik:

```clj
(partition 2 1 words)

;; => [["Julaften" "er"]
;;     ["er" "gøy."]
;;     ["gøy." "Nyttårsaften"]
;;     ["Nyttårsaften" "er"]
;;     ["er" "gøy."]
;;     ["gøy." "Venting"]
;;     ["Venting" "er"]
;;     ["er" "kjedelig."]]
```

Her ser vi altså parvise ord i den rekkefølgen de kommer. Det er dette vi skal
bruke til å generere tekst. Nemlig: *Hvilke ord følger vanligvis etter andre ord?*

Først et lite triks: For at kjeden med ord skal avsluttes ved punktum, så
fjerner vi parene som skaper en bro fra en setning til den neste.

```clj
(defn end-word? [s]
  (str/ends-with? s "."))

(def pairs
  (->> (partition 2 1 words)
       (remove #(end-word? (first %)))))

;; => [["Julaften" "er"]
;;     ["er" "gøy."]
;;     ["Nyttårsaften" "er"]
;;     ["er" "gøy."]
;;     ["Venting" "er"]
;;     ["er" "kjedelig."]]
```

Vi kan gruppere disse på det første ordet for å lage en oppslagstabell:

```clj
(def lookup-table
  (-> (group-by first pairs)
      (update-vals #(map second %))))

;; => {"Julaften" ["er"]
;;     "Nyttårsaften" ["er"]
;;     "Venting" ["er"]
;;     "er" ["gøy." "gøy." "kjedelig."]}
```

Her begynner algoritmen å vise seg ganske tydelig. Etter ordet "Julaften" følger
bare ett annet ord, nemlig "er". Men etter ordet "er" kan vi finne både "gøy."
og "kjedelig."

Men du ser det er to "gøy" der, ikke sant? Ettersom kjeden vår er trent på et
datasett som i hovedsak syns ting er gøy, så vil det være større sannsynlighet
for at den også velger "gøy".

For å kunne sparke hele saken i gang, så trenger vi å velge oss et ord å starte
med. La oss holde det enkelt og bare velge ord med stor forbokstav:

```clj
(defn start-word? [s]
  (let [first-char (subs s 0 1)]
    (= first-char
       (str/upper-case first-char))))

(filter start-word? (keys lookup-table))

;; => ("Julaften" "Nyttårsaften" "Venting")
```

Perfekt. Da har vi dataene vi trenger:

```clj
(def markov
  {:lookup-table lookup-table
   :start-words (filter start-word? (keys lookup-table))})
```

Vi kan lage oss en støttefunksjon for å finne et startord:

```clj
(defn select-start-word [markov]
  (rand-nth (:start-words markov)))

(select-start-word markov) ;; => "Nyttårsaften"
```

Den plukker bare et tilfeldig fra listen. Videre kan vi slå opp i
oppslagstabellen for å finne neste ord:

```clj
(defn select-next-word [markov word]
  (rand-nth (get (:lookup-table markov) word)))

(select-next-word markov "Nyttårsaften") ;; => "er"

(select-next-word markov "er") ;; => "kjedelig."
```

En liten rekursiv funksjon senere, og vår algoritme er ferdig:

```clj
(defn generate-sentence [markov]
  (loop [words [(select-start-word markov)]]
    (let [next-word (select-next-word markov (last words))]
      (if next-word
        (recur (conj words next-word))
        words))))

(generate-sentence markov) ;; ["Julaften" "er" "gøy."]
(generate-sentence markov) ;; ["Venting" "er" "gøy."]
(generate-sentence markov) ;; ["Julaften" "er" "kjedelig."]
```

Her er det mange muligheter for optimalisering, men nå skjønner du hvordan
algoritmen fungerer.

## Hvorfor?

Markov-kjeder brukes til mange nyttige formål, særlig innen simuleringer av
komplekse systemer i biologi, logistikk, og industrielle prosesser.

Å produsere tekst med Markov-kjeder derimot, er kanskje ikke så nyttig, men det
er til gjengjeld morsomt. I eventyrspillet jeg nevnte brukes Markov-kjeder til å
lage en artig syretrip for spillere som har gjort dårlige livsvalg og inntatt i
overkant mye banandop.

Avslutningsvis må vi nesten gå på vår egen syretripp og teste markov-kjeden på
bloggpostene her på kodemaker.no. Det meste blir vrøvl, men her er noen av
forslagene dens:

```
  "Kildekode skal tegnes."

  "Domenemodellering er en operasjon i overgang til det reelle nettverkskallet."

  "Kompileringstidsananlyse hvor mange ubesvarte spørsmål."

  "Initiell tilstand ved å bli tolket som så farlig."

  "JavaScript?"

  "Indeks løvnoder med minneområdet og Flutter."

  "OpenSSL har spilt gjennom alle plattformene."

  "Vis forskjellen mellom hver manipulasjon."

  "Emacs, eller timet ut."
```

Er ikke kjedelig det, vet du.

God jul!
