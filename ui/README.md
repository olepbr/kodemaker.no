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
