:title Git-Secret - en hemmelighet
:author stig
:published 2020-10-21
:tech [:git]

:blurb

Git-secret er en liten perle som gjøre det enkelt å la konfigurasjonsfiler versjonshåndteres sammen med kildekoden, selv om de inneholder hemmeligheter. 
:body
Git-secret er en liten perle som gjøre det enkelt å la konfigurasjonsfiler versjonshåndteres sammen med kildekoden, selv om de inneholder hemmeligheter. 

## Vi ønsker vel ikke å ha hemmeligheter i vår kildekode?
Kildekode skal kunne deles i en organisasjon  
- til inspirasjon og opplæring
- til evaluering og granskning

... uten noen form for risiko for at token eller passord skal komme på avveie.

I veldig mange situasjoner er det også helt unaturlig å blande sammen hemmeligheter som benyttes under kjøring med selve kildekoden.  

Når koden kjører - så skal den hente nødvendige parametre i fra sitt kjøremiljø eller i fra egne config/secret tjenester som HashiCorp Vault eller Spring Cloud Config. 

Det finnes likevel noen situasjoner der det er ønskelig å la konfigurasjonsfilen(e) følge kildekoden 
- kildekoden er skreddersøm og kjører bare i et begrenset antall instanser.
- det er behov for versjonshåndtering av konfigurasjonsfilene slik at konfigurasjonen følger endringer i kildekode og motsatt

Utrulling på ulike instanser og eventuell tilbakerulling vil alltid kunne få riktig konfigurasjon med et slikt oppsett.

