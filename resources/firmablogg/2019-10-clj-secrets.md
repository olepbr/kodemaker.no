:title Maskert config med Clojure
:author alf-kristian
:published 2019-10-30
:tech [:clojure]

:blurb

Clojure sine åpne datastrukturer er utrolig deilig å jobbe med, de brukes til alt,
også config. Men ikke all config er lik, f.eks. bør ikke secrets logges. I oppstart
av enhver app er det fordelaktig at config logges, så hvordan håndtere dette problemet?
Her er et triks for å maskere secrets i config.

:body

Clojure sine åpne datastrukturer er utrolig deilig å jobbe med, de brukes til alt,
også config. Men ikke all config er lik, f.eks. bør ikke secrets logges. I oppstart
av enhver app er det fordelaktig at config logges, så hvordan håndtere dette problemet?
Her er et triks for å maskere secrets i config.

## Hva vi ønsker å oppnå

Ved å logge config i oppstart av en app, har vi et bedre utgangspunkt for å forstå appen.
Vi kan lett verifisere om vi har prod-verdier lokalt, eller enda verre -- lokale verdier i prod?
Vi kan lett sjekke hvilken port som er konfigurert, hvor URL-ene peker, og kanskje også om
vi har satt riktig passord.

Configen ser kanskje ut som følger:

```clj
{:port 10000
 :db-url "jdbc:postgresql://localhost:5435/db"
 :db-pwd-secret "mitt superhemmelige passord"}
```
Så i oppstart av en app:

```clj
(log/info "My pid is" (.pid (ProcessHandle/current)) "and my config is\n" config)
19-10-05 09:45:50 INFO [config:10] - My pid is 31738 and my config is 
 {:port 10000, :db-url "jdbc:postgresql://localhost:5435/db", :db-pwd-secret "mitt superhemmelige passord"}
=> nil
```

Oops, passordet er plutselig på avveie! Hvem har tilgang til loggene, kanskje de aggregeres og sendes til en
tredjepart? Vi kan vel være enige om at dette _ikke er bra_...

Hva om resultatet var:

```clj
(log/info "My pid is" (.pid (ProcessHandle/current)) "and my config is\n" config)
19-10-05 09:45:50 INFO [config:10] - My pid is 31738 and my config is 
 {:port 10000, :db-url "jdbc:postgresql://localhost:5435/db", :db-pwd-secret "m******"}
=> nil
```

Passordet er maskert, men gir likevel et hint om hva det er.

Vi vil selvsagt ikke at passordet skal være maskert når vi eksplisitt ber om det, så dette
må funke:

```clj
(:db-pwd-secret config)
=> "mitt superhemmelige passord"
```

## Problemet

Om du ikke er vant til Clojure, tenker du kanskje, "hæ, er dette et problem"? Du er antakeligvis 
vant til at config er en spesiell greie, pakket inn i en klasse med et eget api. Sånn er det ikke i
Clojure. Der bruker vi maps, sets og vectors til å representere så og si all data.
De har et åpent api, massevis av funksjoner som kan lese og manipulere dem.

Det finnes også flere måter å konvertere disse strukturene, f.eks. til tekst, som loggern
vår her gjør. Så vi må på en eller annen måte endre oppførselen til config mappet vårt.

Clojure er et veldig utvidbart språk, f.eks. finnes det makroer, multi-metoder og protokoller.
Men, det å endre en instans av et map på måten vi her ønsker, viser seg å ikke være helt trivielt.

## Løsningen

Jeg hadde grublet på dette en stund, da en kollega sa, "men hva med [records](https://clojure.org/reference/datatypes#_deftype_and_defrecord)"?

Records ja, da gikk lyset opp for meg. Records er "map-like", og ender opp som Java klasser:

```clj
(defrecord Config [port db-url db-pwd-secret])
=> config.Config

(Config. 1000 "jdbc:postgresql://localhost:5435/db" "mitt superhemmelige passord")
=> #config.Config{:port 1000,
                  :db-url "jdbc:postgresql://localhost:5435/db",
                  :db-pwd-secret "mitt superhemmelige passord"}
```

Hmmm...hva har vi oppnådd her? Knotete, ikke spesielt idiomatisk, og ingen synlig gevinst!? Men, records
har en del triks på lager. Først og fremst oppfører de seg som maps:

