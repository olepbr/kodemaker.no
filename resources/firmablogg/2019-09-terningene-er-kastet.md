:title Terningene er kastet med CSS
:published 2019-09-04
:author magnar
:tech [:css :html]

:blurb

Snart er det JavaZone, og da blir det kosetime med [Christian](/christian) og [meg](/magnar). Vi skal lage et nytt zombiespill - denne gangen med terninger.

Terningspill er langt morsommere hvis man kan se terningene rulle, så jeg brettet opp ermene (ikke armene) og skrev litt CSS i forberedelse til presentasjonen. Her er det jeg lærte om å kaste terninger med CSS.

:body

<style>
.example {
    border: 1px solid #ddd;
    padding: 60px;
    margin: 20px;
    border-radius: 5px;
    text-align: center;
    position: relative;
}
.example-label {
    position: absolute;
    top: 10px;
    left: 20px;
    color: #777;
    font-family: monospace;
    text-align: left;
}
.persp {
    perspective: 400px;
}
.persp-orig {
    perspective-origin: 50% 0%;
}
.dice,
.cube {
    width: 120px;
    height: 120px;
    position: relative;
    transform-style: preserve-3d;
    display: inline-block;
    animation-timing-function: ease-in-out;
    animation-iteration-count: 1;
    animation-duration: 1.4s;
    animation-fill-mode: both;
}
.cube.w-transition {
    transition: transform 600ms ease;
}
.cube.rotated {
    transform: rotateY(30deg) rotateX(30deg) rotateZ(30deg);
}
.face {
    height: 120px;
    width: 120px;
    background-color: rgba(255,255,255,0.7);
    position: absolute;
    border-radius: 6px;
    border: 1px solid #aaa;
    box-shadow: inset 0 0 20px rgba(0,0,0,0.2);
    font-size: 60px;
    line-height: 120px;
    color: #aaa;
}
.fail-face-1 { }
.fail-face-2 { transform: rotateY(90deg); }
.fail-face-3 { transform: rotateY(90deg) rotateX(90deg); }
.fail-face-4 { transform: rotateY(180deg) rotateZ(90deg); }
.fail-face-5 { transform: rotateY(-90deg) rotateZ(90deg); }
.fail-face-6 { transform: rotateX(-90deg); }

.face-1 { transform: translateZ(60px); }
.face-2 { transform: rotateY(90deg) translateZ(60px); }
.face-3 { transform: rotateY(90deg) rotateX(90deg) translateZ(60px); }
.face-4 { transform: rotateY(180deg) rotateZ(90deg) translateZ(60px); }
.face-5 { transform: rotateY(-90deg) rotateZ(90deg) translateZ(60px); }
.face-6 { transform: rotateX(-90deg) translateZ(60px); }

.facing-1 { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
.facing-2 { transform: rotateX(0deg) rotateY(-90deg) rotateZ(0deg); }
.facing-3 { transform: rotateX(-90deg) rotateY(-90deg) rotateZ(0deg); }
.facing-4 { transform: rotateX(0deg) rotateY(180deg) rotateZ(90deg); }
.facing-5 { transform: rotateX(270deg) rotateY(180deg) rotateZ(90deg); }
.facing-6 { transform: rotateX(90deg) rotateY(0deg) rotateZ(0deg); }

@keyframes roll-1-to-1 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-1-to-2 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
}

@keyframes roll-1-to-3 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-1-to-4 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-1-to-5 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-1-to-6 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}

.roll-1-to-1 { animation-name: roll-1-to-1; }
.roll-1-to-2 { animation-name: roll-1-to-2; }
.roll-1-to-3 { animation-name: roll-1-to-3; }
.roll-1-to-4 { animation-name: roll-1-to-4; }
.roll-1-to-5 { animation-name: roll-1-to-5; }
.roll-1-to-6 { animation-name: roll-1-to-6; }

@keyframes roll-2-to-1 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-2-to-2 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(0deg) rotateY(-810deg) rotateZ(360deg); }
}

@keyframes roll-2-to-3 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-2-to-4 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-2-to-5 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-2-to-6 {
    from { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}

.roll-2-to-1 { animation-name: roll-2-to-1; }
.roll-2-to-2 { animation-name: roll-2-to-2; }
.roll-2-to-3 { animation-name: roll-2-to-3; }
.roll-2-to-4 { animation-name: roll-2-to-4; }
.roll-2-to-5 { animation-name: roll-2-to-5; }
.roll-2-to-6 { animation-name: roll-2-to-6; }

@keyframes roll-3-to-1 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-3-to-2 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(0deg) rotateY(-810deg) rotateZ(360deg); }
}

