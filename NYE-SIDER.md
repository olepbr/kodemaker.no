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

## Bilder og bytes

Bilder bør for det meste være JPG, ikke PNG, på grunn av filstørrelse. I begge
tilfeller er det veldig fint om du kjører bildet gjennom tinypng.com for å
ytterligere trimme ned størrelsen. Eventuelt spør pent Magnar eller Christian om
hjelp til dette etter at du har lagt dem til.

**NB!** Bildesystemet vårt finner enn så lenge ikke bilder med JPEG extension,
bruk JPG/jpg.

## Oppdater bilder

Bilder brukes ganske annerledes i det nye designet, og må derfor oppdateres.

Dersom du syns det er vanskelig å finne bilder, så er det en idé å gå til
[Pixabay](http://pixabay.com/) og finne et bilde der. De er gratis å bruke, også
til kommersielt bruk.

### Snakker gjerne om ...

Her trenger nesten alle nye bilder. Du finner dem under:

    resources/public/illustrations/hobbies/

For hobbyen som står først i lista:

 - 600x300 jpg
 - må se bra ut etter at den har blitt en (nedre) halvsirkel
 - unngå helhvit bakgrunn (snø er greit, #fff ikke)

PS! Enn så lenge (håper vi får fikset det snart) så må du lage et 600x600-bilde,
hvor bare nedre halvdel ender opp med å bli brukt.

For hobby nummer to:

 - 600x600 jpg
 - mulig å croppe til både portrett (3/4) og litt landskap (8/7) (altså, ha det mest interessante delen av motivet i midten)
 - unngå helhvit bakgrunn (snø er greit, #fff ikke)

Vi viser ikke flere enn to hobbyer på sidene. Har du mer å vise frem, kan
du godt flytte det til sideprosjekter eller tilsvarende.

### Profilbilder

Ideellt sett har du minst 3, men gjerne flere profilbilder liggende under
`resources/public/foto/profiles/[dittnavn]`. Disse bildene velges det tilfeldig
fra hver gang siten bygges, og det gjøres et forsøk på å ikke bruke samme bilde
mer enn én gang (så langt det er mulig). Du kan påvirke valg av bilder på
følgende måte:

- Legge til og slette bilder fra din mappe. Bildene må ha oppløsning 600x800.
- Til bruk i sirkel croppes bildene fra midten. Dersom dette gir et rart utsnitt
  for et bilde, legg til "no-circle" i filnavnet, så blir ikke bildet brukt i
  sirkler, eks: `resources/public/foto/profiles/stig/stig-no-circle.jpg`.
- "Tag" bilder med `no-cv` for å unngå at det brukes på CV: eks:
  `resources/public/foto/profiles/stig/stig-no-cv.jpg`
- "Tag" bilder med `no-profile` for å unngå at det brukes på profilsiden: eks:
  `resources/public/foto/profiles/stig/stig-no-profile.jpg`
- "Tag" bilder med `no-overview` for å unngå at det brukes på folk-siden: eks:
  `resources/public/foto/profiles/stig/stig-no-overview.jpg`

Filnavn-tags kan kombineres: `resources/public/foto/profiles/stig/stig-no-profile-no-cv.jpg`.

Du kan også velge enkelte bilder selv, men da vil de ikke variere mellom deploys
og siten blir litt tristere, så det er ikke anbefalt:

- Velg selv bilde til CV-en ved å sette `:cv-picture` til
  `"/foto/profiles/[dittnavn]/abc.jpg"` i profil-edn-fila di.
- Velg selv bilde til folk-siden ved å sette `:profile-overview-picture` til
  `"/foto/profiles/[dittnavn]/abc.jpg"` i profil-edn-fila di.
- Velge selv bilde til profilen din ved å sette `:profile-picture` til
  `"/foto/profiles/[dittnavn]/abc.jpg"` i profil-edn-fila di.

### Tech logoer

Disse er helst SVG, og uten navnet på teknologien hvis mulig.
