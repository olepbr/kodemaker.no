:title Verdt å vite om Set i JavaScript 
:author odin
:tech [:javascript]

:blurb

Hvordan funker et Set i JavaScript og når passer det å bruke det?

:body

<svg viewBox="0 0 100 20" xmlns="http://www.w3.org/2000/svg" >
  <g fill-opacity=0.2 stroke-width="0.5">
    <circle cx=35 cy=10 r=6 fill="gold" stroke="goldenrod" />
    <circle cx=45 cy=10 r=8 fill="red" stroke="darkred" />
    <circle cx=55 cy=10 r=6 fill="green" stroke="darkgreen" />
  </g>
</svg>

Et [Set](https://en.wikipedia.org/wiki/Set_%28mathematics%29) er en fundamental datastruktur som er nyttig i mange situasjoner. 
Det er enkelt og greit en samling med unike elementer, for en eller annen definisjon av unikhet (det skal vi komme tilbake til). I [ES2015](https://www.ecma-international.org/ecma-262/6.0/#sec-set-objects) fikk JavaScript støtte for Sets, og i tråd med språkets tradisjon så har implementasjonen noen skarpe kanter som er verdt å vite om.

## API

Her er hvordan man bruker et Set i JavaScript.

```javascript
// Opprette
const dyr = new Set(['katt', 'hund']) 

// Legge til et element
dyr.add('sjiraff') 
=> Set(3) {'katt', 'hund', 'sjiraff'}

// Fjerne et element
dyr.delete('katt')
=> true

// Størrelse
dyr.length
=> undefined

dyr.size
=> 2

// Sjekke medlemskap
dyr.has('hund')
=> true
```

Set holder rede på insertion order, så når du itererer så får du innholdet tilbake i samme rekkefølge som du putta det inn i.

```javascript
for (let d of dyr) console.log(d);
// Logger 'hund' så 'sjiraff'
```

## Likhet, i JavaScript sine øyne
Ingen overraskelser så langt. Et Set støtter vilkårlige typer, og da kan vi putte hva som helst i et Set. Tall, strenger, objekter, lister og til og med andre Set. Dette lover bra!

```javascript
const frukt = new Set()

const eple = {navn: 'eple'}

frukt.add(eple)
=> Set(1)

frukt.add(eple)
=> Set(1)
```

Så kan vi sjekke om der er epler i frukten

```javascript
frukt.has({navn: 'eple'})
=> false
```

Doh! Men dessverre helt som forventet. Det er konsistent med konseptet av likhet i JavaScript. Mekanismen for å sammenligne handler om å sjekke referanser, ikke verdier. 

Det betyr:

```javascript
// Vi bruker referansen til eple
frukt.has(eple)
=> true

// To ulike objekter, med samme verdier
const grunnstoff = new Set([{navn: 'Thorium'}, {navn: 'Thorium'} ])
=> Set(2)
```

Å jobbe med verdier istedenfor referanser er mer intuitivt og gir mindre rom for feil. Hvis du ønsker verdisemantikk så må du ty til bibliotek som Immutable.js eller språk som [ClojureScript](https://www.kodemaker.no/clojurescript).


## Funksjonell programmering
Vi er alle glade i [funksjonell programmering](https://www.kodemaker.no/16-minutter-om-pure-functions/). Arrays har støttet map, filter og reduce lenge. Bruken av Set passer også fint inn i det paradigmet.

```javascript
grunnstoff.map(f => f.navn)
=> Error: grunnstoff.map is not a function 
```

Overraskende nok støtter ikke Set verken map, filter eller reduce. Det foreligger riktignok [et forslag](https://github.com/tc39/proposal-collection-methods) om å legge det til. 

I mellomtiden må man konvertere til en Array først.

```javascript
[...grunnstoff].map(f => f.navn) 
=> ['Thorium', 'Thorium']
```

## Set, en dårlig Venn
Hvis det er én ting som et Set kan gjøre bedre enn noen annen datastruktur, så er det å utføre matematiske Set-operasjoner som union, snitt og disjunksjoner. Hold deg fast.

<svg viewBox="0 0 100 20" xmlns="http://www.w3.org/2000/svg" >
  <g fill-opacity=0.2 stroke-width="0.5">
    <circle cx=45 cy=10 r=8 fill="red" stroke="darkred" />
    <circle cx=55 cy=10 r=8 fill="green" stroke="darkgreen" />
  </g>
</svg>


```javascript
const presidenter = new Set(['Abraham', 'Bill', 'Donald'])
const tegneserieFigurer = new Set(['Donald', 'Langbein', 'Svampebob'])

// Hvilke presidenter er også tegneseriefigurer?
presidenter.intersection(tegneserieFigurer)
=> Error: presidenter.intersection is not a function
```

🤯🤯🤯🤯🤯 

Dette var en stor overraskelse. Det var sikkert en god grunn at det ikke ble støtta ut av boksen. Nok en gang så foreligger det [et forslag](https://github.com/tc39/proposal-set-methods) om å legge til Set-funksjoner i språket. Da har vi hvertfall noe å glede oss til. 

Mens vi venter så kan vi lage våre egne Set-funksjoner, som skissert i denne [Mozilla-artikkelen](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Set#Implementing_basic_set_operations)

```javascript
function intersection(setA, setB) {
    var _intersection = new Set();
    for (var elem of setB) {
        if (setA.has(elem)) {
            _intersection.add(elem);
        }
    }
    return _intersection;
}
```

## Set er data, sant?
La oss prøve å dele informasjon om et Set med andre

```javascript
JSON.stringify({unikeNavn: new Set(['ape', 'katt'])})
=> "{"unikeNavn":{}}"
```

Det er ikke så rart, siden JSON-standarden er basert på et subsett av JavaScript og [er ikke utvidbar](https://github.com/shaunxcode/jsedn). 

Så hvis du vil dele Sets så må du finne på din [egen encoding](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/JSON/stringify#The_replacer_parameter) eller konvertere de til en Array.

Hvis man kunne drømt om en bedre verden så hadde det vært fint om Set fikk sin egen syntaks-literal.

```javascript
const minArray = [1,2,3]
const mittSet  = #{1,2,3} 
```

## Når passer det å bruke Set?

På tross av en noe mangelfull implementasjon så er Set nyttig å ha i verktøykassen.

For de datatypene som faktisk sammenlignes basert på verdier, slik som tall og strenger, så kan du enkelt sørge for at en samling inneholder kun unike verdier.

```javascript
const unikeNavn = new Set([])
unikeNavn.add('Janne')
unikeNavn.add('Janne')
```

Med en array eller et objekt så måtte du jobbet litt hardere for samme effekt.

Et annet eksempel er hvor du har en samling objekter og ønsker å holde oversikt over et subsett av de. 

```javascript
const dyr = [{id: '1', navn: 'Katt', alder: 4}, 
             {id: '2', navn: 'Hund', alder: 5},
             {id: '3', navn: 'Hest', alder: 8}]

const selektert = new Set(['1', '2'])
```

Ut over det så får vi krysse fingrene for at Set-støtten blir bedre i fremtiden.
