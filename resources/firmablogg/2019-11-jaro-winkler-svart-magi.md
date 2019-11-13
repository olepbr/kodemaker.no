:title Jaro-Winkler - svart magi?
:author stig

:blurb

JaroWinkler er en formel som kan benyttes til mye - også adressevask.

:body

JaroWinkler er en formel som kan benyttes til mye - også adressevask.


## Stack overflow

En bedrift ønsket å oversette adresseinformasjon til kartkoordinater. Til å hjelpe seg hadde de et spesialsystem for transportplanlegging med innebygd adresseregister og kartfunksjonalitet. 
Utfordringen for bedriften var løsningens følsomhet for stavefeil i adressefeltet. Dersom ikke adressen var skrevet på samme måte som varianten i systemets adresseregister, så ble adressen forkastet. 
Forkastede adresser måtte behandles manuelt, noe som var svært tidkrevende. 

Den totale arbeidsmengden knyttet til manuell adressevask ble rett og slett for omfattende. Noe måtte gjøres.

Hva var problemet egentlig?

- feil postnummer ble benyttet
- selvfølgelig var det også problemer med æøå grunnet internasjonale avsendere og dårlige kildesystemer 
- adressefeltet ble avkortet som feks. Karl Johans gat
- forkortelser ble benyttet. Som feks. gt. for gaten, vn. for veien, dr. for doktor, prof. for professor, osv...
- gate der det skulle være vei ... eller motsatt
- veiEN der det bare skulle være vei
- gatEN der det skulle være gata eller gate
- etasjeinformasjon sammen med gatenavn - Løvåsveien 6, 5.etg. 
- beskjeder til leverandør - Storgata 3 - ring 12345, 5 minutter før lev.
- og ikke minst et utall skrivefeil. 

Vi fant sikkert 20 ulike måter å skrive Schweigaards gate på. 

- Scheweigaardsgate
- Schveigårds gate
- Schweigårdsgate
- Sveigårdsgate
- Sweigårdsgate
- ...


## Finn den som ligner mest

Heldigvis dukker det ikke opp dusinvis av nye gater daglig. Listen med gatenavn er med andre ord definert. Den er fasiten og vår oppgave er bare å finne den varianten som ligner mest. 

Bedriftens eget system for transportplanlegging hadde jo et gateregister, men det var utilgjengelig for omverdenen. 
Heldigvis - det finnes gode alternativer. Matrikkelens adresseregister inneholder alle gater i norge og er tilgjengelig for fri nedlastning og bruk av alle. 

[https://kartkatalog.geonorge.no/](https://kartkatalog.geonorge.no/) Søk etter Matrikkelen - Adresse

Den virkelige utfordringen blir da å sammenligne to tekststrenger på en måte som gjøre at vi kan vurder hva som ligner mest.

## Jaro Winkler to the rescue

I 1989 beskrev Jaro en teknikk for sammenligning av tekststrenger. Utgangspunktet var behovet for å sammenstille ulike register med personopplysninger. Det er vel ikke god musikk i disse GDPR tider, men for gatenavn er det helt innafor :-)

Prinsippet med formelen er å beregne en verdi som sier noe om graden av likhet mellom to tekststrenger.
To like tekster får jaro-verdi lik 1. og så går det nedover etterhvert som strengene blir mer og mer ulike. 
Det er ikke noen lineær sammenheng med verdien og grad av likhet. 
Vi kan bare benytte verdien til å vurdere om noe er likere enn noe annet. 


Litt forenklet ser formelen slik ut
![jaroformel](/images/blogg/j_formel.png)

Vi finner antall tegn som "matcher" i to tekstrenger og så bygger vi opp jaro-verdien ved hjelp av tre likeverdige bidrag. 

Første bidrag er antall matchende tegn delt på lengden av den første strengen. 
Så gjør vi det samme med den andre strengen. 
Siste bidrag er antall tegn som matcher minus en forflytningsfaktor delt på antall tegn som matcher. 
Denne forflytningsfaktoren kompenserer for at like tegn, ikke har nøyaktig samme posisjon. Lik plass gir ingen straff, mens en liten avstand medfører en liten trekk i sluttverdien.


