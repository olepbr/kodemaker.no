:title Men hva om den går ned!
:author christin
:published 2021-12-08

:blurb
Hva om den eksterne tjenesten vi er avhengig av går ned? Hvordan sikrer vi en robust tjeneste med eksterne avhengigheter?

:body
De fleste systemer nå om dagen er avhengig av data og funksjonalitet fra en eller flere eksterne tjenester.  
Det kan være epost-systemer, betalings-systemer, kart-tjenester, oppslagsverk... 
Det er sjelden vi lager absolutt alt av funksjonalitet fra scratch og tilbyr det selv i egen applikasjon.

Jeg har akkurat jobbet på [AMOI](https://www.amoi.no). AMOI er en tjeneste der man kan bestille varer på tvers av masse forskjellige butikker og betale og få det levert samlet på dagen. 
Betaling for dette er åpenbart en helt kritisk og kompleks del av systemet. Betalingen en kunde sender inn må deles opp og sendes til alle butikkene involvert.  Det er ikke helt rett frem.
Istedenfor å bli bank-sertifiserte og skrive vår egen betalingsplattform, så brukte vi Stripe for å håndtere betaling.
Helt til slutt i checkout-flyten må brukerene fylle inn betalingsinformasjon i stripe (selv om det oppleves som en del av AMOI).

Men hva gjorde vi dersom Stripe gikk ned? 

## Proxy?
La oss tenke litt høyt rundt hva vi kunne ha gjort. 
Vi kunne laget vår egen proxy-tjeneste som mottok betalingskallet. 
Dersom stripe var nede, kunne kallet blitt forsøkt igjen og igjen frem til det lykkes.
Brukeren kunne fått betalingsbekreftelsen, og leveranse kunne bestilles som om ikke noe var galt.

Men vent litt... hva om kallet til Stripe feilet når det omsider gikk gjennom?
Da hadde vi måttet gå inn og kansellere leveransen.
Vi hadde måttet sende en ny epost til brukeren om at bestillingen dessverre måtte kanselleres likevel.
Vi hadde måttet sende oppdateringer til alle butikker involvert om at denne bestillingen ikke skulle leveres likevel.

Men hva om bestillingen, når Stripe omsider fikk svart, allerede hadde blitt kansellert? 
Da hadde vi jo ikke trengt å gjøre all denne kansellerings-jobben en gang til. 

Ikke bare det, la oss tenke oss at Stripe var nede et par timer - og at bestillingen allerede hadde blitt hentet fra butikken og kanskje levert til bruker allerede! 
Da gir det heller ingen mening å organisere masse kanselleringer, da er det bare å innse at vi har tapt de pengene, og må betale for varene av egen lomme.

Til sist: Hva om det er en feil i koden i proxy-tjenesten? Hva om DEN går ned? 
Hva om det er en bug i proxy-tjenesten som gjør at feil fra Stripe ikke kommer tilbake til oss? 
Hva om det er en bug i proxy-tjenesten som gjør at vellykkede kall til Stripe ikke håndteres korrekt? 

For hver kodelinje du skriver, så ØKER du sjansen for bugs. Det blir aldri noen bugs i kode du ikke skriver.  

Vi vurderte (takk og lov) aldri å innføre en slik proxy-tjeneste i AMOI. Hvis Stripe gikk ned, så fikk du ikke gjennomført ordren din.  Ferdig med det. Prøv igjen senere.

Ved å la være å innføre masse ekstra sikkerhetstiltak, så gjorde vi løsningen mye enklere og mer robust.

## Kø?
Andre steder brukte vi andre strategier.  Ta bestilling av leveranse. Her integrerte vi med Bring sitt leveranse-system Glow. 
Hva skjedde hvis de gikk ned? Vi lagde heller ikke noen proxy tjeneste her, men vi bestilte leveranser asynkront. 
Bestilling av leveranse, og evt feil har tross alt ikke like store konsekvenser som en feilende betaling. 

Vi hadde en jobb som gikk jevnlig som fant frem betalte ordre som ikke hadde fått bestilt leveranse, og som så prøvde å kontakte Glow.
Dersom Glow ikke svarte, ville vi ikke lagre noe om at leveranse var bestilt, så da ville jobben prøve igjen og igjen helt frem til leveranse-tidspunktet.

Kunne vi heller lagt leveranse-bestillingen på en kø med en gang? Også "spist" en og en bestilling av køen og bestilt leveranse? 
Ja det kunne vi. 
MEN, hva om noe går galt under bestillingen? Da må vi legge den på en ny kø avhengig av hva det var som feilet. 
Så måtte vi hatt egen prosessering av disse køene. Sjansene for at meldinger blir borte/glemt øker.  

En og en melding på kø gir bedre ytelse enn å måtte gjøre et søk på tvers av alle ordre for å finne kandidater, 
men i oppstartsfasen da vi ikke hadde så mange ordre uansett, så var det viktigere å ha en så robust løsning som mulig, 
der det var minst mulig sjanse for at bestillinger ble "borte".  Kø-systemer er notoriske for å feile i stillhet. 
I lille Norge har vi ofte ikke så store datamengder at slike batch-søk egentlig skaper så store problemer uansett.

## Cache?
Hva med oppslag etter data i eksterne registre? Burde vi cache dataene lokalt, både for ytelse og økt robusthet hvis tjenesten blir utilgjengelig?
Det er en kjent ordtak i bransjen at det kun er 2 vanskelige problemer innen programmering:
1) Å navngi ting
2) Cache-invalidering
3) "Off-by-one" feil 

