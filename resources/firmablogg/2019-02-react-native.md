Jeg har vært med Kodemaker sitt team på 4 hos Bring og laget native mobilapp som sjåførene bruker mens de er ute og plukker opp og leverer pakker. Vi bruker React Native. Her er en liten samling med lærdommer og erfaringer jeg skulle ønske jeg visste før vi kom i gang


## Avanserte animasjoner og UX? Skriv det som ren native

React Native setter veldig få kjepper i hjulene for at du skal skrive fancy native GUI-kode for fjonge animasjoner i ren native-kode, og så fylle innholdet med React Native. Her kommer det til en viss grad an på hva slags animasjoner du ønsker å lage, og det er en del animasjons-støtte innebygget for enklere greier.

Vi hadde et kart som hadde en "drawer" på bunnen som man kunne dra opp, som transformerte innholdet sitt på veg opp litt, osv. Vi endte opp med å gå for en annen UX her av helt andre årsaker. Men skulle vi laget det i dag, ville vi skrevet native kode for denne draweren, og tegnet innholdet i den med React Native.

Uansett hvordan du vrir og vender på det, vil all kommunikasjon mellom native og JS-tråden være asynkron. Enkelte API-er på native-siden krever at den kan få et synkront svar på hva den skal gjøre, og da blir det ikke mulig (uten drakoniske hacks) å kalle på JS-tråden for å dele denne koden mellom platformene, da kall fra native til JS blir asynkront.

Derom du har ekstremt mange fancy custom animasjoner og overganger, ville vi vurdert Flutter, ren native, eller noe annet.

Tvungen asynkron kommunikasjon mellom native main GUI thread og JS-tråden til React Native kommer riktignok til å [endre seg i fremtidige versjoner av React Native](https://facebook.github.io/react-native/blog/2018/06/14/state-of-react-native-2018) (RC er ute i skrivende stund), så det er fare for at dette punktet er utdatert informasjon ganske snart.

## Navigasjon: bruk react-navigation, eller react-native-navigation, eller bare lag helt native animasjon en gang per platform.

[**react-navigation**](https://reactnavigation.org/): veldig greit å komme i gang med, kompatibelt og fint, anbefalt løsning av mange, og er på mange måter standard-valget. Vi likte ikke denne, da den re-implementerer all navigasjon fra bunnen av, og lener seg ikke på eksisterende native API-er.

<blockquote>
React Navigation does not directly use the native navigation APIs on iOS and Android; rather, it re-creates some subset of those APIs. This is a conscious choice in order to make it possible for users to customize any part of the navigation experience (because it's implemented in JavaScript) and to be able to debug issues that they encounter without needing to learn Objective C / Swift / Java / Kotlin. 
</blockquote>

[**react-native-navigation**](https://github.com/wix/react-native-navigation): et "lowest common demoninator" API på JS-siden, som har native implementasjoner for iOS og Android og bruker helt vanlige native API-er for å gjøre navigasjon. Vi bruker denne. En ulempe er at den tar litt over appen, og blir din nye "main". Den er også helt konkret implementert mot en spesifikk version av React Native - du kan ikke oppgradere React Native før react-native-navigation har kommet ut med en ny release som passer til.

**Eller bare bruk helt vanlig native navigation**. På iOS kan du lage en helt vanlig app med storyboards, på Android kan du lage en helt vanlig fragment-basert navigasjon med Android Jetpack. Så kan du skrive littegranne lim som gjør at dine UIViewControllers på iOS og fragments på Android rendrer sitt innhold med React Native. Ulempen er naturlig nok at du må implementere navigasjon to ganger, fordelen er at du får bra verktøystøtte på de uluke native-plattformene, og at du ikke tar på deg ansvaret med å dra inn de to svære dependenciene nevnt ovenfor.

## Grafikk: bruk bitmaps, ikke vektor/SVG/...

I starten tenkte vi "men man må jo bruke vektorer i 2018". Det funka sånn tålelig greit på Android, men på iOS er det rett og slett helt og holdent lagt opp til at du skal ha png-filer. Dersom du lager en ren native app for iOS ender du garantert opp med å bruke png-filer. React Native har også mye bedre innebygget støtte for png-er i stedet for vektorer. Just do it.