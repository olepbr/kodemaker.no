:title Brukergrensesnitt i kode på Apples plattformer
:published 2020-03-25
:author andre
:tech [:swift :ios :macOS]

:blurb

Hvorfor jeg foretrekker å utvikle iOS- og macOS-brukergrensesnitt i kode fremfor å bruke Interface Builder
:body


SwiftUI ble lansert ved WWDC 2019, og ser veldig lovende ut. Ulempen er at SwiftUI bare kan benyttes på de siste versjonene av Apples plattformer, som iOS iOS 13+, macOS 10.15+, tvOS 13.0+ og watchOS 6.0+. Dette hindrer meg i å kunne ta i bruk SwiftUI i dag siden prosjektet jeg jobber med skal støtte de to siste hovedversjonene av iOS. I tillegg er SwiftUI helt nytt og kanskje litt umodent, så vi må nok fortsette med å skrive brukergrensesnitt på "gamlemåten" en liten stund til.

Hva er så "gamlemåten"? Det er å "tegne" opp grensesnittet i en grafisk editor, skrive det i kode eller en kombinasjon av disse fremgangsmåtene. Uavhengig av hvordan man lager brukergrensesnittet så må man kontrollere hvordan komponenter i et skjermbilde oppfører seg på forskjellige skjermstørrelser eller når man roterer en enhet. For å hjelpe oss med dette så har Apple gitt oss [Auto Layout](https://developer.apple.com/library/archive/documentation/UserExperience/Conceptual/AutolayoutPG/index.html).


## Auto Layout
Med Auto Layout setter man regler for hvordan komponenter skal plasseres i forhold til andre komponenter i forskjellige skjermstørrelser. Disse reglene kan man enten sette i kode, eller så bruker man [Interface Builder](https://developer.apple.com/xcode/interface-builder/) hvor man har en visuell editor der man kan sette disse reglene. Reglene kan bli forholdsvis komplekse, så her må man holde tungen rett i munnen.

![Autolayout editor](/images/blogg/autolayout-editor.png) _Autolayout editor_


## Interface Builder
Interface builder er den grafiske editoren i Xcode. Resultatet lagres i enten en .xib fil eller en .storyboard fil, avhengig av hvordan man velger å dele opp designet sitt. Formatet for begge filtypene er XML. 
Ved siden av å tegne brukergrensesnittet, så kan man sette egenskaper som f.eks farge på en knapp, fontstørrelse på en label, hvilken funksjon som trigges ved touch eller regler for hvordan komponenter skal posisjonere seg i forhold til hverandre. Bruker man Storyboard så kan man også kontrollere hvordan navigasjon mellom skjermbilder skal foregå, samt designe celler i UITableView eller UICollectionViews.

Det holder ikke bare å tegne brukergrensesnittet, man må også ha kode som definerer oppførselen i skjermbildene. Man knytter derfor kildekoden opp mot de korresponderende brukergrensesnittene man har tegnet opp. Det er slik man kan bestemme hvilke funksjoner som skal trigges ved hendelser i komponentene. 

## Storyboard og nibs 
Det er forholdsvis lett å tegne opp et brukergrensesnitt i et Storyboard, men er man flere utviklere på et prosjekt så kan storyboards være kilne å jobbe med. De resulterende XML filene blir fort store, og selv om formatet er lesbart, så er det ikke anbefalt å redigere de manuelt i en teksteditor. Til det er de alt for komplekse. Merge konflikter blir ofte så omfattende og komplekse at man fort må løse dette problemet med en felles enighet innad i teamet om at man ikke jobber på samme storyboard samtidig. Det å bruke Dependency injection (DI) gir også noen utfordringer ved bruk av storyboard, siden man ikke selv kontrollerer instansiering av view controllere. Det finnes callbacks som man kan implementere for å sette avhengigheter, men dette er ikke optimalt. 

Et annet alternativ er å ha hvert skjermbilde i en egen .xib fil, men ellers brukes Interface Builder på samme måte som for Storyboard. Ved bruk av Nibs er faren for merge konflikter noe mindre, men er fortsatt utfordrende å løse opp i.

Interface Builder har editorer for å sette opp Auto Layout constraints mellom komponenter, men det blir fort uoversiktelig. Det er ikke alltid lett å se hvilke regler som er satt, regler som mangler, eller eventuelle konflikter mellom regler selv om XCode gir advarsler når dette oppstår. Kjører man kode som har mangler så vil man se feilmeldinger i loggen, men som vi skal se på litt senere så er ikke disse feilmeldingene like enkle å tolke.

Et skjermbilde består ofte av mange grupper av elementer som logisk hører sammen i én komponent, men i koden så ligger de som enkeltkomponenter. Legger man til eller fjerner en komponent, så må man inn i Interface Builder og gjøre endringene der, for så å oppdatere tilhørende constraints. Her er det også lett å gjøre feil.

![Autolayout editor](/images/blogg/visual-constraints.png) _Hold tungen rett i munnen_



## Skrive brukergrensesnitt vha kode

Interface Builder med Nibs og Storyboards høres og ser ut som en grei måte å jobbe på, men min erfaring er at det fort blir uoversiktlig. Gjør man alt i kode, så har man alt på samme sted og man slipper å sjonglere mellom to representasjoner av brukergrensesnittet. Man instansierer objekter selv, og man sender avhengigheter inn der man trenger det. Etter at jeg mer eller mindre har gått bort fra å bruke Interface Builder, så har koden min blitt mer modulær. I stedet for en ViewController med masse enkeltstående elementer, så har det blitt mer naturlig å lage enkle komponenter som lett kan gjenbrukes mellom forskjellige brukergrensesnitt. 

Ulempen er at man må konfigurere komponenter i kode, som tidligere har blitt kontrollert av å sette egenskaper i Interface Builder. For å minske mengden av slik boilerplate kode, så er det nyttig å lage diverse hjelpefunksjoner og extensions som innkapsler dette. Ønsker man å endre på en oppførsel eller utseende, så gjør man det kun ett sted. 

Det minst tilfredstillende aspektet ved å skive alt i kode er måten man setter opp Autolayout. Apples APIer er ofte veldig godt designet og behaglig å jobbe med, men Autolayout APIer har absolutt forbedringspotensiale i det henseende.

Nedenfor er noen eksempler på hvordan man definerer ett parent-view som har ett subview som har padding på 10 på alle sider unntatt den nederste, hvor padding skal være 20.

![Enkel UI](/images/blogg/design-sample.png)


```swift
view.translatesAutoresizingMaskIntoConstraints = false
subview.topAnchor.constraint(
	equalTo: view.safeAreaLayoutGuide.topAnchor, 
	constant: 10).isActive = true
subview.leadingAnchor.constraint(
	equalTo: view.safeAreaLayoutGuide.leadingAnchor, 
	constant: 10).isActive = true
subview.bottomAnchor.constraint(
	equalTo: view.safeAreaLayoutGuide.bottomAnchor, 
	constant: -20).isActive = true
subview.trailingAnchor.constraint(equalTo: 
	view.safeAreaLayoutGuide.trailingAnchor, 
	constant: -10).isActive = true
```

alternativt

```swift
view.translatesAutoresizingMaskIntoConstraints = false
let constraints = [
    subview.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 10),
    subview.leftAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leftAnchor, constant: 10),
    subview.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -20),
    subview.rightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.rightAnchor, constant: -10)
]
NSLayoutConstraint.activate(constraints)
```

eller dersom du virkelig liker å ha det vondt:

```swift
view.translatesAutoresizingMaskIntoConstraints = false
NSLayoutConstraint(
	item: scrollView, 
	attribute: .top, 
	relatedBy: .equal, 
	toItem: view.safeAreaLayoutGuide, 
	attribute: .top, 
	multiplier: 1.0, 
	constant: 10).isActive = true
NSLayoutConstraint(
	item: scrollView, 
	attribute: .left, 
	relatedBy: .equal, 
	toItem: view.safeAreaLayoutGuide, 
	attribute: .left, 
	multiplier: 1.0, 
	constant: 10).isActive = true
NSLayoutConstraint(
	item: scrollView, 
	attribute: .bottom, 
	relatedBy: .equal, 
	toItem: view.safeAreaLayoutGuide, 
	attribute: .bottom, 
	multiplier: 1.0, 
	constant: -20).isActive = true
NSLayoutConstraint(
	item: scrollView, 
	attribute: .right, 
	relatedBy: .equal, 
	toItem: view.safeAreaLayoutGuide, 
	attribute: .right, 
	multiplier: 1.0, 
	constant: -10).isActive = true

```

Man kan også bruke “Visual Format Language” for å definere autolayout constraints. “Visual Format Language” benytter "Ascii Art" for å uttrykke reglene. Jeg har ikke testet ut dette selv, men idéen tiltaler meg så lite at jeg ikke har noe problemer med å stå over.

```
|-[find]-[findNext]-[findField(>=20)]-|
```

Den oppmerksomme leser vil se at man må sette `view.translatesAutoresizingMaskIntoConstraints = false` for hver komponent. Gjør man ikke det, så vil den forrige måten Apple løste posisjonering av elementer slå inn, og layouten man definerer med Auto Layout vil ikke fungere og man får en feilmelding som ser slikt ut:

```
[LayoutConstraints] Unable to simultaneously satisfy constraints.
	Probably at least one of the constraints in the following list is one you don't want. 
	Try this: 
		(1) look at each constraint and try to figure out which you don't expect; 
		(2) find the code that added the unwanted constraint or constraints and fix it. 
	(Note: If you're seeing NSAutoresizingMaskLayoutConstraints that you don't understand, refer to the documentation for the UIView property translatesAutoresizingMaskIntoConstraints) 
(
    "<NSAutoresizingMaskLayoutConstraint:0x600000e747d0 h=--& v=--& UIScrollView:0x7f9bbd867a00.minX == 0   (active, names: '|':UIView:0x7f9bbe9196d0 )>",
    "<NSLayoutConstraint:0x600000e4ead0 UIScrollView:0x7f9bbd867a00.leading == UILayoutGuide:0x600001480620'UIViewSafeAreaLayoutGuide'.leading + 10   (active)>",
    "<NSLayoutConstraint:0x600000e4e800 'UIViewSafeAreaLayoutGuide-left' H:|-(0)-[UILayoutGuide:0x600001480620'UIViewSafeAreaLayoutGuide'](LTR)   (active, names: '|':UIView:0x7f9bbe9196d0 )>"
)

Will attempt to recover by breaking constraint 
<NSLayoutConstraint:0x600000e4ead0 UIScrollView:0x7f9bbd867a00.leading == UILayoutGuide:0x600001480620'UIViewSafeAreaLayoutGuide'.leading + 10   (active)>

Make a symbolic breakpoint at UIViewAlertForUnsatisfiableConstraints to catch this in the debugger.
The methods in the UIConstraintBasedLayoutDebugging category on UIView listed in <UIKitCore/UIView.h> may also be helpful.
2020-03-20 14:53:36.445021+0100 PBLockKitTestApp[13280:2226834] [LayoutConstraints] Unable to simultaneously satisfy constraints.
	Probably at least one of the constraints in the following list is one you don't want. 
	Try this: 
		(1) look at each constraint and try to figure out which you don't expect; 
		(2) find the code that added the unwanted constraint or constraints and fix it. 
	(Note: If you're seeing NSAutoresizingMaskLayoutConstraints that you don't understand, refer to the documentation for the UIView property translatesAutoresizingMaskIntoConstraints) 
(
    "<NSAutoresizingMaskLayoutConstraint:0x600000e74870 h=--& v=--& UIScrollView:0x7f9bbd867a00.minY == 0   (active, names: '|':UIView:0x7f9bbe9196d0 )>",
    "<NSLayoutConstraint:0x600000e4ea80 UIScrollView:0x7f9bbd867a00.top == UILayoutGuide:0x600001480620'UIViewSafeAreaLayoutGuide'.top + 10   (active)>",
    "<NSLayoutConstraint:0x600000e4e7b0 'UIViewSafeAreaLayoutGuide-top' V:|-(44)-[UILayoutGuide:0x600001480620'UIViewSafeAreaLayoutGuide']   (active, names: '|':UIView:0x7f9bbe9196d0 )>"
)
```

Denne feilmeldingen vil for vårt eksempel fortsette 285 linjer til med lignende garble.


## SnapKit

[SnapKit](http://snapkit.io) er et lite bibliotek som har som mål å gjøre Auto Layout enklere å bruke. Jeg har benyttet dette i to prosjekter, hvor det ene har gått gjennom en gradvis omskriving fra Nib/Storyboard, og det har vært en veldig tilfredstillene øvelse. For å gi en smakebit på hvordan bruk av SnapKit kan se ut så har vi et eksempel under:


```swift
subview.snp.makeConstraints {
    $0.top.left.equalTo(self.view.safeAreaLayoutGuide).offset(10)
    $0.bottom.equalTo(self.view.safeAreaLayoutGuide).offset(-20)
    $0.trailing.equalTo(self.view.safeAreaLayoutGuide).offset(-10)
}
```

Ser man nærmere på linjen: `$0.top.left.equalTo(self.view.safeAreaLayoutGuide).offset(10)` så er top og left kjedet sammen, så man slipper en linje mindre enn hva man ellers ville ha gjort. SnapKit vil også sette `view.translatesAutoresizingMaskIntoConstraints = false` på nødvendige views automatisk.
Mangler det nødvendige constraints, så vil man i loggen se noe slikt som dette dersom man ikke bruker SnapKit:

```
  Probably at least one of the constraints in the following list is one you don't want. 
	Try this: 
		(1) look at each constraint and try to figure out which you don't expect; 
		(2) find the code that added the unwanted constraint or constraints and fix it. 
(
    "<NSLayoutConstraint:0x600002d84d70 UIScrollView:0x7fa69a042600.trailing == UILayoutGuide:0x6000037aa4c0'UIViewSafeAreaLayoutGuide'.trailing - 10   (active)>",
    "<NSLayoutConstraint:0x600002d84e60 UIScrollView:0x7fa69a042600.trailing == UILayoutGuide:0x6000037aa4c0'UIViewSafeAreaLayoutGuide'.trailing - 30   (active)>"
)

Will attempt to recover by breaking constraint 
<NSLayoutConstraint:0x600002d84d70 UIScrollView:0x7fa69a042600.trailing == UILayoutGuide:0x6000037aa4c0'UIViewSafeAreaLayoutGuide'.trailing - 10   (active)>
```


Med SnapKit så vil tilsvarende feil logges slik:

```
	Probably at least one of the constraints in the following list is one you don't want. 
	Try this: 
		(1) look at each constraint and try to figure out which you don't expect; 
		(2) find the code that added the unwanted constraint or constraints and fix it. 
(
    "<MyApp.LayoutConstraint:0x6000029a9980@MyViewController.swift#106 UIScrollView:0x7face585d800.trailing == UILayoutGuide:0x600003414540.trailing - 10.0>",
    "<MyApp.LayoutConstraint:0x6000029e0ea0@MyViewController.swift#107 UIScrollView:0x7face585d800.trailing == UILayoutGuide:0x600003414540.trailing - 30.0>"
)

Will attempt to recover by breaking constraint 
<MyApp.LayoutConstraint:0x6000029a9980@MyViewController.swift#106 UIScrollView:0x7face585d800.trailing == UILayoutGuide:0x600003414540.trailing - 10.0>

```

Uten SnapKit så vil man klø seg i hodet og lure på “Hvilket UIScrollView” er det snakk om?, mens med SnapKit så ser man med en gang at det scrollview i MyViewController på linje 106 som er problemet.

Man kan navngi constraints også:

```swift
subview.snp.makeConstraints {
    $0.left.top.equalTo(self.view.safeAreaLayoutGuide).offset(10)
    $0.bottom.equalTo(self.view.safeAreaLayoutGuide).offset(-20)
    $0.trailing.equalTo(self.view.safeAreaLayoutGuide).offset(-10).labeled(“Min riktige constraint”)
    $0.trailing.equalTo(self.view.safeAreaLayoutGuide).offset(-30).labeled("Min dodgy constraint")
}
```
 
som vil gi denne feilmeldingen.

```
	Probably at least one of the constraints in the following list is one you don't want. 
	Try this: 
		(1) look at each constraint and try to figure out which you don't expect; 
		(2) find the code that added the unwanted constraint or constraints and fix it. 
(
    "<SnapKit.LayoutConstraint:Min dodgy constraint@UnlockViewController.swift#107 UIScrollView:0x7fe6f5850000.trailing == UILayoutGuide:0x600003f89260.trailing - 30.0>",
    "<SnapKit.LayoutConstraint:Min riktige constraint@UnlockViewController.swift#106 UIScrollView:0x7fe6f5850000.trailing == UILayoutGuide:0x600003f89260.trailing - 10.0>"
)

Will attempt to recover by breaking constraint 
<SnapKit.LayoutConstraint:Min riktige constraint@UnlockViewController.swift#106 UIScrollView:0x7fe6f5850000.trailing == UILayoutGuide:0x600003f89260.trailing - 10.0>

Make a symbolic breakpoint at UIViewAlertForUnsatisfiableConstraints to catch this in the debugger.
The methods in the UIConstraintBasedLayoutDebugging category on UIView listed in <UIKitCore/UIView.h> may also be helpful.

```

Personlig så bruker jeg ikke å navngi constraints, men ved debugging så kan det være nyttig om man har et komplekst oppsett.

SnapKit tilbyr mer funksjonalitet enn det lille som vises her, og APIet er enkelt å skjønne, spesielt om man allerede kjenner til hvordan Auto Layout fungerer. For mer informasjon om bruken av biblioteket så gir [dokumentasjonen](http://snapkit.io/docs/) en grei oversikt over hvordan man kan benytte seg av det.



## Konklusjon

Inntil vi kan ta i bruk SwiftUI, så kan SnapKit gjøre hverdagen så mye enklere om man har et kodebasert brukergrensesnitt. All logikk for definisjon av UI foregår på ett sted, og man slipper smerten med å bruke Apple sine Auto Layout APIer.

