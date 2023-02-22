:title Dumme klienter
:author magnar
:published 2023-02-22
:tech [:arkitektur :design :clojure]

:blurb

Dette ser ut til å ha blitt et kåseri om data og dumme klienter. Ikke dumme
oppdragsgivere, altså, men noen veldig dumme web-klienter.

:body

Da Norge var underlagt den tyske okkupasjonsmakten på førtitallet var det en
populær aktivitet å ta heisen opp i tredje etasje, slik at man kunne få si
"dritten" til en nazist.

Jeg lurer noen ganger på om det er den samme formen for trøst spillutviklere
bruker når de sier at "klienten må være så dum som mulig" og "aldri stol på
klienten".

I denne bloggposten skal vi ikke snakke om dumme oppdragsgivere, men om en
veldig dum web-klient.

## Terninger og vandøde

Et sentralt spørsmål når man lager en webapp er hvordan arbeidet skal fordeles
mellom klient og server. Ofte er det nok [Conway's
lov](https://en.wikipedia.org/wiki/Conway%27s_law) som til syvende og sist gjør
seg gjeldende, mer enn en villet arkitektur.

I et lite team hvor alle jobber med alt, blir man mer fristilt fra godeste herr
Conway, og kan gjøre mer interessante arkitekturvalg. Dette er i høyeste grad
tilfelle i [Parens of the Dead](https://www.parens-of-the-dead.com),
videoserien som [Christian](/christian/) og jeg gjør sammen.

Ta en titt på dette spillet:

<div class="gif-movie">
  <div class="gm-play-button"><div class="gmpb-head"></div></div>
  <img class="gm-still" title="Video fra Parens of the Dead-spillet" src="/images/blogg/parens-dice-still.gif">
  <img class="gm-movie" src="/images/blogg/parens-dice.gif">
</div>

Ved første øyekast ser det ut som en applikasjon med hovedvekt på klientsiden,
ikke sant?

Du skulle bare visst.

Og ja, hvis du leser videre, så er det nettopp det du får også. Artig hvordan
det der fungerer!

## Dum og dummere

Etter at jeg ble bitt av [Clojure](/clojure/)-basillen for ti år siden, har det
vært en tydelig* utvikling i hvordan jeg skriver kode. Det bærer stadig i
retning av *mer data*. Færre objekter, færre metoder, mindre kode. Ikke bruk
funksjoner der data gjør nytten. Data kan inspiseres, masseres, lagres og sendes
over vaieren. Det er fine saker.

<p>
<small>
* Tydelig nå i etterkant, altså. Som Kierkegaard sa: Livet kan bare forstås baklengs, men det må leves forlengs.
</small>
</p>

Tilbake til den overskriften:

*Zombier, Clojure og to tomsinger fra Østfold* er slagordet til
[ZombieCLJ](https://www.zombieclj.no), den norske forløperen til Parens of the
Dead.

Da vi arkitekterte arkitekturen der (som man gjør) så spurte vi oss selv: Hvor
lite ansvar kan klienten ha? Hvor lite frontendkode kan vi slippe unna med? Hvor
dum kan klienten egentlig bli?

Ved hjelp av denne data-idéen ble svaret: **Ille dum.** Mer om det med data snart.

Du kan [sjekke koden her
selv](https://github.com/magnars/zombieclj-s02/tree/main/src/zombies/client),
men kort fortalt så er det noen få actions, en event bus, litt local-storage,
og en god dose komponentkode for å rendre ut DOM-en.

Enda dummere skulle det altså bli, da vi satt oss ned og skrev koden til
Parens. La meg bare kort skyte inn at all denne kodingen ligger tilgjengelig
som videoer for kos, underholdning, og tidvis hoderystende latter på
[www.parens-of-the-dead.com](https://www.parens-of-the-dead.com).

## Et eksempel

Hvis du er nysgjerrig på hvordan rulle terning med CSS, så er det en [egen
bloggpost om det her](/blogg/2019-09-terningene-er-kastet/), men slik ser altså
komponenten ut i ZombieCLJ (dum):

```clj
(defcomponent Die
  [{:keys [id roll-id current-face previous-face faces locked? status]}]
  [:div.die-w-lock
   [:div.die {:key (str id roll-id)
              :class (str (name id) " " (some-> status name))}
    [:div.cube {:class (if previous-face
                         (str "roll-" previous-face "-to-" current-face)
                         (str "entering-" current-face))}
     (map (fn [face i]
            [:div.face {:class (str "face-" i " " (name face))}])
          faces
          (range))]]
   [:div.clamp {:class (when locked? "locked")
                :on-click [:send-command [:toggle-clamp id]]}
    [:div.lock
     [:div.padlock]]]])
```

Som du kan se, hvis du myser, så er det i hovedsak noen `div`-er med klassenavn
på, og litt logikk for hvilke klassenavn som til enhver tid er gjeldende.

Sa jeg logikk? La oss ta en titt på den samme koden i Parens (dummere):

```clj
(d/defcomponent Die
  [{:keys [die-class faces cube-class key clamp-class lock-command]}]
  [:div.die-w-lock
   [:div.die {:key key
              :class die-class}
    [:div.cube {:class cube-class}
     (for [face faces]
       [:div.face {:class face}])]]
   [:div.clamp {:class clamp-class}
    [:div.lock {:on-click lock-command}
     [:div.padlock]]]])
```

All logikken er borte!

Ikke bare er utvelgelsen av klassenavn borte, men endatil hva som skjer når man
trykker på en lås.

Hvor kommer `die-class` og `cube-class` fra?

Hvor har det blitt av on-click handleren `[:send-command [:toggle-clamp id]]`?

## Strømmende data

En gang i forrige årtiende holdt jeg et foredrag på JavaZone om å strømme data
til klienten [uten å måtte lage det på nytt hver
gang](/strom-data-til-nettleseren-uten-a-lage-det-pa-nytt-hver-gang/). Det er
den kuleste arkitekturen jeg noensinne har jobbet med, men det skal ikke stikkes
under en stol at den var innfløkt.

Når vi lagde Parens så skar vi det helt ned til beinet. Ingen overflødige deler.
Sånn her fungerer det:

- Klienten har et datalager.
- Klienten kobler seg til serveren med websocket.
- Serveren sender konkrete oppdateringer til datalageret.
- Datalageret brukes uendret av komponenttreet til å rendre.

Du kan [sjekke koden selv
her](https://github.com/magnars/parens-of-the-dead-s2/tree/main/src/undead/client),
men bortsett fra selve komponent-definisjonene så er det bokstavelig talt **30
linjer kode** på klienten.

Slik kan meldingene fra serveren se ut:

```clj
[:assoc-in [:dice :die-3 :die-class] "rolling"]
[:assoc-in [:dice :die-3 :cube-class] "roll-5-to-1"]
[:assoc-in [:dice :die-4 :die-class] "rolling"]
[:assoc-in [:dice :die-4 :cube-class] "roll-5-to-3"]
[:wait 1800]
[:assoc-in [:dice :die-1 :clamp-class] "locked"]
[:assoc-in [:dice :die-1 :lock-command] [:set-die-locked? :die-1 false]]
```

Når klienten ser en `:assoc-in`-melding, så legger den noe data i lageret sitt.
Når den ser `:wait` så venter den.

Det er alt klienten kan.

Det er alt.

Det er ikke noe mer.

Til og med on-click-handleren får den tilsendt. Som data. Dette fungerer fordi
[dumdom](/blogg/2021-11-mer-mindre/) er en utrolig flott erstatning for
React, og vet at du helst vil jobbe med data. Og med *du* så mener jeg *jeg*.

## Til slutt

Jeg oppfordrer deg en gang til til å ta en titt på [klientkoden til
Parens](https://github.com/magnars/parens-of-the-dead-s2/tree/main/src/undead/client).
Har du noen gang sett en frontend som ser sånn ut?

Det er klart at en så nedstrippa og enspora arkitektur ikke passer over alt.
Fungerer ille dårlig offline, for å si det sånn. Men jeg tror ganske sikkert at
de fleste arkitekturer kunne ha godt av å være noen knepp nærmere dette
ytterpunktet. Slik som arkitekturen fra [det
foredraget jeg nevnte](/strom-data-til-nettleseren-uten-a-lage-det-pa-nytt-hver-gang/).
Ikke like naivt enkel som Parens, men med mange av de samme tankene. Den var
aldeles herlig å jobbe med.

Og det var alt jeg hadde å si om det.

<script type="text/javascript">
(function () {
document.querySelectorAll(".gif-movie").forEach(function (el) {
  var playing = false;
  var canvas = el.querySelector(".gm-still");
  var still = canvas.src;
  var movie = el.querySelector(".gm-movie").src;
  var button = el.querySelector(".gm-play-button");
  el.addEventListener("click", function (e) {
    canvas.src = playing ? still : movie;
    button.style.opacity = playing ? 1 : 0;
    playing = !playing;
  });
  fetch(movie);
});
}())
</script>
