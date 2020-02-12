:title Kontinuerlig bygging med make
:author nils
:tech [:make]

:blurb

Kontinuerlig integrasjon, javel. Hva med kontinuerlig bygging når du allerede bruker make([fordi du leste min forrige bloggpost](https://www.kodemaker.no/blogg/2019-11-makefile-basics/))?

:body

Når du programmerer har du lyst på hurtig feedback. Du vil ha live reloading av webappen din og du vil at testene verifiserer at du ikke har brukket noe - så raskt som mulig. Byggesystemene på JVM-en støtter ikke dette ut av boksen, men med `make` kan vi løse oppgaven relativt enkelt. 

## v0.1 Bygg hvert 30. sekund med watch

Det enkleste du kan gjøre er å ikke endre Makefilen din i det hele tatt og bruke `watch` for å kjøre bygget ditt med jevne intervaller:

    watch -n 30 make 1>/dev/null
	
Nå bygges applikasjonen din hvert 30.sekund og kommandoen sier fra bare hvis noe er galt. Avbryt med `ctrl-c`. Ulempene lyser imot oss i form av at man må huske `watch`-kommandoen og at kommandoen ikke tar hensyn til om kildekoden er endret eller ikke. 

## v0.2 Lag et target som looper for deg

For å slippe å huske `watch` kommandoen og for at alle utviklerene til appen skal få glede av det samme kontinuerlige bygget kan vi lage et target i Makefila som gjør dette for oss. Dette gjør vi med å bruke `while`-loop fra bash og en `sleep` kommando. For å bruke while-loopen må vi eksplisitt si fra til make at vi vil bruke bash som `SHELL`:

    SHELL := /bin/bash

Deretter er det fritt frem å bruke while: 

    watch:
		while true ; do \
			sleep 30 ; \
			make 1> /dev/null  ; \
		done


Merk at while-løkka egentlig er 1 linje bash-script og vi må derfor bruke `\` når vi vil ha linjeskift. Siden alle kommandoene i while-løkka er på 1 line må de separeres med et `;`. Nå kan vi starte kontinuerlig bygging med kommandoen `make watch` og vi får beskjed når bygget feiler.

Dette ble jo litt bedre enn v0.1, men ikke mye. Vi har egentlig bare oppnådd at vi ikke trenger å huske en noe knotete `watch` kommando, men vi har beholdt de andre ulempene. 

## v0.3 Ikke bygg med mindre koden har endret seg

Hele greia med å bygge applikasjonen med jevne mellomrom lukter dårlig lang vei. Det vi egentlig vil er å bygge med en gang noe har endret seg og ikke gjøre noe ellers. For å få til det må vi dra inn et program som kan si fra når noe på filsystemet har endret seg, f.eks kildekoden vår. Til dette bruker jeg [inotifywait](https://linux.die.net/man/1/inotifywait) på linux, det er godt mulig du har lyst til å bruke `fswatch` eller noe lignende på macOS. Vi kommer tilbake til hvordan vi kan håndtere dette senere.

Vi oppdaterer watch-targetet vårt:

    watch: 
		while true ; do \
			inotifywait -qre close_write . ; \
			date ; \
			make ;\
		done

Her vil `inotifywait` vente helt til det skjer et `close_write`-event i en fil i gjeldende mappe, `.`. Siden dette ikke skjer hele tiden så liker jeg å vite når løkka kjører og har inkludert kommandoen `date` for å skrive ut et lite timestamp. Nå vil du trigge et bygg hver gang en fil blir lagret! Hvis du vil kan du selvfølgelig bytte ut `make`-kommandoen med `mvn` eller `npm` som byggekommando hvis du føler for det. 

Hvis byggeverktøyet ditt gir mye output anbefaler jeg å slenge på  ` 1> /dev/null` som vil ta vekk alt byggeverktøyet skriver til `stdout` samtidig som det som blir logget som feil fortsatt blir skrevet til `stderr` i konsollet ditt. 

## v0.4 Linux og macOS

Utviklerene av applikasjonen du vedlikeholder bruker både macOS og linux og derfor er det greit å kunne støtte begge platformer i Makefilen. I v0.3 hardkodet jeg inn `inotifywait` og overlot macOS-brukerene til seg selv, men dette lar seg ganske enkelt rette opp. 

I toppen av makefilen sjekker vi hvilken av kommandoene `fswatch` og `inotifywait` som er tilgjengelige og lager en variabel for `WATCH_EXEC` og parameterene `WATCH_ARGS`.

    WATCH_EXEC=$(if $(shell which inotifywait),"inotifywait","fswatch")
	# OBS: her ser vi etter filer som slutter på ".elm" for macOS. Endre dette til det du trenger
    WATCH_ARGS=$(if $(shell which inotifywait),"-qre" "close_write" ".","*/*.elm" "-1")


Nå kan vi oppdatere watch-targetet i Makefila vår til å virke for både linux og macOS:

    watch: 
		while true ; do \
		    $(WATCH_EXEC) $(WATCH_ARGS) ; \
			date ; \
			make ;\
		done

På den måten slipper vi en commit-war mellom de to religionene macOS og linux. Men merk at vi ikke tar høyde for at begge eller ingen av kommandoene er tilgjengelig. 


## v0.5 Kjør appen og restart hvis koden endrer seg

Ok. Siden appen din starter på noen millisekunder likevel så lar det seg lett gjøre å ha en kjørende app som restartes hver gang vi gjør kodeendringer. Hot-reloading er for pyser, her er det full restart av appen som gjelder. 

For å få til dette trenger vi 4 targets: 1) for å bygge en kjørende applikasjon 2) for å starte applikasjonen 3) watch, der det hele blir startet

For enkelhets skyld tar vi utgangspunkt i at applikasjonen din er skrevet i `go` og bygges med et enkelt `app` target:

	app: *go
		go build -o app

Og så trenger vi et target for å starte appen og ta vare på PIDen til applikasjonen:

	server.PID: app
		# drep en eventuell kjørende app først hvis vi har en PID-fil
		kill `cat server.PID` 2> /dev/null || echo "Nothing to kill before starting app" 
		#start appen og ta vare på PID i filen 'server.PID'
		./app & echo $$! > server.PID

Vi oppdaterer watch-targetet:

	watch: 
		while true ; do \ 
		    $(WATCH_EXEC) $(WATCH_ARGS) ; \
			date ; \ 
			make server.PID ; \
		done

Gratulerer, du har nå en makefil som restarter applikasjonen din hver gang kildekoden blir oppdatert. 

## Passer kontinuerlig bygging for meg?

Kontinuerlig bygging med make fungerer utmerket med applikasjoner tar kort tid å bygge. For språk som `go` og `elm` så vil byggetiden til en applikasjon ofte krype ned mot millisekunder og kontinuerlig bygging kan gi en kort feedbackloop. Det du evt mister ved å restarte appen er en eventuelt opparbeided tilstand, med mindre du håndterer det eksplisitt ved oppstart/avslutning.

Hvis du derimot har en applikasjon som tar lang tid å bygge/starte så oppleves nok ikke kontinuerlig bygging som noe verdifullt fordi mye av tiden vil gå med til å vente på bygging og restarting. Om du har en slik treg applikasjon er det kanskje på tide å se seg om etter en smidigere platform som gir en kortere feedbackloop?

Et enkelt eksempel for en app med automatisk restart finnes f.eks [her](https://github.com/nilsmagnus/komkujson). En mer avansert variant [her](https://github.com/nilsmagnus/snitch/), der appen også lagrer og gjenoppretter tilstand ved restart.
