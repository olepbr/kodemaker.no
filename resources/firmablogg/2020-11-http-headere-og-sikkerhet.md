:title HTTP-headere og sikkerhet
:published 2020-11-11
:author stian
:tech [:web]

:blurb

I denne bloggposten ser vi på hvordan du kan bruke standardiserte HTTP-headere til å heve sikkerheten i applikasjonen din.

:body

I denne bloggposten ser vi på hvordan du kan bruke standardiserte HTTP-headere til å heve sikkerheten i applikasjonen din.

HTTP-headere er et tema som de fleste utviklere har hørt om, men mange ikke har all verdens kjennskap til. Det skyldes gjerne flere faktorer. Mange bruker rammeverk som tilsynelatende håndterer noe av dette for deg. Andre jobber i store organisasjoner hvor det gjerne er enkeltutviklere som fikser disse tingene, og noen steder er det driftere som setter headere på eksempelvis en webserver i front av applikasjonene. 

Det er likevel ikke til å stikke under en stol at manglende kjennskap til HTTP-headere fører til at mange applikasjoner ikke blir like godt sikret som de kunne ha vært. Det er spesielt uheldig siden det som regel er ganske små grep som skal til.

## Hvordan påvirker HTTP-headere sikkerhet?

Så godt som alltid når det sendes eller hentes data fra en webserver eller tjeneste blir det satt HTTP-headere på både requesten og responsen. Noen kjente eksempler er `Content-Type` og `User-agent`. HTTP-headere settes for å bidra med metainformasjon til motparten som oftest er en server eller en nettleser.

De headerene jeg skal ta for meg her handler konkret om å heve sikkerhetsnivået. I stor grad innebærer dette at vi begrenser angrepsflatene til applikasjonene våre. Det finnes også headere som gjør det motsatte og kan være nødvendig i enkelttilfeller, men de hører hjemme i en annen bloggpost. 

Vi skal ta for oss syv ulike headere som settes av serversiden. De fleste av disse har god nettleserstøtte. Disse er:

* Strict-Transport-Security
* X-Content-Type-Options
* X-Frame-Options
* Referrer-Policy
* Clear-Site-Data
* Permissions-Policy
* Content-Security-Policy 

Til slutt skal vi se på noen som settes av nettleseren. Disse er kun støttet i Chrome i dag. Disse er:

* Sec-Fetch-Site 
* Sec-Fetch-Mode
* Sec-Fetch-User 
* Sec-Fetch-Dest

Mange av headerene som settes på serversiden er såpass enkle og greie at de så godt som alltid burde benyttes. Andre krever litt mer tankearbeid. Vi starter med noen av de enkleste og bygger videre derfra. Det kan også være greit å ha et forhold til om noen av disse skal settes for alle applikasjoner i organisasjonen din. Eller om det må styres per applikasjon. Dette sier jeg fordi det kan være en vurderingssak hvorvidt headerene bør settes på en felles webserver i front. La oss komme i gang.

## Strict-Transport-Security

`Strict-Transport-Security` er nok den mest brukte av alle sikkerhets-headerene vi skal gå gjennom i dag. Den lar deg ganske enkelt styre hvorvidt HTTPS er påkrevd. Headeren settes slik:

```
Strict-Transport-Security	max-age=31536000;
```

hvor `max-age` er tiden i sekunder en nettleser skal huske at domenet kun kan nås via HTTPS. I tillegg er det mulig å legge på `includeSubDomains` som er en valgfri parameter som sier om dette også gjelder alle sub-domener. Kort sagt, denne kan du først som sist bare sette for alle applikasjoner som eksponeres ut. 

