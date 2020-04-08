:title Bruk av SameSite og problemet det løser
:published 2020-04-08
:author stian
:tech [:web]

:blurb
Cookies lar oss ta vare på informasjon mellom ulike forespørsler over ellers tilstandsløs HTTP, og brukes ofte til å implementere sikkerheten i webløsninger. Det er imidlertid noen ting som er viktig å være klar over ved bruk av cookies. I denne bloggposten skal vi se mer på bruk av SameSite-attributtet.

:body
Cookies lar oss ta vare på informasjon mellom ulike forespørsler over ellers tilstandsløs HTTP, og brukes ofte til å implementere sikkerheten i webløsninger. Det er imidlertid noen ting som er viktig å være klar over ved bruk av cookies. I denne bloggposten skal vi se mer på bruk av SameSite-attributtet.

## Hold tunga rett i munnen

En cookie sendes fra serveren til klienten med HTTP-responsen. Nettleseren kan lagre denne cookien og sende den tilbake ved kommende forespørsler til det samme domenet (f.eks eksempel.no). Dette er jo fint for da vil man som nevnt beholde kontekst. Dersom en nettside på et annet domene gjør en forespørsel til eksempel.no, vil nettleseren sørge for at også denne forespørselen inneholder cookien. Det andre domenet får ikke tilgang til cookien fra eksempel.no, **men** dette kan likevel få uønskede konsekvenser.

Si for eksempel at det settes et aksess-token i en cookie, slik at eksempel.no kan sørge for at du forblir innlogget og har riktige tilganger. Det er videre gjort noen helt nødvendige tiltak som å sette `Secure` for å sikre at det kun overføres over https, og `HttpOnly` slik at ingen får tak i den via JavaScript. Er det da trygt? Vel, nja, det avhenger av nettleser og et par andre ting jeg kommer tilbake til. 

La oss se hva som potensielt kan skje om du har en eldre nettleser. Du er altså logget inn på eksempel.no, og du forviller deg inn på en nettside med onde hensikter (via en obskur e-post, eller kanskje det gjøres en submit via html som er lagt inn i et dårlig programmert kommentarfelt, eller noe så enkelt som at det lastes et bilde som egentlig gjør en GET-forespørsel mot en tjeneste på eksempel.no). Hva skjer da? Jo, nettleseren ser at forespørselen skal til eksempel.no, og at den derfor skal ta med cookien i forespørselen. Dersom nå forespørselen har gyldig innhold for tjenesten det kalles mot, er det gode muligheter for at det ondsinnede nettstedet får gjort ting det ikke burde. 

