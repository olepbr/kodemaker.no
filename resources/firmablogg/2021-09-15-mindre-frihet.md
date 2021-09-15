:title Kan mindre frihet være bra?
:author magnar
:tech [:funksjonell-programmering]
:published 2021-09-15

:blurb

Er det alltid bra med mer frihet? Nei, ikke når vi skriver kode. La meg forklare.

:body

Det første dataprogrammet jeg noensinne skrev så slik ut:

```
10 PRINT "Magnar er kul"
20 GOTO 10
```

Det var veldig kult. Åpenbart.

`GOTO` er den ultimate friheten innen programflyt. Man kan flytte eksekveringen
til hvor som helst i koden. Dritbra greier da!

Ikke sant?

Du gikk ikke på den, nei. I dag er vi naturligvis enige alle sammen om at `GOTO`
er en uting. Det startet da Dijkstra popularisterte uttrykket "considered
harmful" på slutten av 60-tallet.

Jeg kunne kanskje allerede her klappet meg selv på skulderen. Spørsmålet i
tittelen er allerede besvart. Men jeg skal skrive litt til.

## En artig øvelse du har gjort før

Her skal du få en kodesnutt som du har sett 100 ganger:

```js
var result = [];
for (var i = 0, l = items.length; i < l; i++) {
  var item = items[i];
  result.push(transform(item));
}
```

Hva gjør den? Ta en ekstra titt gjennom og se.

Svaret er at den samler sammen resultatet av å kalle funksjonen `transform` på
alle elementer i `items`. Det er det vi pleier å kalle map.

En ny snutt:

```js
var result = [];
for (var i = 0, l = items.length; i < l; i++) {
  var item = items[i];
  if (check(item)) {
    result.push(item);
  }
}
```

Hva gjør denne da? Er du sikker?

Joda, du hadde nok rett. Svaret er at den finner alle elementer i `items` som
består sjekken i `check`, og samler dem i en ny liste. Det er altså filter
(eller select).

La oss avslutte denne artige øvelsen med noen flere kodesnutter. Se om du klarer
se hva de gjør.

```js
var result = [];
for (var i = 0, l = items.length; i < l; i++) {
  var item = items[i];
  if (check(item)) {
    result.push(transform(item));
  }
}
```

```js
var result = [];
for (var i = 0, l = items.length; i < l; i++) {
  var item = transform(items[i]);
  if (check(item)) {
    result.push(item);
  }
}
```

```js
var result = [];
for (var i = 0, l = items.length; i < l; i++) {
  var item = items[i];
  if (check(transform(item))) {
    result.push(item);
  }
}
```

## Det er ikke bare GOTO som er harmful

Gjorde det der litt vondt? Kanskje du ikke gadd engang?

Hvorfor var det vondt? Kan det ha noe å gjøre med at kodesnuttene var veldig
like, men gjorde veldig forskjellige ting? Eller at du måtte gjenskape loopen
mentalt for å pusle sammen intensjonen bak koden?

Hadde det vært greiere å parse disse?

```js
var result = items.map(transform);
```

```js
var result = items.filter(check);
```

```js
var result = items.filter(check).map(transform);
```

```js
var result = items.map(transform).filter(check);
```

```js
var result = items.filter(i => check(transform(i));
```

Husker du at jeg spurte deg "Er du sikker?". Da måtte du nesten gå tilbake til
for-loopen og sjekke. Hadde du gått glipp av en detalj? Hadde jeg vært snedig
med min termineringsklausul? Eller den der `++`-en?

Hva om jeg spør deg om `items.map(transform)` ... er du sikker?

Såklart du er sikker.

## for gir oss for mye frihet

Frihet til å rote det til. Frihet til å gjemme snedige detaljer. Og ikke minst:
Frihet til å bygge ut loopen. Legge på litt mer greier. En ekstra sjekk. En
ekstra transformasjon. `+= 2` istedet for `++`.

Og hver gang du snubler over en `for` så må du sjekke. Hvilke detaljer er det
her? Har jeg stirret lenge nok på denne koden til å være sikker på hva den gjør?

Det koster dyrt.