@keyframes roll-3-to-3 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(270deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-3-to-4 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-3-to-5 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-3-to-6 {
    from { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}

.roll-3-to-1 { animation-name: roll-3-to-1; }
.roll-3-to-2 { animation-name: roll-3-to-2; }
.roll-3-to-3 { animation-name: roll-3-to-3; }
.roll-3-to-4 { animation-name: roll-3-to-4; }
.roll-3-to-5 { animation-name: roll-3-to-5; }
.roll-3-to-6 { animation-name: roll-3-to-6; }

@keyframes roll-4-to-1 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-4-to-2 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(0deg) rotateY(-810deg) rotateZ(360deg); }
}

@keyframes roll-4-to-3 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(270deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-4-to-4 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-4-to-5 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-4-to-6 {
    from { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}

.roll-4-to-1 { animation-name: roll-4-to-1; }
.roll-4-to-2 { animation-name: roll-4-to-2; }
.roll-4-to-3 { animation-name: roll-4-to-3; }
.roll-4-to-4 { animation-name: roll-4-to-4; }
.roll-4-to-5 { animation-name: roll-4-to-5; }
.roll-4-to-6 { animation-name: roll-4-to-6; }

@keyframes roll-5-to-1 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-5-to-2 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(0deg) rotateY(-810deg) rotateZ(360deg); }
}

