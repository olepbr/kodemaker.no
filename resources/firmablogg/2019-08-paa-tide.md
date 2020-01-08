:title Tid - hvor vanskelig kan det være?
:author christin
:published 2019-08-14
:discussion
https://twitter.com/KODEMAKER/status/1161532881744859137
https://www.linkedin.com/posts/christin-gorman-2321aa1_tid-hvor-vanskelig-kan-detv%C3%A6re-activity-6567682241147744256-kmuf
https://www.kode24.no/guider/tid---hvor-vanskelig-kan-det-vaere/71490509
https://www.facebook.com/christin.gorman/posts/10156236732105474

:tech [:java :design]

:blurb
Jeg har drevet med programmering i snart 20 år, og i nesten hvert eneste prosjekt jeg har vært med i har vi endt opp med problemer, misforståelser og diskusjoner rundt tid. Er det mulig? Hvor vanskelig kan det være?

:body
Jeg har drevet med programmering i snart 20 år, og i nesten hvert eneste prosjekt jeg har vært med i har vi endt opp med problemer, misforståelser og diskusjoner rundt tid. Er det mulig? Hvor vanskelig kan det være?

Tid er jo bare sekunder som passerer én og én av gangen. Et sekund er en SI-enhet. Tydelig og presist definert som varigheten av 9 192 631 770 perioder av strålingen som svarer til overgangen mellom de to hyperfine nivåene av grunntilstanden til cesium-133-atomet ved null kelvin. Dette er ikke noe jeg har lyst til å drive og måle manuelt, men det å holde styr på tall som bare inkrementerer med en og en med jevne mellomrom er jo en perfekt oppgave for datamaskiner å utføre.  Og akkurat dette aspektet ved tid er også datamaskiner gode 
på. 

## Hva er klokka?
“Hva er klokka nå?” kan jeg spørre datamaskinen.

“1560252913363” svarer den. 
Hmmm… det er jo korrekt, men det var jo ikke helt det jeg mente.

Og det er her det begynner. Det er nemlig ikke passeringen av sekunder vi egentlig forholder oss til når vi snakker om tid.  Tid er to helt separate ting.  På den ene siden har vi denne enkle modellen der tid bare er sekunder som passerer. På den andre siden har vi et ulykkelig ekteskap mellom astronomi og politikk. Med en dårlig spøk av et enhets-system. Det er dette andre aspektet vi dessverre forholder oss til når vi snakker om tid i det daglige.

Når jeg spør deg hva klokka er, så svarer ikke du hvor mange sekunder som har gått siden 1. januar i 1970, det du svarer vil avhenge av hvor på himmelen sola står der du befinner deg kombinert med politiske avgjørelser for den regionen. Det avhenger av regler for sommertid/vintertid, tidssoner, og er du i Japan, spørs det også hvem den nåværende keiser er.  Dette har ingen ting med logikk og matte å gjøre.  Det å sammenstille tidsforståelse på tvers av alle kulturer på kloden, er alt annet enn enkelt. 

## Tidsenhetene
Problemene begynner allerede med enhetene vi bruker. 

### År
La oss begynne med år. Et år består av 365 ELLER 366 dager. Helt håpløst. Ingen andre enheter vi driver med er sånn. En meter er alltid 100cm. Selv når vi måler med fot og tommer er det ikke sånn at hver fjerde fot samt de delelig på 400 har en tomme ekstra!  Men sånn er det altså med tid.  Sånn kommer det til å bli frem til vi tar ansvar og fester raketter rundt om på kloden som kan holde jordrotasjonen på et fornuftig nivå.

### Måneder
Måneder er enda verre. En måned har alt mellom 28, 29, 30 og 31 dager.  Her kan vi ikke skylde på astronomi engang.  Tiden er løst basert på månens rotasjon rundt jordkloden, også kalt en lunasjon. En lunasjon er 29.5 dager.  Igjen slår det en hvor urettferdig astronomien er. Er heltall antall rotasjoner så mye å be om?  I visse kretser diskuteres det hvorvidt det er evolusjon eller intelligent design som står bak universet vårt.  Om designen er intelligent eller ikke skal jeg ikke ta stilling til her, men at den isåfall er slurvete, det kan det herske liten tvil om.  Men måten vi har håndtert denne unøyaktigheten på er mye dårligere enn den kunne vært. 

Man begynner med én måned på 31 dager, 
etterfulgt av en på 28 dager, (ELLER 29!) 
etterfulgt av en ny på 31,
så 30,
31,
30,
31,
og akkurat når man begynner å se et mønster så kommer det EN TIL PÅ 31 dager. Hva i alle dager?!?! Her finnes det ingen gode unnskyldninger. Det er rett og slett dårlig gjort.  

