:title Løs det riktige problemet
:author stig
:published 2023-03-22
:tech [:programming]

:blurb

Jeg elsker å løse oppgaver. Det gir meg mestringsfølelse og skaperglede. 
Når du må sette deg inn i en problemstilling og så eksponere det du har forstått (og misforstått) gjennom den koden du skriver, så lærer du fort. 

... men det er ikke alltid effektivt bruk av tid. 

Jeg har min egen modell som hjelper meg til å forstå viktige sammenhenger og effektivisere skapergleden min.
:body

Mine største blemmer som systemutvikler - handler ikke om teknologi, men derimot om feil fokus og mangel på verktøy til å forstå det raskt nok. 

* Jeg har laget funksjonalitet som aldri har blitt brukt 
* Jeg har laget løsninger som er bedre på unntakshåndtering enn det å støtte de sentrale standardprosessene.

Dette handler om å forstå hva som er viktig.
Hvorfor fokuser vi av og til på feil oppgave? Hvorfor tar det av og til veldig lang tid før oppgavens kontekst forstås?
Hvordan skal du raskt komme under huden på en organisasjon og virkelig forstå DNA'et i det de driver med? 

Det finnes mye materiale om hvordan du skal analysere ett domene. Mye nyttig lesing, men jeg synes ofte mange henger seg opp i for mye implementasjonsdetaljer for tidlig. 

Det blir fort litt for mye tran og teknikker. Jeg ønsket meg en enkel modell som får plass på en serviett. 
Noe som kan ligge der i bakhodet samtidig som jeg kan gjøre det som er gøy - nemling - løse oppgaver.  

Over tid - så har jeg formet min "egen" mentale sjekkliste som hjelper meg å forstå og fokusere. 

Det er jo bare sunn fornuft, faktisk veldig gammel sunn fornuft. 
En gammel greker som het Aristoteles, ble tatt under vingene av en enda eldre greker, Platon, når han bare var 17 år gammel. 

På tenkeskolen var Aristoteles i 20 år og rakk å tenke mange kloke tanker innenfor ulike fagfelt, blant annet filosofi. 
For å forstå menneskers handlinger i fra ett filosofisk ståsted, definerte han det engelsktalende kaller **The 5 W's and one How**... eller på norsk: **Hvem, Hva, Hvor, Hvofor, Hvordan og Når**. 

Dette er en nyttig strategi for å skaffe seg innsikt, innenfor mange fagfelt - også systemutvikling. 

De ulike perspektivene er ikke likeverdige. Det er jo ikke tvil om at **Hvorfor** er viktigere enn **Hvordan**. 
Over tid, har disse boksene ramlet på plass og fått litt spisset budskap på min serviett.

## Servietten
Sånn ser servietten ut. Den har 6 bokser, der _Hvem_, _Hvorfor_ og _Hva_ danner rammene for de andre. 
![Servietten](/images/blogg/paper_all.png)

* **Hvem** - beskriver de aktørene som påvirker eller blir påvirket innenfor domenet vi jobber i.
* **Hvorfor** - beskriver det som er viktig for de ulike aktørene vi forholder oss til. 
* **Hva** - beskriver de sentrale begrepene på en slik måte at det er veldig tydelig og klart hva de betyr. 

Jeg har spisset teksten i boksene for å være litt mer tydelig i forhold til hva jeg legger i dem.
_Hvem_, _Hvorfor_ og _Hva_ har blitt: **Aktører**, **Mål/hensikt** og **Begrep**. 

Denne rammen legger føringer for de andre "boksene". 

### Aktører (hvem)
Aktører er premissgivere for vårt domene eller mottakere av det vi holder på med. 
Slike premissgivere kan være eiere, myndigheter, konkurrenter eller bransjeorganisasjoner. Sentrale bransjestandarder kan også gi føringer som er viktige å forstå.
Mottakere er de som blir påvirket av måten vi driver vår virksomhet.
Det dekker alt i fra leverandører, kunder, egne ansatte og ikke minst eiere.
Aktører er den ene veggen i modellen - fordi vi må forstå deres påvirkning på alle nivå. 

### Mål/hensikt (hvorfor)
Alle aktører har sine målsetninger.
Noen ganger kan det vere overlappende målsetninger på tvers av aktører, men de kan også være veldig ulike. 
I noen tilfeller ser vi også at ulike aktører kan ha målsetninger som er i direkte konflikt. 
De fleste prosjekter er flinke til å belyse hva som er hensikten med det vi skal gjøre, men ofte mangler bredden i forhold til å forstå alle som blir påvirket. 
Mål/hensikt er taket i modellen fordi det er overgripende for alt annet som blir definert. 

### Begrep (hva)
Vårt fag er ikke ukjent med begrep og deres betydning for vårt arbeid. 
Vi snakker om entiteter, klasser, objekter og datastrukturer som alle kan ha en eller annen kobling til en organisasjons virksomhet. 
Uansett hva vi kaller byggeklossene - så er det utrolig viktig at vi klarer å definere tydelige begrep som alle kan enes om. 