Et slikt angrep omtales som Cross Site Request Forgery eller CSRF. [Denne](https://portswigger.net/web-security/csrf) er anbefalt lesning om du vi vite mer om denne typen angrep. Måten man ofte har unngått slike angrep på har vært at man legger inn en liten hemmelighet i HTML-dokumentet som blir hentet fra serveren når brukeren går inn på eksempel.no. Dette kan for eksempel være en streng som er tilnærmet lik umulig å gjette. Når det kommer inn en forespørsel verifiserer vi om det i det hele tatt er en hemmeligheten med og at den stemmer overens med det den burde være. Hvis ikke avvises forespørselen.

Det finnes imidlertid en annen løsning på problemet som etterhvert har fått veldig god nettleserstøtte. Faktisk så god, at min påstand er at gamle nettlesere som ikke støtter dette i mange tilfeller bør avvises ved døra. Se bare [her](https://caniuse.com/#feat=same-site-cookie-attribute). Løsninger er å alltid bruke SameSite-attributtet på alle cookies.

## Hvordan fungerer SameSite?
I praksis forteller SameSite-attributtet nettleseren når den vil at cookien skal sendes med en forespørsel. Her er det mulig å sette tre ulike verdier: `None`, `Lax` og `Strict`. 

`None` er i praksis den gamle standardverdien uten at noen verdier var satt for SameSite i eldre nettlesere. Altså sendes cookien alltid med så lenge det er forespørsel til et domene hvor det eksisterer cookies. 

`Lax` er hakket strengere, og betyr at cookien blir med dersom brukeren kommer inn på domenet via en link-tag eller via `window.open()`, men ikke dersom det kommer en forespørsel fra et annet domene via POST, en iframe, AJAX eller en image-tag. Så dersom bildet hunden.jpg fra eksempel.no skal vises på annet-eksempel.no ved bruk av en bilde-tag blir ikke cookien med på forespørselen, men når en bruker klikker på en lenke til eksempel.no/hund.html fra sidene til annet-eksempel.no så vil cookien bli med. Her er det være verdt å merke seg at dersom utviklerne ved en feiltakelse tillater HTTP GET til å gjøre ikke-idempotente operasjoner er det fremdeles potensiale for CSRF, fordi disse vil ta med cookies ved bruk av LAX. Så dersom du eller rammeverket ditt som standard tillater alle HTTP-verb må du holde tunga rett i munnen!

`Strict` er som navnet antyder det strengeste alternativer. Her må forespørselen opprinnelig komme fra samme domene for at cookien skal bli med. Altså må cookien ha samme domene som det står i adresselinja på nettleseren.

Dette betyr altså at dersom du setter `Strict` eller `Lax`, så ville ikke aksess-tokenet blitt med i eksemplet jeg beskrev tidligere, noe som igjen ville betydd at forespørselen ville blitt avvist.

## Nyere nettlesere
Flere nettlesere har strammet inn praksisen betraktelig de siste månedene, og setter nå `Lax` dersom utviklerne ikke selv eksplisitt har satt SameSite. Videre må du også sette `Secure=true` i kombinasjon med `SameSite=None` for å få gammel oppførsel til å fungere. Så nyere nettleserene fjerner i stor grad det gamle CSRF sikkerhetshullet når utviklere ikke setter SameSite selv. Det betyr imidlertid også at enkelte som ikke har satt SameSite har opplevd at noe av funksjonaliteten på nettsidene sine har sluttet å fungere.

*Kan jeg da bare droppe å sette SameSite?* Svaret er nei! Gitt at du ikke bryr deg om at konsollet spyr ut meldinger om at SameSite ikke er satt, bør du likevel tenke deg om før du dropper å sette dette attributtet. For det første er det noen som bruker eldre nettlesere, og sånn kommer det til å være i overskuelig fremtid. Selv om SameSite har vært støttet i de fleste nettlesere en god stund, betyr ikke det at eldre versjoner av nettlesere kommer til å sette `Lax` automatisk. For det andre bør du ha et forhold til den nye standardverdien. I noen tilfeller kan du sette `Strict` i stedet for `Lax` og dermed heve sikkerheten enda et par hakk. For det tredje har Chrome inntil videre valgt å si at [enkelte cookies som er mindre enn to minutter gamle fremdeles skal fungerere på gamlemåten](https://chromestatus.com/feature/5088147346030592). Det vil si at dersom du ikke selv har satt SameSite-attributtet, så vil Chrome ta med denne cookien når du gjør en POST! 

## Utfordring med cookie-attributter

Det finnes flere attributter som er mulig å sette på cookies. Problemet er imidlertid at de brukes i alt for liten grad. Så sent som i 2019 hadde kun [0,1%](https://tools.ietf.org/html/draft-west-http-state-tokens-00#section-1.2/) av alle cookies satt SameSite! Så selv om muligheten finnes bruker ikke vi utviklere disse attributtene i tilstrekkelig grad. Derfor finnes det i dag flere initiativer for å gjøre det mindre frivillig å bry seg om slike ting. [Storage Access API](https://developer.mozilla.org/en-US/docs/Web/API/Storage_Access_API) er et alternativ som etter hvert har fått støtte i både Firefox og Safari.

Jeg vil avslutte med å si at det skjer mye spennende rundt begrensning av deling av informasjon på tvers av nettsider. Tredjeparts-cookies er nå truet, noe som forhåpentligvis vil gjøre det vanskeligere for store reklameaktører og andre å fotfølge deg rundt på internett. Dette kappløpet leder Safari for øyeblikket. [De blokkerer nå alle tredjeparts-cookies](https://webkit.org/blog/10218/full-third-party-cookie-blocking-and-more/), så da hjelper det ikke å sette SameSite=none lenger. Andre nettlesere følger snart etter. Firefox har siden høsten 2019 blokkert ganske mange kjente trackere.