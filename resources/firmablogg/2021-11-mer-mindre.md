:title Gjør mindre, få til mer
:author christian
:tech [:frontend :design :funksjonell-programmering :clojure]
:published 2021-11-09

:blurb

En liten historie om hvordan en ny feature i et open source-bibliotek ble
nedskalert og samtidig mer nyttig.

:body

Jeg vedlikeholder et rendering-bibliotek
([Dumdom](https://github.com/cjohansen/dumdom)), som lar deg rendre DOM med
komponenter, mye likt React. Jeg er også over gjennomsnittet opptatt av å
representere så mye som mulig som rene data. Med virtuell DOM og komponenter kan
vi ha en data-representasjon av UI-et, med noen få unntak:

```clj
[:button {:style {:background "#ff0000"}
          :onClick (fn [e]
                     (println "Klikka på knappen!"))}
 "Klikk meg"]
```

Dette er helt vanlig Clojure-data: noen keywords, strenger, maps og vektorer.
Det som derimot _ikke_ er data er event-handleren. Funksjoner er opake
objekter - de kan ikke serialiseres som data, de gjør testing mer knøvlete, og
de gjør rendering-biblioteket mindre i stand til å ta effektive avgjørelser om
hva som må oppdateres i DOM-en og ikke.

Vanligvis koder jeg event-handlere til å publisere meldinger på appens
meldingsbuss (les mer om [enkel og god
frontendarkitektur](https://www.kodemaker.no/blogg/2020-01-enkel-arkitektur/)).
Altså omsetter jeg et generisk "klikk" til mer app-spesifikke meldinger:

```clj
[:form
 [:input {:value "christian@kodemaker.no"}]
 [:button
  {:onClick
   (fn [e]
     (bus/publish [[:save-in-store :email-status :saving]
                   [:ui-event :saved-email]
                   [:save-email "christian@kodemaker.no"]]))}
  "Lagre e-postadressen"]]
```

Her har noen fylt inn e-post-adressen sin, og dersom de nå trykker på knappen,
så vil appen få tre meldinger, som er implementert som "actions" et sted.

Med eksempelet over i hodet skulle Dumdom få en ny feature: innebygget støtte
for event-handlere som bare er data.

## Iterasjon #1: Dumdoms meldingsbuss

Jeg startet arbeidet med å formulere en protokoll (Clojure's interface) for
meldingsbussen i vår klientkode. Tanken var at Dumdom skulle ha en
default-implementasjon av denne som en convenience, men at det skulle være mulig
å ta med sin egen meldingsbuss dersom man ville det. Protokollen så slik ut:

```clj
(defprotocol EventBus
  (watch [_ name topic handler])

  (unwatch [_ name topic handler])

  (publish [_ topic args]))
```

Da [Anders](/anders/) så dette sa han umiddelbart: "Den protokollen beskriver
for mye!" Og han hadde helt rett: Dumdom selv skal bare kalle én av disse
funksjonene, `publish`, så hvorfor skal den mene noe om hvordan de andre ser ut?

Første nedskalering var et faktum: protokollen trenger kun å spesifisere
`publish`. Den innebyggede implementasjonen kan fortsatt tilby `watch` og
`unwatch`, men dersom du kommer med din egen implementasjon kan du abonnere på
meldinger akkurat hvordan du selv ønsker.

## Iterasjon #2: Ingen meldingsbus

Etter ytterligere diskusjoner kom vi frem til at Dumdom slettes ikke trenger en
implementasjon av en meldingsbuss. Det er et renderingbibliotek, tross alt.
Verdiforslaget i denne featuren er at du skal kunne uttrykke DOM eventhandlere
som data, slik at Dumdom kan gjøre jobben sin mer effektivt, og du slipper å
søple til dataene dine med funksjoner.

Andre nedskalering ble å fjerne meldingsbussen - Dumdom legger til rette for at
du kan bruke en, men du lager den selv.

## Iterasjon #3: Format på event-data

Dersom Dumdom skal publisere event-data må den nødvendigvis ha noen meninger om
hvordan event-data ser ut. Og for at man virkelig skal kunne klare seg uten
funksjoner må det være mulig å spesifisere at man ønsker å få med vanlige
egenskaper fra event-objektet så som `target`, verdien fra target-elementet osv.

Her er et utkast som lar deg - med rene data - definere en event-handler som får
verdien av input-feltet:

```clj
[:input
 {:onChange [[:save-in-store :email :dumdom.event/target-value]]}]
```

Idéen er at Dumdom bytter ut `:dumdom.event/target-value` med `e.target.value`
slik at meldingen appen din får ser sånn ut:

```clj
[:save-in-store :email "christian@kodemaker.no"]
```

Dette er vel og bra, men det må mange antagelser på plass for at Dumdom skal
levere dette. Det blir en del funksjonalitet å lage, en del ting å dokumentere.
Data på event-handlere må valideres slik at de ikke snubler i treskeverket.

Er vi på vei i feil retning? Det vi ønsker er å gjøre det mulig å uttrykke
event-handlere med data, ikke å diktere hvordan appen din skal sende og motta
meldinger.

Den tredje nedskaleringen kommer i form av at `publish` døpes om til
`handle-event`. Og den protokollen? En protokoll med én funksjon er en veldig
objekt-orientert måte å henge en funksjon fast i "en ting" på. I et funksjonelt
språk kan vi like gjerne bare sende med funksjonen.

## Iterasjon #4: Dumdoms topp-nivå event-handler

Da vi endelig var ved veis ende sto vi igjen med dette:

```clj
(d/render
 [:form
  [:input {:value "christian@kodemaker.no"}]
  [:button
   {:onClick [[:save-in-store :email-status :saving]
              [:ui-event :saved-email]
              [:save-email "christian@kodemaker.no"]]}
   "Lagre e-postadressen"]]

 (js/document.getElementById "app")

 {:handle-event (fn [e data]
                  (println "Event triggered")
                  (println data)
                  (println (.-target e)))})
```

Den opprinnelige idéen om å gi Dumdom en meldingsbuss er skrinlagt til fordel
for at Dumdom kan ha én event-handler ved `render`, som kalles dersom du angir
noe annet enn en funksjon på event-attributter så som `:onClick`. Men drømmen om
en meldingsbuss integrert i renderingbiblioteket er ikke død: Dumdom gir oss nå
akkurat nok verktøy til at vi kan koble på meldingsbussen vi allerede har i
appen vår.

Legg merke til at Dumdom ikke lengre trenger å vite noe som helst om
event-dataene dine. Den sender dem bare videre til handleren, så blir det opp
til deg å bruke dem til noe fornuftig. Vil du ha dataene videreformidlet til en
meldingsbuss? Kjør på! Vil du interpolere verdier fra eventet? Fint, lag en
interpoleringsfunksjon og plugg den på.

## Få til mer ved å gjøre mindre

Den resulterende
[committen](https://github.com/cjohansen/dumdom/commit/fe642dc7a1de71bb63f011823692f60698517b6d)
ble liten og søt, og grovt sett ble det lagt til ~10 linjer kode i Dumdom for
denne featuren, hvis vi ser bort fra dokumentasjon, tester, osv.

Hvorfor shippe noe så nedstrippa? Med denne endringen har Dumdom skapt
muligheter for brukerne sine, uten å gi seg hen til scope creep. Det er fortsatt
et nett lite rendering-bibliotek, og dersom du benytter deg av muligheten som
ligger i denne nye featuren, ja så gjør Dumdom jobben sin enda bedre.

"Look at all the things I'm not doing" ble det en gang sagt i en legendarisk
screencast. Den gangen handlet det om implisitte antagelser, mens her handler
det om å ikke gjøre mer enn akkurat nok. Hvis jeg fortsatt ønsker å levere en
"batteries included" løsning for meldinger til Dumdom så kan jeg lage det i et
eget bibliotek. Ved å komponere disse fra utsiden sikrer vi Dumdom en stabil
fremtid. Alle antagelsene, koden og dokumentasjonen som ble forkastet på veien
er kode som ikke vil samle nye bugs og sikkerhetshull, ikke skaper forvirring
for brukerne, og som ikke åpner døra for å akkumulere enda flere
nesten-relaterte features.

På denne måten kan Dumdom bli "ferdig". Kode som er ferdig er kode du ikke
trenger å kaste bort tid på å oppdatere, vedlikeholde og løpe etter. Da får du
frigjort tiden din til noe mer verdifullt. Du får rett og slett til mer ved at
tingene våre gjør mindre.

En stor takk til [Magnar](/magnar/) og [Anders](/anders/), som skal ha mye av
æren for at Dumdom fikk en så fin utvidelse som det fikk.
