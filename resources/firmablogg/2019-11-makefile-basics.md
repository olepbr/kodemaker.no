:title Hva i $@ $< * er en Makefile
:author nils
:published 2019-11-20
:tech [:make]

:blurb

Etter å ha slåss mot og med maven, gradle, grunt og diverse moderne byggeverktøy så er det deilig å se at den gamle traveren `make` ofte er et bedre og enklere alternativ.

:body

Etter å ha slåss mot og med maven, gradle, grunt og diverse moderne byggeverktøy så er det deilig å se at den gamle traveren `make` ofte er et bedre og enklere alternativ.

_GNU Make_  er et byggesystem som ble laget i 1976. Make er ikke knyttet til å gjøre bygg for ett spesifikt programmeringsspråk og det er bygget opp av enkle konsepter som gjør det robust og fleksibelt slik at det enkelt kan tilpasses nye språk. 

I denne bloggposten får du med deg det viktigste du trenger for å lage en effektiv Makefile. 

## Det vi trenger for inkrementell bygging

De viktigste konseptene i en makefile er _target_ og _dependencies_.

Vi tar utgangspunkt i en veldig enkel Makefile:

    mybinary: main.go
		@echo "Building a binary"
		go build -o mybinary

Her har vi et target, `mybinary`, som kompilerer en applikasjon av kildekodefilen `main.go` som ligger i samme mappe som makefilen. Resultatet er en executable, `mybinary`, spesifisert av optionet `-o mybinary`. Targetet her heter `mybinary` og det er et poeng at det har samme navn som binær-filen det produserer. Dette targetet har en _avhengighet_ til kildekoden `main.go` i samme mappe som makefilen, spesifisert etter kolonet. Etter spesifikasjon av navn på target og avhengigheter så kjøres de kommandoene vi vil for å produsere artifaktet vårt. 

Hvis avhengighetene til et target ikke endrer seg så vil ikke make kjøre targetet, men fortelle deg at target er "up to date". Hvis det ikke finnes noen fil med samme navn som target, eller hvis noen av avhengighetene endrer seg så vil target også kjøres. `make` har altså innebygd støtte for inkrementell bygging!

Venstre side av `:` er navnet på target, høyre side er avhengighetene til target som kan være navn på filer eller navn til andre target. Make lager en graf over alle targets som må kjøres og sjekker også om det finnes sykliske avhengigheter mellom targets før den kjører. 

For å kjøre dette targetet bruker vi kommandoen 

	$ make mybinary

Hvis vi kjører denne kommandoen to ganger uten å endre kildekoden får vi beskjed om at target er oppdatert og make vil derfor ikke kjøre dette targetet:

     $ make: 'mybinary' is up to date

Siden `mybinary` er det første targetet i Makefilen vår kan vi også bygge ved å kjøre `make` uten argumenter:

     $ make
	 make: 'mybinary' is up to date

For at dette skal virke så må resultatet av targetet være en fil av samme navn som targetet. Hvis vi skulle komme til å ha et annet filnavn enn targetnavn vil make alltid kjøre targetet fordi den ikke finner en fil med samme navn som targetet. F.eks med en makefile som dette

	myapp: *.go
		go build -o mybinary

vil make alltid kompilere programmet på nytt siden ingen fil med navn `myapp` blir laget. 

Et target består av et vilkårlig antall kommandoer som kjøres i sekvens. Hvis en av disse kommandoene feiler med `exit-status != 0` så vil make feile bygget og stoppe etter den feilede kommandoen. 

## Flere targets, faktisk inkrementell bygging

Et byggescript har som regel flere targets som er avhengig av hverandre. Vi ønsker f.eks. å kjøre testene før vi bygger en applikasjon, gjøre linting, generere kode for binære schema eller annen galskap. 

Vi lager nye targets i Makefila vår og setter avhengighetene mellom targetene:

	mybinary: test main.go
		# bygge applikasjonen
		go build -o mybinary
	
    test: *test.go events
		go test
		touch test # lage en fil "test" som en  markør-fil 
                   # slik at make vet at test-targetet har blitt 
                   # kjørt ved gjentatte kjøringer av dette targetet
	
	events: *.proto
		# generer go-kode for å håndtere serialisering til og fra protobuf
		mkdir -p events && protoc *.proto --go-out=events
	
	clean: 
		# rydd opp etter oss
		rm -f events test mybinary