Det finnes flere alternative løsninger for å få til dette. [git-secret](https://git-secret.io) er en av de.

## Installasjon
git-secret har to avhengigheter; git og [gpg](https://gnupg.org/). 

```zsh
# Mac
brew install gnupg git-secret
```

```zsh
#Debian
echo "deb https://dl.bintray.com/sobolevn/deb git-secret main" | sudo tee -a /etc/apt/sources.list
wget -qO - https://api.bintray.com/users/sobolevn/keys/gpg/public.key | sudo apt-key add -
sudo apt-get update && sudo apt-get install git-secret

sudo apt install gpg

```

## Lag en GPG nøkkel
Vi trenger en gpg-nøkkel som skal benyttes til signering, kryptering og dekryptering. Se bloggen til min gode kollega [August](https://www.kodemaker.no/blogg/2019-08-public-og-private-keys/#a-trygt-dele-en-hemmelighet-med-public-og-private-keys) for en veldig illustrativ beskrivelse av hvordan slik kryptering/dekryptering fungerer. 

Gnu-GPG har flere kommandoer for å lage nøkler, men bare en av de gir oss det vi trenger 
- definere at nøkkelen ikke skal gå ut på dato (kan jo sikkert diskutere dette, men det er i alle fall praktisk)
- generere en sub-key som blir benyttet til selve krypteringen

```zsh
# full kontroll på det som blir generert
gpg --full-generate-key
#  (1) RSA and RSA (default)
#  4096 bits long
#  0 = key does not expire
# Real name: <Fornavn Etternavn>
# Email address: dinEmailAddresse (dette blir din ID)
# Comment: <en passende kommentar som beskriver hva denne nøkkelen skal benyttes til>
```



Husk også å ta backup av din private nøkkeldel - slik at du har mulighet til å reinstallere denne ved behov. 
```zsh
# Eksporter privat nøkkel til fil
gpg --export-secret-keys stig@kodemaker.no > stig_priv.gpg
```
![gpgpubkeys](/img/blogg/gpgcreate.gif)


## Initier et prosjekt
For å se de mest sentrale operasjonene i aksjon, kan vi jo opprette ett enkelt demoprosjekt med en fil som inneholder hemmeligheter.
```zsh
# Opprett katalog og naviger til denne
mkdir demo && cd demo

# Initier git
git init

# Initier git-secret
git secret init
```
Vi har nå en .gitsecret katalog som skal lagre informasjon om filene som skal krypteres og de offentlige nøklene som skal benyttes.

Legg til deg selv i prosjektet
```zsh
# Legg til din offentlige nøkkel til prosjektet
git secret tell -m
```
Din offentlige nøkkel er nå lagt inn i prosjektet - slik at andre kan benytte den når filer skal krypteres.
Nøkkelen legges i filen ```.gitsecret/keys/pubring.kbx``` Her dukker det også opp en fil som heter ```pubring.kbx~``` Denne filen er forrige versjon av keyringen i prosjektet - den trenger vi ikke å versjonshåndtere. 

På tide å innføre noen hemmeligheter som legges inn i prosjektet og krypteres
```zsh
# Opprett en fil med hemmelig innhold
echo "veldig hemmelig innhold" > mysecrets.txt

# Legg filen med hemmeligheter inn under git-secret administrasjon 
git secret add mysecrets.txt

# Krypter alle filer som er under git-secret administrasjon
git secret hide

# Legg den krypterte filen til versjonskontroll
git add mysecrets.txt.secret
```

Filen som inneholder hemmeligheter i klartekst blir automatisk lagt til .gitignore 

Filen som er kryptert får .secret lagt til filnavnet og må versjonshåndteres.

Ved utsjekk av prosjektet, kan alle krypterete filer dekrypteres av de som har sin offentlige nøkkel som del av prosjektet.
```zsh
# Dekryptering av alle krypterte filer
# Git-secret benytter din private nøkkel for å dekryptere det som er kryptert med din offentlige nøkkel
git secret reveal 
```

![gpgpubkeys](/img/blogg/gitsecinit.gif)

## Legg til en ny utvikler

Før du kan legge til andre utviklere i prosjektet, må du få en eksport av deres offentlige nøkkel.

```zsh
# Eksport av offentlig nøkkel til fil
gpg --export utvikler@kodemaker.no > utvikler_pub.gpg
```

Når du får utviklerens offentlige nøkkel må denne legges inn i din gpg-keyring.
```zsh
# Import av offentlig nøkkel til din gpg keyring
gpg --import ~/Downloads/utvikler_pub.gpg
```
Det neste du må gjøre er å legge utvikler til prosjektet
```zsh
# Utviklers offentlige nøkkel hentes i fra din gpg keyring og legges til prosjektet
git secret tell utvikler@kodemaker.no
```
For å se hvilke utviklere som er lagt til prosjektet
```zsh
# Se liste over offentlige nøkler som er lagt til prosjektet 
git secret whoknows
```
Vi har nå lagt den offentlige delen av utviklerens nøkkel i prosjektet. 
Neste trinn blir å kryptere alle secret filer med alle nøkler som ligger i prosjektet. 
Før vi gjøre en slik kryptering, er det viktig at vi har siste dekrypterte versjon tilgjengelig. 
```zsh
# Dekrypterer alle secret-filer for å sikre oss at vi har siste versjon tilgjengelig
git secret reveal

# Kryptere filer med alle offentlige nøkler i prosjektet
git secret hide
```
![gpgpubkeys](/img/blogg/gitsectell.gif)

Størrelsen på de krypterte filene stiger proposjonalt med antall utviklere. Det skyldes at de faktisk består av en kryptert blokk pr. utvikler. 
Når en utvikler gjøre reveal så er det bare sin egen blokk som dekrypteres.

## Noen erfaringer
### Bare hemmeligheter - ikke vanlig konfigparametre
Diff og merge fungerer jo ikke på krypterte filer på samme måte som vi er vant til med vanlige kildekode.
Du må enten velge dine eller andres versjon av filen. 
Det er derfor viktig å skille vanlig konfigurasjon i fra det som faktisk er hemmeligheter. 
Splitt en konfigfil i to. En for konfigurasjonsparametre som bør være hemmelige og en for alle andre.
På denne måten er det lettere å se hvilke endringer som er utført på konfigurasjonen som ikke er hemmelig.
```properties
#config.props
enablefeature.a = false
enablefeature.b = true
database.username = 'abc'
database.password = 'cba'
```
Lag en fil med bare hemmeligheter som legges til git secret og en fil som versjonshåndteres på vanlig måte.

```properties
#config.props
enablefeature.a = false
enablefeature.b = true
```
```properties
#secrets.props
database.username = 'abc'
database.password = 'cba'
```

Git-secret gir deg mulighet til å se om det er forskjellig innhold i din dekrypterte fil, sammenlignet med den som er kryptert.  
```zsh
# Vis forskjellen mellom en fil i klartekst og dens krypterte versjon
git secret changes mysecrets.txt
```

### Jobbe effektivt
Dersom du har mange filer kryptert - så ønsker du i de fleste tilfeller å gjøre reveal på alle filene i en operasjon - uten å måtte bekrefte overskriving for alle filer. Bruk derfor -f opsjonen. 
```zsh
git secret reveal -f
```
Standard ```hide``` krypterer alle filene som er definert som hemmelige - uavhengig av om de er endret eller ikke. Dette skaper jo også litt støy i commit-loggen. 
Gjør derfor bare hide på filer som er modifisert.
```zsh
git secret hide -m
```

### Pass på nøklene dine
Det gjelder jo ikke bare deg, men alle personene i prosjektet. 
Det er litt logistikk knyttet til å legge til nye nøkler og det skaper unødvendig arbeid dersom en utvikler får ny maskin og mangler backup av sin private nøkkel.  

Dersom en utvikler mister sin nøkkel og må opprette en ny, må du gjøre følgende
- slett den gamle offentlige nøkkelen i din gpg-keyring
- få eksport av den nye offentlig delen av nøkkelen og importere den 
```zsh
# Slett offentlig nøkkel i fra din keyring
gpg --delete-keys utvikler@tullemaker.no

# Importer ny offentlig nøkkel til din keyring
gpg --import utvikler_at_tullemaker_pub.gpg

```
For alle relevante prosjekt må vi så:
```zsh
# ta bort utvikler i fra prosjektet
git secret killperson utvikler@tullemaker.no

# for å sikre at siste versjon av filer med hemmeligheter er dekryptert
git secret reveal 

# Legg til ny public key
git secret tell utvikler@tullemaker.no

# krypter filer med prosjektets offentlige nøkler
git secret hide 
```

... og så repeat på neste prosjekt

Det er jo litt styr... så pass på nøklene dine.

### Halvautomatiser 
Dersom du har flere lignende prosjekt som skal håndteres av samme team - kan du med fordel opprette ett eget repo med teamets offentlige nøkler. 
Lag deg så ett par enkle script som gjør det lett å legge til hele teamet i en enkel operasjon.


![gpgpubkeys](/img/blogg/gpgpubkeys.png)
Prosjektet kan for eksempel ha en katalog med aktive offentlige nøkler til ditt team. 
Nøkkelfilene kan lagres med email som filnavn for å forenkle automatiseringsscriptene

Script for å importere alle offentlige nøkler i teamet til din gpg-keyring

importActivePubKeys.sh
```zsh
#!/bin/bash
# Importerer aktive nøkler til din gpg installasjon
activepath=$( cd "$(dirname "$0")" ; pwd -P )/active_pub_keys/*
gpg --import $activepath

```
Script for å ta bort alle offentlige nøkler i fra ett prosjekt. Det er jo mer grasiøst å bare slette de som ikke lenger er aktive, men dette fungerer også.

removeAllPersonsFromProject.sh
```zsh
#!/bin/bash
# Sletter alle personer i fra git secret prosjektet
for i in `git secret whoknows`; do
   git secret killperson $i
done

```
Script for å legge til alle aktive nøkler.

addAllPersonsToProject.sh
```zsh
#!/bin/bash
# Importerer aktive nøkler til prosjektet
activepath=$( cd "$(dirname "$0")" ; pwd -P )/active_pub_keys/*

for i in `find $activepath -printf "%f\n" | grep -Eo "[^<]+@\S+\.[^>]+"`; do
   git secret tell $i
done

```

## Gotcha
Tanken har kanskje slått deg... er dette et mulig scenarie?
```zsh
## en utvikler cloner ett repo der brukeren ikke er lagt til via git secret tell 
git clone arepo

# denne vil jo feile i og med at prosjektet ikke har brukerens offentlige nøkkel
git secret reveal
> feilmelding

# Brukeren legger til sin offentlige nøkkel
git secret add enluring@kodemaker.no


git commit -a -m "bare en liten uskyldig commit" 
git push...

... venter i spenning til noen i prosjektet tilfeldigvis gjør git secret hide og så git push...

git pull
git secret reveal

```

Svaret er ja. En utvikler som har tilgang til å pushe til ett repo, kan i prinsippet legge seg til selv. 
Det er jo viktig å være klar over!


## Oppsummering
Git-secret fungerer smooth :-) så lenge de som har lov til å skrive til repoet også har lov til å se hemmeligheter.

Det går fint å benytte verktøyet i prosjekt med 5-10 utviklere og 20+ prosjekter. 



