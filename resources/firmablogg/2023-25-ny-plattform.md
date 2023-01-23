:title Ny plattform?
:author christin
:published 2023-01-25

:blurb

Vi trenger en ny plattform!  
Men hvem er "vi"?  
Og hva er det "vi" trenger egentlig?  
Hvordan passer vi på at den nye plattformen blir en suksess?  (For hvem?)

:body

En gang for noen år siden ble jeg bedt om å være med på et jobb-intervju.  Etterpå trakk sjefen meg til side og spurte:
> "Vedkommende er sikkert flink nok til å jobbe på den gamle plattformen,
> men er de flinke nok til å jobbe på den nye?"

Hva hadde man gjort her?  
Laget en ny plattform som var _vanskeligere_ å jobbe med enn den gamle? Jeg mistenker at det ikke var meningen.

Det er bemerkelsesverdig hvor ofte jeg har vært med på å lage "ny plattform" de siste 20 årene.  
Det er mange som vil ha ny plattform.  
Hvorfor det? Og får de det de vil ha?  
Kanskje.   
Spørs hva de er ute etter kanskje.  Er de ute etter å få store budsjetter, så er jo disse platformsatsningene alltid en brakende suksess.   

Men får de det brukerne deres trenger?   
Til en fornuftig pris?   
Stort sett ikke mistenker jeg.  

Vi som følger med på slikt hører ofte om store IT-prosjekter som går på trynet.  Men
hvor mange prosjekter der ute er det vi _ikke_ hører om?  Med
mindre det blir helt katastrofe, så vil ledelsen kjempe med nebb og klør for å unngå at en satsning stemples som et feilgrep.
Jeg tror det er store mørketall hva gjelder implementasjoner av nye plattformer, som koster mer enn de smaker.

Jeg ønsker meg litt ærlig refleksjon rundt hva det er vi (tror vi) driver med når vi bygger ny plattform.

## Hva er en plattform?
"Plattform" er et ord som brukes i mange forskjellige fag, men overalt der det brukes, så gir plattformen 
kun verdi i form av hvilke ting som vises frem eller monteres på den.  

En olje-plattform er der for å hente ut olje.  

En tog-plattform er der for å gjøre det lettere å komme seg inn og ut av toget.  

Det er togreisen og oljen som gir verdi.  

En togplattform i feil høyde, eller et sted der ingen reiser til eller fra er bare i veien.  
En oljeplattform i en fremtid der alle har gått over til å bruke fornybar elektrisk kraft er en bortkastet investering.


## Hvem er det som trenger en plattform? Ikke brukerne.
Behovet for "plattform" innen IT tas ofte for gitt.  Men _hvem_ er det som har behovet?   
Ikke brukerne.  
Jeg sier det igjen:  

***Brukerne driter i plattformen.***

Om de er brukere av en frontend-løsning, eller utviklere som skal kalle et API:  
Brukerne driter i plattformen.   
Brukerne har lyst på intuitive skjermbilder eller APIer som lar dem gjøre jobben sin effektivt.
Hvordan disse skjermbildene eller APIene leveres er ganske uinteressant.

Man skyver gjerne brukerne foran seg når man selger inn plattform-begrepet:
> "Brukerene ønsker ikke ha så mange systemer å forholde seg til."

Men dette er faktisk ikke sant.  Brukere må uansett forholde seg til mange forskjellige 
"systemer".  Det går ikke an å lage _ett_ brukergrensesnitt som løser _alle_ behov enkelt og 
intuitivt.  Det er fysisk umulig. En snekker vil ikke ha _ett_ verktøy som både er hammer, 
skrutrekker, vegg-isolasjonsmateriale og matboks.  Vi må slutte å innbille oss at brukere av IT-systemer ønsker seg tilsvarende alt-i-ett-løsninger.  

***Det er _innkjøpere_ som kjenner behov for dette, ikke brukerne.***

Det brukere av web-applikasjoner og slikt ønsker seg, er gode brukeropplevelser.    
Ja, de ønsker helst å slippe å logge seg inn forskjellige steder.  Men hvis de må logge seg inn et 
nytt sted for å få en mye bedre brukeropplevelse, så gjør de gjerne det.

