:title Hack slack sin URL-redirect
:published 2020-04-01
:author nils
:tech [:go :gcp]

:blurb

Har du også irritert deg over Slack sin trege redirect-funksjon? Her her en oppskrift for å redusere irritasjonen med din egen URL-redirecter.

:body

Når du deler en URL i Slack så tar Slack all trafikken gjennom en redirecter. Så når du limer inn f.eks `https://slashdot.org/`, så lager Slack linken `<a href="https://slack-redir.net/link?url=https://slashdot.org">https://slashdot.org/</a>`, slik at det ser ut som om linken går rett til slashdot, men i virkeligheten går til Slack sin URL-redirecter. Hva en slik redirecter gjør kan man bare spekulere i, men mest sannsynlig lagrer Slack statistikk om hvilke URLer du klikker på og i beste fall beskytter de deg mot virkelig onde URLer. 

Problemet er at Slack sin URL-redirecter 

* ofte er treig
* mest sannsynlig høster data om linkene jeg trykker på og knytter det til min profil
* har negativ verdi for oss som Slack-brukere

Og jeg liker ikke trege ting eller å gi fra meg data om hvilke lenker jeg trykker på. Dessverre er det lite jeg kan gjøre med linken som Slack lager, men jeg kan gjøre noe med URLen den peker på!

## /etc/hosts to the rescue

Det første jeg gjør er å legge inn en entry i `/etc/hosts` med verdien `127.0.0.1 slack-redir.net`. Dette gjør at hver gang nettleseren min (eller noen andre applikasjoner) på PC-en min prøver å resolve `slack-redir.net` så havner de på localhost. Ikke så veldig nyttig, det eneste jeg har oppnådd så langt er at Slack ikke får tak i URLen min, men ingen av URLene vil nå virke i Slack.

## Skriv din egen URL-redirecter

Dessverre finnes det ingenting på min maskin som svarer på port :80 eller :443 og pathen `/link`. Så jeg lager en egen URL-redirecter. Her oppfordrer jeg deg til å lage en redirecter i favorittspråket ditt, men her er all koden til mitt forslag, skrevet i go:

```go
import (
	"fmt"
	"log"
	"net/http"
	"os"
)

func main() {
	http.HandleFunc("/link", func(w http.ResponseWriter, r *http.Request) {
		// Sjekk om query-parameter "url" finnes og redirect dit. Hvis ikke, redirect til duckduckgo.com
		link := r.Url.Query()["url"]
		if len(link) != 1 {
			http.Redirect(w, r, "https://duckduckgo.com", http.StatusMovedPermanently)
		} else {
			http.Redirect(w, r, link[0], http.StatusMovedPermanently)
		}
	})

	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
		log.Printf("Defaulting to port %s", port)
	}

	log.Printf("Listening on port %s", port)
	
	// kommenter ut + lag ditt eget sertifikat og nøkkel for å kjøre på localhost:
	//log.Fatal(http.ListenAndServeTLS(":443", "server.crt", "server.key", nil))
	
	log.Fatal(http.ListenAndServe(fmt.Sprintf(":%s", port), nil))

}

```


Applikasjonen bruker go sine innebygde bibliotek for http og logging. Ingen 3.parts avhengigheter. Den starter på noen nanosekunder og tar ca. 7mb ferdig kompilert med komplett runtime for linux. 

For å kunne kjøre https på localhost kunne jeg generert mine egne sertifikater og nøkler og kommentert ut den nest siste linjen. Dette er ikke mye jobb, men det er slitsomt å vedlikeholde en slik service lokalt så jeg vil ha en skyløsning istedenfor.


## Deploy til skyen

Den enkleste løsningen for å deploye en slik tilstandsløs app til skyen er å deploye til google sin app-engine løsning. Det jeg trenger er enn app.yaml fil som sier at jeg bruker go og at alle URLer skal håndteres av appen:

```yml
runtime: go112

handlers:

# the app
- URL: /.*
  script: auto
```

Deretter oppretter jeg et prosjekt i [Google Cloud Platform(GCP)](https://console.cloud.google.com/). Jeg oppretter et som heter `cleanredirect`. 

Deretter spesifiserer jeg hvilket gcp-prosjekt jeg vil jobbe med og deployer med cli:

    # gcloud config set project cleanredirect
	# gcloud app deploy
	
20-30 sekunder senere svarer appen min på https://cleanredirect.appspot.com. Nå må jeg bare oppdatere `/etc/hosts` for å bruke cloud-varianten av URL-redirecteren min:


```
slack-redir.net cleanredirect.appspot.com
```


## Konklusjon

Nå har du en løsning der du får så kjappe som mulig redirecter fra Slack uten noen mellommenn. Hvis du vil så kan du jo bruke `cleanredirect.appspot.com` i din `/etc/hosts`, men du vil aldri kunne vite om jeg høster dataene dine i den URL-redirecteren. Derfor lager du deg din egen URL-redirecter i ditt favorittspråk og deployer til din favoritt skytjeneste. Du risikerer å lære en hel del og ha det moro på veien. 
