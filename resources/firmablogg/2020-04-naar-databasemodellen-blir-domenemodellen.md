:title Når databasemodellen blir domenemodellen
:author sindre
:tech [:domain-modeling :design :ddd]
:published 2020-04-15

:blurb

Domene-kode kan med rette kalles _kjernen_ i applikasjonene 
vi utvikler, men den gjør ikke mye nytte for seg uten støttende 
infrastruktur, som lagring av data og kommunikasjon med andre systemer. 

Men hva skjer når infrastrukturen er med på å forme domene-koden?

:body

Mange av oss jobber til daglig med domener med komplekse forretningsprosesser. 
Når vi implementerer slike prosesser i IT-systemer skaper vi verdi for bedriftene 
vi jobber for, og denne koden kan derfor med rette kalles _kjernen_ i applikasjonene 
vi utvikler. Likevel vil ikke domene-kode gjøre mye nytte for seg uten støttende 
infrastruktur, som lagring av data og kommunikasjon med andre systemer. 

Men hva skjer når infrastrukturen er med på å forme domene-koden?

## Før vi fortsetter

For at vi skal slippe å snakke om ting som domene-kode, ønsker jeg å introdusere det 
litt mer fancy begrepet _domenemodell_. Domenemodellering er en prosess hvor man lærer 
om et domene og bygger opp en mental modell med relevante deler av domenet som kan 
brukes for å løse forretningsproblemer. Denne mentale modellen kalles en domenemodell. 
En domenemodell i seg selv er altså ikke noe håndfast, men den kan uttrykkes som tekst, 
som bokser og piler på en tavle, som kode osv.

La oss for enkelhets skyld bli enige om at når vi snakker om domenemodell i denne 
bloggposten, mener vi den delen av koden vår som inneholder forretningslogikken.

## Problemet

Som konsulent har jeg vært innom en del prosjekter. En ting jeg har merket meg er at når jeg har 
sett database-tabellene som brukes i et prosjekt, så vet jeg hvordan domenemodellen ser 
ut. I objektorienterte verdener konverteres tabeller direkte til domeneobjekter med for eksempel 
Hibernate, mens i den funksjonelle verden konverteres resultatet av SQL-spørringer én-til-én til 
for eksempel dataklasser i Kotlin. 

"Er dette et problem da?" lurer du, "Det høres jo deilig og enkelt ut."

"Databaser og domenemodeller har helt forskjellige formål", svarer jeg da.

Oppgaven til en database er å _lagre data_, mens domenemodeller på sin side _løser kompliserte forretningsproblemer_.

Databaser optimaliseres gjerne for ytelse og effektiv lagring. Hva optimaliseres domenemodeller for? 
Det eneste konstante er endring, som man sier, og domenemodeller kan med fordel optimaliseres for 
_rask endring_. Får vi til dette når domenemodellen er basert på databasemodellen da?

## Utvikling i en kodebase hvor databasemodellen rår

Da må vi først spørre oss hvordan man egentlig optimaliserer for rask endring. For å effektivt implementere 
nye forretningsregler og endre eksisterende, er det en forutsetning at koden vår er lett å lese og 
forstå. Det er repetert til det kjedsommelige, men vi bruker mesteparten av tiden vår foran skjermen 
på å lese kode. Og hvor lett er det egentlig å eksplisitt uttrykke forretningsregler med domenemodeller 
bestående kun av databasevennlige typer som "ints", "strings" og boolske verdier?

Gustav jobber med en nettbutikk og skal gjøre en endring relatert til produktkoder, 
som i koden representeres som "strings". Hva er det lovlige formatet på produktkodene? Er det flere lovlige formater? 
Hvor lange kan de være? Spørsmålene er mange.

Hans neste oppgave er å implementere støtte for visning av priser i forskjellige valutaer. 
Utgangspunktet er et felt for pris ("double") og et felt for valuta ("string"). Han lager seg en funksjon 
for å veksle som tar inn pris, valuta og ny valuta. Når han kaller funksjonen er han litt rask på labben 
og sender inn valuta og ny valuta i feil rekkefølge. Han blir sittende og feilsøke en god stund før han 
oppgitt oppdager feilen.

En uke senere dukker det opp en feil i produksjon som følge av at en annen utvikler har sendt inn en 
negativ pris til funksjonen. Gustav legger på validering, og han fortsetter deretter med en oppgave for 
å støtte registrering av butikkadresser. Det finnes allerede en klasse for adresse og den har til og med 
et flagg for å sette gyldighet – akkurat det han trenger. Gustav sjekker at både adressenavn, adressenummer, 
postnummer og poststed er på plass og setter gyldighetsflagget til "true". Når han er ferdig med endringen 
tester han kartvisningen av butikker i nettbutikken og legger deretter fornøyd funksjonaliteten ut i produksjon.

