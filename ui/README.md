# Kodemakers UI-komponenter

I denne mappa finner du UI-komponentene som kan brukes for å bygge websider til
kodemaker.no. Komponentene er definert som funksjoner som returnerer
[hiccup](https://github.com/weavejester/hiccup). For å jobbe med UI-komponentene
kan disse brukes fra ClojureScript og rendres med devcards og figwheel (live
reload FTW), og for bruk i prod så kalles disse funksjonene fra Clojure for å
bygge HTML som vi lagrer på disk. For å understøtte dette må alle
komponent-filene være cljc-filer.

## Utviklingsmiljø

For å jobbe med komponentene må du kopiere `figwheel-main.edn.sample` til
`figwheel-main.edn`. Hvis du ikke ønsker å åpne filer i Emacs kan du endre
`:open-file-command` til en editor som passer deg.

Deretter kan du starte REPL fra en terminal med:

```sh
clojure -A:repl
```

Når denne kommer opp og blir ferdig med å kompilere CLJS finner du devcards på
[http://localhost:9560](http://localhost:9560) (med mindre du valgte et annet
port-nummer i `figwheel-main.edn`).

### Emacs

Fra Emacs starter du REPL-et med `cider-jack-in-cljs`. Du sparer noen tastetrykk
ved å opprette filen `.dir-locals.el` og fylle den med:

```lisp
((nil
  (cider-default-cljs-repl . figwheel-main)
  (cider-figwheel-main-default-options . ":dev")))
```

Etter at du først har opprettet denne filen må du åpne en fil i samme mappe fra
disk (ikke bare gå til bufferet dens) for at variablene skal bli satt. Deretter
vil CIDER droppe å prompte deg for disse valgene ved jack-in.

## Ikoner

På kodemaker.no bruker vi ikoner fra [Ego](https://www.ego-icons.com).

Dette repoet inneholder bare de ikonene vi har brukt så langt. Resten finner du
[her](https://github.com/kodemaker/kodemaker-icons).

Grunnen til at de ligger der er for å unngå å bloate opp kodemaker.no-repoet med
150mb ubrukte ikoner.

For å bruke et slikt ikon kan du kopiere det til `ui/resources/public/icons`.

Ikke bruk ikoner rett fra Ego - ved å minifisere (som er gjort i vårt eget repo)
så sparer vi opp til 90% av størrelsen.

## UI-vokabular

Det er fort gjort å ende opp med mange begreper for de samme tingene. I et spakt
forsøk på å unngå at en overskrift heter både "heading", "title" og "big text"
har vi her en liten ordliste over begreper som er i bruk - skal du lage nye
komponenter, se gjerne på denne og bruk de av disse begrepene som passer før du
introduserer nye. Takk.

- `:title`: Overskrift/tittel
- `:text`: Tekstbolk
- `:href`: URL