```clj
(:db-pwd-secret (Config. 1000 "jdbc:postgresql://localhost:5435/db" "mitt superhemmelige passord"))
=> "mitt superhemmelige passord"
```

`toString` er ikke veldig spennende:

```clj
(str (Config. 1000 "jdbc:postgresql://localhost:5435/db" "mitt superhemmelige passord"))
=> "config.Config@9aee29e5"
```

Dette ligner på `toString` fra Java sin Object-klasse. Dette kan vi utnytte og overstyre:

```clj
(defrecord Config [port db-url db-pwd-secret]
  Object (toString [this] (str {:port port 
                                :db-url db-url
                                :db-pwd-secret (str (first db-pwd-secret) "******")})))
```

In action:

```clj
(str (Config. 10000 "jdbc:postgresql://localhost:5435/db" "mitt superhemmelige passord"))
=> "{:port 10000, :db-url \"jdbc:postgresql://localhost:5435/db\", :db-pwd-secret \"m******\"}"
```

Whoa, nå begynner det å ligne på noe.

Clojure har mange måter å konvertere data til tekst. `str`,
som i dette tilfellet ender opp med `.toString`, er bare en av dem. `pr` og `prn` gjør det på en annen måte:

```clj
(prn (Config. 10000 "jdbc:postgresql://localhost:5435/db" "mitt superhemmelige passord"))
#config.Config{:port 10000, :db-url "jdbc:postgresql://localhost:5435/db", :db-pwd-secret "mitt superhemmelige passord"}
=> nil
```

`pr`, og alle varianter av denne, kan kontrolleres med `print-method`, en multi-metode:

```clj
(defmethod print-method config.Config [m w]
  (.write w (str m)))
```

Effekten:

```clj
(prn (Config. 10000 "jdbc:postgresql://localhost:5435/db" "mitt superhemmelige passord"))
{:port 10000, :db-url "jdbc:postgresql://localhost:5435/db", :db-pwd-secret "m******"}
=> nil
```

Du tenker kanskje ok, "men det var mye jobb for liten gevinst". Og så vil det jo være relativt
mye jobb hver gang du skal lage en ny config, eller endre på en... Her er det lett å gjøre feil!


## Metaprogrammering FTW!

Nå blir det litt mer hårete. Men effekten er at du ikke trenger å skrive noe spesiell kode for å maskere secrets.
La oss hoppe rett ut i det:
 
```clj
(defn mask-secrets [config]
  (let [with-masked-secrets (->> config
                                 (map (fn [[k v]]
                                        (if (str/ends-with? (name k) "-secret")
                                          [k (str (first v) "******")]
                                          [k v])))
                                 (into {})
                                 pr-str)

        ks (keys config)
        vs (map config ks)
        kss (map symbol ks)
        class-name `Config#

        c (eval `(defrecord ~class-name [~@kss]
                   Object (toString [_] ~with-masked-secrets)))
        constructor (symbol (str (.getName (.getPackage c)) "/" class-name "."))

        masked-config (eval `(~constructor ~@vs))]
    (defmethod print-method c [m w]
      (.write w (str m)))
    masked-config))
```
 
Ok, dette var heftig! Men, slapp av, jeg skal forklare hvert steg. Først, la oss se resultatet:

```clj
(def config (mask-secrets {:port 10000
                           :db-url "jdbc:postgresql://localhost:5435/db"
                           :db-pwd-secret "mitt superhemmelige passord"}))
=> #'config/config

(str config)
=> "{:port 10000, :db-url \"jdbc:postgresql://localhost:5435/db\", :db-pwd-secret \"m******\"}"

(prn config)
{:port 10000, :db-url "jdbc:postgresql://localhost:5435/db", :db-pwd-secret "m******"}
=> nil

(:db-pwd-secret config)
=> "mitt superhemmelige passord"
```

Og sist, men ikke minst:

```clj
(log/info "My pid is" (.pid (ProcessHandle/current)) "and my config is\n" config)
19-10-05 09:45:50 INFO [config:10] - My pid is 31738 and my config is 
 {:port 10000, :db-url "jdbc:postgresql://localhost:5435/db", :db-pwd-secret "m******"}
