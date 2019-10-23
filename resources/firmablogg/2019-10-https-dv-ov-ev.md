:title HTTPS-sertifikater og tillitsnivåer
:published 2019-10-23
:author finn
:tech [:https]

:blurb

Du har kanskje sett at når du browser nettbanken din så står hele navnet på banken ved siden av URL-en.

![Sbanken verifisert av nettleseren](/images/blogg/ev-cert-sbanken.PNG)

Hva er dette for noe?

:body

Du har kanskje sett at når du browser nettbanken din så står hele navnet på banken ved siden av URL-en.

![Sbanken verifisert av nettleseren](/images/blogg/ev-cert-sbanken.PNG)

Hva er dette for noe?

HTTPS er kryptert datatrafikk. Ok, men i hjertet av HTTPS banker et tillitssystem (trust) som er essensielt for at krypteringen skal fungere. Det visste du nok allerede, men visste du at det finnes 3 tillitsnivåer i HTTPS-verden som heter domain-, organization- og extended validated sertifikater? Hvis du svarer nei så er du ikke aleine, så la oss raskt snakke om disse.

## 1. DV - Domain validated

Du kjenner kanskje til [Let's Encrypt](https://letsencrypt.org)? De er den mest kjente utstederen av DV-sertifikater i dag. Let's Encrypt gir fritt tilgjengelig for alle et script som henter sertifikat for domenet du ber om - så lenge du disponerer domenet. Scriptet må typisk kjøre på webserveren som DNS-en peker på. 'Domain Validated', navnet er ganske intuitivt synes jeg.

DV er den enkleste, raskeste og billigste måten å sikre kryptere webtrafikk på. Automatiserbart fra første stund. Så hvis du kun er ute etter kryptert datatrafikk på webserveren din, trenger du nok ikke noe mer enn dette noen gang.

## 2. OV - Organization validated

OV og EV må du betale for, og de som selger disse sertifikatene sier at det er mer tillit til websider med denne typen sertifikater (enn DV). Det er litt uklart for meg hvorfor en skal kjøpe akuratt OV, og hvem som faktisk bruker det. De som selger det er litt vage på hvorfor en skal kjøpe denne sertifikattypen og bruker bare generelle salgspitcher om økt sikkerhet og tillitt. Ut fra litt kjapp googling ser det ut til å ligge rundt $200 pr år for disse sertifikatene.

## 3. EV - Extended validation

De som selger EV-sertifikater sier at det er maks tillit til websider med disse sertifikatene. EV blir kalt 'Banking level' tillit til sertifikatet. Her er det DigiCert og Tha som råder og disse setifikatene koster fort $400 i året.

Her dukker firmanavnet ditt opp ved siden av URL-en og skal signalisere maks tillitt til nettstedet ditt.

## Hva skal vi tenke?

Jeg er veldig tvilende til verdien av OV - og er skeptisk til sertifikatutstederene sitt insentiv her. Å selge sertifikater er god butikk, de koster jo gjerne flere hundre dollar pr år. Bransjen overspiller nok verdien til disse sertifikatene og hvor aktivt forhold en vanlig surfer på internett har til det. 

Min konklusjon er at vanlige nettsider kan holde trygt seg til DV (Let's Encrypt). I hvertfall i starten. Du _må_ jo ha kryptert https trafikk, og verdien ligger først og fremst der!

EV derimot - er forfengelighet. Jeg tror ikke jeg hadde reagert på om nettbanken min manglet det. Men for oss som jobber med softwareutvikling så er det nyttig å vite at disse nivåene av HTTPS-sertifikater eksisterer. Sikkerheten til en webside ligger tross alt i koden, infrastruktur og andre sikkerhetsmekanismer som kan svikte og ikke om du bruker et fancy HTTPS-sertifikat.
