:title Elixir: prosesser og meldinger
:author alf-kristian 
:published 2020-03-11

:tech [:elixir :erlang]

:blurb

Elixir er et relativt nytt språk som kjører på Erlang VM'en BEAM. Dette er en
annerledes plattform, som har vist seg å være svært robust, skalerbar og
tillater høy oppetid. En av årsakene til dette er minnehåndtering, som er svart
annerledes fra mange andre plattformer.

:body

Erlang ble først sluppet for 34 år siden, og ble opprinnelig laget for å
håndtere telefonsvitsjer. Plattformen BEAM (Bogdan's Erlang Abstract Machine)
ble derfor designet for robusthet, skalerbarhet og høy oppetid. Dette vil jeg
påstå er krav til de fleste applikasjoner i dag, men kanskje ikke alle
plattformene hadde dette som primærmål da de ble designet.

I dag skal vi se på en av tingene som gjør at BEAM skalerer så bra,
meldingsutveksling og minnehåndtering - og samtidig bruke det moderne
programmeringsspråket Elixir.

## Elixir-syntaks

For å komme i gang, vi må nesten se på litt enkel kode.

```elixir
# Tildeling (som egentlig er pattern matching)  
a = 1

# Funksjoner
f = fn a -> a + 1 end

# Kjøre funksjoner
f.(1) # gir 2

# Så en async prosess, som bare skriver hello world og terminerer
spawn fn -> IO.puts "Hello world" end

# Om prosesser skal snakke sammen trenger de å vite om hverandre, vi trenger process identifiers (PIDs)
parent = self() # typisk #PID<0.601.0>

# Oppretter en prosess som sender melding til meg selv
spawn fn -> send parent, "Hello from me" end

# Så må vi jo få tak i denne meldingen da
receive do 
  msg -> IO.puts "Received message: '#{msg}'" 
end

```

Jeg regner med at du ser hva det siste uttrykket skriver ut?

Elixir har en god del syntaks og en god del konsepter. Men for denne bloggposten
trenger du ikke forstå så veldig mye mer.

## Prosesser

BEAM sine startes altså med "spawn". De er lettvekt, helt isolerte og håndteres
av BEAM. Man kan konseptuelt tenke på prosessene som en OS-prosess, men de er
ikke det. Den eneste måte prosesser kan kommunisere med hverandre på er via
meldinger.

En prosess kan dessuten kun håndtere én slik melding om gangen. Om prosessen
gjør et blokkerende kall, så vil prosessen i praksis være låst og kan ikke
håndtere flere meldinger før kallet har returnert eller timet ut.

Du synes kanskje dette høres tungt ut å blokkere en hel prosess? Men en prosess
er som sagt en lettvekts greie, så om du trenger fler, så starter du flere. Det
er ikke uvanlig å ha millioner prosesser i en Erlang-applikasjon.

## Funksjonell programmering og immutability

Elixir (og Erlang) har ingen muterbare datastrukturer. Dette betyr i praksis at
verdier aldri endrer seg, at man skaper nye datastrukturer via transformasjon.

Men, et program må ha muterbar tilstand, i hvert fall noen steder. Dette løser
man med prosesser.

```elixir
# Vi må kjøre vår stateful prosess som en modul
defmodule Counter do
  def run(state) do
    receive do
        :inc -> run(state + 1)
        {:current, to} -> send to, state
      end
  end
end

counter = spawn(Counter, :run, [42])

send counter, :inc
send counter, {:current, self()}
v = receive do current -> current end
```

`v` er nå 43.

State, som her var et tall, kan selvsagt godt være en stor datastruktur. Men på
samme måte som tall så kan den ikke muteres. Den "muteres" via transformasjon og
rekursjon.

## Meldingsutveksling og minnehåndtering

So far so god. Vi har en funksjonell plattform som gjør asynkronitet lett, og
med immutable datastrukturer så kan plattformen vel bare sende rundt referanser?
Feil! På BEAM blir alle meldinger kopiert mellom prosesser.

Men...hva...i...alle...dager...

```elixir
# Lager et lite map
a = %{foo: 1}

# Vi må ha PID
parent = self()

# Sende den samme datastrukturen tilbake til oss selv
spawn fn -> send parent, a end

# Motta den
b = receive do msg -> msg end

# Tilsynelatende like
a == b # true
```

I Elixir/Erlang skal man egentlig ikke tenke på pekerlikhet, men det finnes en
udokumentert funksjon som forteller en litt annen historie:

`:erts_debug.same(a, b) # false`

Og da har vi omsider kommet til poenget med denne bloggposten. En av
trade-offene som BEAM har tatt, er at data kopieres mellom prosesser selv om
prosessene kjører "single threaded" og selv om alle datastrukturer er immutable.

Fordelen er total isolasjon. Prosessene kan leve helt uavhengig av hverandre,
også når det gjelder minnehåndtering. Hver prosess har sin egen heap og stack.
Dette gjør garbage collection (GC) veldig mye enklere. 

På andre plattformer så må GC traversere hele heapen og også stoppe
applikasjoner fra å prosessere, for å fullføre en full GC. På BEAM er det ingen
store heaps å traverse, og mange prosesser dør før det er behov for GC. Dette er
altså en av egenskapene som gjør at BEAM applikasjoner skalerer så bra.

## Toppen av isfjellet

Erlang bundles med OTP (Open Telephone Platform), som er mye av årsaken de gode
egenskapene til Erlang-applikasjoner.

Elixir-communitiet har utviklet webrammeverket Phoenix, som gjør at bruken av Erlang
og OTP blir veldig enkel.

Som du kanskje skjønner finnes mange flere fordeler med denne plattformen. Du
skal ikke se bort ifra at vi skriver mer om dette ved en annen anledning.


