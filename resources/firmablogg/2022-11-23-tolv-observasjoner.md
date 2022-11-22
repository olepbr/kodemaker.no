:title 12 observasjoner gjennom 12 år som utvikler
:author sindre
:tech [:programming]
:published 2022-11-23

:blurb

Tiden flyr. Hvilke erfaringer har jeg egentlig gjort meg gjennom alle disse årene?

:body

Tiden flyr. Hvilke erfaringer har jeg egentlig gjort meg gjennom alle disse årene?

### 1. Å lage de aller beste løsningene krever god domenekunnskap

Vi kan ikke forvente å kunne lage gode arbeidsverktøy for helsepersonell uten
å forstå deres oppgaver, arbeidshverdag og behov. Det holder heller ikke at
noen få personer i teamet har denne kunnskapen; vi gjør alle stadig valg hvor
utfallet påvirkes av hvor godt vi kan domenet. Utviklere med god domenekompetanse
vil kunne sparre med fagpersoner rundt funksjonalitet og prioriteringer. Lange
utredninger blir overflødige. Og applikasjonene vi lager vil løse brukernes
faktiske behov, og bidra til å forbedre noen sin hverdag.

### 2. Det er utrolig hvor mye et lite team med dyktige folk kan få til

“For mange kokker…” er relevant i programmeringssammenheng også. Samle noen
få utviklere, designere og domene-eksperter i et rom, og nyt magien som oppstår.
I mine mest effektive oppdrag har vi vært to utviklere i teamet. Liten
kommunikasjons-overhead, begge har kontroll på hele kodebasen, og beslutninger
tas lynraskt.

### 3. Eksplisitt > implisitt

Noen ganger har vi konsepter i koden vår som gjemmer seg godt. La oss si vi har
en pappeske med høyde, bredde og lengde. Flere steder i koden multipliserer vi
disse, og resultatet brukes blant annet til å finne ut hvor mange pappesker som
får plass i en varebil. Multiplikasjonen skjuler et konsept i domenet -- som til
og med kan virke ganske sentralt. Ved å innføre _volum_ som et eksplisitt konsept,
vil vi øke lesbarheten i koden, og kanskje også oppleve mer fruktbare diskusjoner
med fagpersonene rundt oss.

### 4. Don’t Repeat Yourself (DRY) handler om kunnskap

Det er en utbredt misforståelse at DRY advarer oss mot å duplisere kodelinjer.
DRY handler om å ikke representere samme kunnskap flere steder i koden. Det
handler om å kun trenge å endre ett sted når kunnskapen endrer seg. Denne
misforståelsen kan føre til uheldige avhengigheter i kodebasen.
La oss si at vi utvikler en applikasjon hvor både produkter og kunder har _navn_.
Koden for å håndtere navn er duplisert, så vi trekker dette ut i en felles greie.
Nå har vi innført en avhengighet mellom to konsepter som hadde fortjent å utvikle
seg uavhengig av hverandre. Hva gjør vi når vi finner ut av vi ønsker å
representere kundens navn som _fornavn_ og _etternavn_?

### 5. Komplisert forretningslogikk trives best i isolasjon

I mange kodebaser blandes forretningslogikken sammen med persistering,
API-detaljer og annen infrastruktur. Ved å isolere domene-koden, vil man
redusere den kognitive belastningen som kreves for å forstå logikken.
Dette er viktig, da mange utviklere skal lese, forstå og endre logikken
i løpet av systemets levetid. I tillegg blir det veldig enkelt å skrive
enhetstester for det som tross alt er hjertet i applikasjonene våre.

### 6. Vi bør se systemene våre i bruk

Det skaper et sterkt inntrykk å se en bruker knote frem og tilbake med
funksjonalitet man trodde var selvforklarende. Å observere sluttbrukere i aksjon
vil garantert gi mange aha-opplevelser (og mange nye oppgaver på tavla). Og jeg
tror at det gjør oss til bedre utviklere. Vi utvikler empati for menneskene bak
“bruker”-begrepet. Kanskje tenker man seg litt ekstra nøye om når man gjør den
neste endringen i brukergrensesnittet.