[Du kan lese mer om formlene på wikipedia.](https://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance)

En ulempe med denne formelen er at den vekter likhet i starten av strengene på samme måte som slutten av strengene. 
Det var dette Winkler løste ved å gi jaro-verdien et ekstra tilskudd dersom det er likhet i de første tegnene. 

JaroWinkler formelen var født. 

Dette passer bra for oss som skal sammenligne gatenavn. 
Torggata og Storgata har jo noen likhetstrekk, men at begge inneholder teksten gata er ikke så viktig for oss.
For gatenavn, er det viktigere å lete etter likheter i starten av adressen.   

## En liten test

[Apache Commons](https://commons.apache.org/proper/commons-text/apidocs/org/apache/commons/text/similarity/JaroWinklerSimilarity.html) har implementert metodene i sin org.apache.commons.text.similarity pakke.

I vårt tilfelle - så hadde vi adressene som skulle geokodes og matrikkelens adresseregister i en database. 

Pogrammeringsmodellen i en database er ikke så rik som vi liker at den skal være, men når vi jobber med datasett har det verdi å ha funksjonaliteten så nærme datasettet som mulig. 
Jeg implementerte derfor en apache-commons variant som en funksjon i den samme databasen. MySQL/MariaDb-varianten finner du på [GitHub](https://github.com/stigmelling/JaroWinkler)

Nå kan jeg raskt se etter likhet ved hjelp av den nye jws-funksjonen.

```sql
select jws( 'Stora gatan', 'Storgata') as "Storgata",
       jws( 'Stora gatan', 'Torggata') as "Torggata";

+--------------------+--------------------+
| Storgata           | Torggata           |
+--------------------+--------------------+
| 0.8954545464385657 | 0.7418831167619048 |
+--------------------+--------------------+

```

For å sammenligne med adresser i matrikkelen, opprettes en tabell som data kan lastes til.


```sql
create table matrikkel_adresse
(
    lokalid                             numeric,
    kommunenummer                       varchar(5),
    kommunenavn                         varchar(50),
    adressetype                         varchar(50),
    adressetilleggsnavn                 varchar(100),
    adressetilleggsnavnKilde            varchar(100),
    adressekode                         varchar(100),
    adressenavn                         varchar(100),
    nummer                              numeric,
    bokstav                             varchar(10),
    gardsnummer                         numeric,
    bruksnummer                         numeric,
    festenummer                         numeric,
    undernummer                         numeric,
    adresseTekst                        varchar(100),
    adresseTekstUtenAdressetilleggsnavn varchar(100),
    EPSG_kode                           varchar(100),
    Nord                                DECIMAL(10,2),
    Oest                                DECIMAL(10,2),
    postnummer                          char(5),
    poststed                            varchar(100),
    grunnkretsnummer                    varchar(10),
    grunnkretsnavn                      varchar(100),
    soknenummer                         varchar(10),
    soknenavn                           varchar(100),
    organisasjonsnummer                 varchar(10),
    tettstednummer                      varchar(10),
    tettstednavn                        varchar(100),
    valgkretsnummer                     numeric,
    valgkretsnavn                       varchar(100)
);

```

Neste trinn er å laste matrikkeldata inn i min MariaDb:

```sql
load data local infile './matrikkelenAdresse.csv'
    into table matrikkel_adresse character set utf8 COLUMNS terminated by ";" 
    IGNORE 1 LINES;

```

For enkelhetsskyld opprettes et lite subset av matrikkeladresser i en egen tabell:

```sql
create table adresse as 
    select distinct adressenavn, postnummer, poststed 
    from matrikkel_adresse 
    where adressetype = 'vegadresse';

```

Vi kan nå enkelt finne gaten som ligner mest.

```sql
select jws("Sweigardsgate", adressenavn) as score, 
        adressenavn, 
        postnummer 
from adresse 
where poststed = 'OSLO' 
order by score desc 
limit 10;

+--------------------+-------------------+------------+
| score              | adressenavn       | postnummer |
+--------------------+-------------------+------------+
| 0.8717194571200845 | Schweigaards gate | 0185       |
| 0.8717194571200845 | Schweigaards gate | 0656       |
| 0.8717194571200845 | Schweigaards gate | 0191       |
| 0.8182692310008888 | Sigurd Syrs gate  | 0273       |
| 0.8057692309215849 | Sigurds gate      | 0650       |
| 0.8005494505302272 | Sverdrups gate    | 0559       |
| 0.7715384617782602 | Sveriges gate     | 0658       |
|        0.761172161 | Gjørstads gate    | 0367       |
| 0.7560439562039142 | Schønings gate    | 0362       |
| 0.7560439562039142 | Schønings gate    | 0356       |
+--------------------+-------------------+------------+
10 rows in set (2.22 sec)


```
Vi fant den riktige skrivemåten, men det gikk ikke veldig raskt. 
I praksis blir nå alle gater i Oslo tatt med i sammenligningen. 
For å redusere antall gatenavn som sammenlignes, gjør jeg et grovt utvalg ved hjelp av Soundex-funksjonen. 
[Soundex](https://en.wikipedia.org/wiki/Soundex) er en funksjon for å beregne en fonetisk-verdi for en tekst. 
Denne beregningen kan med fordel prekalkuleres og indekseres, men selv uten indekser, så har vi fått en markant ytelsesforbedring.

```sql
select jws("Sweigardsgate", adressenavn) as score, 
        adressenavn, 
        postnummer 
from adresse 
where poststed = 'OSLO' 
and left( soundex( "Sweigardsgate"), 3) = left( soundex( adressenavn), 3)
order by score desc 
limit 10;

...
...
10 rows in set (0.05 sec)

```

Dette gjør susen for vårt problem. 
I veldig mange tilfeller klarer vi å finne riktig gate. 

I den endelige løsningen valgte vi å kombinere jaroWinkler metoden med teknikker i fra phonetiske algoritmer som soundex og enkle regulare uttrykk som normaliserte tekstene med hensyn til kjente variasjoner (gt. vs gaten osv)

Vi gikk i fra å ha en treffprosen på 30% til å få nesten 90% av adressene riktig. 