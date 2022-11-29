:title Ikke glem denne indeksen
:author frode
:tech [:postgresql :sql]
:published 2022-11-30

:blurb

Hvordan kan en enkel sletting som **delete from users where id = :id** bruke mange minutter på å fullføre på en kraftig
Postgres-database i produksjon? Denne utfordringen støtte jeg på nylig og det ga meg en liten aha-opplevelse.

:body

For en liten stund siden støtte jeg på en ytelsesutfordring i Postgres da jeg skulle slette én enkelt bruker fra et
system jeg jobber med. Den enkle spørringen `delete from users where id = :id` ble aldri ferdig. Til slutt fikk jeg
kalde føtter og avbrøt. Hva var det som tok så lang tid? Jeg prøvde igjen og sjekket om den ventet på noen låser holdt
av andre. Det gjorde den ikke. De låsene jeg så ble holdt av min egen transaksjon, og de var på andre tabeller enn `users`.
Hvorfor det? Svaret var enkelt, men ikke så opplagt der og da.

I et [tidligere innlegg](/blogg/2020-11-grunnkurs-indeksering/) anbefalte jeg å lage indekser på fremmednøkler for å få
god ytelse på joins. Dette er spesielt viktig for nested-loop-joins. Men det finnes en annen viktig grunn som ikke er
like opplagt: Validering av fremmednøkkel-constraints.

En fremmednøkkel betyr at alle verdier i en kolonne i tabell B må matche en eksisterende verdi i en kolonne i tabell A.
Dette kalles referential integrity mellom to tabeller og er et viktig verktøy for å ivareta god datakvalitet.

```sql
create table users
(
    id   bigserial primary key,
    name text not null
);

create table audit
(
    id         bigserial primary key,
    created_by bigint not null references users (id),
    message    text   not null
);
```

Gitt det enkle skjemaet ovenfor så kan vi kun sette inn rader i `audit` med en `created_by` som finnes i `users`. I
praksis betyr dette at hver gang vi setter inn en rad i `audit` så må databasen slå opp i `users` for å finne en `id`
som matcher `created_by`. En fremmednøkkel kan kun referere til en primærnøkkel eller en kolonne med en unique
constraint. Slike kolonner vil derfor være indeksert og oppslaget vil være effektivt og raskt. Derfor vil en insert
eller update på `audit` gå raskt uten at vi trenger å gjøre noe spesielt. Hvis vi derimot vil slette en bruker eller
endre `id` til en bruker så må databasen sjekke alle rader i `audit` om den har en `created_by` som refererer til den
aktuelle brukeren. Hvis `audit` blir stor kan dette ta lang tid.

> **Derfor er det nesten alltid lurt å lage en indeks på kolonner som er fremmednøkler**

## Analyse

Hvorfor var ikke dette opplagt da jeg støtte på problemet? Fordi validering av fremmednøkkel-constraints er en implisitt
handling som databasen utfører for oss. At den i det hele tatt trenger å gjøre det fremgår verken av spørringen,
definisjonen av tabellen eller explain-planen til spørringen. Jobber man på et stort skjema er det ikke sikkert man har
full oversikt over eksisterende fremmednøkler.

La oss forsøke å generere litt data for å illustrere problemet. Først lager vi 10 000 brukere:

```sql
insert into users (name)
select 'user_' || user_id
from generate_series(1, 10000) user_id;
```

Deretter lager vi mange rader i audit-tabellen:

```sql
insert into audit (created_by, message)
select floor(random() * 10000 + 1),
       repeat('En passe lang melding om at brukeren har utført en handling som er verdt å merke seg. Pluss litt kontekst...', 10)
from generate_series(1, 5000000);
```

Fordi testdatabasen er tom, har null trafikk og laptoppen er alt for rask, trenger vi ganske mange rader for å
illustrere effekten. I produksjon vil problemet være gjeldende selv med langt færre rader.

```sql
delete from audit where id = 400;
1 row affected in 7 ms

delete from users where id = 400;
1 row affected in 4 s 810 ms
```

Det tar altså mer enn 600 ganger lenger tid å slette en rad fra `users` enn `audit`. Og det til tross for at det er 500
ganger flere rader i `audit` 🤨 Kanskje vår venn explain plan kan gi oss svaret?

```sql
explain
delete from audit where id = 400;
------------------------------------------------------------------------------
Delete on audit  (cost=0.43..8.45 rows=1 width=6)
    ->  Index Scan using audit_pkey on audit  (cost=0.43..8.45 rows=1 width=6)
          Index Cond: (id = 400)

explain
delete from users where id = 400;
------------------------------------------------------------------------------
Delete on users  (cost=0.29..8.30 rows=1 width=6)
    ->  Index Scan using users_pkey on users  (cost=0.29..8.30 rows=1 width=6)
          Index Cond: (id = 400)
```

Noe overraskende mener databasen at det skal ta eksakt like lang tid å slette en rad fra de to tabellene, noe vi vet at
ikke stemmer. Vi kan forsøke å la databasen både planlegge og analysere eksekveringen ved hjelp av `explain analyse` og
se om det gir noen hint. Vær obs på at med **analyse** vil spørringen bli kjørt og raden slettet: 

```sql
explain analyse
delete from users where id = 400;
------------------------------------------------------------------------------------------------------------------------
Delete on users  (cost=0.29..8.30 rows=1 width=6) (actual time=0.091..0.092 rows=0 loops=1)
    ->  Index Scan using users_pkey on users  (cost=0.29..8.30 rows=1 width=6) (actual time=0.058..0.059 rows=1 loops=1)
          Index Cond: (id = 400)
Planning Time: 0.124 ms
Trigger for constraint audit_created_by_fkey: time=4803.206 calls=1
Execution Time: 4803.345 ms
```

Og der fikk vi svaret i klartekst: **Trigger for constraint audit_created_by_fkey** er det som tar lang tid. 
Altså verifisering av fremmednøkkel-constrainten. Så for å gjøre sletting raskt igjen har vi 2 alternativer:

1. Fjerne fremmednøkkel-constrainten og gi opp dataintegriteten den gir oss
2. Lage en index på `created_by` i `audit`

Med mindre vi har helt spesielle hensyn å ta så bør vi lage indeksen:

```sql
create index concurrently on audit (created_by);
```

Med indeksen på plass kan vi forsøke å slette brukeren på nytt:

```sql
explain analyse
delete from users where id = 400;
------------------------------------------------------------------------------------------------------------------------
Delete on users  (cost=0.29..8.30 rows=1 width=6) (actual time=0.160..0.161 rows=0 loops=1)
    ->  Index Scan using users_pkey on users  (cost=0.29..8.30 rows=1 width=6) (actual time=0.134..0.136 rows=1 loops=1)
          Index Cond: (id = 400)
Planning Time: 0.107 ms
Trigger for constraint audit_created_by_fkey: time=1.466 calls=1
Execution Time: 1.666 ms
```

Denne gangen gikk det raskt 🎉 og vi er tilbake til forventet kjøretid for sletting av én enkelt rad.

## Oppsummering

En uindeksert fremmednøkkel kan dramatisk påvirke ytelsen til deletes og updates. Behold constraints på fremmednøkler,
men husk å lage indekser på de. Gjør det gjerne samtidig som du oppretter kolonnene siden ytelsesproblemene vil komme
senere når du minst aner det. Så sparer du deg for unødvendig feilsøking.

Og som alltid: Det skader ikke å ta en titt i [manualen](https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-FK) 😅