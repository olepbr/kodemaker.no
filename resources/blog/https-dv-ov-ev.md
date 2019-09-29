:title HTTPS sertifikater og tillitsnivåer
:published 2019-09-15
:author finn
:tech [:https]

:blurb

Du har kanskje sett at når du browser nettbanken din så står hele navnet på banken ved siden av URL-en.

![Bilde av kjøkkenbordet](/images/blogg/finn/ev-cert-sbanken.PNG)

Hva er dette for noe?

:body

HTTPS er kryptert datatrafikk. Ok, men i hjertet av HTTPS banker et tillitssystem (trust) som er essensielt for at krypteringen skal fungere. Det visste du nok allerede, men visste du at det er finnes 3 tillitsnivåer i HTTPS-verden som heter domain-, organization- og extended validated sertifikater? Hvis du svarer nei så er du ikke aleine så la oss raskt snakke om disse.

Disse 3 typene jeg snakker om heter som sagt Domain-, Organization- eller Extended Validation. La oss starte med den vanligste. DV aka 'Domain Validated' -sertifikat.

## 1. DV

Du kjenner kanskje til Let's Encrypt? De er den mest kjente utstederen av DV sertifikater i dag. Let's Encrypt gir fritt tilgjengelig for alle et script som henter sertifikat for domenet du ber om - så lenge du disponerer domenet. Scriptet må typisk kjøre på webserveren som DNS-en peker på, ellers funker det ikke. 'Domain Validated', navnet er ganske intuitivt synes jeg.

DV er den enkleste, raskeste og billigste måten å sikre kryptere webtrafikk på. Automatiserbart fra første stund. Så hvis du kun er ute etter kryptert datatrafikk på webserveren din, trenger du nok ikke noe mer enn dette noen gang.

## 2. OV

Organization validated. OV og EV må du betale for, og de som selger disse sertifikatene sier at det er mer tillit til websider med denne typen sertifikater. Det er litt uklart for meg hvorfor en skal kjøre dette, og hvem som faktisk bruker det. De som selger det er litt vage, og det mistenker er fordi de ikke helt klarer å selge det inn. Litt kjapp googling så ser det ut til å ligge rundt $200 pr år for disse sertifikatene.

## 3. EV

Extended validation. Selgeren av EV sertifikater sier dette er max tillit til websider med disse sertifikatene. EV blir kalt 'Banking level' tillit til sertifikatet. Her er det DigiCert og Tha som råder og disse setifikatene koster fort $400 i året.

Her dukker firmanavnet ditt opp ved siden av URL-en og skal signalisere max tillitt til nettstedet ditt.

## Hva skal vi tenke?

Jeg er veldig tvilende til verdien av OV - og er skeptisk til sertifikatutstederene sitt insentiv her. Å selge sertifikater er god butikk, de koster jo gjerne flere hundre dollar pr år. Bransjen overspiller nok verdien til disse sertifikatene og hvor aktivt forhold en vanlig surfer på internett har til det. 

Min konklusjon er at vanlige nettsider kan holde trygt seg til DV (Let's Encrypt). I hvertfall i starten. Du _må_ jo ha kryptert https trafikk, og verdien ligger der først og fremst!

EV der i mot - er forfengelighet. Jeg tror ikke jeg hadde reagert på om nettbanken min manglet det. Men for oss som jobber med softwareutvikling så er det nyttig å vite at disse nivåene av https -sertifikater eksisterer.
...
