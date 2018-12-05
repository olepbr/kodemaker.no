--------------------------------------------------------------------------------
:page-title Bring Express - Glow
:type reference
:img /references/jerker-dammbro.jpg
:logo /logos/bring.svg
:name Jerker Dammbro
:phone +46 733 90 23 33
:title Head of Digital Development & IT, Division Express
:body

Kodemaker deltog med upp till fyra utvecklare i ett team som utvecklade en ny plattform för våra budtjänster. 

Vi är mycket nöjda med kvalitén på leveransen, tempot i utvecklingen, fokuset på att utveckla säkra lösningar och engagemanget för projektet.



--------------------------------------------------------------------------------
:type illustrated-column
:title Glow - Nytt logisitikksystem
:body

Glow er et innovativt prosjekt for budbil-bransjen. Glow setter kunde (avsender) og mottaker i sentrum, der de i sanntid kan følge sin leveranse. Mottakere vil f.eks. få et ganske presist leveransetidsvindu, vil kunne kommunisere direkte med sjåfør, kjøpe tilleggstjenester, og følge med på kartet når bilen nærmer seg. Kunde kan følge opp at leveransen går etter planen i sanntid.

Glow var først et proof of concept. Dette ble skrevet om til et produksjonklart system på ca 3,5 måned. Det nye systemet måtte blant annet kunne:

- ta imot ordre
- tilby verktøy for å planlegge og optimalisere leveranser inn på ruter
- tilby sjåfører en app som verktøy for å kjøre rutene
- tilby mottakersider for sporing
- tilby kundesider for oppfølging
- integrere med mange av Bring sine eksisterende systemer

Glow var i tillegg et pilotprosjekt i Azure for Bring.

Dette ble utviklet med et fokusert lite team med eksperter over relativt kort tid. Systemet har hatt fokus på høy sikkerhet, skalering, høyest mulig oppetid og stor fleksibilitet for endringer.

--------------------------------------------------------------------------------
:type reference-meta
:title Bring - Glow
:body

Fire Kodemakere og en fast ansatt laget en ny applikasjonsportefølje fra bunnen av i løpet av få måneder. Nytt språk, ny plattform, økt sikkerhet og skalerbarhet. Prosjektet videreutvikles og blir en sentral del av Bring Express.

:team-size 4
:factoid-1 4 Kodemakere
:factoid-2 1100 timer / 02.2018-06.2018

--------------------------------------------------------------------------------
:type illustrated-column
:title God erfaring med Kotlin
:body

Det har vært sterkt fokus på å utvikle brukervennlige, skalerbare løsninger, hvor sikkerhet på flere plan og enkelt driftsmiljø også var viktig faktorer.

Hele backend av systemet er skrevet fra bunnen av i Kotlin. Kotlin brukes også i Android-appen og man får dermed nyttiggjort seg denne kompetansen på flere nivåer.

Frontend er skrevet i React. Appene er skrevet ved hjelp av React Native. Noen områder i appen har man måtte skrive native kode som man integrerer mot. Her har man så langt brukt Kotlin siden man i første omgang har man fokuserte på en Android versjon, men Swift vil også måtte benyttes på iOS versjonen når arbeidet på den startes om kort tid.

Vi har jobbet veldig tett med prosjekteiere og brukere av systemet. Det har dermed vært kort vei fra endrede og nye ønsker ble presentert til denne funksjonen fantes implementert i Glow. Er godt samarbeid med designere har sikret gode løsninger.

Det har vært et pilotprosjekt for Bring i forhold til nye driftsrutiner. Vi har innført Kubernetes for håndtering av ‘maskinparken’. Samtidig har vi har gått opp løypa for å gå fra en stor maskinparkleverandør til å bruke skyløsninger.  Prosjektet ble det første som tok i bruk Azure som en del av Bring sin satsning på denne plattformen.



--------------------------------------------------------------------------------
:type grid
:content

/azure/                       /photos/tech/azure.png
/kotlin/                          /photos/tech/kotlin.png
/kubernetes/                /photos/tech/kubernetes.png
/react/                          /photos/tech/react.png
/javascript/                             /photos/tech/js.svg

--------------------------------------------------------------------------------






:type participants
:title Kodemakere hos BN Bolig
:content

alf-kristian

Han var førstemann inn hos Bring, og var derfor den naturlige hovedrolleinnehaver i starten av prosjektet. Har jobbet mye med backend (Kotlin) og integrasjon, og ikke minst vært sentral i utviklingen av devops og overgangen til Azure. Hovedkontakt mot kunden i denne delen av prosjektet.

andre

Kom tidlig inn i prosjektet og var sentral i utviklingen av backend (Kotlin) og integrasjonsløsninger. Med sin brede erfaring har han vært med på å tilrettelegge for en god arkitektur på flere nivåer. André er også en del av teamet som utvikler Sjåfør Appen i React Native, samt deltatt i arbeidet rundt designet av brukergrensesnittet for denne Appen.

august

August har primært jobbet med frontend og app. Med sin erfaring fra kompleks logistikk og trafikk fra tidligere oppdrag, har han også hatt mye å bidra med på domenesiden. August har primært jobbet med app-delen og frontenddelen av løsningen, og herunder hatt mye dialog med designer og brukere.

stein-tore

Stein Tore har mye erfaring fra kompleks logistikk og trafikk fra tidligere oppdrag. Har hovedsakelig fokusert på frontenddelen av løsningen (React), og også tatt tak i organisatoriske oppgaver. Stein Tore er en samlende figur som sørger for at teamet jobbet godt sammen.

--------------------------------------------------------------------------------
