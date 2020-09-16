:title Rebusløp
:author christin
:published 2020-07-16
:blurb
Kode skal være lett å lese. Men hva betyr dette i praksis? 
Hva er lett å lese og hva er det koden skal formidle til leseren? Hvorfor leser vi kode i det hele tatt?

:body 
Kode skal være lett å lese. 
Det er vi alle enige om. Men hva betyr dette i praksis? 
Hva er lett å lese og hva er det koden skal formidle til leseren? 
Hvorfor leser vi kode i det hele tatt?

(Dette har jeg [skrevet om for mange år siden](https://kranglefant.tumblr.com/post/30266564679/programming-like-a-pirate-alt-shift-m) forresten, men bare på engelsk.)

"Uncle Bob" (Robert C Martin) har kanskje vært den mest ivrige forkjemperen for dette med "clean code" og lesbarhet. 
Målet han sier vi skal oppnå er å abstrahere alt til hver del, hver funksjon, hver klasse/modul kun har én eneste oppgave. 
I en av hans presentasjoner går han gjennom hvordan man deler opp en kodesnutt i et automatisk testrammeverk han har vært med på å lage. 
Den resulterende koden inneholder kode som ser sånn her ut 

```java
if (isTestPage())
  surroundPageWithSetupsAndTeardowns()
```


Dette er helt klart lett å lese. 
Men hva er det vi får ut av å lese det? Nesten ingenting! Det inneholder ikke noen kode! Det forteller deg kun _hva_ som gjøres.
Men sier lite om _hvordan_.

Tenk litt over hvorfor du er inne og leser kode. Det er gjerne fordi du vil endre på funksjonalitet eller legge til ny funksjonalitet. 
La oss si du skal legge til eller endre et felt i et registrerings-skjema.  Hva er det du da trenger å finne ut fra koden? 
Du trenger å vite _hvordan_ registreringen fungerer. 

Når jeg går inn for å se på kode, så ser jeg først gjennom hvilke klasser/moduler som finnes. 
Og hvilke funksjoner disse har tilgjengelig. Dette er APIet til koden, og APIet bør helst vise intensjonen - altså _hva_ som gjøres.  
Dette er også alt jeg strengt tatt bør trenge å lese fra et eksternt bibliotek jeg bruker. Men for kode jeg selv skal forvalte, så trenger jeg å vite mer enn _hva_ som gjøres. 
Jeg er pokka nødt til å vite hvordan det gjøres også.

"Kode skal kunne leses som en god bok" har det blitt sagt. En god bok har en innholdsfortegnelse - med oversikt over kapitler og kanskje underkapitler. 
Dette er viktig for å kunne finne frem i boka, og vite hva den handler om, og hvordan historien er bygget opp.
Men når jeg går til kapittel 5 fordi jeg har lyst til å lese den delen av historien. Så blir jeg ikke særlig imponert hvis kapittel 5 kun inneholder 
enda en innholdsfortegnelse! Hvis jeg må gå fra innholdsfortegnelse til innholdsfortegnelse til innholdsfortegnelse kaster jeg fort boka i søpla, og skriver gjerne en litt syrlig review på Amazon som en advarsel til potensielle kjøpere. 

Altfor ofte er "god kode" akkurat slik.  Man kan bli helt gal av mindre.  Man må hoppe fra funksjon til funksjon til funksjon til en til slutt ikke lenger husker hva i huleste det var man lette etter eller hvordan man kom seg dit.
Jo lengre ned i funksjonskallene man kommer, jo mer generiske blir de også. Koblingen til domenet man faktisk jobber med blir mindre og mindre klar.  
Konsekvensene av endringer blir mindre og mindre tydelige, samtidig som de også blir mer og mer alvorlige - da sjansen for at koden blir brukt andre steder også øker.

Hvis du vil at koden din skal være lett å lese, 
ikke hakk den opp i tusenvis av deler og gjem den vekk spredd over alt, der eneste mulighet til å få den tilbake er å løse et jævla rebusløp der hver post refererer til 3 andre poster, som alle refererer til 3 andre poster igjen og igjen inn i evigheten. 
   
Når jeg skal endre på `registrer bruker`-funksjonaliteten for å legge til et nytt felt for samtykke,
så har jeg lyst til å finne en tilhørende funksjon som heter `registrer_bruker` som tar inn en struktur med parametre.  I denne funksjonen 
har jeg lyst til å se hvordan dataene i strukturen blir prosessert og lagret.  Alt trenger ikke være i samme funksjon naturligvis, 
men det er grenser for hvor mange "hopp" jeg skal måtte gjøre før jeg forstår implementasjonen.  

99% av programmerings-oppgaver er ganske enkle å implementere når det kommer til stykket.
Likevel klarer vi lage helt vanvittig mye kompleksitet for å løse dem. 
Så slår vi oss på brystet over hvor smarte vi er som klarer å forstå kompleksiteten.  
Driver vekk mindre selvsikre programmerere (jenter er typisk mindre selvsikre f.eks) ved å få dem til å tro at programmering er altfor vanskelig til at de kan gjøre en god jobb.

Ironisk nok innfører vi denne kompleksiteten nettopp i enkelhetens navn. 
Vi skal gjøre det enkelt å gjenbruke for eksempel. 
Eller enkelt å lese _hva_ som gjøres. 
Eller enkelt å kunne bytte ut implementasjoner.  
Dette er alle ting som er bra å tenke på, det er ikke det. Men de går så altfor ofte på bekostning av det som er viktigst av alt: 
At det skal være lett å forstå og endre på koden.

Gjenbruk kan være veldig fint, men det kan også bli et stort problem.  Den gjenbrukbare koden er ofte vanskeligere å lese - ettersom den ikke kan inneholde nødvendig kontekst, og ofte må bruke generiske begreper og masse konfigurasjonsmuligheter for å kunne støtte alle brukstilfellene.
Gjenbrukbar kode er også mye mer risikabel å endre på. En endring vil kunne treffe mange flere case enn det du tilfeldigvis er inne og flikker på.

Tekstforfattere er de som historisk har vært mest opptatt av lesbarhet. Og de er ikke så opptatt av gjenbruk. 
De prøver ikke febrilsk å gjenbruke setninger og referere til andre bøker og kapitler når de skal beskrive noe. 
De skriver ikke sykt vage paragrafer med masse kompleksitet bare sånn at de _kanskje_ kan bruke den samme paragrafen i neste kapittel også.
(Politiske rådgivere er kanskje ett unntak her, men de er heller ikke kjent for sine lesbare og oppklarende tekster.) 
Gode, lesbare bøker inneholder kanskje en og annen fotnote og "se kapittel 3, side 45", men de inneholder først og fremst sammenhengende tekst man leser fra øverst til nederst.

Tilbake til koden til "Uncle Bob" i begynnelsen: 
 
```java
if (isTestPage()) 
  surroundPageWithSetupsAndTeardowns()

```
Gikk man til `isTestPage()` implementasjonen sto det bare
 
```java
public boolean isTestPage() {
  page.hasAttribute("test")
}
```

Her vil det _øke_ lesbarheten å inline funksjonen:

```java
if (page.hasAttribute("test"))
  surroundPageWithSetupsAndTeardowns() 
```

Da ser man både hva som skjer og hvordan, uten å måtte context-switche til en annen metode. 
Dersom if-statementen blir for vanskelig kan man også flytte det ut i en egen variabel:

```java
boolean testPage = page.hasAttribute("test")
if (testPage)
  surroundPageWithSetupsAndTeardowns() 
```

Ja, det blir en ekstra linje, men det hjelper så mye på lesbarhet å slippe å måtte context-switche ut i en egen metode for å forstå hva som foregår.

Kom igjen folkens!  
Tør å vise litt kode i koden din! Ikke gjem den bort! Rebusløp kan du lage på fritiden. Ja, det kan virke mindre imponerende å bare ha en klasse/modul som viser alt som skjer bak et gitt use-case. 
Men drit i å imponere - gjør det enkelt!  



  