### Dager og timer
Dager består av 24 timer. 24? Hva er det for et antall da? 2 dusin timer, liksom? Hvorfor det? Aner ikke. Men dette gjelder jo også bare 363 (eller 364) dager i året, for en gang i året er det kun 23 og en annen gang er det 25 timer i døgnet. På grunn av innføringen av sommertid/vintertid.  Dette er selvskading på høyt nivå.  Et 100% unødvendig tiltak, som ikke hjalp med noen ting som helst, og foruten alle problemer i software, har forårsaket enorme økonomiske tap hvert eneste år siden innførelsen, da folk kommer for sent eller for tidlig i møter, for ikke å snakke om de [tusenvis av dødsfall som hvert år inntreffer når vi stiller klokka frem en time på våren.](https://www.livescience.com/40903-daylight-saving-time-affects-your-body.html).  En skandale på alle plan.  Ikke bare stiller vi klokka frem og tilbake 2 ganger i året, men vi gjør det på forskjellige tidspunkter i forskjellige regioner.  Og reglene kan endre seg når som helst. Helt nydelig! 

### Minutter
Ett nivå ned har vi minutter per time. 60. 60? Hvor kommer dette fra? 3 snes? Hvorfor det? Jo, det viser seg å komme fra Sumererne som for 4000 år siden utviklet et skriftsspråk for å holde styr på handel mellom dem og noen andre. De ble visst enige om at forholdet mellom den Sumeriske eksportvaren og importvaren til de andre hadde et forhold 1/60 (eller motsatt), så de utviklet et 60-basert tallsystem.  Så derfor har vi 60 minutter i en time. I 2019. Mmmm. 

### Sekunder
Vi har også 60 sekunder i et minutt.  Bortsett fra en og annen gang da ett sekund blir lagt til enten i siste minutt den 30. juni, eller siste minutt 31. desember.  Når dette skjer vet ingen, men blir bestemt av en komité som måler jordrotasjonen og sier fra med 6 måneders varsel at det kommer til å bli lagt til et skuddsekund.  

### Millisekunder
Først når vi er nede på millisekund-nivå begynner ting å ordne seg litt. For her ENDELIG har vi ordentlige metriske enheter. Det er 1000 millisekunder i et sekund. Takk og lov.  Bortsett fra hvis du for eksempel jobber med java, og koden din kjører idet et skuddsekund skal legges til. For da vil java runtime’en sakke ned prosesseringen, slik at de siste 1000 sekundene i døgnet varer ett millisekund ekstra.  Jobber du med .Net derimot kan det hende at det siste sekundet varer dobbelt så lenge, eller så kan det hende klokka rett og slett kommer til å være feil med ett sekund, frem til NTP-synk vil justere tiden til å komme tilbake igjen til standard UTC. Men dette avhenger helt av det underliggende operativsystemet, så det er ikke godt å vite hva som kommer til å skje i praksis. Lykke til!  Går det galt [er du i alle fall ikke alene om det.](https://blog.cloudflare.com/how-and-why-the-leap-second-affected-cloudflare-dns/)

## Standardisering
Jeg tror vi trygt kan konstatere at dette er, med god margin, det dårligste systemet for beregning av noe som helst, noen sinne.  

Takk og lov jobbes det med standardisering. UTC er en god start. UTC fungerer som en tidssone, basert på Greenwitch Mean Time (GMT), men inneholder ingen sommertidsjusteringer.  Det er ment som et standardisert svar på spørsmålet “hvor mye er klokka?”.   Om du er i Sydney, eller i Stockholm, så vil svaret være det samme. Det er klokkeslett vi kan være enige om på tvers av kloden.  
UTC står for “Coordinated Universal Time”.   
“Det gjør det vel ikke - det hadde vært CUT.” hører jeg deg si.  
 Ja, jeg skjønner at det virker rart, men det er fordi at på fransk så heter det “Temps Universel Coordonné”.  
“Ok. …nei vent litt - det blir jo TUC! Ikke UTC.” sier du kanskje nå.   
Ja, her har vi et eksempel på at man har brukt søsken-metoden for konfliktløsning: 
 “Hvis ikke jeg får det som jeg vil, skal i alle fall ikke du få det som du vil!” 

Så det ble hetende UTC. Så er det like urettferdig for alle. 

## Instant, ZonedDateTime og LocalDateTime
I det nyeste tidsbiblioteket til java, og i biblioteket [NodaTime](https://nodatime.org/) for .Net har vi 3 hovedkonsepter vi bruker for å jobbe med tid. 

Vi har `Instant`, `LocalDateTime` og `ZonedDateTime` 

`Instant` er rett og slett representasjonen av sekunder som passerer. Det er en samling funksjoner som opererer på et heltall. Tallet er antall millisekunder siden en gitt epoke. For eksempel 1. januar 1970 for java sin del.  Dette tallet er det samme uansett hvor i verden du befinner deg og forteller deg ingenting om når på dagen det var eller noe som helst.  

`LocalDate` (og `LocalDateTime`) derimot sier deg ingenting om når noe skjedde på tidslinjen over sekunder som passerer, den forteller deg “når på dagen” noe skjedde. Var det morgen eller kveld? Hvor sto sola på himmelen? “klokka 3, tirsdag 11. juni, 2019” for eksempel.  Det betyr noe annet i New York enn det gjør på Notodden. 

`ZonedDateTime` er kombinasjonen av en LocalDate - og en tidssone.  Med tidssonen følger alle politisk bestemte regler om tids-transisjoner som sommertid og offset fra UTC.  Vet man disse reglene kan man oversette en LocalDate til et Instant.  

Det finnes andre biblioteker som lar deg jobbe med tid.  Java sitt første Dato-bibliotek for eksempel, med hovedklassen `java.util.Date`.  Her hadde det vært lett å spore av, så jeg velger heller å begrense oppmerksomheten til å si følgende: 
HOLD DERE FOR GUDS SKYLD UNNA! 

.Net har også innebygde datatyper for tidshåndtering.  `DateTime`, `DateTimeOffset` osv.  Disse er betydelig bedre enn java sine opprinnelige Dato-klasser, men de støtter for eksempel ikke justering mellom sommer- og vintertid på noen god måte. Med andre ord er du nesten dømt til å skrive kode som feiler 2 ganger i året.  I tillegg er arbeid med forskjellige tidssoner heller ikke helt enkelt.  Jeg anbefaler å bruke biblioteket NodaTime, som lar deg resonnere korrekt rundt tid med en gang.  Du vil ikke angre.

## Planlegging
Men, la oss nå ta for oss noen konkrete eksempler på programmering med tid. 

La oss si du lager en applikasjon der du skal planlegge ting.  En konferanse for eksempel.  En konferanse skjer på et tidspunkt på et sted, og består selv av mange forskjellige hendelser: foredrag, lunsj, fest-middag, osv.  Hvordan skal vi modellere dette i koden? 

```java
public class ConferenceEvent {
  public final String description;
  public final Instant startTime;
  public final Instant endTime;
  
  public ConferenceEvent(String description, Instant start, Instant end) {
    this.description = description;
    this.startTime = start;
    this.endTime = end;
  }
}
// Look mom! No getters or setters! 
// Why would I need them when everything is final and immutable anyway :thinking_face: #cargocult?

```

Kan vi bruke Instant her? En konferanse skjer jo på et bestemt tidspunkt i tidslinjen.  Den åpner  “klokka 09:00, 13. desember”, men det skjer jo kun en gang. Det er ikke sånn at den skjer igjen og igjen hver 09:00 overalt på kloden.  Hvis konferansen er i Oslo, og vi tilbyr live streaming av konferansen, vil vi ikke at noen i Ottawa skal få notification i kalenderen sin klokka 09:00 i deres tidssone. Det hadde blitt feil.  

Likevel kan vi ikke bruke Instant.

Fordi vi med fremtidige datoer rett og slett ikke kan vite med sikkerhet når på tidslinjen klokka 0900, 13. desember i Oslo vil være.  Regler for sommertid kan bli justert mellom nå og da, og isåfall vil 09:00 den 13.desember intreffe på et helt annet tidspunkt.  ZonedDateTime er det eneste konseptet som kan garantere oss at vi ender opp med korrekt tidspunkt.  

## Hendelser med varighet
Hva med planlegging av regelmessige hendelser med en gitt lengde. Flyruter for eksempel. Flyselskap har regelmessige ruter mellom forskjellige destinasjoner.  Utreise fra Oslo lørdag klokka 23, ankomst Marokko 05:00 søndag morgen. Hvilke datatyper bruker vi her? Skal vi bruke en startdato og en sluttdato uttrykt med ZonedDateTime?  

```java
public class Flight {
  public final String origin;
  public final String destination;
  public final ZonedDateTime departure;
  public final ZonedDateTIme arrival;
  [...]
}
```

Nei, dette blir også feil.

Dette vil med stor sannsynlighet feile 2 ganger i året.  Når denne flyreisen starter ved overgang til vintertid, så vil tidsrommet fra 02:00-02:59 skje to ganger.  Og med mindre flyselskapet har for vane å la flyet fly rundt i sirkler i den ekstra timen, så vil flyet lande en time tidligere dagen etter.  (I Vy er dette forresten løsningen på nattog ved overgang til vintertid.  Togene står parkert i den ekstra timen, slik at de kommer frem på samme tidspunkt som ellers på året).  

På vårparten er det derimot ingen måte å unngå at flyet (eller toget) kommer frem “en time for sent”.
Skal man uttrykke gjentakende fremtidige hendelser med varighet bør man derfor lagre det som et start-tidspunkt med en angitt tidssone, samt en varighet.  Vi vet når flyet tar av, vi vet hvor lang tid det tar.  Slutt-tidspunktet blir beregnet som en funksjon av start + varighet - der funksjonen tar høyde for tidssone-reglene.

```java
public class Flight {
  public final String origin;
  public final String destination;
  public final ZonedDateTime departure;
  public final Duration duration;
  
  public ZonedDateTime findEndTime() {
    return departure.plus(duration);
  }
}
```

## Nok for denne gang
Jeg kunne fortsatt inn i evigheten med tips og overraskelser om tid. Men dette
får være nok for denne gangen. Neste gang skal jeg fortelle dere litt om lagring
av tid i relasjonsdatabaser. Håper dere har fått litt mer innsikt i hvordan man
kan jobbe med og forstå seg på tid når man jobber med kode. Takk for meg og
lykke til!