Her er `test`-targetet satt opp med avhengighet til alle filene som slutter på `test.go` og til `events`-targetet, dvs både main.go og alle andre filer i samme mappe. Det oppdaterte targetet `mybinary` har nå avhengighet til både `test` og `events` og vil kjøres på nytt hver gang disse targetene blir oppdatert. Test-targetet vil kun kjøre om noen av test.go-filene er oppdatert eller filen test ikke finnes fra før. `events` vil kun bli oppdatert når en `.proto` fil endrer seg.

For hygienens skyld lager vi et `clean` target som sletter filene som blir produsert av de andre targetene. Test-targetet lager en tom fil for å markere at testene er up to date. 

Hvis vi vil kjøre flere targets etter hverandre gjør vi det ved å liste opp targetene i den rekkefølgen vi vil de skal kjøres:

	make clean events mybinary

Vi vil at clean-targetet alltid skal kjøres selv om det skulle finnes en fil som heter clean. Dette kan vi si fra til make om ved å bruke det innebygde `.PHONY` targetet i toppen av fila vår som markerer at dette targetet alltid skal kjøres:

	.PHONY: clean

## What the $() $@ $< ?

Hvis du har sett noen makefiler før så har du sikkert sett en del ukjent syntaks som kan virke litt skremmende. Ikke la deg skremme! Disse kan være veldig nyttige og det er lurt å lære seg et minimum av hva disse kråketegnene betyr. En god start er [automatic variables](https://www.gnu.org/software/make/manual/html_node/Automatic-Variables.html) og [custom variables](https://www.gnu.org/software/make/manual/html_node/Using-Variables.html#Using-Variables).

En variabel kan assignes med `VARIABEL_NAVN=verdi` og aksesseres med dollartegn og paranteser `$(VARIABEL_NAVN)`. Variabler opprettes oftest på toppnivå i makefila for å kunne gjenbrukes i flere targets.

Den innebygde variabelen `$@` har alltid _navn på target_ som verdi. Den er praktisk å bruke som parameter til output for kompilatoren vår slik at vi sikrer oss at output heter det samme som target. En annen innebygd variabel er `$<`, som angir navnet på første avhenginghet til targetet  Med ett blir makefilen litt mer kryptisk når vi tar i bruk variabler, så det er en avveining om man vil bruke dette i den første makefilen man lager.

	BUILD_COMMAND=go build
	
	mybinary: main.go
		$(BUILD_COMMAND) -o $@ $<

For å kunne se hvordan make ekspanderer variablene kan vi bruke `--just-print` som option til make:

	$ make mybinary --just-print
	
	> go build -o mybinary main.go

## En smak av funksjoner

Det finnes en rekke innebygde funksjoner i make i tillegg til at man kan definere nye funksjoner. En av de funksjonene jeg bruker ofte er `if` for å sjekke om ting er på stell i utviklermiljøet før bygget går videre.

For å sjekke at f.eks docker er installert kan jeg kjøre funksjonen `if` i kombinasjon med funksjonen `shell` begynnelsen av en target som en guard:

	dockerbuild: mybinary
		$(if $(shell which docker),@echo "Found docker on path",@echo "Docker not installed"; exit 1)
		## flere kommandoer for å bygge docker-imaget ditt
		touch $@ 

Som sagt så stopper make ved første kommando som feiler med exit-status != 0. Hvis docker ikke er installert her så feiler byggescriptet med den brukervennlige feilmeldingen "Docker not installed".		

Siden docker ikke produserer en fil som make kan bruke så lager jeg her en tom fil med samme navn som target for å si til make at dette targetet har kjørt. 

Andre nyttige funksjoner er f.eks `subst` og andre string-funksjoner, `foreach` [og masse annen moro](https://www.gnu.org/software/make/manual/html_node/Functions.html).

## Wrap up

Det er ikke mange triks som skal til for å lage en effektiv makefile. Når du har fått kontroll over target/dependencies så har du fått inkrementell bygging. Med bruk av noen variabler og litt funksjoner så er du i stand til å sette sammen en badass makefile for prosjektet ditt!

Eksempel på en komplett Makefile med triksene brukt ovenfor finner du [i denne gisten](https://gist.github.com/nilsmagnus/908e518f7d1e657c2b19671d7cda41aa).
