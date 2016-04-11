:title Kodemaker PULS #7
:published 2016-04-11
:author august
:body

Velkommen til Kodemaker PULS! Dette er et månedlig nyhetsbrev som vi har holdt på med internt i Kodemaker i et halvt års tid, og nå har bestemt oss for å dele med resten av verden.

Kodemaker PULS er en oppsummering av alt det synlige Kodemakere driver med, som bloggposter, foredrag, open source-bidrag, med mere.

**Christin påstår at programmerere er mennesker**

Christin [snakket på meetup](http://www.meetup.com/ProductTank-Oslo/events/228606645/)! Meetupen heter Product Tank Oslo, og bidro til at Christin ytterligere fester grepet om tittelen Kodemaker Resident Thought Leader.

<iframe src="https://player.vimeo.com/video/160150795" width="500" height="281" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>

**Kjetil viser at iPad pro har noe for seg**

På QCon London 2016 tok Kjetil med seg sin iPad pro, en Apple Pencil og Paper by fiftythree.com, og [skisset i vei](http://www.akafred.com/2016/03/notes-from-qcon-london/). Endte jammen opp med [en retweet](https://twitter.com/akafred/status/707989008400453633) fra QCon London sin offisielle twitter-konto også.

**Eivind trashtalker JVM-en**

Eivind er en skikkelig søppelmann for tiden, og graver seg dypt inn i JVM-en sin søppeltømming (eller garbage collection som fancy folk kaller det). Vi har sett ham snakke om dette internt tidligere, og denne gangen var det på [Oslo4J (også kjent som javaBin) sin lyntalekveld](http://www.meetup.com/javaBin/events/228737519/).

<iframe src="https://player.vimeo.com/video/158720082" width="500" height="281" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>

**Christian er hot-shot i godt selskap**

Christan er en av 25 "world leading web development experts " når det skrives om [When did you start coding and why](http://webagility.com/posts/when-did-you-start-coding-and-why)? Nå er forfatter riktignok en tidligere kollega av Christian, men det betyr ihvertfall at Christian er likandes nok til at tidligere kollegaer tåler trynet hans, og kanskje til og med gir ham en klapp på skulderen.

**Magnar sine emacs-videoer som USP på emacs sin hjemmeside**

[Emacs har ny hjemmeside](https://www.gnu.org/software/emacs/), og Magnar sine vidoer fra [emacsrocks.com](http://emacsrocks.com) går i bresjen! Etter litt om og men fant man til og med ut at det var kosher for FSF også, til tross for litt frem og tilbake med Google Analytics og annet non-free JavaSript.

<img src="/photos/blog/puls-7-emacs-website.png" height="400">

**Open source-bidrag**

Magnar har startet et nytt prosjekt, [cljs-styles](https://github.com/magnars/cljs-styles). Dette er et lite knippe funksjoner for å genere vendor-prefixed CSS på inline styles i React. I Optimus får også Magnar nok en gang [smake på](https://github.com/magnars/optimus/commit/c81d0c6ebbebcc9fe5561032ca6cd977f6c80d02) hvordan det er å leve med en README som inneholder kode-eksempler som aldri kjøres. I Prone (sexy exception-printing under dev for Clojure-webapper) skjer det også ting ([#34](https://github.com/magnars/prone/pull/34), [#36](https://github.com/magnars/prone/pull/36), [#38](https://github.com/magnars/prone/issues/38)), og version 1.1.1 er lansert. Etter litt frem og tilbake lærer vi også at [Google Analytics er ok for FSF](https://github.com/magnars/emacsrocks.com/commit/bbe408fffac1a327f2eb527f376f51ea2f764c19), så lenge siden ikke er avhengig av det for å fungere. [En bugfix ble bidratt i form av pull request](https://github.com/magnars/s.el/pull/90) til s.el.

Christian driter seg ut og [melder inn issues på gamle versjoner av ting](https://github.com/keybase/keybase-issues/issues/2152). Heldigvis til folk som driver med open source på jobb, så ingens fritid er påvirket av denne ganske flaue blemmen.

Ronny [melder ifra](https://github.com/grails3-plugins/mail/issues/16) om problemer med Grails sin mail-plugin. Og i [dette github-repoet](https://github.com/rlovtangen/maven-vs-gradle-incremental) demonstreres det at Maven egentlig ikke bør brukes i noen sammenhenger da det er svært lett å putte maven i en tilstand hvor bygget ikke reflekterer kildekoden. Litt tweaking på grails-wkhtmltopdf har også forekommet, og vi blir bl.a [nok en gang minnet på](https://github.com/rlovtangen/grails-wkhtmltopdf/commit/b56456d8783e2d01851329701dc21857354d5622) at primitive types er noe herk.

Eivind [graver i LMAX Disruptor](https://github.com/eivindw/disruptor-examples), og gjør litt god gammeldags maintenance på [Jenkins sin grinder-plugin](https://github.com/jenkinsci/grinder-plugin).

Kjetil har rota rundt med Docker-installasjon via ansible og sendt inn to pull requests ([#7](https://github.com/dochang/ansible-role-docker/pull/7), [#9](https://github.com/dochang/ansible-role-docker/pull/9)) til ansible-role-docker. Et nytt prosjekt ser verdens lys med [formasjon](https://github.com/akafred/formasjon), som er Kjetil sitt helautomatiserte VPS-oppsett.

Magnus har [merget et pull request](https://github.com/LightTable/Clojure/pull/79) på LightTable sin clojure-plugin. Han har også [jobbet videre med elm-light](https://github.com/rundis/elm-light), og lansert både version 0.3.5 og version 0.3.6.

Odin hacker videre på sitt cashflow-prosjekt. Bl.a. [har vi her en commit](https://github.com/Odinodin/cashflow/commit/1e6a8f7fb912916c31840fe9100998d3ab2ec61b) hvor man ser diffen mellom å bruke component og mount i Clojure-prosjekter.

**Bloggposter**

Magnus blogger om at Elm sin LightTable-plugin i siste versjon gir deg inline dokumentasjon: http://rundis.github.io/blog/2016/elm_light_docs.html

Magnus blogger også om en kul sak hvor Elm og ClojureScript snakker med hverandre. LightTable er ClojureScript, men likevel er deler av Elm sin LightTable-plugin skrevet i Elm: http://rundis.github.io/blog/2016/elm_light_modules.html



