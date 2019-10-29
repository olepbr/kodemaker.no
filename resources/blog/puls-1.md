:title Kodemaker PULS #1
:published 2015-09-30
:author august
:body

Velkommen til den første utgaven av Kodemaker PULS!

**JavaZone**

Fire Kodemaker holdt fem foredrag på JavaZone 2015!
Christian holdt to: ett om [EcmaScript 2015 (ES6)](https://vimeo.com/138956041), og et annet på sparket grunnet frafall, [You Can't Change This: Immutable JavaScript](https://vimeo.com/138873443).
Alf-Kristian: [Erfaringer med Clojure og Scala (og Java 8)](https://vimeo.com/138955227)
Eivind: [Plugin-basert arkitektur i Java!](https://vimeo.com/138954653)
Odin: [Interaktiv programmering med ClojureScript](https://vimeo.com/138862375)

**Sinon.JS**

Christian er pappaen til [Sinon.JS](http://sinonjs.org/), et mock- og stub-bibliotek til JavaScript med over 2300 stars på
GitHub. Dette prosjektet er på mange måter det ultimate open source-prosjektet, for det er nemlig ikke Christian som vedlikeholder prosjektet lengere! Sinon.JS har et stort nok miljø rundt seg til at andre tok over når Christian ble opptatt med å kjefte på unger, brygge øl og gå på røde løpere. Hovedpersonen i prosjektet er nå Morgan Roderick, mens Christian sitter på sidelinjen og merger en pull request en gang i blandt. 2.0 nærmer seg med stormskritt, første major release på mange år! Sjekk forresten ut "Help wanted" i issue trackeren, hvis du har lyst til å bidra kan du jo f.eks starte med de som også er tagget med "Difficulty: Easy"

**Parens of the Dead**

Magnar fortsetter å lage screencasts. Denne gangen på [parens-of-the-dead.com](http://parens-of-the-dead.com/), på engelsk. I samme ånd som [zombietdd.com](http://zombietdd.com/) er dette medium korte episodiske screencasts.

**Open source den siste måneden**

Magnar har vært aktiv i emacs-prosjektene sine clj-refactor.el, s.el, js2-refactor.el og expand-region.el, og prone, exception-reporting for Clojure sin HTTP-stack. For det meste buggfixer og merging av pull requests. Den mindre glamorøse men kanskje viktigste biten av open source.

Ronny bidro med en [forbedring/bugfix til grails-profile-repository](https://github.com/grails/grails-profile-repository/commit/c1cbc583469fd2e6fa2f03d2c75d5288dc4f74d0). Dette repoet inneholder de innebygde malene når man genererer en ny app, og Ronny fikset en default som alltid måtte overstyres i templatet som brukes når man generer en ny grails-plugin.

Magnus fikk merget [et pull-request til LightTable](https://github.com/LightTable/LightTable/pull/1964). En ganske fersk bug gjorde at LightTable ikke ryddet skikkelig opp etter seg når man lukket editor-vinduer. Men nå vil alle eventuelle child-prosesser og annet snacks nok en gang synke med skipet.

Kjetil er så heldig at han får gjøre open source på jobb, og har flesket til med hele 30 commits i [Deichmann sitt ls.ext](https://github.com/digibib/ls.ext). Hele systemet Kjetil jobber på er open source.

August har [tweaket litt på ruby-openssl-cheat-sheet](https://github.com/augustl/ruby-openssl-cheat-sheet), en samling eksempler for bruk av OpenSSL via Ruby sitt API for dette. SHA1 i stedet for SHA256 når man skal generere SSL-sertifikater, for
eksempel.
