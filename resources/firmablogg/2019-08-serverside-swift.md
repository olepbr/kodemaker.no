:title Enklere backender med færre rammeverk - og Swift?
:published 2019-08-07
:author andre
:tech [:swift :backend]

:blurb

Er Swift klar for serverside, og hva finnes av biblioteker og rammeverk?

:body


Hva sier du? Swift på serveren?
Vel, det er ikke sikkert at det er så dumt, skjønner du. Swift har blitt et fantastisk bra språk som minner en del om Kotlin. Swift har bra interop med Objective-C, med det skinner ikke det så godt gjennom som Java gjør med Kotlin.

Jeg er på mitt 24 år som profesjonell utvikler, og har gjennom denne tiden jobbet med en del forskjellige backend-systemer. Dette er alt fra C++, Ruby, og til JVM-baserte språk som Java, Groovy, Clojure og Kotlin. For Java og til dels Kotlin så virker defaulten å basere seg på et eller annet stort tungt rammeverk, da typisk Spring, Grails for Groovy eller Rails for Ruby. 

### Ut med store og tunge rammeverk

Siden 2015 så har backendprosjekter jeg har jobbet med vært skrevet i enten i Clojure eller Kotlin. I forhold til Java så har dette vært et utrolig positivt steg fremover. Spesielt i Clojure-verdenen så baserer man seg på å velge små, elegante og fokuserte biblioteker som gjør én ting bra, fremfor store og tunge rammeverk. Oppgraderinger kan man gjøre pr bibliotek, og min erfaring så langt er at dette ikke har skapt noen problemer eller ført til mye arbeid å få til. 

På mitt siste backendprosjekt bestemte vi oss for å gå for Kotlin. Teamet ble også enige om at vi skulle gjøre det på Clojure-måten, ved enten å skrive koden selv eller å trekke inn fokuserte og enkle biblioteker. Dette gjør at vi kan bestemme hvordan systemet designes og koden skrives fremfor å la rammeverkene langt på vei diktere hvordan vi skal gjøre det.

