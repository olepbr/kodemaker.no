:title Data hører hjemme i en database, også på framsiden
:published 2019-06-19
:author odin
:tech [:datascript :clojurescript]

:blurb

Putt tilstand i ClojureScript-appen din i en DataScript-database for å få kontroll på tilstanden på framsiden.

:body

Når du lager en webløsning i disse dager er sjansene store for at klienten må holde rede på en del tilstand. 
Da må du gjøre noen valg med tanke på hvordan tilstanden skal struktureres for lesing og skriving. 

Det dukker opp spørsmål som:

* Skal du lagre tilstanden som nøsta strukturer? 
* Skal du normalisere datasettet og ha referanser mellom entiteter? 
* Skal du duplisere data for å få raske svar på visse typer spørsmål? 

I en ikke-triviell applikasjon så ender man fort opp med å skrive en halveis implementasjon av 
en in-memory-database, hjemsøkt av feil. Å gjøre det riktig er ikke en triviell øvelse. Spesielt hvis du lagrer
samme data flere steder i klienten så er det fort gjort å havne i grøfta.


## Hva er løsningen?
Det vi ønsker oss er å kunne gjøre vilkårlige spørringer uten å måtte håndkode egne datastrukturer, samtidig som at all
tilstand er lagret ett sted. Altså en database. For at man skal få ytelse trenger man indekser og det betyr tradeoffs 
med tanke på minnebruk og skriveytelse, men det er ikke så farlig i disse dager med kraftige klienter.

[DataScript](https://github.com/tonsky/datascript) er en Clojure(Script)-basert immutable in-memory-database som støtter 
spørringer via [Datalog](http://www.learndatalogtoday.org) (tenk SQL, bare bedre). Den er inspirert av 
[Datomic](https://www.datomic.com), bare uten historikk- og persisteringstøtte.  

Du oppretter en DataScript-database når en side laster. Du putter data inn og henter ut og så forkastes databasen 
når brukeren forlater siden. Du kan riktignok persistere den via local-storage i nettleseren om du vil, men det limet
må du i så fall skrive selv. Poenget er å få et godt API for å gjøre vilkårlige spørringer uten at det går på 
bekostning av ytelse.

## Eksempel

I DataScript så må du deklarere et skjema over databasen din. Der må du inkludere hvilke attributter som er 
unike og hvilke som er referanser. En referanse kan være en-til-en eller en-til-mange. 

Her er et eksempel hvor vi har dyr og dyrehagene de bor i. 

```clj
(def schema {:dyrehage/navn {:db/unique :db.unique/identity}

             :dyr/navn {:db/unique :db.unique/identity}
             :dyr/bor-i {:db/valueType :db.type/ref 
                         :db/cardinality :db.cardinality/one}})
```

### Skrive data

For å legge inn data kan du gjøre slik:

```clj
(require '[datascript.core :as d])
(def conn (d/create-conn schema))

(d/transact! conn [{:dyrehage/navn "Kristiansand dyrepark", :dyrehage/adresse "Norge"}
                   {:dyrehage/navn "London Zoo", :dyrehage/adresse "England"}] 
```

Her blir det opprettet to dyrehager i en transaksjon. Legg merke til at en dyrehage har en adresse, uten at du trenger
å deklarere det i skjemaet.

Om du vil oppdatere adressen, så bruker du den unike identifikatoren til entiteten, `:dyrehage/navn`

```clj
(d/transact! conn [{:dyrehage/navn "Kristiansand dyrepark", :dyrehage/adresse "Sverige"}])
```

Vi kan også legge til noen dyr:

```clj
(d/transact! conn [{:dyr/navn "Julius", :dyr/alder 14, :dyr/bor-i {:dyrehage/navn "Kristiansand dyrepark"}}
                   {:dyr/navn "Barney", :dyr/alder 3, :dyr/bor-i {:dyrehage/navn "London Zoo"}}
                   {:dyr/navn "Nellie", :dyr/alder 5}])
```


### Spørringer

Nå kan vi hente ut data. Her er noen eksempler.

Alle dyr:

```clj
(d/q '[:find [?e ...] :where [?e :dyr/navn]] db)
=> [1 3 5]
```

Dette er id'ene til entitetene. Vi ønsker å hente attributtene, og da kan du gjøre slik:

```clj
(->> (d/q '[:find [?e ...] :where [?e :dyr/navn]] db)
       (map #(d/entity db %))
       (map #(select-keys % [:db/id :dyr/navn :dyr/alder :dyr/bor-i])))
=> [{:db/id 1, :dyr/navn "Julius", :alder 14, :dyr/bor-i {:db/id 2}} 
    {:db/id 3, :dyr/navn "Barney", :alder 3, :dyr/bor-i {:db/id 4}} 
    {:db/id 5, :dyr/navn "Nellie", :alder 5}]
```

Alternativt så kan vi bruke [pull-syntaks](https://docs.datomic.com/on-prem/pull.html) for å hente ut alle attributter direkte: 

```clj
(d/q '[:find [(pull ?e [*]) ...] :where [?e :dyr/navn]] db)
=> [{:db/id 1, :dyr/navn "Julius", :alder 14, :dyr/bor-i {:db/id 2}} 
    {:db/id 3, :dyr/navn "Barney", :alder 3, :dyr/bor-i {:db/id 4}} 
    {:db/id 5, :dyr/navn "Nellie", :alder 5}]
```

Finn en dyrehage basert på navn:

```clj
(d/q '[:find (pull ?e [*]) .
       :where [?e :dyrehage/navn "London Zoo"]]
    db)    
=>  {:db/id 7, :adresse "England", :dyrehage/navn "London Zoo"}
```

Siden navn er et unikt attributt kan vi gjøre det enklere også:

```clj
(d/entity db [:dyrehage/navn "London Zoo"])
```

Finn alle ville dyr:

```clj
(d/q '[:find [(pull ?a [*]) ...]
       :where
       [?a :dyr/navn]
       (not [?a :dyr/bor-i])]
    db)
=> [{:db/id 5, :dyr/navn "Nellie", :alder 5}]]
```

## Oppsummert

Vi har brukt DataScript med stor suksess i et kundeprosjekt. Du unngår sølete, egenutviklet tilstandskode og kan 
lene deg på et gjennomtenkt spørrespråk. En bonus er at DataScript følger samme data-abstraksjon som Datomic, noe som 
åpner opp for spennende løsninger hvor man lett kan strømme data fra baksia til fremsia. 
[Magnar](https://www.kodemaker.no/magnar) har i så fall en 
[tankevekkende presentasjon](/strom-data-til-nettleseren-uten-a-lage-det-pa-nytt-hver-gang/) som er verdt å få med seg.


## Hva med JavaScript?
DataScript har et JavaScript API, men det er ikke spesielt tilrettelagt for bruk fra JavaScript. Det finnes 
alternativer som [Lovefield](https://google.github.io/lovefield/) som ser lovendes ut. Det vil ikke overraske meg om 
at vi ser mer av databaser i frontenden også i JavaScript-land i fremtiden.
