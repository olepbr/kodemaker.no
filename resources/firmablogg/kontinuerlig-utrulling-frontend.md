:title Kontinuerlig utrulling - også på klienten
:author christian
:tech [:frontend]
:published 2020-03-04

:blurb

Du har en Single Page Application (SPA). Brukerne har typisk appen åpen i
nettleseren lenge - mange dager, eller til og med uker. Samtidig sitter du og
dytter ut nye versjoner opptil flere ganger om dagen. Hvordan i alle dager skal
du sørge for at folk ikke blir sittende på en gammel frontend som kanskje har
bugs du allerede har fiksa, eller bruker en utdatert API-klient? Vel, her får du
ett forslag.

:body

Du har en Single Page Application (SPA). Brukerne har typisk appen åpen i
nettleseren lenge - mange dager, eller til og med uker. Samtidig sitter du og
dytter ut nye versjoner opptil flere ganger om dagen. Hvordan i alle dager skal
du sørge for at folk ikke blir sittende på en gammel frontend som kanskje har
bugs du allerede har fiksa, eller bruker en utdatert API-klient?

Det er mange måter å løse dette problemet på, men nå skal jeg fortelle deg om
hvordan vi gjør det på prosjektet mitt.

## Steg 1: Klare å skille to versjoner fra hverandre

For at vi skal kunne ta stilling til hvorvidt en bruker sitter på en utdatert
versjon av appen vår må vi ha en eller annen form for versjonering. Dette kan
være hva som helst, og gjerne noe som kan genereres. Det eneste kravet er at du
får en ny versjon hver gang du deployer. En git sha eller en timestamp er
tilgjengelige eksempler (se nederst for et bedre forslag).

Når du har valgt en streng som representerer en versjon må du tilgjengeliggjøre
den slik at klienten kan spørre serveren om gjeldende versjon. En lavterskel
måte å gjøre dette på er å putte strengen i en fil. Dette fungerer også med
serverless hosting av frontendapper (S3 eller lignende).

Med andre ord kommer du langt med:

```sh
git rev-parse --short HEAD > public/version.txt
```

## Steg 2: Noter kjørende versjon ved oppstart

For at appen din skal vite om det har kommet en ny versjon må den vite hva
gjeldende versjon er. Altså, idet appen din starter opp gjør du noe ala:

```js
var APP = {};

function getVersion() {
  return fetch("/version.txt")
    .then(res => res.text());
}

getVersion().then(version => APP.currentVersion = version)
```

Fordelen med denne tilnærmingen er at vi kan bruke nøyaktig samme kodesnutt for
å se etter nye versjoner. Ulempen er at versjonen ikke leveres atomisk sammen
med scriptet. Dette kan du fikse ved å bundle versjonen direkte i scriptet:

```sh
echo ';window.APP = {version:"'$(cat version.txt)'"};' >> app.js
```

## Steg 3: Sjekk for nye versjoner

Etter at appen har starta opp må den periodisk høre med serveren om det finnes
en nyere versjon. Dette kan gjøres ved å polle fila vi sjekka ved oppstart, og
sammenligne versjonen med den vi fant ved oppstart. Dersom de er forskjellige så
venter en ny og (forhåpentligvis) bedre versjon. Foreløpig skal vi kun notere
oss dette:

```js
function pollVersion() {
  getVersion().then(version => {
    if (APP.currentVersion !== version) {
      APP.needsRefresh = true;
    } else {
      setTimeout(pollVersion, 60000);
    }
  });
}

pollVersion();
```

## Steg 4: Hjelp brukeren over på ny versjon

Så er tiden kommet for å faktisk laste inn en ny versjon. Ideelt sett skulle
dette skjedd så fort som mulig, men hva om brukeren er midt i å fylle ut et
skjema?

En ganske vanlig løsning er å ha en diskret popup med "ny versjon tilgjengelig -
trykk for å oppdatere", men den er suboptimal av minst to grunner:

1. Brukeren er kanskje midt i å fylle ut et skjema, men forstår ikke at de
   mister innsatsen ved å trykke på knappen.