Abstrasjonsnivå er viktig. Fokuser på begrep som handler om det organisasjonen driver med .... ikke hvordan noe er implementert. 

Selv om dette er velkjent - dukker det fremdeles opp vonde modeller. 
_Hvis vi bare tilpasser denne strukturen - så kan vi gjenbruke dette_... selv om vi egentlig burde definert nye begrep. 
Typiske problembegrep er: ordre, produkt eller kunde. 
I en verdikjede dukker jo disse begrepen opp i alle ledd, men betyr kanskje helt ulike ting på reisen. 

Begrep er den andre veggen i modellen fordi de skal gjenomsyre alle andre lag. Alt som beskrives i andre lag - må benytte begrep som er tydelig definert. 

### Regler og beste praksis (hvordan)
Denne er viktig! Det er her du bestemmer om du skal lage gull eller gråstein. 
Regler og beste praksis skal beskrive hvordan en organisasjon faktisk har tenkt å innfri målsetningene til de ulike aktørene. Dette er i praksis _Hvordan_ på ett prinsippnivå. Ikke hvordan noe er implementert, men hvordan skal vi skal klare å få noe til.

* Hvilke triks har organisasjonen i ermet sammenlignet med sine konkurrenter?
* Hva er regnet for å være beste praksis?
* Hvilke lover og regler må vi forholde oss til? 

Jeg opplever at denne "boksen" ofte er underkommunisert. Noen har klart for seg hva som gjelder, men det er ikke tydelig formidlet til brukere og prosjektdeltagere.
Noen ganger oppleves det også som litt vondt å grave i regler, det er ikke alltid de er så tydelig definert. 
* Det har alltid vært sånn
* Noen har sagt at det skal være sånn
* Det er sånn det er solgt inn

Dette er ikke regler, men unnskyldninger. 

Noen ganger jobber vi med ett forretningsområde som er i støpeskjeen og da er det kanskje ikke så klart hva som er beste praksis. 
Da er det fint å ha noen hypoteser om hva som kan være lurt og sørge for at vi raskt kan håndtere endringer. 

Systemer som har litt tid på baken, må ofte tilpasses fordi noe i denne "boksen" endres. Vi kaster oss over slike endringer og lager nødvendige tilpassninger, men glemmer ofte å sjekke ut hvilke regler eller praksis som ikke lenger har livets rett. 

Regler som ikke kan knyttes til noen målsetning - må bort. 

### Hendelser og prosess (når)
Hendelser og prosess definerer alt det viktige som vil, eller kan, inntreffe og hvordan dette blir håndtert. Dette dekker _Når_ og _Hvordan_ i fra ett mer praktisk perspektiv. 

* Hva gjør aktørene som vi må håndtere?
* Hva skjer når vi har tilstandsendringer i sentrale begrep (ordre utført, ordre kansellert)?

Vår begrepsmodell må utfordres i forhold til tid, hendelser og prosesser. 
Tenk på en plan for eksempel. 
Den kan være veldig vagt definert når den opprettes. 
Kanskje bare en dato, grovt kapasitetsestimat og litt geografi. 
Etter hvert så kommer mer detaljer på plass. Vi får konkrete klokkeslett, adresser og ikke minst allokerte ressurser. 

På ett tidspunkt glir planen over til å bli en instruks som noen skal utføre. 
Utførte oppgaver må loggføres og kan danne grunnlag for at gjenstående aktiviteter replanlegges. 

Slike scenarier kan være krevende å modellere .... men desto viktigere å forstå. 

### Systemstøtte
Endelig, det som er gøy, funksjonaliteten som vi skal lage samtidig som vi forsøker å sette oss inn i ett ukjent domene. 
Det er først når vi viser vår forståelse, gjennom den koden vi har skrevet, at vi får de virkelig fruktbare diskusjonene og god læring. 

Programmering er supergøy - og enda artigere blir det når vi innimellom løfter blikket og forsøker å fylle boksen på "servietten" med innhold. 

Vi må forstå den røde tråden i fra funksjonalitet som vi lager opp gjenom boksene
og helt til målsetning og hensikt.
* Hvordan skal funksjonaliteten understøtte viktige hendelser og
prosesser.
* Hvordan griper de inn i regler og beste praksis?
* Hvordan påvirker de aktørenes målsetninger?

![Boksene](/images/blogg/maal_og_hensikt.png)

Fossefall? - På ingen måte, jeg har ingen ønsker om å komme tilbake til en tid med fossefall og forsøk på å tenke alle tanker før spaden settes i jorda. 
Det fungerte ikke før - og fungerer fremdeles ikke. 
Jeg lærer best når jeg løser oppgaver. Jeg må bare ha en enkel sjekkliste som hjelper meg til å fokusere på det som faktisk gir verdi.
