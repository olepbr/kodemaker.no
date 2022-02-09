:title Manipulering av Ikke-Muterbare Datastrukturer i Kotlin
:author kristian
:tech [:kotlin :arrow]
:published 2022-02-09

:blurb

Har du omfavnet funksjonell programmering ? Savner du en effektiv måte å manipulere ikke-muterbare datastrukturer på ?
Hva om det faktisk finnes et bibliotek som løser dette på en elegant måte ?  

:body

Det begynner å bli noen år siden _funksjonell programmering_ for alvor kom på agendaen igjen. Konseptene er veletablerte, men
har sett renessanser gjennom nye programmeringsspråk og teknologier. Jeg ble selv introdusert for denne verdenen for noen år
tilbake da jeg lærte meg [Clojure](https://www.kodemaker.no/clojure), en [Lisp](https://www.kodemaker.no/lisp)
som kompileres til Java bytecode.

Dette var et fundamentalt brudd med alt jeg var kjent med på daværende tidspunkt. Et fullstendig
paradigmeskifte i forhold til hvordan man skriver kode og hvordan strukturen i koden ser ut. Den viktigste lærdommen var dog ikke
språket i seg selv, selv om det er fryktelig morsomt å skrive, men heller prinsippene som ligger fundamentalt.

_Data_, _funksjoner_ og _ikke-muterbare datastrukturer_ er sentrale byggeklosser i en funksjonell verden. Jeg har omfavnet alle og tar med 
meg konseptene uavhengig av hvilket programmeringsspråk jeg jobber i. De siste par årene har jeg brukt mye tid i Kotlin, som kort 
fortalt er en solid oppgradering av den gamle arbeidshesten Java. [Kotlin](https://kotlinlang.org) er et objekt-funksjonelt språk som 
gir deg muligheten til å jobbe med ikke-muterbare data via konstruksjoner bygd inn i språket.

Spesielt har jeg lyst til å fokusere på Kotlin sine [data-klasser](https://kotlinlang.org/docs/data-classes.html). Dette er rene
dataholdere som gir deg en helt konkret garanti mot mutasjon. De kan dog manipuleres og Kotlin har innebygd funksjonalitet for å gjøre
nettopp det. Men det fungerer jo ikke optimalt, ellers hadde hele denne bloggposten vært overflødig.

## I feel the need - the need for speed!

Hva er det som er så forlokkende med ikke-muterbare datastrukturer da? Mutasjons-garantien medfører at dataene du nå har opprettet 
trygt kan sendes mellom funksjoner eller over nettverk for den saks skyld. Og du kan fortsatt sove godt om natta uten 
å bekymre deg for uhyggelig overraskelser. Dette gjør det også lettere å resonnere rundt dataene dine. Du kan spore hvert steg i en
funksjons-kjede og få et tilstandsbilde etter hver manipulasjon.

En slik flyt kan ses på som en [audit logg](https://en.wikipedia.org/wiki/Audit_trail). Hver endring som gjøres resulterer i et nytt
datasett. Det høres så fornuftig ut, men ikke alle ser på dette som en selvfølgelighet. Det må kjennes i fingrene, har du først fått
smaken på ikke-muterbare data er det vanskelig å se for seg noe annet.

Fint, dette høres jo vel og bra ut. Men la oss snakke mer om hvordan disse dataene da kan manipuleres. Den innebygde funksjonaliteten i Kotlin
er som nevnt over ikke optimal og vil for mange oppfattes tungrodd når man jobber med dype datastrukturer. Heldigvis finnes det et langt bedre
alternativ.

[Arrow](https://arrow-kt.io) er et Kotlin-bibliotek bygd på funksjonelle prinsipper. Rett og slett en godtebutikk av frittstående moduler.
En av disse er [Optics](https://arrow-kt.io/docs/optics/) - modulen. Optics er en gren innen [typeteori](https://en.wikipedia.org/wiki/Category_theory)
og uten at vi skal fordype oss veldig i det matematiske aspektet trenger vi bare å vite at optics er en samling abstraksjoner som gjør det
trivielt å manipulere ikke-muterbare datastruktuer. En konkret implementasjon er [linser](https://arrow-kt.io/docs/optics/lens/) som brukes
til å zoome inn på data og oppdatere disse.

 
## Houston, we have a problem

La oss flytte fokus fra teori til anvendelse. Det er mye lettere å forholde seg til konsepter når man ser en konkret implementasjon. For øyeblikket
sitter jeg stasjonert ute hos en kunde både jeg og _Kodemaker_ har et langvarig kundeforhold til. Jeg har nylig blitt brukt som sparringspartner i forhold 
til rekruttering hos denne kunden. I løpet av rekrutterings-prosessen blir alle potensielle kandidater verifisert gjennom en kodetest.

Kort fortalt skal det implementeres en versjon av [BlackJack](https://en.wikipedia.org/wiki/Blackjack) med noe tilpassede regler.
Det viktigste er ikke at kandidatene løser oppgaven 100% etter spesifikasjon, men heller viser logisk tenking og programmeringsferdigheter.
Siden jeg selv aldri har vært gjennom denne testen tenkte jeg det kunne være en morsom øvelse å løse denne med en ren funksjonell kodestil. Oppgaven passer
også ypperlig til å demonstrere verdien av ikke-muterbare datastrukturer siden vi har tilstandsendringer for hver eneste operasjon i spillet.

Vi definerer følgende regelsett for oss selv:

* Kun bruk av ikke-muterbare datastrukturer
* Hele spillets tilstand skal returneres etter hver operasjon
* Alle funksjoner skal, så langt det lar seg gjøre, være fri for sideeffekter ([pure](https://en.wikipedia.org/wiki/Pure_function))

Med ovennevnte regelsett slått i stein definerer vi følgende datastruktur:

```kotlin
data class Game(
    val deck: Deck = Deck(),
    val dealer: Dealer = Dealer(),
    val player: Player = Player(),
    val turn: Turn = Turn(),
    val score: Score = Score()
)
```

Et spill opprettes med et sett av defaults som er utgangspunkt før første operasjon.
Først la oss se hvordan et spill kan oppdateres med de verktøy vi har i språket:

```kotlin
val updatedGame = Game(
    Deck(Data(More(Data(...)))), 
    Dealer(Data(More(Data(...)))),
    Player(Data(More(Data(...)))),
    Turn(Data(More(Data(...)))),
    Score(Data(More(Data(...))))
)
```

Dette er altså fattigmannsversjonen. Jo dypere datastrukturer vi har, jo mer tungrodd blir det. Vi må altså flytte alle data fra eksisterende datasett
til et nytt og i tillegg sørge for at vi oppdaterer med riktig tilstand. Dette er jo en oppskrift på hvordan skape en mindre hyggelig hverdag for seg selv. 

Alternativt kan man bruke den innebygde [copy](https://kotlinlang.org/docs/data-classes.html#copying ) - funksjonen alle data-klasser i Kotlin automatisk 
får på kjøpet. Det betyr at vi får en lignende struktur:

```kotlin
val updatedGame = game.copy(
     player = game.player.copy(
               data = game.player.data.copy(
                       more = game.player.data.more.copy(
                               field = game.player.data.more.field.<function-call>
                      ) 
              )
     ) 
)
```

Det eneste positive vi kan si om denne strukturen er at du faktisk kun oppdaterer ett enkelt felt og resten av strukturen
forblir lik. Men altså, vil vi jobbe sånn? Nei, ikke når det finnes bedre alternativ.

## Show me the money!

Arrow Optics løser altså dette problemet på en langt mer elegant måte er påstanden. La oss se hva optics faktisk gjør med
kodebasen vår. Første steg er å aktivere optics-modulen for gjeldende prosjekt. Det er en ganske rett frem operasjon:

```kotlin
dependencies {
    ...	
    implementation("io.arrow-kt:arrow-optics:$arrow_version")
    ...
}
```

Her benyttes Gradle som byggeverktøy. I tillegg tilbys et eget Kotlin-plugin som genererer optics for dine datatyper. Dette
anbefales på det sterkeste:

```kotlin
plugins {
    id("com.google.devtools.ksp") version "x.y.z"
}

dependencies {
    ksp("io.arrow-kt:arrow-optics-ksp-plugin:$arrow_version")
}
```

Sånn! Da er alt klart. Meeeeen, hvordan blir optics generert for dine data da? Vi må kunne differensiere på hva vi faktisk ønsker det på.
Kort fortalt trenger vi å instrumentere data-klassene våre:

```kotlin
@optics
data class Game(
    val deck: Deck = Deck(),
    val dealer: Dealer = Dealer(),
    val player: Player = Player(),
    val turn: Turn = Turn(),
    val score: Score = Score()
) {
    companion object // required by @optics
}
``` 

Perfekt. Da ser det bra ut. Bygges prosjektet nå vil det bli generert kode og vi kan inspisere dette under en _generated_ -> _ksp_ -> _main_ -> _kotlin_ 
katalog. Det kan også være verdt å nevne at noen editorer har problemer med lese de genererte klassene og trenger litt hjelp. Dette kan løses ved
å fortelle Gradle at vi har ekstra _source sets_ å forholde oss til:

```kotlin
kotlin {
    sourceSets.main {
        kotlin.srcDir(".../generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir(".../generated/ksp/test/kotlin")
    }
}
```

## Remember, all I’m offering is the truth. Nothing more. 

Da er det på tide å ta et dypdykk ned i koden vår og se litt på hvordan dette fortoner seg i praksis. Et lite utsnitt av maskineriet som setter igang spillet
kan se ut som dette:

```kotlin
fun drawCards(game: Game): Game {
    return when (game.score.winner) {
        "" -> drawCard(game).map(::evaluateScores)
            .map(::evaluateWinner)
            .map(::drawCards)
            .getOrElse { evaluateWinner(evaluateScores(game)) }
        else -> game
    }
}
```

En rekursiv funksjon sørger for at det blir trukket kort og poengsummer blir kalkulert. Har vi da en vinner  brytes rekursjonen. I henhold til
design-prinspippene vi la til grunn returnerer altså hver funksjon i denne kjeden et nytt og oppdatert spill. Jeg har lyst til å titte litt inn 
i _drawCard_-funksjonen. Den er ansvarlig for at kort blir fjernet fra kortstokken og lagt til spiller eller dealer sin hånd. La oss begynne 
med å se på hvordan kort blir fjernet fra kortstokken:

```kotlin
val (drawnCard, deck) = game.deck.cards.uncons() 
```

Jeg nevnte tidligere at en konkret implementasjon av optics er linser. Her har vi en annen variant for dette er faktisk en
[prisme](https://arrow-kt.io/docs/optics/prism/). Men slapp helt av, det er veldig lite dramatisk. I motsetning til en linse så 
kan prismer oppdatere mer krevende datastrukturer som lister. I dette tilfellet ønsker vi å ta første kort av kortstokken hver 
gang det trekkes kort og gi oss tilbake både kortet og en manipulert versjon av hele kortstokken. Prismen [uncons](https://arrow-kt.io/docs/optics/cons/)
er en funksjon skreddersydd for dette formålet. Den returnerer et nullable [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/)
som inneholder både elementet som ble tatt av og den oppdaterte lista. Akkurat hva vi ønsker oss.

Men altså? Kompilerer det da? Nei, observante lesere vil kanskje se at å destrukturere data utifra noe som kan være null ikke vil fungere.
Poenget var å vise en litt forenklet versjon av bruken og i tillegg unngå å snakke for mye om andre deler av Arrow. Fordi den reelle koden
ser faktisk mer ut som dette:

```kotlin
fun drawCard(game: Game): Option<Game> = option.eager {
    // short circuit on empty deck
    val (drawnCard, deck) = game.deck.cards.uncons().toOption().bind()
    val gameWithDrawnCard = when (game.turn.next) {
        PLAYER -> updatePlayerWithCard(game, deck, drawnCard)
        DEALER -> updateDealerWithCard(game, deck, drawnCard)
    }
    gameWithDrawnCard
}

```

Selv om vi i all hovedsak forholder oss til optics brukes hele registeret til Arrow gjennom kildekoden. [Option](https://arrow-kt.io/docs/apidocs/arrow-core/arrow.core/-option/index.html#option) 
er bare et alternativ til Kotlin sin innebygde støtte for [null-safety](https://kotlinlang.org/docs/null-safety.html) og gir mer flytende kode.
Men det får vi heller snakke mer om senere. Grunnen til at vi i det hele tatt sjekker om kortstokken potensielt kan være tom er fordi
et tilleggskrav til oppgaven er at man skal kunne lese inn en fil med tilfeldige kort. Du trenger altså ikke spille med en full kortstokk på 52 kort.
Ellers ville denne sjekken vært helt meningsløs. 

Allright. Når et kort er tatt av kortstokken må det legges til enten hos spiller eller dealer. Hvem som får hva avhenger av reglene i spillet uten at vi skal fordype
oss noe mer i det nå. Isteden la oss se hvordan dette ser ut når en spiller får et nytt kort: 

```kotlin
val updatedGame = Game.player.cards.modify(game) { it.snoc(drawnCard) }
```

Her ser vi kombinasjonen av både en linse og en prisme hvor modify-funksjonen kalles på en linse(fra den genererte koden) og den underliggende datastrukturen(kortstokken)
 får et ekstra kort lagt til gjennom [snoc](https://arrow-kt.io/docs/optics/snoc/) - funksjonen. En betraktning er at optics både dekorerer data-klassene våre med linser, men
også [utvider](https://kotlinlang.org/docs/extensions.html) standard datastrukturer som lister med ekstra funksjoner. Graden av lesbarhet øker også betraktelig gjennom en 
_dot_-basert syntax. Vi dotter oss ned til relevant datastruktur, manipulerer og returnerer en ny oppdatert struktur.

Når vi først har varmet opp, la oss se litt nærmere på bruken av optics. En observasjon er jo at disse operasjonene vil gjentas og det kan ligge noe verdi i å forsøke 
og generalisere koden vår. Ok, en relativt enkel øvelse på typegymnastikk gir oss:

```kotlin
fun <T, U> update(container: U, item: T, lens: Lens<U, T>): U {
    return lens.modify(container) { item }
}
```

En generisk og enkel måte og oppdatere ett enkelt datafelt på. Som kan brukes på denne måten:

```kotlin   
val gameWithUpdatedDeck = update(game, deck, Game.deck.cards)
```

Muligens ikke veldig stor nytteverdi, men litt trening før vi forsøker å generalisere hvordan kort legges
til på f.eks en spiller:

```kotlin
fun <T, U> insert(container: U, item: T, lens: Lens<U, List<T>>): U {
    return lens.modify(container) { it.snoc(item) }
}
```

Da blir syntaxen straks litt hyggeligere når koden ser slik ut:

```kotlin
val updatedGame = insert(game, drawnCard, Game.player.cards)
```

Vi har sett på oppsett og konfigurasjon av optics, hvordan koden manifesterer seg og hvordan vi kan generalisere på typer.
Det hadde jo også vært litt gøy å se hvordan dette faktisk fortoner seg når koden kjører så da gjør vi det. Her er drawCards - funksjonen
oppdatert med en _println()_ som skriver ut tilstanden på hele spillet etter hver iterasjon. For enkelhets skyld har jeg begrenset kortstokken
 til kun og inneholde 5 kort.

```sh
kristian@local ~/R/k/b/dist> ./bj-cli -i cards.txt
Snapshot of game state: Game(deck=Deck(cards=[CA, D5, H9, HQ, S8]), dealer=Dealer(name=dealer, cards=[]), player=Player(name=sam, cards=[]), turn=Turn(next=PLAYER), score=Score(playerScore=0, dealerScore=0, winner=))
Snapshot of game state: Game(deck=Deck(cards=[D5, H9, HQ, S8]), dealer=Dealer(name=dealer, cards=[]), player=Player(name=sam, cards=[CA]), turn=Turn(next=DEALER), score=Score(playerScore=11, dealerScore=0, winner=))
Snapshot of game state: Game(deck=Deck(cards=[H9, HQ, S8]), dealer=Dealer(name=dealer, cards=[D5]), player=Player(name=sam, cards=[CA]), turn=Turn(next=PLAYER), score=Score(playerScore=11, dealerScore=5, winner=))
Snapshot of game state: Game(deck=Deck(cards=[HQ, S8]), dealer=Dealer(name=dealer, cards=[D5]), player=Player(name=sam, cards=[CA, H9]), turn=Turn(next=DEALER), score=Score(playerScore=20, dealerScore=5, winner=))
Snapshot of game state: Game(deck=Deck(cards=[S8]), dealer=Dealer(name=dealer, cards=[D5, HQ]), player=Player(name=sam, cards=[CA, H9]), turn=Turn(next=PLAYER), score=Score(playerScore=20, dealerScore=15, winner=))
Snapshot of game state: Game(deck=Deck(cards=[S8]), dealer=Dealer(name=dealer, cards=[D5, HQ]), player=Player(name=sam, cards=[CA, H9]), turn=Turn(next=DEALER), score=Score(playerScore=20, dealerScore=15, winner=))
Snapshot of game state: Game(deck=Deck(cards=[]), dealer=Dealer(name=dealer, cards=[D5, HQ, S8]), player=Player(name=sam, cards=[CA, H9]), turn=Turn(next=PLAYER), score=Score(playerScore=20, dealerScore=23, winner=sam))

Winner: sam, Player score: 20, Dealer score: 23, Player hand: [CA, H9], Dealer hand: [D5, HQ, S8]
```

## This is the way

Arrow sin optics-modul har vist seg å være veldig nyttig når man ønsker å jobbe med dype ikke-muterbare datastruktur på en enkel og konsis måte.
Har man det minste snev av interesse for funksjonell programmering eller hvertfall elementer fra det bør optics stå høyt oppe på ønskelista. Nå
er det jo heller ikke sånn at man trenger å gjøre full buy-in på hele Arrow stacken for å bruke optics. Man står selvfølgelig helt fritt til å bruke de
modulene som gir mest mening i forhold til egne behov. 

###### **Notis**: Kun utdrag av koden blir vist og ingen lenke til Github i denne omgang siden testen fortsatt er i bruk hos kunden
