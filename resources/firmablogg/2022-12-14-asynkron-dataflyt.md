:title Asynkron Dataflyt i Kotlin
:author kristian
:tech [:kotlin :coroutines]
:published 2022-12-14

:blurb

Ser du verdien av en asynkron dataflyt og ønsker å implementere dette i Kotlin ? Kanskje har du blitt eksponert for begreper som coroutines, suspend-funksjoner og flows uten at du ble spesielt klokere av den grunn ? Da er denne bloggposten for deg.

:body

En asynkron programmeringsmodell gir merverdi når vi ønsker _samtidighet_ i koden vår. I utganspunktet vil en sekvens av funksjoner kalles synkront hvis vi ikke eksplisitt har bedt om noe annet. Synkront i denne sammenhengen betyr at alle funksjoner er blokkerende og ikke går til neste steg i funksjonskjeden før nåværende funksjon er avsluttet eller returnerer en verdi. 

Det motsatte, altså asynkrone funksjoner, gjerne også omtalt som ikke-blokkerende funksjoner er det vi skal se nærmere på. Dette er begreper som ofte brukes om hverandre uten at det alltid vil være et en-til-en forhold. I Kotlin finnes det et samlebegrep som omfavner all type asynkronitet: _coroutines_.

## Destination unknown

Det finnes mange forskjellige tilnærminger til en asynkron modell og spesielt nærliggende er det å sammenligne med de konstruksjonene som allerede finnes i jvm-økosystemet. Vi har callbacks, futures, promises og tråder for å nevne noen. Sistnevnte er fortsatt veldig sentralt og selve fundamentet for nye asynkrone modeller som bygges. 

