:title Hva er frontendens domene?
:published 2023-01-11
:author christian
:tech [:frontend :web :javascript :clojurescript]

:blurb

En forutsetning for å treffe med softwaredesign er at vi har god kontroll på
domenet vi opererer i. Men hva er egentlig domenet til frontendkoden din?

:body

Nylig satt jeg meg ned for å implementere en konto-valg komponent, som ser sånn
ut:

<img alt="Bruker-komponent" src="/images/blogg/bruker-komponent.png" style="max-width: 449px">

Det skulle være grei skuring, ettersom vi allerede har implementert denne
komponenten:

<img alt="Bolig-komponent" src="/images/blogg/bolig-komponent.png" style="max-width: 447px">

Da jeg fant frem eksemplene på bruk av den siste innså jeg at dette ville bli
noe mindre rett frem enn jeg først hadde tenkt:

```clj
(FacilityToggler
 {:selected {:street-address "Gromvegen 42"
             :meter-label "Målernummer 000123456999"
             :icon :ui.icons/apartment
             :actions []}})
```

Dersom jeg skal gjenbruke denne komponenten må jeg altså sende brukerens navn
som `:street-address` og kundenummeret som `:meter-label`. Fjes, møt håndflate.

Denne `FacilityToggler`-en viser seg å ha flere triks i ermet:

<img alt="Ekspandert komponent" src="/images/blogg/ekspandert-komponent.png" style="max-width: 450px">

Altså kan den ekspanderes. Da vises det flere detaljer om det valgte anlegget,
andre anlegg du kan velge, og atpåtil et valg for å legge til anlegg. Disse
tingene er også relevante for den nye kontovelgeren jeg har fått i oppgave å
lage. I kode ser det dessverre sånn ut:

```clj
(FacilityToggler
 {:selected {:street-address "Liksomveien 27A"
             :meter-label "Målernummer 11331100"
             :meter-point-id "Målepunkt-ID 707057500012345678"
             :customer-id-label "Kundenummer 999000"
             :icon :ui.icons/apartment
             :actions []}
  :options [{:street-address "Ostepopveien 3"
             :meter-label "Målernummer 11911199"
             :icon :ui.icons/house
             :actions []}
            {:street-address "Popcorngata 12"
             :meter-label "Målernummer 11711177"
             :icon :ui.icons/house
             :actions []}]
  :actions [{:icon :ui.icons/bare_plus
             :title "Legg til bolig"
             :text "eller strømmåler"}]})
```

Denne implementasjonen snakker feil språk, fordi den opererer i feil domene. I
rendering-delen av koden er det ikke appens domenemodell som gjelder. For et
brukergrensesnitt er begrepene "street-address", "meter-label", "meter-point-id"
og andre begreper fra backendens modell totalt meningsløse. Hvordan ser en
gateadresse ut?

## Gode abstraksjoner

Gode abstraksjoner i frontendkode snakker om visuelle konsepter og
interaksjoner, ikke tekstlig innhold. For å forstå hvorfor det er sånn kan vi
filosofere litt over hva de forskjellige begrepene kan fortelle oss om hva som
foregår.

Komponenten i eksempelet over stammer fra strømverdenen. I backenden er det til
stor hjelp for meg å vite at en kunde kun kan se forbrukstall for et anlegg/en
bolig i de periodene de har en aktiv kontrakt for det. Begrepene hjelper meg å
forstå koden, og utgjør basisen for gode abstraksjoner som letter videre arbeid.

I brukergrensesnittet er ingen av disse begrepene til hjelp for meg, fordi ingen
av dem har noen iboende visuell representasjon. Ei heller representerer de
brukerinteraksjon på noe vis.

I et brukergrensesnitt har vi større glede av begreper som "boks", "knapp",
"faner", "klikk" osv. Det er irrelevant hva en knapp gjør eller hvilken tekst
som står på den når du skal skrive CSS slik at den matcher designet.

## En liten refaktorering