@keyframes roll-5-to-3 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(270deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-5-to-4 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-5-to-5 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(270deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-5-to-6 {
    from { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}

.roll-5-to-1 { animation-name: roll-5-to-1; }
.roll-5-to-2 { animation-name: roll-5-to-2; }
.roll-5-to-3 { animation-name: roll-5-to-3; }
.roll-5-to-4 { animation-name: roll-5-to-4; }
.roll-5-to-5 { animation-name: roll-5-to-5; }
.roll-5-to-6 { animation-name: roll-5-to-6; }

@keyframes roll-6-to-1 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-6-to-2 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(0deg) rotateY(-810deg) rotateZ(360deg); }
}

@keyframes roll-6-to-3 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(270deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-6-to-4 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-6-to-5 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(270deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-6-to-6 {
    from { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(720deg); }
}

.roll-6-to-1 { animation-name: roll-6-to-1; }
.roll-6-to-2 { animation-name: roll-6-to-2; }
.roll-6-to-3 { animation-name: roll-6-to-3; }
.roll-6-to-4 { animation-name: roll-6-to-4; }
.roll-6-to-5 { animation-name: roll-6-to-5; }
.roll-6-to-6 { animation-name: roll-6-to-6; }

@keyframes scale {
    from { transform: scale3d(1, 1, 1) translate3d(0, 0, 0); }
    50% { transform: scale3d(0.2, 0.2, 0.2) translate3d(0, -200px, 0); }
    to { transform: scale3d(1, 1, 1) translate3d(0, 0, 0); }
}

.rolling.dice {
    animation-name: scale;
    animation-timing-function: ease-in-out;
    animation-iteration-count: 1;
    animation-duration: 1.8s;
    transition: transform 300ms ease;
}
</style>

Snart er det JavaZone, og da blir det kosetime med [Christian](/christian) og [meg](/magnar). Vi skal lage et nytt zombiespill - denne gangen med terninger.

Terningspill er langt morsommere hvis man kan se terningene rulle, så jeg brettet opp ermene (ikke armene) og skrev litt CSS i forberedelse til presentasjonen. Her er det jeg lærte om å kaste terninger med CSS.

## Først trenger vi en kube

En kube har seks sider, og de må vi tegne hver for seg:

```html
<div class="example">
  <div class="cube">
    <div class="face face-1"></div>
    <div class="face face-2"></div>
    <div class="face face-3"></div>
    <div class="face face-4"></div>
    <div class="face face-5"></div>
    <div class="face face-6"></div>
  </div>
</div>
```

La oss forankre sidene i en kube:

```css
.cube {
    width: 120px;
    height: 120px;
    position: relative;
    transform-style: preserve-3d;
    display: inline-block;
}
```

Her sier vi `position: relative` fordi alle sidene av terningen skal ligge oppå
hverandre i utgangspunktet.

Det neste viktige poenget er `preserve-3d`: Dette lar oss rotere dette elementet
og dets barn i samme tredimensjonale kontekst. Dette kommer vi tilbake til snart.

På tide å legge litt styling på sidene:

```css
.face {
    height: 120px;
    width: 120px;
    background-color: rgba(255,255,255,0.7);
    position: absolute;
    border-radius: 6px;
    border: 1px solid #aaa;
    box-shadow: inset 0 0 20px rgba(0,0,0,0.2);
}
```

På dette tidspunktet ser terningen vår slik ut:

<div class="example">
<div class="cube">
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
</div>
</div>

Ikke så tredimensjonal enda. Litt vanskelig å se at det er seks sider. For å kunne se hva som skjer videre, må vi først rotere kuben litt:

```css
.cube {
    transform: rotateY(30deg) rotateX(30deg) rotateZ(30deg);
}
```

<div class="example">
<div class="cube rotated">
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
  <div class="face"></div>
</div>
</div>

La oss starte litt naivt, og bare rotere all sidene på plass:

```css
.face-1 { }
.face-2 { transform: rotateY(90deg); }
.face-3 { transform: rotateY(90deg) rotateX(90deg); }
.face-4 { transform: rotateY(180deg) rotateZ(90deg); }
.face-5 { transform: rotateY(-90deg) rotateZ(90deg); }
.face-6 { transform: rotateX(-90deg); }
```

<div class="example">
<div class="cube rotated">
  <div class="face fail-face-1"></div>
  <div class="face fail-face-2"></div>
  <div class="face fail-face-3"></div>
  <div class="face fail-face-4"></div>
  <div class="face fail-face-5"></div>
  <div class="face fail-face-6"></div>
</div>
</div>

Observer at alle sidene har rotert om den sentrale aksen i kuben. Det ser ikke ut som noen terning akkurat.

Vi trenger å skyve hver side vekk fra midten. Hvor mye? Sidene våre er jo
`120px` store, så vi må skyve `60px` i hver retning. Voila:

```css
.face-1 { transform: translateZ(60px); }
.face-2 { transform: rotateY(90deg) translateZ(60px); }
.face-3 { transform: rotateY(90deg) rotateX(90deg) translateZ(60px); }
.face-4 { transform: rotateY(180deg) rotateZ(90deg) translateZ(60px); }
.face-5 { transform: rotateY(-90deg) rotateZ(90deg) translateZ(60px); }
.face-6 { transform: rotateX(-90deg) translateZ(60px); }
```

<div class="example">
<div class="cube rotated">
  <div class="face face-1"></div>
  <div class="face face-2"></div>
  <div class="face face-3"></div>
  <div class="face face-4"></div>
  <div class="face face-5"></div>
  <div class="face face-6"></div>
</div>
</div>

Og dermed har vi fått en kube. Men den ser ikke helt ... riktig ut. Det er noe
galt med perspektivet:

```css
.example {
    perspective: 400px;
}
```

<div class="example persp">
<div class="cube rotated">
  <div class="face face-1"></div>
  <div class="face face-2"></div>
  <div class="face face-3"></div>
  <div class="face face-4"></div>
  <div class="face face-5"></div>
  <div class="face face-6"></div>
</div>
</div>

Ahh, det var bedre.

Når man setter `perspective` så sier man hvor langt det er mellom brukeren og
punkter i posisjon Z0. Punkter med høyere Z oppleves nærmere, og punkter med
lavere Z lenger unna. Forsvinningspunktet er per default i midten av elementet
med perspektiv, men dette kan også flyttes.

La oss prøve det. Først fjerner vi roteringen av kuben for å gjøre det tydeligere:

<div class="example persp">
<div class="cube">
  <div class="face face-1"></div>
  <div class="face face-2"></div>
  <div class="face face-3"></div>
  <div class="face face-4"></div>
  <div class="face face-5"></div>
  <div class="face face-6"></div>
</div>
</div>

Nå kan vi flytte forsvinningspunktet:

```
.example {
    perspective-origin: 50% 0%;
}
```

<div class="example persp persp-orig">
<div class="cube">
  <div class="face face-1"></div>
  <div class="face face-2"></div>
  <div class="face face-3"></div>
  <div class="face face-4"></div>
  <div class="face face-5"></div>
  <div class="face face-6"></div>
</div>
</div>

Helt supert. Nå ser det mer ut som om terningen ligger på et bord, og ikke
svever i lufta.

PS! Tidligere nevnte jeg såvidt `transform-style: preserve-3d;`. Nå er det lettere å
forklare hvorfor denne er viktig: Vi roterer kuben og hver side med separate `transform`-regler. Uten å
spesifisere `preserve-3d` ville disse blitt rotert uavhengig av hverandre. Nå
roteres de i samme kontekst.

La oss også slenge på noen tall på sidene:

```html
<div class="example">
  <div class="cube">
    <div class="face face-1">1</div>
    <div class="face face-2">2</div>
    <div class="face face-3">3</div>
    <div class="face face-4">4</div>
    <div class="face face-5">5</div>
    <div class="face face-6">6</div>
  </div>
</div>
```

```css
.face {
    font-size: 60px;
    line-height: 120px;
    color: #aaa;
}
```
<div class="example persp persp-orig">
<div class="cube">
  <div class="face face-1">1</div>
  <div class="face face-2">2</div>
  <div class="face face-3">3</div>
  <div class="face face-4">4</div>
  <div class="face face-5">5</div>
  <div class="face face-6">6</div>
</div>
</div>

## Så var det dette med kastingen da

For at en terning skal oppfattes som kastet må den:

- være i lufta
- rotere
- lande på en side

La oss starte på begynnelsen. For å gi inntrykk av å være i lufta uten å ta for
mye plass på skjermen, så bestemte jeg meg for å zoome den vekk og litt opp. Her
er animasjonsdefinisjonen:

```css
@keyframes scale {
    from { transform: scale3d(1, 1, 1) translate3d(0, 0, 0); }
    50% { transform: scale3d(0.2, 0.2, 0.2) translate3d(0, -200px, 0); }
    to { transform: scale3d(1, 1, 1) translate3d(0, 0, 0); }
}
```

Den starter på utgangsposisjonen, forsvinner ned til 20% i størrelse og opp
200px, før den kommer tilbake igjen.

Ettersom jeg ønsker at denne animasjonen skal skje uavhengig av hvordan
terningen er rotert, så må jeg gjøre skaleringen utenfor `.cube`. Jeg
lager en `.dice`:

```html
<div class="example">
  <div class="dice">
    <div class="cube">
      <div class="face face-1">1</div>
      <div class="face face-2">2</div>
      <div class="face face-3">3</div>
      <div class="face face-4">4</div>
      <div class="face face-5">5</div>
      <div class="face face-6">6</div>
    </div>
  </div>
</div>
```

Den får samme regler som kuben, slik:

```css
.dice,
.cube {
    width: 120px;
    height: 120px;
    position: relative;
    transform-style: preserve-3d;
    display: inline-block;
}
```

Så gjelder det å koble inn animasjonen når terningen skal rulles:

```css
.rolling.dice {
    animation-name: scale;
    animation-timing-function: ease-in-out;
    animation-iteration-count: 1;
    animation-duration: 1.8s;
}
```

<script>
    function scaleDice(e) {
       var label = document.getElementById('scaling-class');
       e.className = 'dice rolling';
       label.innerText = '.dice.rolling';
       setTimeout(function () { e.className = 'dice'; label.innerText = '.dice'; }, 2000);
    }
</script>

<div class="example persp persp-orig">
  <div class="example-label" id="scaling-class">.dice</div>
  <div class="dice" onclick="scaleDice(this)">
    <div class="cube">
      <div class="face face-1">1</div>
      <div class="face face-2">2</div>
      <div class="face face-3">3</div>
      <div class="face face-4">4</div>
      <div class="face face-5">5</div>
      <div class="face face-6">6</div>
    </div>
  </div>
</div>

Klikk på terningen for å "rulle" den. Scriptet legger her bare på klassen
`rolling` - det er alt som skal til for å sparke animasjonen i gang. (klassen
fjernes også igjen etter et par sekunder, for at du skal kunne klikke flere ganger)

På tide å rotere terningen også. Vi kan starte med å finne hvordan kuben må
roteres for å vise hver side:

```css
.facing-1 { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
.facing-2 { transform: rotateX(0deg) rotateY(-90deg) rotateZ(0deg); }
.facing-3 { transform: rotateX(-90deg) rotateY(-90deg) rotateZ(0deg); }
.facing-4 { transform: rotateX(-90deg) rotateY(180deg) rotateZ(90deg); }
.facing-5 { transform: rotateX(90deg) rotateY(180deg) rotateZ(90deg); }
.facing-6 { transform: rotateX(90deg) rotateY(0deg) rotateZ(0deg); }
```

Vi kan også legge på en transition, for syns skyld:

```css
.cube {
    transition: transform 600ms ease;
}
```

<script>
    function changeFacing(e) {
        var i = Number(e.className.substring(25));
        var f = ((i % 6) + 1);
        e.className = 'cube w-transition facing-' + f;
        document.getElementById("facing-class").innerText = ".cube.facing-" + f;
    }
</script>
<div class="example persp persp-orig">
  <div class="example-label" id="facing-class">.cube.facing-1</div>
  <div class="dice">
    <div class="cube w-transition facing-1" onclick="changeFacing(this)">
      <div class="face face-1">1</div>
      <div class="face face-2">2</div>
      <div class="face face-3">3</div>
      <div class="face face-4">4</div>
      <div class="face face-5">5</div>
      <div class="face face-6">6</div>
    </div>
  </div>
</div>

Klikk på terningen for å snu den til neste side.

Problemet med denne teknikken er at den ikke riktig fanger opplevelsen av en
snurrende terning i lufta. Spesielt de første transisjonene var temmelig
trauste. Men du la kanskje merke til at de påfølgende transisjonene hadde mer
futt?

Trikset her er å rotere til riktig side, men å snurre litt ekstra mange ganger.
La oss si at vi skal snurre til side 1. Istedet for å gå til `0 0 0` kan vi gå
til `720 -360 360`. Det vil være samme side som vises, men kuben må rotere langt
mer for å komme seg dit.

Det kan jo også hende at terningen skal lande på samme side som den startet. Da
må vi også sørge for at terningen ser ut til å snurre litt først.

Det jeg endte opp med var å definere animasjoner fra/til alle sider. Noe slikt:

```css
@keyframes roll-1-to-1 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(720deg) rotateY(0deg) rotateZ(0deg); }
}

@keyframes roll-1-to-2 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(360deg) rotateY(-810deg) rotateZ(0deg); }
}

@keyframes roll-1-to-3 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(-90deg) rotateZ(360deg); }
}

@keyframes roll-1-to-4 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-360deg) rotateY(180deg) rotateZ(-270deg); }
}

@keyframes roll-1-to-5 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(-450deg) rotateY(540deg) rotateZ(450deg); }
}

@keyframes roll-1-to-6 {
    from { transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg); }
    to { transform: rotateX(450deg) rotateY(360deg) rotateZ(0deg); }
}
```

Og så videre for `2-to-1`, `2-to-2`, `2-to-3` etc etc. Totalt 36 keyframes, med tilhørende css-klasser:

```css
.roll-1-to-1 { animation-name: roll-1-to-1; }
.roll-1-to-2 { animation-name: roll-1-to-2; }
.roll-1-to-3 { animation-name: roll-1-to-3; }
.roll-1-to-4 { animation-name: roll-1-to-4; }
.roll-1-to-5 { animation-name: roll-1-to-5; }
.roll-1-to-6 { animation-name: roll-1-to-6; }
```

Her er animasjonsdeklarasjon for kuben:

```css
.cube {
    animation-timing-function: ease-in-out;
    animation-iteration-count: 1;
    animation-duration: 1.4s;
    animation-fill-mode: both;
}
```

Og her kan du se de siste to triksene mine:

- `animation-fill-mode: both`

    Denne sørger for at terningen beholder posisjonen sin når den er ferdig
    animert. Uten denne vil terningen hoppe tilbake til utgangsposisjonen sin når
    animasjonen er ferdig.

- `animation-duration: 1.4s`

    Roteringen er satt til 1.4s, mens skaleringen er satt til 1.8s (lenger oppe
    i artikkelen). Dermed vil ikke kuben rotere hele veien - den stabiliserer seg
    mot slutten, og ser ut til å bli satt pent ned på bordet de siste 400ms.

Resultatet kan du se her:

<script>
    function rollDice(e) {
        var prev = Number(e.className.substring(e.className.length - 1));
        var next = Math.floor(Math.random()*6) + 1;
        var c = 'roll-' + prev + '-to-' + next;
        var label = document.getElementById("rolling-class");
        e.className = 'cube w-transition ' + c;
        label.innerHTML = '.dice.rolling<br>.cube.' + c;
        var d = document.getElementById("rolling-dice");
        d.className = 'dice rolling';
        setTimeout(function () { d.className = 'dice'; label.innerHTML = '.dice<br>.cube.' + c; }, 2000);
    }
</script>
<div class="example persp persp-orig">
  <div class="example-label" id="rolling-class">.dice<br>.cube.facing-1</div>
  <div class="dice" id="rolling-dice">
    <div class="cube w-transition facing-1" onclick="rollDice(this)">
      <div class="face face-1">1</div>
      <div class="face face-2">2</div>
      <div class="face face-3">3</div>
      <div class="face face-4">4</div>
      <div class="face face-5">5</div>
      <div class="face face-6">6</div>
    </div>
  </div>
</div>

Klikk for å rulle terning.
