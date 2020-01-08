# Krisepakke

Er du forberedt hvis katastrofen inntreffer? Hva skal du og dine nærmeste gjøre
hvis Kodemaker skal lansere nye nettsider?

1. Pust rolig.
2. Ta en titt på profilsiden din.

Mest sannsynlig må du gjøre litt innholdsarbeid for at det skal se bra ut.

## Starte ny web

Slik ser du de nye nettsidene:

```
lein repl
(start)
```

Gå deretter til http://localhost:3333

## Oppdater bilder

Bilder brukes ganske annerledes i det nye designet, og må derfor oppdateres.

### Snakker gjerne om ...

Her trenger nesten alle nye bilder. Du finner dem under:

    resources/public/illustrations/hobbies/

For hobbyen som står først i lista:

 - 544x272 jpg
 - må se bra ut etter at den har blitt en (nedre) halvsirkel
 - unngå helhvit bakgrunn (snø er greit, #fff ikke)

For hobby nummer to:

 - 800x800 jpg
 - mulig å croppe til både portrett og landskap (altså, ha det mest interessante delen av motivet i midten)
 - unngå helhvit bakgrunn (snø er greit, #fff ikke)

Vi viser ikke flere enn to hobbyer på sidene. Dersom du har mer å vise frem, kan
du godt flytte det til sideprosjekter eller tilsvarende.