### 7. Funksjonell programmering er fine greier

Det viser seg at prinsipper som funksjoner uten sideeffekter og ikke-muterbare data
er veldig gode. Koden blir enkel å resonnere rundt, og skriving av tester blir en
enkel øvelse i “gitt disse dataene, får jeg tilbake de dataene jeg forventer?”.
Ikke noe mikk-makk.

### 8. Kode bør organiseres etter funksjonalitet

Kodebaser har ofte pakkestrukturer som gjør det tungvint å navigere i koden.
`dto`, `db`, `modell` og `controller` hjelper oss ikke så mye når vi skal få
oversikt over en ny kodebase, eller når det skal innføres en ny betalingsmåte
i nettbutikken. Derfor er jeg stor tilhenger av å ha pakker som `bestilling`,
`betaling` og `levering` på toppnivå. Betaling kan igjen inneholde for eksempel
`faktura` og `kortbetaling`. Da blir åpenbart hvor den nye betalingsmetoden
passer inn. En annen gevinst med en slik inndeling er at avhengighetene blir
tydelige: `bestilling` har avhengigheter til `betaling` og `levering`, `betaling`
har kun eksterne avhengigheter osv.

### 9. Det er vanskelig å slå tavle og tusj

Å tegne på tavle er et veldig effektivt kommunikasjonsverktøy -- om man så
skal lære bort noe, skape en felles forståelse, eller samarbeide om å løse
et problem. Informasjon fester seg bedre når den visualiseres, og aktiv bruk
av tavle holder godt på oppmerksomheten. Og ikke minst er det en naturlig
felles arbeidsflate hvor flere kan drodle og tegne samtidig.

### 10. Det lønner seg å være nøye med språk og begreper

Det har vært begrepsforvirring i alle prosjekter jeg har jobbet i. Det kan være
at man i teamet bruker flere begreper for samme “ting”. Det kan være at vi
utviklere rett og slett bruker et domene-begrep på feil måte, eller at vi kommer
opp med egne ord der det allerede finnes et etablert språk i domenet. Og ikke
minst kan folk med ulik bakgrunn i forskjellige roller tillegge et begrep ulik
betydning. Dette kan gi relativt små utslag, som for eksempel at kode er vanskelig
å lese, men det er heller ikke uvanlig at unøyaktig bruk av begreper fører til
større misforståelser og dårlig kommunikasjon. Det kan koste mye tid og penger.
Jeg tror at vi kan minimere disse problemene ved å sette oss godt inn i domenet
vi jobber med, og ved å ha et mer bevisst forhold til språket vi bruker i koden
og i dagligtalen.

### 11. “Hvorfor?” er et kraftig verktøy

De fleste av oss har blitt bedt om å lage en knapp. Bak ønsket om ny knapp ligger
det gjemt et behov. Ved å grave oss bakover, vil vi til slutt avdekke det
grunnleggende behovet. Da kan vi utviklere bruke våre problemløsningsferdigheter
til å sparre rundt alternative løsninger. Kanskje ender vi opp med en automatisert
løsning som alle er enda mer fornøyde med?

### 12. Å skrive kode er en bare en del av jobben vår

Likevel er det i all hovedsak programmeringsspråk, biblioteker og rammeverk det
fokuseres på i jobbannonser, i artikler og på konferanser. Det virker som at vi
av og til glemmer at jobben vår også består av å lære nye domener, dele kunnskap
med andre, løse problemer, samarbeide i team, håndtere konflikter, organisere og
bryte ned arbeid, prioritere, strukturere informasjon, og mye mer. Her er det mange
erfaringer og egenskaper som er nyttige å ha som utvikler -- som kanskje ikke helt får den
oppmerksomheten de fortjener.