Samme gjelder utviklere som skal kalle et API.  Akkurat som at et skjermbilde for å gjøre én ting,
nødvendigivs er annerledes enn et skjermbilde for å gjøre en helt annen ting, så er ett API-kall
nødvendigvis ikke det samme som et annet API-kall.  
Det å søke etter poststeder er noe annet enn å oppdatere preferanser om frekvens på nyhetsbrev.  Koden
som håndterer disse to vil være veldig forskjellig uansett.
La oss si at en plattform tilbyr begge to over "enhetlige" SOAP-apier.  I tillegg finnes det en ny
tjeneste på en annen "plattform" som lar en søke etter poststeder via REST.  Det er
ikke REST- eller SOAP-tekniske ting utviklere bruker mest tid på uansett.  Vi velger
de løsningene som er lettest å jobbe med. Jeg ville nok valgt å bruke REST-tjenesten til poststed, og SOAP kun der det ikke var noe annet alternativ.
Selv om det ville føre til "mindre enhetlig" kode.

Det vil uansett _aldri_ finnes én plattform som leverer absolutt alt av tjenester man kommer til å trenge.
Vi vil alltid trenge å forholde oss til forskjellige systemer.
Her gjelder det å finne gode måter å gjøre det på. Ikke lure oss selv til å tro at det kan eller bør unngås.  


## Plattform som en bi-effekt
Selv om skummelt mange plattform-prosjekter går åt skogen, så finnes det forskjellige typer "plattform" innen IT som både fungerer bra, 
er fleksible og innehar enorm kompleksitet.  Det disse vellykkede plattformene har til felles er at 
de har vokst frem i parallel med tjenestene som leveres. Man begynte med å levere en tjeneste noen 
trengte. Så leverte man flere og flere over tid, trakk ut felles-komponenter og bygget plattformen rundt funksjonaliteten som ga verdi.
De som har lykkes i å bygge plattform har ikke organisert arbeidet rundt "å bygge plattform":      
***De har organisert arbeidet rundt å levere verdi.***  Konkret verdi brukere merker nytten av.   

Da Apple jobbet med sin første iPhone, var ikke arbeidet fokuset på å lage en mobil "plattform" med masse apps.  
Fokuset var på å levere en skikkelig bra telefon som folk likte å bruke.  

Det er helt OK å ha tanker om en fin stor generisk plattform som skal løse alt for alle en gang i fremtiden. Men ikke organiser arbeidet slik. Fokuser på noe som gir bruksverdi med en gang.
La plattformen bli til organisk ved at man legger til flere og flere gode støttemekanismer for å levere de tjenestene kundene/brukerne har behov for.

## Conway's law
Ikke trekk ut felleskomponenter før akkurat samme funksjonalitet er i bruk _minst_ 2
forskjellige steder.  Vær veldig forsiktig med å gruppere utviklings-organisasjonen etter 
teknologiske felleskomponenter som ikke er av interesse for bruker/kunde i isolasjon.  Ellers legger man fort kjepper i 
hjulene til de som skal levere konkret verdi.  Hvis du for å levere en ny tjeneste en bruker trenger, 
må koordinere større mengder utviklings-arbeid mellom f.eks database-teamet, infrastruktur-teamet, 
meldings-mottak-teamet, sikkerhets-teamet, personvern-teamet, test-teamet, osv osv, så blir det mye vanskeligere å 
jobbe effektivt.  Man legger opp til at ingen i noen av teamene er istand til å gjøre noe av verdi 
for kunde/bruker alene.  Det er veldig lett å miste fokus på ting som bruker/kunde faktisk trenger, når man er organisert slik.   

Det er stort sett bedre å organisere team etter brukergruppene de leverer løsninger for. 
Istedenfor at felles-kode forvaltes av egne team, kan heller alle team ha tilgang til all kode.
Da kan eventuell "felles-kode" endres av produktteamet som trenger endringen når de trenger den.   

Begyn i alle fall med å gjøre det slik.  Ikke skill ut egne felles-komponent-team før det er et konkret behov for det.  


## Små komponenter. Opt in.
For å lykkes med felleskomponenter bør de være små, og opt-in.  De skal 
velges fordi de gir merverdi for tjenesten som leveres.  Det er tjenesten som leveres til bruker / kunde som må avgjøre om det er verdt å bruke felles-tjenesten.  
Gode eksempler på felles/plattform-tjenester er ting som sky-leverandører tilbyr.  Ting som 
ikke er knyttet til domenet overhodet. Amazon Web Services har uhorvelig mange forskjellige tjenester,
og verdien ligger i at man velger ut de tjenestene man ønsker å bruke selv. Ingen bruker alt.
AWS legger ingen føringer på hvordan du lager ting, de bare tilbyr nyttige verktøy. Det er slik en plattform bør være.