=> nil
```

Mission accomplished! `mask-secrets` har nå generalisert maskeringen. Alt du nå trenger å huske på
er å gi secrets postfixen `-secret`.

Jeg lovte å gå igjennom dette steg for steg. Først skaper vi `with-masked-secrets`:

```clj
with-masked-secrets (->> config
                         (map (fn [[k v]]
                                (if (str/ends-with? (name k) "-secret")
                                  [k (str (first v) "******")]
                                  [k v])))
                         (into {})
                         pr-str)
```

Dette er egentlig ganske vanlig Clojure-kode. En transformasjon av en datastruktur til en annen. Dette er stegene:

1. Vi mapper om alle verdier under keys som slutter med "secret" til første karakter og ******, alt annet uforandret.
2. Vi har nå en seq som inneholder key-value tupler, `[[key value]]`, vi kjører det igjennom `(into {})` for å lage et map.
3. Vi konverterer mappet til en string med pr-str.

Så henter vi ut nøkler `ks` og verdier `vs` fra mappet:

```clj
ks (keys config)
vs (map config ks)
```

Så begynner vi med metaprogrammeringen. Da vi skal bruke nøklene til å lage feltnavn trenger vi symboler, `kss`:

```clj
kss (map symbol ks)
``` 
 
I tilfelle vi ønsker å maskere flere maps, må hvert kall generere forskjellige klasser, hvis ikke får vi navnekollisjon.
Clojures auto-gensym `#` gir oss unike navn, den må brukes sammen med en syntax quote \`, en back-tick:

```clj
class-name `Config#
``` 

Da er vi klare til å generere klassen `c`:

```clj
c (eval `(defrecord ~class-name [~@kss]
           Object (toString [_] ~with-masked-secrets)))
```

Om du myser litt ser du kanskje likheten med oppbygging av defrecord vi hadde over?

Her bygger vi dynamisk opp kallet til `defrecord`. Den inneholder feltene som
config mappet vårt inneholder via `kss`. Og så overrider vi toString med den
maskerte verdien. Til slutt `eval`-uerer vi dette, som trigger
Clojure-kompilatoren, og vi ender opp med en `java.lang.Class`.


For å være dynamiske må vi håndtere at Config kan ha havnet i en pakke/namespace som vi ikke helt har kontroll
på, litt avhengig av hvordan det evalueres. For å kunne kalle konstruktøren må vi bygge opp et full kvalifisert
klassenavn i `constructor`:

```clj
constructor (symbol (str (.getName (.getPackage c)) "/" class-name "."))
```

Dette blir f.eks. `hs.config/Config__12721__auto__.`. Da er vi klare til å instansiere objektet vårt
`masked-config`:

```clj
masked-config (eval `(~constructor ~@vs))
```

Nesten i mål nå. Vi trenger bare å fikse `print-method` for klassen vår, og returnere resultatet:

```clj
(let [...]
   (defmethod print-method c [m w]
     (.write w (str m)))
  masked-config)
```

Phew, igjennom. Dette var da enkelt, easy peasy lemon squeezy?

For å være ærlig, dette er ikke den mest trivielle Clojure koden jeg har skrevet, så om du ikke hang med
i alle svingene her, så er det forståelig. Ta gjerne kontakt om noe er uklart!

## Ansvarsfraskrivelse

Det finnes mange måter å omgå denne maskeringen på. Det enkleste eksempelet er å transformere datastrukturen
til et map f.eks. med `into`:

```clj
(str (into {} config))
=> "{:port 10000, :db-url \"jdbc:postgresql://localhost:5435/db\", :db-pwd-secret \"mitt superhemmelige passord\"}"
```

Så om du velger denne strategien, må du likevel ha kontroll på configen din, men det har du vel?

Når sant skal sies, grunnen til at vi lagde denne løsningen var at vi ikke hadde 100 % kontroll på
configen vår. Ved en feilsituasjon ble litt for mye logga, blant annet et kontekst-map som inneholdt
config...ooops! Dette løste problemet for oss.

### En oppgave til leser

Om en forenkler problemstillingen, si at en ikke bryr seg om `str`/`.toString` kan dette gjøres enklere?
Hint hint, svaret er ja!