Merk at det finnes en svakhet selv om du setter denne headeren. Nemlig i de tilfellene hvor noen aldri før har vært i kontakt med domenet. Derfor har Google laget en [tjeneste](https://hstspreload.org/) støttet av de fleste nettlesere, med en liste over domener som ikke skal kunne nås uten å benytte HTTPS. For å legge til ditt domene her er det noen krav du må oppfylle, og du må også huske å legge til `preload` bakerst i headeren. `preload` er forøvrig ikke en del av spesifikasjonen for `Strict-Transport-Security` headeren.

## X-Content-Type-Options

Denne headeren kan bare settes til en verdi, nemlig:

```
X-Content-Type-Options nosniff;
```

Headeren håndhever at MIME-typen tjenesten påstår at den serverer stemmer overens med innholdet som faktisk serveres. Med andre ord vil det eksempelvis ikke tillates at det lastes noe annet enn stilsett, dersom MIME-typen er definert som `text/css`. Her kan være verdt å merke seg at det er ekstra viktig at du setter riktig `Content-Type` når du tar i bruk denne headeren på eksisterende tjenester. Ellers risikerer du å få tomme svar tilbake om du serverer noe annet enn forventet.

## X-Frame-Options

Hele hensikten med `X-Frame-Options` er å si om du tillater at nettsiden din kan presenteres i en iframe eller ikke. Noen ganger ønsker du kanskje det, men som regel ikke. Målet er at sluttbrukerne ikke skal bli utsatt for såkalt clickjacking, hvor nettsiden din blir pakket inn i et annet nettsted uten at brukeren er klar over at de egentlig går via en iframe. Headeren kan settes til tre ulike verdier:

* `DENY` som hindrer all innpakking
* `SAMEORIGIN` som tillater at et nettsted pakker inn seg selv i en iframe
* `ALLOW-FROM https://eksempel.no` som lar deg definere nettsteder som kan pakke inn din nettside. 

En ting man kan bite seg merke i er at denne headeren ikke hindrer at serveren din får trafikk fra andre domener. Det er altså her kun snakk om innpakning av nettsiden i en iframe. Denne headeren begrenser heller ikke risikoen for at det kan gjøres en `window.open()` via JavaScript her, som igjen åpner en ny fane med din nettside.

## Referrer-Policy

Når brukerne navigerer bort fra ditt nettsted er det som regel ingen grunn til å avsløre særlig mye om hvor de kom fra. Spesielt ikke dersom du eksempelvis har brukerdata, sesjons-id eller lignende i URLen. Her kommer `Referrer-Policy` headeren inn. Dersom man ikke setter noe her vil `referer` si hvor du kom fra når du navigerer til et nytt domene, men ved å eksplisitt sette en strengere `Referrer-Policy` kan dette begrenses. Lovlige verdier er:

* `no-referrer` 
* `no-referrer-when-downgrade`
* `origin` 
* `origin-when-cross-origin`
* `same-origin`
* `strict-origin`
* `strict-origin-when-cross-origin`
* `unsafe-url`.

Enkelte av disse som `no-referrer` er selvforklarende, andre sørger for at du bare overfører protokoll, domene og lignende. Jeg anbefaler å ta en titt [her](https://www.w3.org/TR/referrer-policy/) for å finne et nivå som passer din applikasjon og begrenser privatlivet til dine brukere mest mulig.

## Clear-Site-Data

Dette er en header som lar deg definere hva du ønsker at nettleseren skal slette, for eksempel ved utlogging av en tjeneste. Du kan bruke:

```
Clear-Site-Data: "*"
```

for å slette data i `cookies`, `cache` og `storage`, men kan også angi hvilke av disse individuelt som skal slettes. Man kan argumentere med at dette ikke er en sikkerhets-header. Jeg har imidlertid tatt den med fordi den gir deg en ekstra mulighet til å fortelle nettleseren at den skal fjerne data som ikke lenger hører hjemme på klienten.

## Permissions-Policy

Denne headeren het tidligere `Feature-Policy`, men har nylig byttet navn og heter nå `Permissions-Policy`. Dette navnet beskriver bedre hvilke tilganger som vil bli håndhevet som følge av at denne headeren er satt. Dette er nok den mest avanserte headeren vi har sett på så langt, og gir mange konfigurasjonsmuligheter der vi bare skal skrape i overflaten. Headeren lar deg definere ut i fra et sett av egenskaper, om din nettside tillater bruk av disse. Et eksempel på setting av denne headeren kan være:

```
Permissions-Policy: microphone ('self'); geolocation ();
```

Her spesifiserer du eksplisitt at mikrofonen kan brukes av eget nettsted, mens geolocation ikke kan brukes i noen som helst kontekst. At man definerer en tom liste her betyr altså at man ikke tillater noen bruk. Egenskapene i eksempelet mitt er noen utvalgte av et [knippe muligheter](https://www.w3.org/TR/permissions-policy-1/). Flere vil sannsynligvis bli lagt til på sikt. Det kan imidlertid være verdt å merke seg at nettleserstøtten ikke er spesielt god enda, og enn så lenge bør man vurdere om `Feature-Policy` skal benyttes i stedet. 

Det finnes i praksis fem måter du kan tildele tilganger, altså `*`, `self`, `src`, `none` og `<et egendefinert opphav>`. Dette er kanskje de tingene det er viktigst å ha kontroll på.

* `*` brukes for å si at denne egenskapen alltid skal være tilgjengelig, selv når nettstedet tas inn via en iframe
* `self` betyr at du tillater bruk av denne egenskapen på eget nettsted, samt om du pakker inn eget nettsted i en iframe.
* `src` brukes kun i forbindelse med en iframe. Altså i dette tilfellet `<iframe src="https://eksempel.no" allow="geolocation">` vil `https://eksempel.no` ha tilgang til geolocation, så lenge iframen er på dette domenet. Derimot ikke om brukeren navigerer vekk innenfor iframen. 
* `none` gir som navnet antyder ingen tilgang til denne funksjonaliteten, og det gjelder også alle iframes som eventuelt benyttes.
* `<et egendefinert opphav>` Vil si at man definerer headeren slik som vist tidligere `Permissions-Policy: geolocation (https://eksempel.no)`, og gir dette domenet tilgang til denne konkrete egenskapen.

## Content-Security-Policy 

Denne headeren lar deg hvitliste hvor du tillater at nettleseren henter ressurser fra. Dette gjør at faren for såkalte Cross Site Scripting (XSS) blir betraktelig mindre. En måte dette kan gjøres på er å si at du kun ønsker å hente skript fra ditt eget nettsted, altså slik:

```
Content-Security-Policy: script-src 'self'
```

Det finnes et knippe ulike direktiver du kan bruke til å begrense lasting av alt fra skript til fonter, media, bilder og lignende. Disse finner du [her](https://owasp.org/www-community/attacks/Content_Security_Policy), men om du bare ønsker at alt uansett skal hentes fra ditt nettsted kan du bruke direktivet `default-src` som omfatter alt. Reglene for å definere hvor du kan hente ressurser er i praksis lik som for `Permissions-Policy` headeren. Altså ved bruk av enten `*`, `self`, `none` og `<et egendefinert opphav>`.

En annen nyttig sak her er at man også kan sette opp et `report-uri` direktiv slik:

```
Content-Security-Policy: default-src 'self'; report-uri http://eksempel.no/violation-reports
```

Brudd på direktivet blir automatisk rapportert. Det er imidlertid ditt eget ansvar å sette opp en server, eller bruke en skytjeneste som kan motta disse rapportene. 

Jeg vil bare påpeke at denne headeren kanskje er spesielt viktig i prosjekter der man bruker mange JavaScript-biblioteker, som igjen drar inn en jungel av andre biblioteker. Du klarer dessverre ikke med denne headeren å hindre utviklernes overforbruk av alskens avhengigheter. Du hindrer imidlertid noen av disse kjørende bibliotekene å laste ned ondsinnet kode fra andre domener.

## Sikkerhets-headere på klienten

Som nevnt i starten av bloggposten finnes det noen sikkerhets-headere som settes av klienten. Disse støttes bare av Chrome per nå. Hensikten er å la serveren avvise mistenkelige requester. Dette betyr med andre ord at om du skal benytte disse krever det ytterligere prosessering på serversiden. Headerene er:

* Sec-Fetch-Site 
* Sec-Fetch-Mode
* Sec-Fetch-User
* Sec-Fetch-Dest

Disse gir serveren kort fortalt informasjon om hvordan denne requesten ble laget. Eksempelvis kan man da skille på om det kom fra lasting av nettsiden ved at noen har skrevet URLen rett i browseren. Hvorvidt kallet kom via JavaScript ved at man endret `document.location`. Om kallet kom fra et annet domene. Eller om requesten kom via eksempelvis en bilde-tag. 

Hvorvidt disse headerene vil få støtte i andre nettlesere gjenstår å se. Det er selvsagt noen ulemper her, som at angripere som er hakket mer sofistikerte kan sette de riktige headerene slik at mistenkelig trafikk ikke oppdages. Det krever også noe av serversiden å lage regler og håndheve disse headerene. I tillegg er det jo snakk om at det i mange tilfeller sendes over ekstra data som de fleste servere bare overser. 

## Et lite kinderegg til slutt

Hvis du har lest helt hit, skal du få en liten godbit av meg. [securityheaders.com](https://securityheaders.com) er et nettsted hvor du kan sjekke hvordan nettsidene du bygger ligger an. Mange av headerene jeg har gått gjennom i denne bloggposten sjekkes her. Dersom karakteren allerede står til A kan du klappe deg selv på skulderen (sannsynligvis visste du det før du sjekka...). Om nettsiden derimot får stryk er det bare å brette opp ermene.
