:title Teknisk egenkapital og gjeld
:author august
:published 2020-09-17
:blurb
Paul Graham sin klassiker [Beating The Averages](http://www.paulgraham.com/onlisp.html) forteller at Lisp er et hemmelig våpen, hvor konkurrentene dine blir stående igjen i støvet som legger seg etter din enorme effektivitet og produktivitet.

Kanskje tidlig på 2000-tallet. Men i dag må språkene se seg slått av _plattformene_.
 
Koden vår renner nå over med tredjeparts avhengigheter, fra et gedigent økosystem av tidsbesparende hjelpemidler.

Men er disse avhengighetene _egenkapital_, eller er de _gjeld_?

:body

Paul Graham sin klassiker [Beating The Averages](http://www.paulgraham.com/onlisp.html) forteller at Lisp er et hemmelig våpen, hvor konkurrentene dine blir stående igjen i støvet som legger seg etter din enorme effektivitet og produktivitet.

Kanskje tidlig på 2000-tallet. Men i dag må språkene se seg slått av _plattformene_.

Koden vår renner nå over med tredjeparts avhengigheter, fra et gedigent økosystem av tidsbesparende hjelpemidler.

Men er disse avhengighetene _egenkapital_, eller er de _gjeld_?


## Assets and liabilities

<a href="https://twitter.com/garybernhardt/status/1034519171986423809">
<img style="max-width: 500px" src="/images/blogg/gary-bernhardt-tweet.png" />
</a>

I regnskap snakker man om "assets" og "liabilities", altså egenkapital og gjeld.

Med **1 000** i egenkapital og **2 000** i gjeld, så har du **-1 000** på bok.


## Luksusfella

Det er  deilig å dra inn tredjepartskode.

Vi har jo ekte problemer å løse.

Vi skal ikke sitte her og mekke med tekniske detaljer, vi skal dra Trello-kort mot høyre. Vi skal _levere_.

<blockquote>Oooh, vi slipper å fikse state-håndtering selv</blockquote>

<blockquote>Ah, så digg å ikke måtte manuelt mekke masse forms hele tiden</blockquote>

<blockquote>Smuuud, vi kan betale regningene senere, og bare **få** masse penger, helt gratis?</blockquote>

<blockquote>Jeg fryser. La meg tisse litt i buksa.</blockquote>


## Om romskip

Hvis du har _to_ brukere, skal du lage hele stacken din selv, fra bunnen av?

Ja, noen ganger så skal du faktisk det.

<img style="max-width: 700px" src="/images/blogg/spacex-crew-dragon-control-panel.png" />

[Her](https://www.reddit.com/r/spacex/comments/gxb7j1/we_are_the_spacex_software_team_ask_us_anything/ft5zou3/), [her](https://www.reddit.com/r/spacex/comments/gxb7j1/we_are_the_spacex_software_team_ask_us_anything/ft62781/) og [her](https://www.reddit.com/r/spacex/comments/gxb7j1/we_are_the_spacex_software_team_ask_us_anything/ft68jpb/) kan du lese om hvordan software-utviklerene hos SpaceX har laget sitt eget "reactive framework" og har gjort alt fra bunnen av.

For SpaceX er tredjepartskode gjeld. De lever helt på kanten av hva som er mulig å få til, og trenger full kontroll på miljøet sitt.

Men.

De bruker jo Chromium. Og de bruker Linux. De har riktignok sin egen Linux-distro. Men de lener seg på et helt hav av tredjepartskode.

Så hva er det de _egentlig_ driver med på SpaceX?

## Wait a moment

Moment.js var lenge de facto tredjepartsavhengighet for å jobbe med tid og dato i nettlesere. 
 
Prosjektet er nå på vei inn i en annen fase, og i sin ["project status"](https://momentjs.com/docs/#/-project-status/) forteller de nå:

> * We will not be changing Moment's API to be immutable.
> * We will not be making any major changes (no version 3).
> * We may choose to not fix bugs or behavioral quirks, especially if they are long-standing known issues.
> * We **will** address critical security concerns as they arise.

Med andre ord: Moment.js var tidligere **gjeld**, men nå er det **egenkapital**.


## Noe tredjepartskode er **ren egenkapital**

Linux-kernelen drar det så langt at hvis de har shippa en bug, og det finnes kode der ute som lener seg på den buggen, ja så [fikser de den ikke](https://lkml.org/lkml/2012/12/23/75).

Hvis du lener deg på Chromium, kan du glede deg over at Google har som ansvar at masse gammal drit-kode på internett skal fortsette å fungere.

Microsoft hoppet fra Windows 8 til Windows 10, for å ikke generere gjeld i all den gamle koden der ute som sjekket etter Windows 95 og 98 med `osVersion.startSwith("Windows 9")`.

Hvis du bruker Moment.js i sin nåværende form, er det helt trygt å oppdatere når det kommer nye versjoner.

Sier jeg at du fra nå av skal lage alt selv?

Niks.

Men neste gang du drar inn tredjepartskode i prosjektet ditt:

**Tenk nøye igjennom om du øker egenkapitalen eller gjelda di.**