Tilbake til vår `FacilityToggler`. Visst brukes den i dag til å velge mellom
anlegg, men som vi så av konto-velgeren kan en tilsvarende komponent også tjene
helt andre formål. Løsningen er ikke å kopiere komponenten og bytte ut
begrepene, men å skrive om den vi har til å fungere for begge tilfellene.

Før vi starter: Hva er dette, om det ikke er en `FacilityToggler`? Vel, det er
en slags meny. Som kan åpnes og lukkes. Når du åpner den "detter den ned" over
innholdet under. Jeg vil våge å påstå at `DropdownMenu` ikke er helt på jordet.
Når navnet på komponenten sitter er vi et godt stykke på vei. Det neste er å
finne mer generiske visuelle termer for de konkrete datapunktene. Her er hva jeg
endte opp med:

```clj
;; Fra denne:

(FacilityToggler
 {:selected {:street-address "Gromvegen 42"
             :meter-label "Målernummer 000123456999"
             :icon :ui.icons/apartment
             :actions []}})

;; ...til denne:

(DropdownMenu
 {:selected {:title "Gromvegen 42"
             :details ["Målernummer 000123456999"]
             :icon :ui.icons/apartment
             :actions []}})
```

Det domenespesifikke `:street-address` har blitt til det mer
brukergrensesnitt-tilpassede `:title`. Jeg tror de fleste vil være enig i at en
tittel er den mest iøyenfallende teksten på et element. `:meter-label` ble til
`:details`. Tekst på dette nivået kan være vrient å navngi, og det kunne like
gjerne vært `:text`, `:description` eller lignende. Så lenge det går klart frem
at det er tekst som er mindre prominent enn en tittel, og navnet ikke peker på
noe domenespesifikt, er vi på rett vei.

`:icon` og `:actions` (altså hva som skjer når brukeren trykker på menyen) var
allerede passende navngitt, og får bli.

## En innebygget antagelse

Noen ganger kan komponenten havne på skjermen før dataene dens er tilgjengelige.
Da ser den sånn ut:

![Spinner mens komponenten venter](/images/blogg/bolig-spinner.gif)

Og koden ser sånn ut:

```clj
(FacilityToggler {:loading? true})
```

Ser du problemet?

Den opprinnelige menyen viser et ikon som er tilpasset boligtypen: hus,
rekkehus, leilighet eller hytte. Når vi venter på data vet vi ikke hvilken type
det er, så defaulten er et rekkehus. Men hvor er denne defaulten? Jo, den er
hardkoda inne i komponenten selvfølgelig. Det fremstår sikkert idiotisk nå (og
det er det), men i lys av at komponenten opprinnelig var tenkt som en
boligvelger er det ikke helt urimelig. Begrepene vi bruker påvirker koden vi
skriver, og et for snevert fokus resulterer ofte i innsnevret bruksområde.

## Den ferdige DropdownMenu-komponenten

Som du kanskje husker kan boligvelgeren vise flere ting. Når den ekspanderes
viser den flere opplysninger om boligen, flere boliger, og til og med et valg
for å legge til bolig. Sånn ser det ut:

```clj
(FacilityToggler
 {:selected {:street-address "Liksomveien 27A"
             :meter-label "Målernummer 11331100"
             :meter-point-id "Målepunkt-ID 707057500012345678"
             :customer-id-label "Kundenummer 999000"
             :icon :ui.icons/apartment
             :actions []}
  :options [{:street-address "Ostepopveien 3"
             :meter-label "Målernummer 11911199"
             :icon :ui.icons/house
             :actions []}
            {:street-address "Popcorngata 12"
             :meter-label "Målernummer 11711177"
             :icon :ui.icons/house
             :actions []}]
  :actions [{:icon :ui.icons/bare_plus
             :title "Legg til bolig"
             :text "eller strømmåler"}]})
```

Da jeg skrev om denne komponenten syns jeg ikke det var noe poeng å skille
"actions" fra "options". Den eneste forskjellen er at "actions" har et mindre
ikon enn de andre, men ellers er det bare forskjellige valg du kan ta. Jeg endte
opp med følgende:

```clj
(DropdownMenu
 {:selected {:title "Liksomveien 27A"
             :details ["Målernummer 11331100"
                       "Målepunkt-ID 707057500012345678"
                       "Kundenummer 999000"]
             :icon :ui.icons/apartment
             :actions []}
  :options [{:title "Ostepopveien 3"
             :details ["Målernummer 11911199"]
             :icon :ui.icons/house
             :actions []}
            {:title "Popcorngata 12"
             :details ["Målernummer 11711177"]
             :icon :ui.icons/house
             :actions []}
            {:icon :ui.icons/bare_plus
             :icon-size :small
             :title "Legg til bolig"
             :sub-title "eller strømmåler"}]})
```

Her har "actions" flytta inn i "options" og fått en ny `:icon-size` som tar seg
av den lille visuelle forskjellen. Det er også verdt å merke seg at alle de
domene-spesifikke detaljene fra `FacilityToggler` nå bare er en liste med
`details`. Det har forsvinnende lite å si for den visuelle fremstillingen hva
disse er. Så lenge vi vet at det er flere distinkte detaljer kan vi vise dem på
hver sin linje, og det er godt nok.

Etter denne refaktoreringen gikk implementasjonen fra 150 til 110 linjer kode.
Det er nesten en tredjedels forbedring i kodemengde samtidig som komponenten ble
mer fleksibel og kan brukes til flere ting. Sånn er det ofte når koden vår blir
mindre spesifikk: Det blir færre konkrete ting å håndtere, og den nye kodens mer
generelle natur åpner for bredere bruk.

## Datadrevne komponenter

Som du kanskje har gjettet ut fra kodeeksemplene i dette innlegget gjør ikke
`DropdownMenu` selv noe for å vise/skjule elementer i menyen. Dersom du ønsker å
vise menyen lukket gir du den kun `:selected`. Vil du vise den ekspandert gir du
den også `:options`. Dette holder komponenten er så "dum" som mulig - den
rendrer bare det du gir den.

For å styre komponenten har jeg andre funksjoner som sørger for å gjøre klar en
nedtrekksmeny for brukerens kontoer, eller for anlegg. Det er denne koden som
avgjør om menyen er åpnet eller lukket og andre detaljer. Dette kan jeg skrive
tester for helt uten å tenke på rendering og alle de flyktige detaljene som
følger med. [Magnar](/magnar/) har skrevet [mer om denne
tilnærmingen](/blogg/2020-01-enkel-arkitektur/) tidligere.

Ettersom komponenten selv ikke egentlig skiller på `selected` og `options` kunne
vi like gjerne bare gitt den én liste med `options`. Jeg kunne argumentert for
begge løsningene. Det viktige poenget her er at komponenten ikke befatter seg
med domenekonsepter.

## Funksjonen som oppdaterer DOM-en

Da React kom på banen for snart 10 år siden(!) var det mye snakk om at React lot
deg uttrykke grensesnittet ditt som en funksjon av dataene dine:

```js
fn(data) => vdom
```

(`vdom` er "virtuell DOM", altså det komponentene dine beskriver. React og
tilsvarende biblioteker sørger for å speile strukturen i den faktiske DOM-en på
en effektiv måte).

Jeg vil påstå at dette bildet mangler en nyanse. Dersom du ønsker en ryddig
frontend bestående av gjenbrukbare komponenter bør du etterstrebe to funksjoner:

```js
domainToUI(data) => uiData
component(uiData) => vdom
```

Dine `uiData` bør ikke inneholde et eneste domenebegrep som ikke direkte
understøtter arbeidet med å bygge et brukergrensesnitt.

Ta en titt på frontendkoden din: Hvor mange domenebegreper finner du i
komponentene dine? Er de til hjelp for å bygge et brukergrensesnitt? Eller låser
de bare enkelte komponenter unødig til spesifikke funksjoner?
