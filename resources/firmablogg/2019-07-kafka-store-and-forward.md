:title La applikasjonen din overleve et kafka-krasj
:author nils
:tech [:kafka]
:published 2019-07-10

:blurb

Du kan sikre deg mot å miste kritiske meldinger når kafka er nede ved å bruke "store-and-forward" i de applikasjonene dine som produserer meldinger til kafka. 

:body

På mitt forrige oppdrag var jeg så heldig at jeg fikk jobbe med en trivelig gjeng på FINN. I FINN har de en event-drevet arkitektur med kafka som meldingsbuss. Selv om en slik arkitektur gir mange muligheter for skalering og løst koblede applikasjoner fører det også med seg andre utfordringer som for eksempel det å håndtere at meldingsbussen er nede eller utilgjengelig. ["Store-and-forward"](https://en.wikipedia.org/wiki/Store_and_forward) er en teknikk som vi brukte for å sikre oss mot at kafka var nede.

## Store-and-forward enkelt forklart

Store-and-forward går ut på at istedenfor å sende meldingen/pakken din med en gang, så lagrer du den i et mellomlager og lar en annen prosess ta seg av selve sendingen. Teknikken kommer fra telekom og nettverksverdenen, men kan fint brukes for alle meldingssystemer. I nettverksverdenen er dette typisk at routeren din har en liten buffer av nettverks-pakker som den samler opp og sender etterhvert som nettverket er klart. Hvis nettverket er ustabilt eller routeren ikke får en ACK(acknowledgement) på at en pakke er sendt vil routeren prøve å sende pakken på til den klarer det eller gir opp og slette pakken fra mellomlageret.

Denne teknikken brukte vi i FINN for å sende meldinger for at vi ikke skulle miste meldinger som skulle blitt sendt når kafka var nede. 

## Er kafka nede? 

Nei. Som regel er det jo ikke det, det er litt av greia til kafka at det skal være stabilt og til å stole på. Men til og med kafka må oppgraderes, og av og til krasjer det rett og slett av ulike årsaker(høy last, lite disk, feil konfigurering etc). Og da er det greit at applikasjonene du har ikke går ned, men fortsetter å virke uavhengig av tilstanden kafka måtte være i.

##  Store-and-forward prosessen

I FINN brukte vi en enkel postgres-database som mellomlager for meldingene. Alle meldingene ble serialisert til json, noe som er en enkel affære når man først bruker f.eks [avro-schemaer](https://avro.apache.org/docs/current/index.html) for meldingene. Meta-informasjon som schema-type og versjon er også lurt å lagre for å sikre at meldingene blir deserialisert riktig før sending.

Forward-jobben er en separat prosess fra resten av applikasjonen og må kjøres hyppig for å ikke skape unødvendige forsinkelser i meldingene. I vårt tilfelle kjørte applikasjonene alltid i 2 eller flere containere, så da må man sikre seg at forward-jobben ikke kjører på flere enn 1 node samtidig for å unngå at man sender duplikate meldinger. Dette gjorde vi med en lås i databasen. Etter at meldingen var bekreftet sendt slettet vi meldingen fra mellomlageret.

Om man bruker postgres, influx eller noe annet som mellomlager er ikke så viktig så lenge man kan sikre seg at kun 1 instans av forward-jobben kjøres samtidig og at man holder track på hvilke meldinger som er sendt og ikke. 

## Overordnet beskrivelse

1. Produsenter i applikasjonen sender meldinger til forward-prosessen som persisterer meldingene til et mellomlager.
2. Forward-jobben som kjører på 1 og bare 1 instans samtidig leser usendte meldinger og sender til kafka. Hvis det var vellykket oppdateres eller slettes meldingen fra mellomlageret. Hvis meldingen feilet oppdateres ikke raden for meldingen og den vil bli prøvd igjen ved neste kjøring av forward-jobben.

## Men om databasen er nede?

Hvis du implementerer store-and-forward og databasen din går ned vil ikke denne teknikken hjelpe deg lenger. Man kunne evt implementert en store-and-forward som først prøver å sende til kafka og som en fallback lagre til databasen, eller som i vårt tilfelle: innse at hvis databasen er nede så er hele applikasjonen i en tilstand der den ikke fungerer og ikke har behov for å produsere meldinger til kafka. 

## Avveininger

Store-and-forward kan være en nyttig teknikk for å unngå å miste data når kafka går ned. Men hvis det ikke er så farlig å miste en og annen melding er denne teknikken overkill. Overhead med en egen databasetabell og en jobb som skal kjøre på kun 1 av nodene dine kan være mer enn det du trenger hvis meldingene dine ikke er kritiske. 

Hvis du på den andre side har meldinger du for enhver pris ikke kan miste kan "Store-and-forward" være akkurat det som redder deg når kafka av en eller annen grunn går ned for telling.

## Hør mer!

Hvis du har lyst til å høre mer om dette og andre triks for å tryggere håndtere meldinger i en event-drevet applikasjon er det bare å møte opp [12.september 15:40 på javazone](https://2019.javazone.no/program/b960dd77-a5b0-458e-901a-5d9fa3dbd527) der jeg skal snakke mer om dette :)