Så hvorfor kan vi ikke bare fortsette å bruke tråder direkte? Det er jo et betimelig spørsmål. [Tråder](https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/lang/Thread.html) har vært med oss siden JDK 1.0 ble releaset for [26](https://en.wikipedia.org/wiki/Java_version_history) år siden. Verden har endret seg relativt mye siden den gang og selve tråd-api'et har også blitt overhalt i takt med tiden. I tillegg fungerer tiden som en kontinuerlig erfaringsrapport som naturligvis gir nye ideer om hvordan asynkronitet kan løses.

En annen faktor som heller ikke bør undervurderes er de tekniske utfordringer som følger med på kjøpet når man benytter seg av tråder. Alle som har et snev at erfaring med å bruke tråder vet at de er kostbare å benytte seg av og det er lett å spise alt av tilgjengelige ressurser hvis vi ikke er påpasselige. Underliggende hardware er også en helt begrensende faktor for hvor mange tråder som kan operere i parallell og det kan være krevende å finne den riktige balansen. 

Tråd-api'et skyver også mye av ansvaret for hvordan koden skal operere over på den implementerende part. Feilhåndtering er eksempelvis noe man må ha et eksplisitt forhold til og en tydelig strategi på.

Jeg tror ikke vi skal demonisere bruken av tråder mer enn nødvendig for selv om det er noe iboende kompleksitet er det selvsagt fullt mulig å fortsette i samme sporet. Hvis du er glad i selvpining da. 

Heldigvis finnes det abstraksjoner som skjuler denne kompleksiteten og gir oss mer raffinerte verktøy for raskt å komme igang med en asynkron dataflyt. Coroutines er i så måte en åpenbar kandidat. Skal vi ta et dypdykk?

 
## When the going gets tough, the tough get going

Før vi begynner å fleske ut kode tror jeg det er lurt at vi etablerer et begrepsapparat for å forstå hva corutines faktisk er og hvordan disse fungerer. Jeg kommer ikke til å beskrive alle detaljer, men heller legge dette på et nivå godt nok til at vi kan resonnere rundt det på en fornuftig måte. 

### Coroutine

En [coroutine](https://kotlinlang.org/docs/coroutines-basics.html#your-first-coroutine) er en lettvektsprosess med asynkrone garantier. Det høres jo passe fluffy ut, hva betyr det egentlig? 

Coroutines-biblioteket er en abstraksjon over java tråder og enhver coroutine blir eksekvert i en eller annen tråd. Vi kan ha mange couroutines per tråd og hvordan dette styres trenger heldigvis ikke vi ofre mye tankeaktivtet på.

I hjertet av coroutine-maskineriet har vi suspend-funksjoner. Enhver funksjon av denne typen vil bli eksekvert asynkront. Den kan startes, settes på pause og restartes. Det eneste kravet som stilles til implementerende part er å definere funksjoner av denne typen. Resten blir håndtert for deg.

```kotlin
suspend fun startProcess() { ... }
```

Det er allikevel ikke så banalt at en suspend-funksjon automatisk kjøres i en asynkron kontekst. Vi må ha litt mer staffasje. Suspend-funksjoner kan startes på to forskjellige måter, enten via `launch` eller `async`. Førstnevnte er i all hovedsak fire-and-forget mens sistnevnte returnerer en `Deferred` av type `Future`.

Felles for begge funksjonene er at de returnerer en referanse til en `Job`, som er en    unik identifikator for denne coroutinen. Via `Job`-referansen har vi direkte link til den underliggende livssyklusen.

```kotlin
val job = launch { startProcess() }
job.cancel()
```

### Scope

En couroutine må også tilknyttes et coroutine [scope](https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html#coroutine-scope). Dette kan gjøres på to forskjellige måter.

Den aller enkleste formen er å implementere `CoroutineScope`.

```kotlin
class CustomProcessor(...) : CoroutineScope {
    override val coroutineContext: CoroutineContext get() = ...
	
    fun startProcessor() = launch { doProcess() }
	
    private suspend fun doProcess() {...}
}
```

Det eneste som overlates til oss er å definere konteksten. Jeg kommer tilbake til hva dette faktisk innebærer. 

Vi kan merke oss at alle coroutinene som trigges inne i klassen vil bli eksekvert i samme scope. Det er ikke sikkert vi ønsker oss. Det kan være at vi vil ha mer fingranulert kontroll over hva som kjøres hvor. Heldigivs er det mulig å definere en mer eksplisitt variant.

```kotlin
class CustomProcessor(...) {
    private val scope = CoroutineScope(...)
	
    fun startProcessor() = scope.launch { doProcess() }
	
    private suspend fun doProcess() {...}
}
```

... hvor scope-funksjonen tar inn en kontekst som argument.

### Context og Dispatcher

En [kontekst og en dispatcher](https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html) er gjensidig avhengig av hverandre så det gir ikke noe mening å beskrive disse separat. En kontekst vil alltid inneholde en dispatcher og en dispatcher vil alltid være en del av en kontekst.

Litt forenklet kan en kontekst ses på som et `map`. Med mindre du selv har definert en dispatcher vil konteksten ha en referanse til `Dispatchers.Default`. 

En dispatcher er en abstraksjon som bestemmer hvilken eller hvilke tråder korresponderende coroutines skal kjøres på. Det finnes et sett av predefinerte dispatchere som vil passe i de fleste tilfeller. Noen utvalgte typer:

* `Dispatchers.Default`
* `Dispatchers.IO`
* `Dispatchers.Unconfined`

Default dispatcheren får du ut av boksen og den begrenser paralelliseringen til antallet cpu-kjerner tilgjengelig, minimum to. 

IO-dispatcheren passer typisk til io-tunge oppgaver og har i utgangspunktet allokert 64 tråder i en tråd-pool. Hvis kapasiteten på underliggende hardware overgår 64 cpu-kjerner vil isteden denne verdien bli brukt for å bestemme størrelsen på tråd-poolen.

En unconfined dispatcher passer til lette prosesser som krever lite ressurser. Siden den ikke er begrenset til én bestemt tråd eller tråd-pool hekter den seg på `main`-tråden i den kjørende prosessen.

Disse predefinerte dispatcherne er strengt tatt bare _views_ over underliggende arkitektur. Således hadde det vært prakisk om vi selv kunne komponere dispatchere skreddersydd etter egne behov. Vel, faktisk så er det mulig. Vi kan skru litt på elastisiteten med `limitedParallelism`.

```kotlin
val customDispatcher = Dispatchers.IO.limitedParallelism(80)
val currentDispatcher = Dispatchers.IO.limitedParallelism(100)
```

Her allokeres henholdsvis 80 og 100 tråder til de respektive tråd-poolene.

Ser vi igjen på eksemplet ovenfor kan vi nå implisitt definere kontekst ved å spesifisere ønsket dispatcher.

```kotlin
class CustomProcessor(...) {
    private val scope = CoroutineScope(Dispatchers.IO)
	
    fun startProcessor() = scope.launch { doProcess() }
	
    private suspend fun doProcess() {...}
}
```

### Exception Handler

Noen få ting bør man være klar over rundt feilhåndtering. Enhver coroutine har et sikkerhetsnett i form av en [exception handler](https://kotlinlang.org/docs/exception-handling.html#coroutineexceptionhandler). Den vil  fange opp eventuelle feil det ikke finnes noen strategi for.

Dette høres jo bra ut og vil fungere akkurat som forventet med nøyaktig én coroutine kjørende, noe som i realiteten ikke vil skje veldig ofte. Det som derimot kommer til å oppstå er at man etablerer ett eller flere scope med mange couroutiner i parallell.  

Hvordan dette utfolder seg kan sammenlignes med en [trestruktur](https://no.wikipedia.org/wiki/Tre_(datastruktur)). Vi har en klassisk relasjon med foreldre- og barne-noder. Det vil alltid være én rotnode som alle barn lever under. 

Det som oppstår når man etablerer slike relasjoner er at hver eneste barnenode vil neglisjere sin egen exception-handler og propagare feil oppover i hierarkiet. Med mindre du har gjort noe utover standard konfigurasjon vil rotnoden skrive ut full stracktrace til standard error og avslutte egen livssyklus. Dette betyr også at alle barnenoder vil dø. En relativt hard og brutal [fail-fast](https://en.wikipedia.org/wiki/Fail-fast), noe som forsåvidt er helt greit hvis det er konfigurerbart.

I praksis fortoner det seg sånn at når kontekst opprettes vil det også automatisk genereres en rotnode i form av en coroutine. `Job`-referansen til denne er ikke umiddelbart synlig, men vi kan hente den ut fra konteksten. Eventuelt kunne vi eksplisitt ha spesifisert den selv.

```kotlin
class CustomProcessor(...) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
}
```

Fleksibilitet er fint og det har vi behov for nå. Vi ønsker en rotnode som ikke er basert på fail-fast strategi. Heldigvis er det allerede tilgjengelig i form av en `SupervisorJob`.

```kotlin
class CustomProcessor(...) {
    private val superVisorJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + superVisorJob)
}
```

Nå begynner vi å snakke! Siden rotnoden vår er en supervisor betyr dette at alle barnenodene fortsatt vil propagere feil helt ut, men dette vil ikke ta ned rotnoden, kun den enkelte barnenode.

Er det noe mer spennende vi kan gjøre da? Absolutt. En helt standard konfigurert exception handler skriver ut feil til `standard error`, men hva om vi ønsker å få dette inn en logg vi allerede har etablert? Ikke utypisk finnes det logg-aggregering via `slf4j` eller tilsvarende om vi ønsker en mer strukturert form på exceptions. Hva med en pen stringifisert variant som vi kan logge som `error` og varsle på ?

```kotlin
private val coroutineExceptionHandler = CoroutineExceptionHandler { _, ex ->
    log.error {
        "CoroutineExceptionHandler caught: ${ex.stackTraceToString()}"
    }
}
```

Gir oss et oppsett vi kan bruke videre.

```kotlin
class CustomProcessor(...) {
    private val superVisorJob = SupervisorJob()
	
    private val exHandler = CoroutineExceptionHandler { _, ex ->
        log.error {
            "CoroutineExceptionHandler caught: ${ex.stackTraceToString()}"
        }
    }

    private val scope = CoroutineScope(Dispatchers.IO + superVisorJob + exHandler)
	
    fun startProcessor() = scope.launch { doProcess() }
	
    private suspend fun doProcess() {...}
}
```

## Nothing's gonna stop us now

En ting som irriterer meg når jeg leser om nye spennende teknologier er at eksemplene ofte fremstilles veldig forenklede og langt fra er noen god illustrasjon på hvordan det faktisk kan brukes. En god kollega av meg pleier å karakterisere dette som Mikke Mus - eksempler. Jeg er langt på vei enig så istedenfor legger meg på Supermann-analogien. Så ja, hvordan får jeg superkrefter da ?

Jeg har jobbet mye med [Kafka](https://kafka.apache.org) de siste årene og er stor fan av hvordan man kan dele informasjon gjennom en ren datadrevet modell. Jeg har derfor mange ganger skrevet kode for både å konsumere og produsere data til Kafka. 

Etter å ha smakt litt ordentlig på coroutines tenkte jeg det kunne være morsomt å integrere konsumeringen som en del av en asynkron dataflyt. Så hvordan kan vi gjøre det da? 

Jeg har en god og en dårlig nyhet. Den dårlige er at hvis du trodde du var utlært så er du ikke det. Flows sa du ? Nettopp. Den gode nyheten er at Flows er gromme saker og passer perfekt til det vi skal løse. 

Men hva er _egentlig_ en [Flow](https://kotlinlang.org/docs/flow.html) ? Kort fortalt brukes en flow til asynkront å produsere flere verdier. I motsetning til suspend-funksjoner som gir oss én verdi. Når vi konsumerer fra Kafka kan vi populere en flow med en jevn strøm av data.

En annen viktig egenskap er at en flow i utgangspunktet er _kald_. I praksis betyr det at det ikke går noe data via flow'en før du faktisk konsumerer fra den. Som igjen betyr at du kan sende en flow rundt som en ren verdi. Musikk i ørene til alle som er glad i en funksjonell programmeringsstil.

Take my money and shut up! Enig, vi må se på kode.

```kotlin
fun <K, V> KafkaConsumer<K, V>.pollAsFlow(topics: List<String>): Flow<ConsumerRecord<K, V>> =
    callbackFlow {
        thread(name = "flow-thread") {
            Either.catch {
                subscribe(topics)
                while (isActive) {
                    val records = poll(ofSeconds((3)))
                    for (record in records) {
                        trySendBlocking(record).getOrThrow()
                    }
                    commitSync()
                }
            }
                .fold(::close) {}
        }
        awaitClose { log.info { "Stopped Kafka consumer (for topics: $topics)" } }
    }
```

Vi benytter oss av Kotlin sin innebygde støtte for å utvide 3-parts biblioteker via [extension](https://kotlinlang.org/docs/extensions.html)-funksjoner og utvider consumer'en med en `pollAsFlow`. Da blir det straks mer trivielt å jobbe med. 

[Polle](https://kafka.apache.org/28/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html#poll(java.time.Duration)) data fra Kafka er i utgangspunktet en prosess som vil gå over lang tid og blokkere for hver gang den henter ned nye data. Konverteringen mellom en synkron og asynkron verden skjer i [`callbackFlow`](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/callback-flow.html). Dette betyr at funksjonalitet som back-pressure håndteres for oss rett ut av boksen.

Ovenfor så vi på typer av feilhåndtering og i dette tilefellet er det implementert en fail-fast variant som vil propagere exceptions hele veien ut til rotnoden og terminere flow'en. 

Jeg har tidligere [skrevet](https://www.kodemaker.no/blogg/2022-02-09-manipulering-ikke-muterbare-datastrukturer/) om min begeistring for en funksjonell kodestil og hvordan [Arrow](https://arrow-kt.io) tilbyr funksjoner og kontruksjoner for nettopp dette. Her har jeg brukt [Either](https://arrow-kt.io/docs/apidocs/arrow-core/arrow.core/-either/) i feilhåndteringen. 

Så hvordan bruker jeg denne flow'en? La oss forsøke å gjøre dette så produksjonsnært som mulig. 

En viktig merknad rundt Kafka sin [consumer](https://kafka.apache.org/28/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html) er at denne i utgangspunktet kun kjøres i én tråd. Med andre ord, hvis jeg ønsker å parallellisere konsumeringen må jeg ha flere consumer-instanser. 

Det er heller ikke uvanlig at et Kafka [topic](https://kafka.apache.org/documentation/#intro_concepts_and_terms) inneholder mye data med mange tilhørende  [partisjoner](https://kafka.apache.org/documentation/#intro_concepts_and_terms) for å optimalisere lagringen. God kotyme er å matche antallet consumere med antallet partisjoner. La oss nå si at vi gjør akkurat det. 

```kotlin
class CustomConsumer(
    private val topics: List<String>,
    private val consumers: List<KafkaConsumer<String, SpecificRecord>>
) {
    ... 
}
```

Hvordan hver consumer genereres med tilhørende konfigurasjon lar jeg være en øvelse til den enkelte. Det er ikke veldig avansert. La oss bare anta at det er gjort og at vi har en liste av consumere vi ønsker å konsumere fra i parallell. 

Siden vi også ønsker å konsumere fra forskjellige topic har vi gjort dette mer fleksibelt ved å operere på en liste av disse. Det er verdt å merke seg at alle meldinger på tvers av topic behandles som `SpecificRecord`. Oppsiden er følgelig at vi kan håndtere disse generisk. Inntil videre. 

Ok. Nå har jeg lyst på en flow over alle datene vi konsumerer. Får jeg til det da? 

```kotlin
fun consumeMessages() = consumers.map { it.pollAsFlow(topics) }.merge()
```

Vi bruker poll-funksjonen vår i kombinasjon med en `merge` og vipps så har vi én flow over alle dataene våre helt asynkront og parallellisert. Det er vel ganske heftig?

Observante lesere har muligens lagt merke til at jeg ikke tagger flow-funksjonene med `suspend`. Det er forøvrig helt riktig. Husker vi definisjonen ? Nettopp, en flow er altså kald i utgangspunktet. En ren verdi det ikke går noe data via før vi faktisk ber om det. Men når vi konsumerer så må det gjøres via en coroutine, eller? Det stemmer. La oss se på hele oppsettet.

```kotlin
class CustomProcessor(private val consumer: CustomConsumer) {
    private val superVisorJob = SupervisorJob()
	
    private val exHandler = CoroutineExceptionHandler { _, ex ->
        log.error {
            "CoroutineExceptionHandler caught: ${ex.stackTraceToString()}"
        }
    }

    private val scope = CoroutineScope(Dispatchers.IO + superVisorJob + exHandler)
	
    fun startProcessor() = scope.launch { startConsumer() }
	
    private fun startConsumer() = scope.launch {
        consumer
            .consumeMessages()
            .collect(::processRecord)
    }

    private fun processRecord(record: ConsumerRecord<String, SpecificRecord>) {
        when (val message = record.value()) {
            is OneType -> ...
            is AnotherType -> ...
            else -> ...
        }
    }
}
```

Og sånn ser det ut. Det er `collect` som sørger for at flow'en endrer status fra kald til varm og at det flyter data.

## (Keep on) Rockin' in the free world

Hvis vi forsøker oss på en liten oppsummering er jeg freidig nok til å foreslå coroutines som et veldig godt alternativ til en asynkron dataflyt. 

Bruksområdene er omfattende og det handler egentlig bare om å finne den riktige balansen. Det finnes et helt bibliotek med byggeklosser som dekker de aller fleste behov.

Jeg har selv brukt en god del tid på å bearbeide den offisielle dokumentasjonen fra Kotlin-miljøet og syntes den er noe tungrodd og mangelen på gode eksempler har gjort det utfordrende. Jeg har også slitt med å finne relevante artikler generelt sett bortsett fra noen veldig spesifikke eksempler fra Android-miljøet.

Tanken var at denne bloggposten skulle være en komprimert introduksjon til coroutines og gi nok kjøtt på beinet til at man selv kan sette igang å kode. Så da foreslår jeg nettopp det :)
