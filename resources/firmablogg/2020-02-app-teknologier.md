:title Hjælp, vi må ha en app!
:author olga
:tech [:android :ios :web :pwa :react-native :xamarin :xamarin-forms :cordova]
:published 2020-02-26

:blurb

Det finnes veldig mange teknologier der ute, og det kan fort bli vanskelig å navigere i jungelen når man skal velge hva man skal gå for. Hvilken teknologi passer min app best?

:body

Det finnes veldig mange teknologier der ute, og det kan fort bli vanskelig å navigere i jungelen når man skal velge hva man skal gå for. Hvilken teknologi passer min app best? La oss prøve å få litt overblikk.

## Viktige ting å tenke på først

* Trenger vi virkelig en app, eller kan en responsiv nettside dekke behovet?
* Hvilken utviklingskompetanse har vi allerede på huset? (Hvem kommer til å utvikle og vedlikeholde appen?)
* Hvilke plattformer skal vi støtte? (Bare iOS, bare Android, eller begge?)
* Vil vi ha separat design for Android og iOS?
* Kommer det til å være behov for mye avansert grafikk?
* Er det viktig å kunne lansere nye versjoner opptil flere ganger per dag?
* Hvem er brukerne av appen? (Trenger vi å nå mange, eller en lukket gruppe?)

## Litt om teknologier

Hvis svaret på det første punktet over er "ja, vi trenger en app!", får du her en overordnet oversikt over hva som finnes ute i markedet nå:

### REN NATIVE

Swift/Objective-C for iOS, og Kotlin/Java for Android. Det lages to helt separate apper, én for hver plattform, med hver sitt design og implementasjon.
Alt som er mulig å gjøre på mobil, kan du få til med ren native, og vil ha raskest mulig rendring og performance. Appen legges ut i App Store/Google Play, og kan derfor ta opptil flere dager å komme ut i markedet med nye versjoner.

**Viktigste fordel:**
Performance, performance, performance.

**Viktigste ulempe:**
Skriver kode dobbelt.

### KRYSS-PLATTFORM (COMPILE TO NATIVE)

Kryss-plattform native utvikling betyr at du skriver koden i ett språk, også kompileres den til native kode. Det er typisk én felles kodebase for Android og iOS både for UI og logikk-koden, i tillegg til noen plattform-spesifike tilpasninger som må gjøres i hvert sitt respektive prosjekt. Ofte lages kun ett design for begge plattformene, og renderen tar seg av jobben å lage riktige plattform-elementer (med unntak av Flutter, som bruker egne UI-widgets). For eksempel om du skriver "Button", så vil den på iOS bli tolket som UIButton og Button på Android. Det er også mulig å lage egne renderers av komponenter (for eksempel en custom datepicker), slik at de oppfører seg ulikt per plattform, men her er det lurt med en balanse - blir det for mange slike tilpasninger, så forsvinner også litt av poenget med kryss-plattform.

Et alternativ til å tilpasse komponenter per plattform er å finne en ekstern pakke som allerede løser behovet. Her er det litt smak og behag over hva folk foretrekker - men en balansert blanding av egen kode og eksterne pakker er kanskje sunt. Ulempen med eksterne pakker er at man kan fort havne i en situasjon der du må vente på en tredjepart til å oppgradere til å støtte for eksempel den siste versjonen av React Native.

Eksempler på de mest kjente kryss-plattform rammeverk er React Native, Xamarin (+ Xamarin Forms) og Flutter. Alle tre har støtte for hot-reloading, slik at man slipper å bygge appen på nytt for å se endringene i layout.