2. Det blir opp til brukerne når de oppgraderer.

Nei, vi må gjøre oppgraderingen automatisk, og så fort som mulig, men på et
trygt tidspunkt. Dersom du har god nok kontroll på tilstanden i grensesnittet
til å vite hvorvidt brukeren jobber med et skjema eller er igang med noe som
ikke bør avbrytes kan du kanskje gjøre en `location.reload()` så fort du har en
trygg tilstand. Men dette er risikabelt, fordi du fortsatt risikerer å miste
tilstand som er mer perifer for appen din så som scrollposisjon, merket tekst
osv.

Et tidspunkt som bør være trygt å snike inn en reload på er navigering mellom
"sider". Ved navigering kan vi sjekke `APP.needsRefresh`. Hvis den er satt gjør
vi `location.href = url`, som tvinger en reload av HTML-siden (og dermed sender
brukeren til den nye versjonen) istedet for `history.pushState`:

```js
function navigate(location) {
  var url = router.url(location);

  if (APP.needsRefresh) {
    location.href = url;
  } else {
    renderApp(location);
    history.pushState(null, "", url);
  }
}
```

Og der har du det: En enkel og pragmatisk løsning som sørger for at brukerne
dine stort sett er på siste versjon av frontenden din. Denne løsningen antar at
du bruker URL-er til å adressere tilstand i appen din, men det gjør du vel
allerede?

## Forbedring 1: Forhåndslasting

Etter steg 3 kan vi gjøre en smart liten manøver: forhåndslaste versjonen vi
snart skal tvinge brukeren over på. Dersom appen er én eneste JavaScript-bundle
kan du bruke
[rel="preload"](https://developer.mozilla.org/en-US/docs/Web/HTML/Preloading_content):

```js
function preload(url, type) {
  var preloadLink = document.createElement("link");
  preloadLink.href = url;
  preloadLink.rel = "preload";
  preloadLink.as = type;
  document.head.appendChild(preloadLink);
}

preload("/ny-app.js", "script");
```

Dette fordrer at du vet URL-en til den nye versjonen: enten at den er statisk
(ikke så heldig for caching), eller at URL-en kan beregnes med
versjons-strengen, eller at versjons-endepunktet returnerer data som gir URL-en,
for eksempel:

```js
{
  "appFile": "/bundles/e35fe20b.js"
}
```

Dersom appen består av flere script-filer, og kanskje en CSS-fil eller to, så
kan du fortsatt bruke teknikken over, bare inkludere en liste med ressurser som
skal forhåndslastes i stedet:

```js
version.appResources
  .forEach(resource => preload(resource.url, resource.type));
```

En småfrekk brute-force approach til forhåndslasting er å hente ned hele appen i
en usynlig iframe som du fjerner så fort den har lasta:

```js
frame = document.createElement("iframe");
frame.src = "/";
frame.style.width = 0;
frame.style.height = 0;
frame.addEventListener("load", () => document.body.removeChild(frame));
document.body.appendChild(frame);
```

Dette forslaget bør kanskje tas med en klype salt da du vil kjøre opp en
fullt kjørende klient i bakgrunnen (selvom den fjernes igjen).

## Forbedring 2: Selektive oppdateringer

Det er tenkelig at frontenden din deployes uten at brukeren egentlig trenger å
refreshe siden. CI-serveren bygger og deployer kanskje uansett hva som skjer -
om du bare endrer Readme-en, eller legger til noen tester. For å unngå å tvinge
en refresh på brukerne i disse situasjonene kan du generere en versjons-streng
som representerer innholdet i app-bundelen heller enn tidspunktet den ble bygget
på.

Dersom appen din er én enkel JS-fil kan dette gjøres på følgende vis:

```sh
cat app.js | shasum  -a 256 | awk '{print $1}' > version.txt
```

Består appen av flere filer kan du `cat`-e alle filene sammen før du lager en
checksum.

På denne måten vil klienten kun oppdateres når det er faktiske endringer i
filene som utgjør appen.
