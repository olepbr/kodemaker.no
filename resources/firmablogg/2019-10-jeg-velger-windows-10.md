:title Jeg velger Windows 10, det beste utviklingsmiljøet (for meg)
:author august
:published 2019-11-06
:blurb

Linux, macOS eller Windows 10? Jeg har landa på den siste.

:body

Linux, macOS eller Windows 10? Jeg har landa på den siste.

Jeg [prøvde faktisk dette i 2016](https://augustl.com/blog/2016/notes_on_windows/), og feilet. Den største endringen siden den gang er en mye, mye bedre WSL.

I to måneder har utviklingsmiljøet mitt både hjemme og på jobb vært Windows 10. Denne bloggposten er skrevet på Windows, ved bruk av tech-stacken til kodemaker.no, som ikke kjører på Windows. Jaha, javel, sier du? Les videre, det er utrolig hva man kan få til i 2019.


## Men... hvorfor?

Jeg har et hjerte for Linux. Men, som Linus Torvalds sier, [det finnes ikke software til Linux](https://www.youtube.com/watch?v=5PmHRSeA2c8&t=387) (6:27).

Jeg synes macOS er helt OK, men jeg har ikke noe til overs for det begrensede utvalget av kjedelige laptops med ødelagte tastatur og mangel på porter. Dessuten liker jeg ikke mange av defaultene. Jeg må ha masse tredjepartsgreier for at command + tab ikke skal vise applikasjoner, men vinduer. Og jeg er visst veldig sær, så den eneste jeg fant som virker sånn noenlunde OK, viser mange sekunder/minutter gamle previews av vinduene. Så må jeg ha noen tredjepartsgreier for å kunne maksimere og minimere vinduer med tastaturet. Og nå har jo [macOS blitt til Vista uansett](https://tyler.io/macos-10-15-vista/), og jeg vil jo ikke bruke _Vista_.

Jeg bryr meg ikke om OS-et mitt er proprietært.

Det er viktig for meg å lett kunne kjøre software som er laget for UNIX, siden de fleste utviklere i min verden - JVM-land - sitter på en UNIX-ish.

Windows 10 løser alt dette for meg.

## WSL gjør det mulig

Windows NT er ganske seriøs software. NT er kernelen til X-Box, Windows Phone, Windows Server, Hololens, Windows XP, Vista, 7, 8 og 10. Når Microsoft lagde Windows NT, designet de kernelen for å kunne kjøre software for win32, DOS, OS/2 og POSIX. Ja, POSIX. I disse dager er det bare win32 som gjenstår, sånn i praksis. Men så kom WSL.

WSL er en komplett re-implementasjon av alle API-ene til Linux-kernelen, bare med Windows NT i bunnen. Ja, du leste riktig. [Wine](https://www.winehq.org/), bare andre vegen. En blob med kode startet som en prosess bryr seg jo ikke om den "kjører på Linux". Helt konkret, bryr den seg om at C-funksjonene den kaller gjør det den forventer de skal gjøre.

Arkitekturen til NT gjør dette veldig mulig å få til. F.eks kan git være skikkelig treigt på Windows, siden git er skrevet slik at den lager nye sub-prosesser ganske ofte. Og på Windows er det ganske dyrt å lage en prosess. Men det er Windows. Ikke NT. I selve NT-kernelen, er en prosess lettvekts og fin. Så når Microsoft re-implementerer `fork(2)`, gjør de det direkte mot NT. Og det fungerer supert!

Det er noen problemer her som de jobber videre med, som ytelsen på filsystemet. Neste versjon av WSL får et eget filsystem som ikke piggybacker på NTFS. Så dette jobber de med og det blir bedre og bedre hele tiden.

Og det er ikke virtualisering. Binder jeg til localhost port 3000 i WSL, kan jeg gå på localhost port 3000 i nettleseren i Windows. Integrasjonen er smoooooooooth.

Det viktigste med WSL: det blir nå null stress joggedress å kjøre all softwaren jeg trenger på Windows 10. F.eks kodemaker-bloggen! Den bruker noe V8-greier inni JVM-en som ikke funker på Windows 10. Men den kjører helt fint på Windows 10 likevel!

## Jeg er en ekte utvikler, altså

Nå må jeg være litt forsiktig her. Jeg vil ikke rakke ned på mine fantastiske medsammensvorne som også bruker Windows. Men det er nå en gang sånn at en viss andel utviklere som velger å bruke Windows, er folk som ikke bryr seg så mye. Definisjonen min av å ikke bry seg, er å ikke bytte hostname på maskina si.

Heldigvis har jeg også et veldig kraftig hipster-gen. Jeg liker å ha PC (uansett om det er Linux eller Windows) fordi da er jeg en av fem som har PC og som er litt sære, hver gang jeg er på konferanse eller noe sånt.

Dessuten er jeg god til å [se irritert ut mens jeg jobber](https://www.youtube.com/watch?v=Kafq7yrKAOQ), så jeg tror folk tar meg seriøst.

## Det hjelper kanskje litt at jeg er en println-debugger

Nå er jeg på et Clojure-prosjekt. Clojure er repl-orientert. Kort forklart, nesten all koden kjører på Windows 10. Den som ikke gjør det, kjører jeg i WSL på denne måten:

```shell script
lein repl :start :port 5008
```

I din editor (emacs, IntelliJ, whatever) kan du nreple deg inn i prosessen som kjører i WSL, og editoren din vet ikke forskjellen på om du kjører det i WSL eller Windows 10.

Om jeg i fremtiden havner på et prosjekt som ikke er Clojure, og hvor koden ikke kjører fint på Windows 10, så finner jeg nok ut av det.

Kanskje jeg kan bruke Visual Studio Code, som har masse støtte for å [kjøre prosjektet ditt under WSL](https://code.visualstudio.com/docs/remote/wsl), bare at VSCode ordner det for deg, uten at du trenger å styre med det.

Dessuten er jeg ikke en sånn utvikler som bruker debuggeren. Så det går ofte greit for meg at editoren min ikke kjører koden min. Jeg liker å bruke `println` og `console.log` og venner til å debugge kode.

## Git, og andre rare defaults

Noe av det sykeste med Windows 10, er git sin helt utrolige default.

La oss snakke om linjeskift. Litt kjapt, en fil har ikke linjer. Det er bare en strøm med bytes. Så en fil med linjer ser egentlig sånn ut:

```text
Hei, alle sammen!\nDette er en fin bloggpost, synes jeg.\n\nTakk for meg!
```

Når en som bruker macOS eller Linux sjekker in den fila, og jeg puller git-repoet ditt, så.. **ENDRER GIT HVORDAN FILA SER UT PÅ DISK**. Wtfasflkjsdflkjsdflkj.

Den blir seende sånn ut:

```text
Hei, alle sammen!\r\nDette er en fin bloggpost, synes jeg.\r\n\r\nTakk for meg!
```

Det er en så hinsides dårlig default at det er rart at noen ikke har blitt bøtelagt. Så du _må_ gjøre dette på Windows 10.

```shell script
git config --global core.autocrlf input
```

Dette forteller git at den skal sjekke ut fila på disk sånn den faktisk ser ut i repoet. **L O L**.

Det er en grunn til at det er sånn. I alle år har "linjeskift" på Windows vært representert som `\r\n`, ikke bare `\n`. Og alle apper støtter begge deler, så du vil kanskje ikke se det en gang. Men hvis du f.eks har et shellscript som skal bygges inn i et docker-image, basert på filene på din disk, vil du se masse rare feil om at Linuxen i docker-imaget ditt sier `unknown symbol ^M`.

## Terminalen er så fin, atte

Jeg bruker [cmder](https://cmder.net/). Det er egentlig ikke så mye å si om det, utover at jeg nå har en helt super terminal i Windows 10 som funker helt fint. Dessuten har den en del unix-like kommandoer, så "ls" og "cat" og "less" og andre essensielle ting funker som forventet.

![cmder og git under Windows 10](/images/blogg/win10_cmder.jpg)

EDIT: I disse dager bruker jeg faktisk Windows sin nye innebyggede terminal, som finnes i preview. Mye mere minimalistisk enn cmder, som har tusen millioner options. Også digg at copy/paste bare er ctrl+shift+c/v.

Kan installeres [fra Microsoft Store](https://www.microsoft.com/en-us/p/windows-terminal-preview/9n0dx20hk701), eller [bygges fra source](https://github.com/microsoft/terminal) om du vil.

## Bootcamp på en Apple-maskin

Jeg kjører enn så lenge Windows 10 i bootcamp på en Apple-maskin. Men.... Jeg har bestilt meg en PC!!!

(Reaksjonen din nå skal være ca. den samme som noen som forteller deg at de skal få barn, eller noe sånt. Åååh giiiiiidameg!)

Ny harwdare er jo alltid gøy. Så det er kanskje grunn nok i seg selv?

Jeg må reboote hver dag, siden Bootcamp ikke akkurat er heeeeelt supert. Når Bootcamp kom, sa Apple at det nå var _de_ som lagde de beste Windows-maskinene. Men Bootcamp blir nok ikke vedlikehold så veldig dedikert av Apple. Når de fikser de slunkne Macbook Pro-ene sine thermal throtting, [gjør de det i macOS, ikke i firmware, så Mac-en din yter dårligere](https://www.youtube.com/watch?v=WxocVricANg).

Og det er jo litt irriterende at Windows-knappen (cmd) og alt-knappen har byttet plass..

## Konklusjon

Digger du Linux? Så fint!

Elsker du macOS og Apple-hardware? Hurra!

Bryr du deg ikke og bruker bare det som er tilgjengelig der og da? Glimrende!

Det er null viktig for meg hva andre bruker. Men jeg er fornøyd med Windows 10, ihvertfall!