Native har også kommet med sitt svar på kryss-plattform, og det er nå mulig å dele mye av koden ved hjelp av [Kotlin Native Multiplattform](https://dev.to/kuuurt/maximizing-code-sharing-between-android-and-ios-with-kotlin-multiplatform-54h8), og å lage [kryss-plattform apper med Swift](https://blog.readdle.com/why-we-use-swift-for-android-db449feeacaf).

Appene distribueres gjennom App Store/Google Play og må vente på godkjenning, med unntak av React Native som kan få javascript-oppdateringer "over the wire" med for eksempel [CodePush](https://docs.microsoft.com/en-us/appcenter/distribution/codepush/).

**Viktigste fordel:**
Dele koden mellom plattformer.

**Viktigste ulempe:**
Kommer ikke unna plattform-spesifikke tilpasninger, noe kode må uansett skrives dobbelt.


#### React Native

Javascript/Typescript for felles-kodebasen, i tillegg til Java/Kotlin for Android og Objective C/Swift for iOS. Veien fra React til React Native er kort, og frontendutviklere kan fint utvikle/vedlikeholde appen. Javascript-koden kan oppdateres "on the fly". Appen må først godkjennes av Google Play/App Store, og deretter kan JS-endringene pushes til appen. Alle endringer som berører native-koden må fortsatt gå via stores.

#### Xamarin

Xamarin er delt i to hovedkategorier - Xamarin og Xamarin Forms. Forskjellen er at Xamarin deler logikk-koden mellom prosjektene, og UI er separat per plattform, mens i Xamarin Forms deles all kode (unntatt såklart plattform-spesifikke tilpasninger). Språket som brukes er C# eller F# og Xamarin Forms støtter i tillegg XAML for UI.

#### Flutter

Flutter er laget av Google, og skrives i Dart. Flutter har en egen rendering engine der du kan velge mellom to hovedstiler for UI widgets - Material design for å få til Android "look and feel" eller Cupertino for iOS.


### KRYSS-PLATTFORM HYBRID

Hybrid-apper er bygget på toppen av Cordova. Du lager i praksis en web-app ved hjelp av HTML, JS og CSS, som kjøres inne i en native webview i appen. Appen må godkjennes første gang i stores, deretter kan koden oppdateres fortløpende fra serveren. Hybrid-appene kompileres ikke ned til native, så alle komponenter rendres slik de er kodet som, likt for alle plattformene. Man kan også detektere hvilken device man er på og kode spesifikt for den plattformen, så med litt ekstra tilpasninger kan du få til det meste. Enkelte HTML-komponenter har også native implementasjoner (for eksempel datepicker på iOS).

Hybride apper drar kryss-plattform tankegangen enda lenger - her kan du faktisk ha websiden og appen som samme løsning.

**Viktigste fordel:**
Appen markedsføres gjennom stores, samtidig kan du oppdatere den så ofte du vil.

**Viktigste ulempe:**
Tilgang til native features er avhengig av at det støttes i rammeverket/plugins du bruker.

### Progressive Web Apps (PWA)

Progressive web apps er apper laget med HTML, JS og CSS som du laster ned direkte fra en nettside. På lik linje med hybrid-apper, kan utseende bestemmes helt og holdent av utvikleren, og vil være likt for begge plattformer (men kan også tilpasses hver enkel plattform om ønskelig). Man kan også ha samme løsning for web og app. Det som skiller PWA fra de andre teknologiene nevnt ovenfor er at man slipper helt å forholde seg til App Store/Google Play, på godt og vondt. Fordelen med det er at du raskt kan oppdatere appen med ny versjon, men man mister samtidig muligheten til å nå et bredere marked. Dette kan passe perfekt for eksempel for en lukket forening eller gruppe.

**Viktigste fordel:**
Ingen ventetid for stores godkjenning, kan oppdatere så ofte man vil.

**Viktigste ulempe:**
Mister mulighet til markedsføring som stores tilbyr.

## Oppsummering

Så, tilbake til det opprinnelige spørsmålet - hvilken teknologi bør vi velge? Svaret er enkelt - det spiller faktisk ikke så stor rolle. Bruk det som passer deres organisasjon best, og som utviklerne er komfortable med fra før, eller er entusiastiske for å lære. Resten ordner seg selv!