Det vi endte opp med å bruke var et enkelt micro web-rammeverk som heter [Jooby](https://jooby.org). Det høres kanskje litt selvmotsigende ut, men det eneste vi brukte av Jooby var å håndtere forespørsler, og returnere resultater. Grunnen til at akkurat dette rammeverket ble valgt var at vi da trodde vi hadde behov for Server Sent Events, SSE. Etter en tid så fant vi ut at det ikke var nødvendig, så vi kan bytte ut Jooby med noe annet med minimal innsats, ettersom vi bare bruker det som et bibliotek. Det eneste stedet man finner avhengigheter til Jooby er i en main klasse. 

For resten av applikasjonen så plukket vi ut bl.a [Kotlin Query](https://github.com/seratch/kotliquery) mot databasen, [Kotson](https://github.com/SalomonBrys/Kotson) for JSON og et lite antall andre biblioteker for annen funksjonalitet. Det viktigste biblioteket, eller rettere sagt samling biblioteker er [Arrow](https://arrow-kt.io), siden vi også ønsket å skive koden i en funksjonell stil. Anbefales.

Prosjektet ble en suksess. Det var enkelt å arbeide med, svært testbart, og kodebasen ble overraskende liten. Andre utviklere og team hos kunden har i ettertid vist svært stor interesse i å se hvordan vi løste oppgaven. Den eneste ulempen jeg kan ser er at det kan være uvant for utviklere som ikke har noe erfaring med funksjonell programmering, men det bør de jo lære seg uansett :-)

### Swift 

Så hva har dette med Swift å gjøre? Vel, ingenting egentlig, men les videre.

Jeg har nå jobbet med tre iOS applikasjoner hos samme kunde. Den første baserte seg på React Native, men med en gode del native kode i Swift, samt to andre native applikasjoner som jeg jobber med nå. Jeg har tidligere jobbet med [Objective-C](https://en.wikipedia.org/wiki/Objective-C) på to prosjekter hos FINN.no. Jeg likte godt å jobbe med Objective-C, men Swift er et mye mer moderne og elegant språk. Swift minner som nevnt veldig mye om Kotlin, selv om arven fra Objective-C også vises godt, men da "The Good Parts".

Min begeistring for Swift er så stor at jeg godt kunne tenke meg å bruke det på serversiden også. Jeg hadde jo hørt at Swift var tilgjengelig for Linux i tillegg til Apple sine plattformer, men jeg hadde aldri satt meg inn i hvordan det egentlig var der. 

For å teste ut dette så tenkte jeg å skrive en versjon Swift av en "mock-server" som vi benytter under utvikling av appene. Grunnen til denne mock-serveren eksisterer er at baksystemene vi går mot er veldig tunge å jobbe med, spesielt det å få satt opp riktig testdata. Jeg antar at flere kjenner seg igen i dette. Mock-serveren er skrevet i [Go](https://golang.org) av en [kollega](https://www.kodemaker.no/nils/) og har gjort hverdagen min betraktelig lettere. 

Det jeg fant ut var at det er hovedsaklig tre web-rammeverk som blir benyttet for backend Swift. Disse var [Vapor](https://vapor.codes), [Kitura](https://www.kitura.io) og [Perfect](https://perfect.org). 
Jeg bestemte meg for å teste ut [Vapor](https://vapor.codes). Serveren jeg skal skrive trenger ikke modell structer, jeg trenger heller ikke å persistere data. Det jeg trengte var å kunne lese inn noen json filer, kanskje manipulere litt på de, og så returnere et resultat. 

For min del så er ulempen med Vapor at man forventer at json som man skal returnere genereres fra modell structer, så i stedet for å få hjelp av rammeverket, så motarbeidet det meg. Den endelige løsningen ble ikke så ille, men jeg hadde en følelse om at det kunne gjøres enklere, samt at jeg satt med følelsen av at Vapor sa "My way, or the highway". Det jeg ønsket meg var en [Sinatra](https://sinatrarb.com) for Swift. 
 

Jeg testet ikke ut [Kitura](https://www.kitura.io), men dokumentasjonen gav meg litt av samme følelse. Derimot så virket [Perfect](https://perfect.org) perfekt for mitt behov. 
Jeg er langt fra ferdig med portingen av mock-serveren, men det tok meg ikke mer enn en drøy time før jeg hadde et av endepunktene klar.

Den initiell byggetiden for de to rammeverkene jeg testet var som Swift ellers, ikke superraskt, men senere bygg tar mye kortere tid. Det er snakk om mindre enn ett sekund, og oppstarttiden er også snakk om mindre enn ett sekund på mitt lille prosjekt. Alle tre rammeverkene baserer seg på [Swift Package Manager](https://swift.org/package-manager/) som støtter inkrementelle og parallelle bygg. Jeg har ennå ikke nok erfaring med rammeverkene til å gi noen bedre tall på dette.

For å bygge prosjektet med Swift Package Manager (SPM) så kjøres:

```
swift build
```

For å bygge og kjøre et target så er kommandoen:

```
swift build <target>
```

Til en mer avansert løsning så virker alle disse tre rammeverkene å ha det meste av hva man trenger for å skrive en komplett serverapplikasjon, selv om økosystemet rundt Serverside Swift har ett langt stykke til å gå før man er på samme nivå som JVM baserte systemer med tanke på utvalg av biblioteker. 

I forhold til hvilket av de tre rammeverkene jeg ville ha benyttet i et potensielt nytt backendsystem, så vil det selvfølgelig være avhengig av kravene til dette systemet. Kitura og Vapor ligner en del på hverandre. Kitura er litt mer konservativt i forhold til nye features som legges til, mens Vapor er litt mer "bleeding edge". Personlig så holder jeg allikevel en knapp på Perfect fordi det ikke virker å være så opinionated som de to andre.

###Arrow for Swift?

Vår positive erfaring med bruk av Arrow gjør at jeg gjerne skulle hatt noe lignende for Swift, men heldigvis så har [47 Degrees](https://www.47deg.com) som står bak Arrow også laget [Bow](https://bow-swift.io). Pr i dag så ligger dokumentasjonen langt bak hva Arrow har å tilby, men mye av det som er dokumentert der virker langt på vei og også gjelde for Bow. Ved bruk av disse bibliotekene så får man ett verktøy i verktøykassen som vil hjelpe Swift-utviklere til å kunne skive funkjonell kode enda mer elegant enn nå, selv om Swift i utgangpunktet har mye bra.

### Videre fremover

Jeg ser for meg at det vil ta litt tid før en kunde ber om en backend hvor man vil bruke Swift siden Java og .Net er så innarbeidet i mange miljøer, men jeg tror at man mister noe ved å være så kategorisk. Når forespørselen kommer, så vil jeg ikke ha noe problem med å si at Swift er klar for oppgaven.
