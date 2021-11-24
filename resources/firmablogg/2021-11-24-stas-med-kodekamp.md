:title Noen artige tekniske detaljer fra Kodekamp
:author magnar
:tech [:design :clojure]
:published 2021-11-24

:blurb

Jeg arrangerte Kodekamp i helga, en hjemmesnekret konkurranse i kodeskriving.
Det var intenst og gøy. Her er en liten samling fikse finurligheter og lett
underholdende anekdoter derfra.

:body

Jeg arrangerte Kodekamp i helga, en hjemmesnekret konkurranse i kodeskriving.
Det var intenst og gøy. Her er en liten samling fikse finurligheter og lett
underholdende anekdoter derfra.

## Først, to ord om opplegget

<img src="/images/blogg/kodekamp-brett.png" align="right" style="margin: 20px 0 20px 20px; width: 40%;"/>

Det hele er inspirert av [Extreme
Startup](https://github.com/rchatley/extreme_startup): Hver deltaker registrerer
seg med et endepunkt som kan motta requester. Så sender spillet ut oppgaver til
deltakernes servere som de svarer på ved å skrive kode.

I Kodekamp er oppgaven å spille et spill etter beste evne. Krigere og skyttere
beveger seg på et lite brett og prøver å ta ned motstanderne. Turneringen varer
hele dagen, med to og to deltakere som spiller mot hverandre.

Hver runde sender serveren en POST med spillets nåtilstand til den som har tur,
som svarer med ordre til enhetene sine. Noe slikt:

```js
[["unit-6", "attack", {"x":2, "y":1}],
 ["unit-7", "move",   {"x":1, "y":0}],
 ["unit-7", "move",   {"x":1, "y":1}],
 ["unit-7", "attack", {"x":1, "y":2}]]
```

Det er en artig oppgave å løse. Og stressende, særlig når det sitter 15 andre
utviklere i rommet som alle prøver å finne den beste logikken for å vinne.

Som lovet, la oss se på noen artige småting som dukket opp.

## Kamper på overheaden

<img src="/images/blogg/kodekamp-in-situ.jpg" align="right" width="35%" style="margin: 20px 0 20px 20px;"/>

Det ble tidlig klart at jeg ville ha kampene kjørende på overheaden. Men hvordan
kunne jeg sikre at spillet på skjermen og spillet som foregikk på serveren holdt
seg i sync?

Trikset jeg kom på var dette:

- Serveren spiller én action hvert 500 millisekund.
- Klienten spiller én action hvert 450 millisekund.

Da følte jeg meg litt lur. Hvis det ble nettverkstrøbbel underveis, så ville
klienten ganske raskt ta igjen serveren.

## Hva skjedde med requesten min?

<img src="/images/blogg/kodekamp-500.png" align="right" width="35%" style="margin: 20px 0 20px 20px;"/>

I kampens hete er det ekstra viktig med gode feilmeldinger. Jeg ville gi tydelig
beskjed til de som spilte når serveren deres ikke oppførte seg skikkelig.

Det viste seg å bli et dypdykk i forskjellene på `UnknownHostException`,
`ConnectException`, `SocketTimeoutException` og alt mulig ræl. Visste du at
`java.net.InetAddress` sin `isReachable` er helt på viddene? Aldeles håpløs. Jeg endte opp med:

```clj
(defn is-reachable? [host]
  (= 0 (.waitFor (.exec (java.lang.Runtime/getRuntime)
                        (str "ping -c 1 -W 1000 " host)))))
```

Ikke akkurat kryssplattform, men det funka i det minste.

## Payment Required

Det gikk en latter gjennom salen når en av spillerne, midt i en kamp, fikk opp
denne lille rakkeren av en feilmelding:

<img src="/images/blogg/kodekamp-payment.png" width="200px" style="margin: 16px 0;"/>

Det viste seg at hans versjon av [ngrok](https://ngrok.com) gjerne skulle hatt
betalt etter alle påkjenningene. 😅 Passet ikke så bra akkurat da, si.

Ngrok er forøvrig et veldig bra utviklerverktøy som lar deg eksponere en
tjeneste på lokal maskin for omverdenen. Jeg bruker det til å teste webhooks,
eller vise frem noe jeg jobber med remote.

## Ugyldige ordre

<img src="/images/blogg/kodekamp-ugyldige.png" align="right" width="40%" style="margin: 20px 0 20px 20px;"/>

Én ting er å implementere en spillmotor, en annen er å gjøre det og gi gode
feilmeldinger underveis.

Det interessante er at man på forhånd ikke kan vite om en ordre er gyldig eller
ei. Man må spille gjennom og prøve ordrene én etter én. Det endte opp med at koden
for å gi gode feilmeldinger står for 150 av de 400 linjene som implementerer
hele spillogikken.

Min favorittfeilmelding er denne:

<img src="/images/blogg/kodekamp-themselves.png" width="400px" style="margin: 16px 0;"/>

## Hvordan velge de neste to spillerne?

Ettersom man får poeng i kampene man spiller, så ville jeg gjerne sørge for at
alle fikk spille like mange kamper i løpet av dagen.

Jeg ville også sørge for at de som ikke hadde spilt på en stund fikk prøve seg.
Og at man ikke møtte samme person hele tiden. Algoritmen min ble slik:

    ms = antall millisekunder siden forrige kamp
    n = antall kamper du har spilt

    score = ms / (n * n)

Første spiller er den med høyest score. Andre spiller velges på samme måte,
med unntaket at man ikke skal møte den samme spilleren som forrige gang.

Dette fungerte bedre enn forventet. De som møtte hverandre forrige gang (samme
`ms`) ender etter kampen opp med et stort gap i millisekunder, og dermed går det
hele fint på rundgang.

## Request / response

For en del år tilbake hadde jeg en fantastisk kveld på Oslo Extreme Programming
meetup. [Johannes](https://twitter.com/jhannes) arrangerte Extreme Startup, en
konkurranse han hadde tatt med seg fra XP 2011 i Spania. Det er lenge
siden nå, men den opplevelsen glemmer jeg aldri.

Mitt eneste savn den kvelden var å kunne se request/response-paret. Serveren
bombarderte endepunktet mitt med stadig nye spørsmål, men jeg kunne ikke se dem
noe sted.

Det var selvfølgelig en del av oppgaven.

Jeg likte den delen av konseptet. Lag din egen tooling. Men i Kodekamp ville jeg
tilby litt mer hjelp til deltakerne, så de kunne fokusere på spillogikken.

<img src="/images/blogg/kodekamp-request-page.png" style="margin: 32px 0;"/>

Her ser man både request JSON-payloaden som ble sendt fra serveren, og response body
som endepunktet svarte med. Man ser listen over ugyldige ordre. Man får se
spillet tegnet opp - og kan se ordrene bli spilt gjennom med play-knappen.

Men det kuleste her er antagelig knappen nederst til høyre. "Test denne på nytt"
trigger en ny utsending av requesten fra serveren, slik at man kan implementere
en fiks, og se det funke.

## Event bus

Jeg har skrevet litt om å [bruke en event-bus i
framsiekoden](/blogg/2020-01-enkel-arkitektur/) for å snu avhengigheter og få en
ensrettet dataflyt. Kommer tilbake til det straks.

Jeg hadde en artig arkitektur for dataflyt mellom klient og server denne gangen.
Alle sidene hadde litt ulike behov for strømmende data, så da fikk de hver sin
handler. Det var altså en egen websocket-handler for hver side i løsningen. Det
minner litt om "code behind"-konseptet fra ASP.NET.

Dermed kunne jeg sende eventer fra backenden rett inn på frontenden sin
event-bus.

Ikke noe mellomledd.

Alle meldingene fra backenden; rett ut på frontendens bus.

```clj
(put! ws-channel [[:assoc-in [:player] (prep-player player)]
                  [:publish :updated-player]])
```

Jeg sier ikke at det er en passende arkitektur over alt, men det gjorde det
jammenmeg lett å sende oppdateringer fra serveren. 😄

## Heartbeat

Når man strømmer data over websocket, så er det ganske essensielt å implementere
en heartbeat. Du kan ikke akkurat stole på å få beskjed om problemer i tide fra
nettverkslaget, for å si det sånn.

Events fra backenden ble sendt rett ut på frontendens bus, ikke sant? Dermed så
heartbeat-meldingene slik ut:

```js
[]
```

En tom liste. Ingen actions. Bare heartbeat.

Da var jeg godt fornøyd med meg selv. Sånn er det når man sitter alene og
kosekoder. Må huske å klappe seg selv på skulderen innimellom.

## Det beste til slutt

Jeg har snakket en del om "functional core / imperative shell". Blant annet i
lyntalen [16 minutter om pure functions](/16-minutter-om-pure-functions/) (tatt
opp på brettspillrommet i kjelleren), og nå ganske nylig på podkasten
[Kodeskikknemda](https://kodeskikknemnda.no/ep/3-magnar-sveen-adventur-delux/).

Poenget er kort fortalt å omstrukturere arkitekturen din, slik at
business-logikken ikke hviler på databasen, men får være selve kjernen i
kodebasen - OG runtimen.

Man sender altså inn "all relevant informasjon" til den funksjonelle kjernen (en
samling pure functions), som så svarer tilbake med liste av ordre som skal
utføres. Disse utføres av det imperative skallet.

Så hva var poenget?

Jo, det er jo akkurat sånn denne konkurransen fungerer. Alle som deltar får
kjenne på gleden av å implementere en funksjonell kjerne. Etter min mening det
morsomste med programmering: Renspikka business-logikk.

Deilig.

Så hadde jeg kanskje en baktanke likevel. For jeg mener det er veien å gå. Ikke
bare i kodekonkurranser.