Caching er ikke trivielt. Akkurat som diskutert under proxy-tjenester, så innfører man fort en hel bråte med feilsituasjoner.
Men.. hva er alternativet? Skal vi være avhengig av at det eksterne registeret er oppe for å få opp data? 

Ja, det kan hende det er helt greit. Jeg bruker AMOI som eksempel igjen. 
I checkout-flyten så må du taste inn adressen dit du vil ha varene levert. Her integrerer vi med en ekstern tjeneste som foreslår adresser.
Dette gjør at man får færre feil-stavede adresser som budet så sliter med å finne. Hva gjorde vi hvis denne tjenesten ikke svarte? 
Ingen ting - da måtte du bare fylle inn adressen manuelt. For det er faktisk ikke helt krise om du ikke får foreslått adresser.  
Det samme gjelder tross alt de fleste slike registre.  Folkeregisteret, kontaktregisteret, matrikkelen og statens kartverk. 
Det er sjelden det er tids- og livskritisk å vite hvor noen er folkeregistrert, eller hva gårds- og bruksnummeret ditt er.
Disse registrene er der stort sett som en "convenience" - for at du skal slippe å måtte finne ut av og skrive inn masse greier selv. 
Slik vi måtte før. Men vi klarte det jo før! Det at vi nå har oppslagsverk, betyr jo ikke at du ikke lenger kan adressen din f.eks. 
Og hvis det er noe man ikke kan eller husker, så kan man stort sett klare å vente i de minuttene det tar før tjenesten er oppe igjen før man får informasjonen.

For ordens skyld så hadde vi en lokal cache av noen data i AMOI.  
Alle postnumre, poststeder og hvilke kommuner de lå i, lå i csv-filer vi bare hadde liggende lokalt.
Dette funket helt strålende. Det er data som endres sjelden, og det er lett å bytte ut en csv fil de få gangene det er endringer. 
Jeg vet ikke om det finnes noe postnummer-register noe sted, men for denne typen ganske statiske og begrensede data så gir det åpenbart mening å ha en lokal kopi.

## Konklusjon?
Tenk gjennom hva de relle konsekvensene er dersom en ekstern tjeneste ikke er tilgjengelig.

- Noen ganger er det ikke så farlig, man kan ha failover til manuell håndtering slik vi gjorde med adresser.
- Andre ganger er det ikke noe vi kan gjøre: "Betaling feilet". Dette kan vi rett og slett ikke rette opp i. Ikke noen vits i å prøve. 
- Til sist lønner det seg ofte å ha asynkrone løsninger der en "melding" kan prosesseres igjen og igjen til den lykkes en god løsning. 

Uansett hva man gjør, så må man ikke glemme det at hver eneste kodelinje du legger til øker sjansen for feil.
Så jo mer feilhåndtering du legger til, jo flere feil kan du ha. Så velg strategi med omhu og gjør minst mulig.










