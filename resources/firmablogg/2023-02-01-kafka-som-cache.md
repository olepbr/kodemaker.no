:title Kafka som cache
:author nils
:published 2023-02-01

:blurb

Kafka er et mangehodet beist med mange konfigurasjoner å skru på for å få det slik som vi vil.

Jeg bruker kafka som cache-lager for å raskt kunne varme opp cachen.
I et sånt scenario må tjenestene selv sende oppdaterte data til kafka og la andre konsumenter bruke disse dataene asynkront. 
På den måten unngår jeg også å overbelaste tjenester ved start av applikasjonen min når jeg trenger en full cache.

Her er hvordan jeg bruker Kafka for å vedlikeholde cacher. 

:body

Kafka er et mangehodet beist med mange konfigurasjoner å skru på for å få det slik som vi vil. Her er hvordan jeg bruker Kafka for å vedlikeholde cacher.

## TLDR;

Bruk et compacted topic i Kafka for dataene som skal caches. 
Ikke bruk *subscribe*, men *assign* for å manuelt lese alle partisjoner, og les alle meldinger fra begynnelsen av topicet. 
Pass på at du bruker riktig konfigurasjon for topicet og for consumerene dine. Og ikke skriv cachen din selv, men bruk et bibliotek.

## Compacted topic, hva og hvordan

På Kafka sender vi meldinger med en nøkkel og en verdi. Med et compacted topic lar vi Kafka slette duplikate meldinger, slik at det kun finnes én verdi med én gitt nøkkel. 
På den måten unngår vi store mengder data på topicet og det blir overkommelig å lese alle meldinger selv med høy trafikk. 
Dette er spesielt nyttig når vi ikke er opptatt av historikken til dataene, men bare siste snapshot.

Et compacted topic er såkalt "eventually consistent", noe som betyr at vi risikerer å finne duplikate nøkler 
hvis Kafka-serveren ikke har gjort en cleanup først. 
Men konseptuelt kan vi tenke at det bare finnes unike nøkler på et compacted topic.

Den viktigste konfigurasjonen er `cleanup.policy`. Det er denne som gjør at et topic er compacted, og det sier noe om 
hvordan Kafka skal rydde opp i gamle meldinger. Default verdi er `delete`, for et compacted topic skal verdien være `compact`. 

Den andre konfigurasjonen jeg kan finne på å skru på er `min.cleanable.dirty.ratio`. Denne trenger du ikke skru på med mindre det tar lang tid å lese inn cachen på grunn av mange meldinger.
Den sier noe om hvor stor andel duplikater det kan være på topicet før duplikatene blir slettet.

```properties
cleanup.policy = compact
min.cleanable.dirty.ratio = 0.25
```

## Lytt til meldinger uten group.id

"Vanlige" Kafka-consumere bruker `Consumer.subscribe()` for å lytte på meldinger fra et topic. 
Da får consumeren tildelt en eller flere partisjoner som den henter meldinger fra med jevne mellomrom, og den begynner å lese 
meldinger fra der den slapp sist, slik at den slipper å lese meldinger som allerede er lest.

Men siden vi skal lese inn en cache så ønsker vi at alle instansene av applikasjonene skal lytte på 
alle partisjoner og i tillegg lese alle tilgjengelige meldinger.

For å få til dette tildeler vi alle partisjonene fra topicet manuelt med `Consumer.assign()` og søker tilbake til første tilgjengelige melding med `seek()`.

```kotlin
// 1. lese ut metadata om alle partisjoner for et topic
val allPartitions: Collection<TopicPartition> = consumer.partitionsFor(topic).stream()
    .map { t -> TopicPartition(topic, t.partition()) }
    .collect(Collectors.toList())
// 2. manuelt assigne alle partisjoner til consumeren
consumer.assign(allPartitions)
// 3. søk tilbake til første melding for alle partisjoner 
consumer.seekToBeginning(allPartitions)

```

Ved å bruke `assign()` istedenfor `subscribe()` for å tildele partisjoner mister vi all funksjonalitet som en consumergroup i Kafka har, 
og vi kan med fordel sløyfe `group.id` som konfigurasjon for consumeren. Siden dette er en cache som vi ønsker å 
fylle fortest mulig med relevante data er det lurt å lese så mange meldinger som mulig hver gang vi poller, så
sett `max.poll.records` til et høyt tall.

Lese ut meldinger gjøres som normalt med `poll()`.

```properties
# group.id sløyfer vi
max.poll.records = 10000
```

## Bruk et bibliotek for caching

Ikke skriv cachen din selv. Det finnes mange gode cache-biblioteker der ute og selv hvor enkelt det høres ut som, så er caching kompliserte saker. 

Jeg synes for eksempel [caffeine](https://github.com/ben-manes/caffeine) er et glimrende bibliotek for caching på JVM-platformen. Den kan du bruke som cache for både aktiv, passiv og manuell lasting av data. 

Manuell innlesing av cache-data med caffeine:

```kotlin

val cache: Cache<String, MyPreciousValue> = Caffeine.newBuilder()
    .expireAfterAccess(7, TimeUnit.DAYS)
    .maximumSize(10_000)
    .build()

//...

consumer.poll(100.milliseconds.toJavaDuration()).map { record ->
    record.key() to record.value().mapToInternalDomain()
}.forEach{ key, value ->
    cache.put(key, value)
}
```