## Vær klar over fordelene med løsningene du allerede har.  
Ikke vær så kjapp til å tro at gresset er grønnere "hvis vi bare får en enhetlig ny plattform."
Her er det fort gjort å kaste babyen ut med badevannet.  At forskjellige tjenester bruker litt 
forskjellige tilnærminger til "samme ting" (visning, persistering, mottak av data osv), det er stort sett helt OK.
Ikke bare gjør det at man er istand til å gjøre nødvendige spesialtilpasninger i hver tjeneste
(uten at man må "hacke" seg forbi plattformen). Men det gjør også at man lettere kan migrere
plattform-tjenestene til nye og bedre teknologiske løsninger.
Hvis alle tjenester _må_ bruke samme løsning, så kan man ikke endre løsning uten at alle må endre.
Det er tungt. Noe som gjør at plattformen stagnerer, og man til slutt føler man må kaste hele greia og bygge enda en ny plattform. 


## Ting kan integreres uten at de kjører på samme plattform
Det er mange som tror at for å integrere systemer, så må de kjøre på samme plattform.  
***Dette er helt feil.***  
Jeg var i sin tid med på å lage en løsning for å kunne søke om bostøtte på nett.  Bostøtteordningen forvaltes av Husbanken, 
og søknadsløsningen ble laget og driftet i Husbankens "plattform".  
Fra denne plattformen integrerte vi så med mange andre tjenester på mange andre plattformer:    

For å logge seg inn, gikk bruker via ID-porten (leveres via Difi sin "plattform").  
Etter innlogging hentet man inn adresse- og familie-informasjon fra folkeregisteret (leveres via skatt sin "plattform").  
Vi hentet bolig-informasjon fra matrikkelen, (Statens kartverk sin "plattform").  
Trygdeopplysninger hentet vi fra nav, som igjen har _sin_ plattform.  

5 forskjellige plattformer måtte integreres.  De hadde helt forskjellige APIer og forskjellige autentiserings-mekanismer, og forskjellige formater.  Men gikk selvsagt helt greit.

For brukeren av bostøtte-søknadsløsningen, oppleves det som at alt leveres fra Husbanken sin nettside, 
selv om det er massevis av forskjellige APIer og "plattformer" i bakgrunnen.    

***Man trenger ingen felles plattform for å lage integrerte løsninger.***


## Kontinuerlig utskiftning
IT er i konstant endring. Nye ting skjer hele tiden. Ny hardware, nye tjenester, nye muligheter. 
Business-modeller, lover og regler, alt er i konstant endring.  Det er lurt å holde IT-systemene sine oppdatert. 
Men endringene bør aller helst skje som en del av vanlig forvaltning.  Det kan godt være at hele dagens løsning må erstattes. 
Men denne prosessen både kan og bør skje gradvis. Bygg opp "plattformen" du vil ha ved å forbedre konkrete små ting inkrementelt. 

Om 7 år har alle celler i kroppen din blitt erstattet.  Uten at du aktivt har måttet "bytte kropp".
Med IT-systemer kan vi ikke bare erstatte "cellene", men forbedre dem for hver iterasjon.  

## Pass på å få verdi for pengene
***En plattform er et sett av verktøy som en IT-organisasjon lager til seg selv.***  
Å forbedre plattformen er noe organisasjoner som leverer software alltid må jobbe med hele tiden. 
Uansett hvor gode systemer du har nå, så bør du jobbe kontinuerlig med å holde dem oppdatert og gjøre dem bedre. 
Dette gjelder i alle bransjer.  Man må holde verktøykassa oppdatert. 
Men de som kjøper tjenester skal normalt sett ikke betale direkte for verktøyinnkjøpene hos leverandøren.  
Skal vi bygge på huset, betaler vi gjerne for planker og skruer og tiden til håndtverkerne.
Men vi betaler ikke for verktøykassa og varebilen deres.   
Vær veldig forsiktig med å finansiere verktøy-bygging, verktøy-innkjøp og verktøy-innkjøpsplanlegging blindt i årevis.  
Det gir åpenbart stor verdi for leverandøren, men er stort sett en dårlig investering for deg!






