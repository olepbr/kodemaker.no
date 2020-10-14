:title Konfig for Flutter-apper
:published 2020-10-14
:author nils
:tech [:flutter :dart ]

:blurb

Som utvikler med mye erfaring fra backend så vet jeg akkurat hvordan jeg liker å konfigurere applikasjonene mine.  Men hva er alternativene for mobile apper som er skrevet med [flutter](/flutter/)?

:body 

Som utvikler med mye erfaring fra backend så vet jeg akkurat hvordan jeg liker å konfigurere applikasjonene mine.  Men hva er alternativene for mobile apper som er skrevet med [flutter](/flutter/)?


## Manuell styring

Manuell endring av konfig fra dev til prod før man bygger appen er mulig. Utvikleren holder selv styr på hvilken konfig som er i bruk og endrer disse før man bygger til de ulike miljøene. F.eks urlen til rest-apiene som skal brukes. 

Ikke gjør dette. Jeg har inntrykk av at mange apper bygges på laptopen til utviklerene, fremfor reproduserbare bygg på byggservere. Som profesjonelle håndverkere vet vi at dette ikke er måten å gjøre det på. Det er en kilde til feil og vanskelig å ettergå i etterkant hvis man lurer på hva som egentlig havner i play-store eller appstore. 

Men man kan altså gjøre det ved å ha flere konfiger tilgjengelig, enten i kode eller i byggeverktøy, og endre hvilken som brukes manuelt. Dette er noe du bør unngå for å ikke release feil konfig til feil miljø ved et uhell.

## Alternativ 1, flere varianter av appen

Både Android og iOS har "build-variants" som i praksis gir deg frie tøyler til å lage flere varianter av appen med ulike parametere og forskjellig grafisk uttrykk. Måten dette gjøres på varierer mellom iOS og Android, men oppførselen er omtrent det samme, der man legger til konfigurasjon for de ulike variantene i byggeverktøyet som brukes.

I flutter gjøres dette ikke i konfigurasjon av byggeverktøyet, men som en del av koden. Man oppretter ganske enkelt flere `main()`-metoder og velger hvilken main-metode man vil kjøre når man bygger:

```bash
# bygge appen for dev-miljøet
$ flutter build -t lib/main_dev.dart
```

Da står man fritt til å bruke den konfigurasjonen man selv vil i `main_dev.dart`, så lenge filen inneholder en `main()`-metode som starter appen du vil.

```dart
// snippet fra main_dev.dart

main(){
	const API_URL = "https://dev.vintilbud.no/v1";
	runApp(VintilbudApp(API_URL));

}

```

Fint, dette løser problemet og gir oss enkelt mulighet til å f.eks lage varianter som `main_nils_sin_spesifikke_super_main.dart`. Nå er det bare å holde tunga rett i munnen for hvilken main-metode du vil ha ut i prod. Kan vi gjøre det bedre?

## Alternativ 2, generer konfig

Jeg har sett flere forslag på hvordan man gjør dette, men ideen er enkel: les inn en fil og/eller miljø-variabler og generer en fil som tilgjengeliggjør disse som kode. Et enkelt shell-script eller en [Makefile](https://www.kodemaker.no/make/) er som skapt for jobben:

```Makefile
# Makefile

config:
	# target for å generere config fra miljø-variabler
	echo ' const API_URL = "$(BASE_URL)";' > lib/.env
	
build: config
	# target for å bygge til iOS, avhenger av targetet som lager config
	flutter build ios
```


## Alternativ 3, miljøvariabler!

Som backendutvikler er jeg vant til at ting eksponeres som miljøvariabler. Som utvikler må jeg bare sørge for at disse er tilgjengelig for utviklermiljøet mitt når jeg utvikler og tester lokalt. Fra og med flutter 1.17 som kom i mai 2020 så ble dette mulig også for apper som er skrevet med flutter. 

I koden refererer jeg til en miljø-variabel som en hvilken som helst backend-app:

```dart
const API_URL = String.fromEnvironment("BASE_URL", defaultValue "https://vintilbud.no/v1")
```

`String`-klassen har funksjonen innebygd, med et optional parameter, `defaultValue`, hvis du ønsker å ha noe sånt når variabelen ikke er satt. For å få tilgang til denne må vi fortelle flutter at den skal gjøres tilgjengelig når vi kjører eller bygger appen:

```bash
$ flutter build apk --dart-define=BASE_URL=https://dev.vintilbud.no/v1
```

Flutter leser inn variabelen og kompilerer den inn i appen for oss slik at vi ikke trenger å bekymre oss for miljøvariabler på telefonen vi skal installere på.

Det fine med denne måten er at de variablene du definerer med `--dart-define` også kan brukes i koden som er native for iOS og Android. 


## Hva skal jeg velge?

Det er som alt annet; smak og behag og det spørs. Jeg foretrekker alternativ 3 med miljøvariabler slik man gjør i [The Twelve Factor App](https://12factor.net/config). Fordelen med denne måten er at man holder kode og konfig separert. De som setter opp ci-serveren kan lage flere bygg med ulike variabler uten å måte endre koden. 

Alternativ 1 er kanskje lettest, men det å release en binærfil med ubrukt kode føles skittent. I tillegg vil uvedkomne lett kunne laste ned appen din og dra ut konfigen for utvikler-miljøet ditt med kommandoer som `strings` og lignende. Kanskje ikke så farlig, men unødvendig. 

Alternativ 2 er ikke så dumt, men jeg har aldri vært glad i generert kode. Ikke kode jeg selv genererer heller. 

Smak og behag. 