Dagen etter er det ramaskrik i gangene på kontoret. Alle produktsidene i nettbutikken kræsjer! Produktsidene 
viser nemlig et kart over butikker med gyldige adresser – hvor gyldighet er definert som at _adressen faktisk 
eksisterer_.

Ops.

Feilen fikses i hui og hast, men når produktsidene er oppe igjen er mange av prisene negative. En annen utvikler 
har jobbet med funksjonalitet for å regne ut produktprisene, og en feil i beregningen, samt manglende validering 
av negative priser, har ført til denne leie situasjonen.

Gustav bruker resten av dagen på å skrive validering for priser rundt omkring i kodebasen.

## La meg slå et slag for rike og eksplisitte domenemodeller

Du jobber med en nettbutikk og skal gjøre en endring relatert til produktkoder. Kodene er 
representert ved en egen type: `Produktkode`. Konstruktøren er privat og det er laget en egen funksjon for 
opprettelse av produktkoder. Funksjonen sørger for at kodene starter med to sifre og at de tre påfølgende 
tegnene er store bokstaver. Du utvider funksjonen med et nytt lovlig format og tar deg et eple.

I neste oppgave lager du deg en funksjon for å veksle mellom valutaer. Funksjonen tar inn en pris og den nye 
valutaen. Typen `Pris` består av et beløp ("double") og en valuta ("enum"), og opprettes med en funksjon som sjekker 
at beløpet er større enn 0. "Fort gjort", sier du og slår hendene sammen.

Ny dag, ny oppgave. Du skal støtte registrering av butikkadresser, og, heldig for deg, finnes det allerede en 
type `Adresse`. Etter at utviklingen er gjort åpner du kartvisningen av butikker for å teste. Men hæ? Adressene 
dine vises ikke. Det var da som bare...

Du finner funksjonen hvor adresser for kartvisningen hentes ut, og du ser at SQL-spørringen kun henter ut adresser 
med et gyldighetsflagg satt til "true". Du finner raskt ut at dette kun settes til "true" dersom adressen som 
lagres er av typen `GyldigAdresse`. I funksjonen din for registrering av butikkadresse har du allerede 
validert at adressenavn, postnummer osv. er tilstede, så du forsøker å opprette en `GyldigAdresse` i stedet for 
en `Adresse`.

Går ikke. Privat konstruktør og ingen tilgjengelig funksjon. Makan.

Det viser seg at `GyldigAdresse` kun opprettes ett sted i applikasjonen: fra en funksjon som tar inn `Adresse` 
og kaller et annet system for å sjekke at adressen faktisk finnes. Det andre systemet er tregt og har lav 
oppetid, så utviklerne før deg har valgt å gjøre denne sjekken i en periodisk jobb.

Du irriterer deg litt over at du brukte et kvarter på å finne ut av dette.

Dagen etter er det nok en rolig dag på kontoret.

## Ikke bare fryd og gammen?

Vi kan være enige om at Gustav trakk det korteste strået i historiene ovenfor. Likevel har nok Gustav og 
kollegaene utviklet første versjon av applikasjonen ganske raskt, og kanskje var det det som måtte til i 
en konkurranseutsatt bransje? Og Gustav kan enkelt slenge dataklassene rett i databasen. Så ja, prisen man 
må betale for en rik og eksplisitt domenemodell er tid, samt ekstra konvertering når man skal lagre og hente 
opp dataene sine.

Den gode nyheten er at konvertering er forholdsvis enkelt. Sett på litt god musikk, opprett noen 
databasevennlige klasser og konverter i vei!

## For å oppsummere

Domenemodeller og databaser har vidt forskjellige oppgaver, og det vil være lønnsomt å _unngå å la en 
lagringsmekanisme drive designet til en domenemodell som skal løse forretningsproblemer_.

Når det er sagt, så kan enkle, anemiske domenemodeller gjøre jobben når kun ren CRUD er nødvendig. 
Vær likevel oppmerksom på at problemer som i utgangspunktet virker enkle har en tendens til å bli mer kompliserte 
over tid.

Er du interessert i å lese mer om hvordan du kan modellere og implementere en god domenemodell? Da kan du 
gjøre som meg og dykke ned i [domenedrevet design](https://leanpub.com/theanatomyofdomain-drivendesign). 
I eksemplene ovenfor har jeg nemlig bare skrapt i overflaten av hva man kan oppnå med god domenemodellering. 

God modellering og på gjensyn!
