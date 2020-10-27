:title Samspill mellom generiske UI-komponenter
:published 2020-10-28
:author magnar
:tech [:frontend :design :functional-programming]

:blurb

I forbindelse med bloggposten om [en enkel frontendarkitektur som
funker](/blogg/2020-01-enkel-arkitektur/), spurte
[Ove](https://twitter.com/ovegram/status/1222819751279329281): "Hvis du har en
tekstboks og en knapp, hvem har ansvaret for å ta verdien fra tekstboksen og
sende den til eventbussen når man trykker på knappen?" Det er et betimelig
spørsmål med noen interessante detaljer.

:body

I forbindelse med bloggposten om [en enkel frontendarkitektur som
funker](/blogg/2020-01-enkel-arkitektur/), spurte
[Ove](https://twitter.com/ovegram/status/1222819751279329281): "Hvis du har en
tekstboks og en knapp, hvem har ansvaret for å ta verdien fra tekstboksen og
sende den til eventbussen når man trykker på knappen?" Det er et betimelig
spørsmål med noen interessante detaljer.

La oss starte med en liten recap: Vi bygger UI-et vårt med generiske
komponenter. De er implementasjonen av vårt visuelle uttrykk, men kjenner ikke
til domenet vårt. De kjenner ikke konteksten de brukes i. *De vet ikke hva slags
handlinger som utføres når knapper trykkes på*.

De er altså byggeklosser som blir brukt i mange kontekster. Handlingene vil være
forskjellige fra gang til gang. Vi sender actions inn til komponentene i form av
data. For eksempel:

```js
PrimaryButton({action: ["sign-in"]})
```

Her vil en sentralt registrert event handler lytte etter `"sign-in"` på bus-en,
og gjennomføre handlingen. Men hvordan får den tak i dataene sine? I dette
tilfellet, hvor kommer brukernavn og passord fra?

Jeg skrev i introen at det var et betimelig spørsmål. Det er fordi jeg ikke har
funnet én løsning som alltid funker bra. Det jeg har funnet er 1) løsningen
som til tider kan være treg, og 2) løsningen som legger litt begrensninger på
hva man kan få til.

## Løsningen som til tider kan være treg

Dette er nok den som fremstår mest riktig. Kort fortalt: Hver gang et input-felt
endrer verdi (on change, on blur, on key up?), så blir den oppdaterte verdien
kommunisert tilbake til det sentrale dataregisteret.

Det gjøres omtrent slik:

```js
TextInput({onChange: ["save", "sign-in-form/username", "@value"]})
```

Så er det opp til `TextInput`-komponenten å registrere en passende DOM event
handler, og fyre av en event på bus-en med `"@value"` erstattet av den ekte
verdien.

I main-funksjonen er det implementert en generell `"save"` event handler som
lagrer dataene og trigger en ny prepare med påfølgende oppdatering av UI-et.

Det er naturligvis dette siste som til tider kan være tregt. Dersom prepare har
mye arbeid å gjøre, så kan det bli i overkant å skulle kjøre den for alle
tastetrykk.

Noen triks:

- husk at dette nesten alltid går raskt nok, men hvis ikke ...
- kanskje det holder å oppdatere på `onBlur` istedet for `onKeyUp`?
- kanskje `prepare` kan deles i to: en for tunge operasjoner og en for raske?
- kanskje du kan bruke den alternative tilnærmingen fra neste avsnitt?

Ove spurte: "Hvem har ansvaret for å ta verdien fra
tekstboksen og sende den til eventbussen når man trykker på knappen?"

Svaret i dette tilfellet er: Dataene er allerede lagret sentralt idet man trykker
på knappen. Event handleren kan hente dem rett fra store.

## Løsningen som legger litt begrensninger på hva man kan få til

Denne løsningen oppfatter jeg som litt grisete, men den er til tider nødvendig.
Kort fortalt: Event handleren henter selv verdiene ut av DOM-en.

For at det skal fungere, så sender vi en ID inn til input-feltene:

```js
TextInput({id: "sign-in-form/username"})
```

Den eneste jobben til `TextInput` er å sette denne ID-en i DOM-en. Når event
handleren senere skal håndtere `"sign-in"`-eventet, så går den selv ut i DOM-en
og henter verdier fra feltene.

Dette er åpenbart en raskere løsning, nettopp fordi prepare ikke kjøres på nytt
og UI-et ikke oppdateres når verdiene endrer seg. Med andre ord: verdiene i
feltene er usynlige for prepare-funksjonen, og kan dermed heller ikke brukes.
Det gode, gamle toveis databinding-partytrikset kan ikke gjennomføres. Eller
kanskje mer prekært: vi kan ikke gi valideringsfeil underveis.

Her også er det noen triks:

- kanskje du faktisk kan bruke løsningen fra forrige avsnitt?
- kanskje det holder å vise valideringsfeil når man trykker på knappen?
- eller hva med en hybridmodell hvor bare de viktigste feltene blir holdt i synk?

Svaret på spørsmålet til Ove i dette tilfellet er: Det er event handleren som
har ansvaret for å binde de løse delene sammen.

## Til slutt

Jeg hører av og til innvendinger mot [denne
frontendarkitekturen](/blogg/2020-01-enkel-arkitektur/). At den ikke kan fungere
i praksis. At det blir for treigt å kjøre den prepare-funksjonen så ofte. Det
overrasker meg også, til tider, hvor raske datamaskinene og telefonene våre har
blitt. De har ingen problemer med å tygge unna litt data. Det hender at man må
gjøre noen triks, men for det meste går det mer enn fort nok.

Takk til [Ove](https://twitter.com/ovegram/status/1222819751279329281) for et bra spørsmål!
