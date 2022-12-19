:title Noen tanker om hvorfor jeg ikke savner statiske typer
:author magnar
:tech [:programming :clojure]
:published 2022-11-16

:blurb

Statiske typesystemer er åpenbart bra greier. Ikke bare får man uttrykt
intensjon og form på dataene veldig eksplisitt, men det muliggjør også masse
deilig editor-støtte som auto completion og refaktorering. Likevel er det
[Clojure](/clojure/) -- et særdeles dynamisk språk -- som er min favoritt. Jeg
har tenkt litt i det siste ... Hvorfor savner jeg ikke statiske typer?

:body

Statiske typesystemer er åpenbart bra greier. Ikke bare får man uttrykt
intensjon og form på dataene veldig eksplisitt, men det muliggjør også masse
deilig editor-støtte som auto completion og refaktorering. Likevel er det
[Clojure](/clojure/) -- et særdeles dynamisk språk -- som er min favoritt.

Jeg har tenkt litt i det siste ... Hvorfor savner jeg ikke statiske typer?

### Stabilitet og robusthet

Statiske typesystemer hjelper deg å lage robust og stabil kode. Man "låser ned"
koden med typer, og får noen garantier tilbake. Den blir sikrere, men samtidig
mer rigid.

Det kan sammenlignes med å øke sikkerheten ved å prodsette kode sjeldnere. Hvis
vi tar en ekstra uke til kvalitetskontroll og testing, så får vi færre bugs i
produksjon. Hvis vi har flere prosedyrer og kontrollpunkter, så får vi fanget
feilene før de går ut til kundene.

Både når det gjelder kode og prodsetting, bytter vi i disse scenarioene
fleksibilitet og hastighet for stabilitet og robusthet.

Det er nok en fornuftig trade-off i mange bransjer -- særlig livskritiske
systemer knyttet til flymaskiner, medisin og sånt. Likevel jobber de fleste av
oss med systemer hvor feil ikke betyr slutten på visa.

Kanskje er det andre avveininger som gir bedre utfall da?

### Fleksibilitet og hastighet

Etter 10 år som Clojure-utvikler på fulltid gjør jeg fortsatt mange teite feil
som et godt typesystem hadde reddet meg fra. Men greia er: Jeg fikser dem
stort sett i løpet av sekunder eller minutter. Som oftest på grunn av god
testdekning eller fra [øyeblikkelig tilbakemelding i
REPLet](/blogg/2022-10-repl/).

Noen ganger klarer jeg å fomle en sånn teit feil helt ut til prod. Da er det
ikke like moro lenger. Men heldigvis kan jeg få ut en fiks i løpet av minutter
-- fordi vi dytter kode til produksjon mange ganger om dagen.

Og her er kanskje poenget: Det er ikke bare bugfikser som går fort ut i
produksjon. Det er features også. Det er en egen følelse å komme tilbake til
kunden rett etter lunsj og si "Nå er den endringen du ønsket deg i dag morges
ute i prod."

For å være helt tydelig: Man kan selvfølgelig fikse ting og prodsette dem raskt
med statisk typede språk også. Det avhenger av så mange faktorer. Poenget mitt
er at det er mulig å sammenligne de avveiningene som gjøres i disse to
situasjonene: Mer fleksibilitet vs mer stabilitet. Mer hastighet vs mer
robusthet.

Det er utvilsomt fordeler og ulemper med begge tilnærminger, men mine personlige
preferanser lener seg mer mot hyppig og smidig, enn mot robust og stabil.

### Alt er trade-offs

Mine avveininger er ikke nødvendigvis riktig for deg. Du kan ha andre personlige
preferanser, for eksempel. Eller kanskje det har noe å gjøre med størrelsen på
team? Jeg vil tro at denne hyppige/smidige tilnærmingen fungerer best med små
team. Jo større team, jo større behov for tydelig struktur og prosess. Jo
viktigere at ting er robuste og stabile.

Kanskje det er derfor jeg trekkes til mindre team også, for denne måten å jobbe
på er en stor grunn til at jeg fortsatt elsker jobben min. Jeg elsker følelsen
av å levere noe raskere og bedre enn det kunden hadde forventet. Det gjør jeg
personlig best med et dynamisk språk, fleksibel kode, i et lite team som dytter
kode til produksjon på løpende bånd. Selv om jeg da av og til må rette en feil i
prod litt brennkvikt.

<br><br><br>

*Psst! Brenner du inne med noe? Jeg slår gjerne av en prat om
temaet, så fyr av en melding til meg på
[Mastodon](https://snabelen.no/@magnars).* 😊
