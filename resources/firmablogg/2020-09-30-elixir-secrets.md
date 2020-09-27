:title Elixir/Phoenix: Logge config og maskere secrets
:author alf-kristian 
:published 2020-09-30

:tech [:elixir :erlang :funksjonell-programmering]

:blurb

Logger du config i oppstart av appen din? Nei? Det gjøres heller ikke i Elixir/Phoenix. Dette er rett og slett ikke godt nok. Her er en oppskrift på hvordan gjøre dette. 


:body

Jeg er sjokkert over hvorfor ikke alle logger sin config i oppstart. Og jeg hadde forventa at et så gjennomtenkt rammeverk som Phoenix i det minste gjorde dette enkelt for sine brukere. Men den gang ei!

Så da gjorde jeg det eneste fornuftige, og skrev kode for å gjøre det selv.

## Ehmm...logge config...hvorfor?

Ok, dette et selvsagt ikke den viktigste egenskapen en applikasjon har, men jeg vil påstå det er veldig nyttig. Lurer du på hva en config er, bare sjekk i loggen. Endrer du en config, men er litt usikker på om den er med, bare sjekk i loggen.

Å ha dette på plass er verdt en liten innsats i et hvert prosjekt, vil jeg påstå. Er du så heldig å bruke Elixir til vanlig, men ikke har dette på plass? Vel, da er du enda mer heldig, jeg har gjort jobben for deg.


## Men dette er jo enkelt, kan jeg ikke bare logge configen da?

Klart du kan. Men, du bør nok maskere secrets i configen din, hvis ikke kan de komme på avveie. Så utfordringen er å identifisere og maskere dem. Jeg har tidligere skrevet om hvordan dette kan gjøres [i Clojure](/blogg/2019-10-clj-secrets/).

Men altså, nå er vi på en helt annen plattform, og her funker ting litt annerledes. Og for å forstå dette, trenger vi å forstå en god del ting om Elixir.

## Nok prat, "show me some code"

Vi må ha litt kjennskap til Elixir sine datastrukturer, da Elixir sin config er fleksibelt og kan inneholde nesten hva som helst.

#### Atomiske verdier
 
Jepp, ganske likt som i andre språk. F.eks.
```elixir
 # Tall
 42

 # Tekst
 "Hello world"

 # Atoms, som ligner mye på symboler i andre språk 
 :start_with_colon
```

#### Lister
 
Funksjonelle singly linked lists. Ganske så like som i andre funksjonelle språk egentlig. Fin kompakt syntaks:
 
 ```elixir
 [1, 2, 3]
```

#### Tupler

Veldig likt tupler i andre språk. Kan ikke utvides på samme måte som en liste, men har indeks oppslag: 

 ```elixir
{1, 2, "tre"} # Tuppel med lengde 3
```



#### Maps
 
Funksjonelle maps, key-value par, der key er unik...hmm...men det visste du jo vel?
 
 ```elixir
# String som keys
%{"foo" => "bar"}

# Atoms som keys
%{:foo => "bar"}

# Og så en liten twist til stor frustrasjon for alle Clojure entusiaster. Samme som utrykket over
%{foo: "bar"}

```
#### Struct
 
Map'ish verdi, men med faste felter:
```elixir
defmodule Foo do
  defstruct [:a, :b, :a]
end

%Foo{a: 1, b: 2, c: "tre"}
```
En struct kan nesten brukes som et map, men inneholder et `:__struct__` felt. Dette gjør det mulig å sjekke struct-typen. Om en tar dette feltet bort, kan en bruke Map api'et mot structen.

Structer i seg selv er ikke vanlig å bruke i config. Men det er basis for typer som DateTime og Regex, så de
må vi at høyde for.

## Secrets i Elixir config

Det er ikke noen konvensjoner om hva som er en secret, så jeg har derfor valgt å velge alt som høres hemmelig ut. Her er et passende regex for vårt system:

```elixir
~r/key|secret|salt|encryption|password|connection_string|creds/
```

Elixir har egen syntaks for regexer. Egentlig ikke noe spesielt for regexer, dette er såkalt [sigils](https://elixir-lang.org/getting-started/sigils.html), som under panseret blir strukter.

Når vi treffer en secret i config så må den maskeres. Dette er basert på navnet til verdien. Her er maskeringsfunksjonen:

```elixir
  @secret_matcher ~r/key|secret|salt|encryption|password|connection_string|creds/
  
  defp secrets_masker(k, v) do
    s =
      if is_atom(k) do
        # Veldig vanlig at nøkler i maps er atoms, men vi skal jo regex matche på strings
        Atom.to_string(k)
      else
        k
      end

    if is_binary(v) &&
         is_binary(s) &&
         Regex.match?(@secret_matcher, s) do
      {k, ((v || "") |> String.first()) <> "*****"}
    else
      {k, v}
    end
  end
```

`is_binary` tenker du sikkert? Og dette er litt rart, men kommer nok av Erlang. Måten å sjekke om noe er tekst på i Elixir er å sjekke om det er en binary.

Denne funksjonen "in action":

```elixir 
secrets_masker("test", "foo") # {"test", "foo"}
secrets_masker("testkey", "foo") # {"testkey", "f*****"}
```

Når den treffer en secret, så maskerer den altså alt bortsett fra første bokstav. Dette kan være nyttig for å identifisere at riktig secret er konfigurert.

Så til funksjonen som gjør det tunge løftet:

 ```elixir
  def deep_transform(data) do
    cond do
      is_tuple(data) && tuple_size(data) == 2 ->
        {k, v} = data

        if is_binary(v) do
          {k, secrets_masker(k, v)}
        else
          {k, deep_transform(v)}
        end

      is_struct(data) && data.__struct__ in [Regex, DateTime, NaiveDateTime, Time] ->
        data

      is_map(data) || is_struct(data) ->
        Enum.reduce(data, %{}, fn {k, v}, acc ->
          if is_binary(v) do
            Map.put(acc, k, secrets_masker(k, v))
          else
            Map.put(acc, k, deep_transform(v))
          end
        end)

      is_list(data) ->
        Enum.map(data, fn value -> deep_transform(value) end)

      true ->
        data
    end
  end
```

deep_transform går altså rekursivt igjennom hele config mappet og maskerer alle secrets den finner. Ganske kraftig vil jeg si!

Til slutt api'ets ene public funksjon. Den som gjør den ene side-effekten i all denne koden, logger ut config med maskerte secrets.


 ```elixir
  def log_all_applicaion_config(my_app) do
      conf =
        ([{my_app, "Top level"}] ++ Application.started_applications())
        |> Enum.map(&elem(&1, 0))
        |> Enum.map(fn app -> {app, Application.get_all_env(app)} end)
        |> Enum.reject(fn {_app, conf} -> Enum.empty?(conf) end)
        |> Enum.map(fn {app, v} -> {app, deep_transform(v)} end)
        |> inspect(pretty: true)

      Logger.info("My configuration is #{conf}")
    end
  end
```


## Hvordan en Elixir/Erlang app er bygget opp.

På en av de første linjene over henter altså ut `Application.started_applications()`, hva betyr det da?

En kan tenke på BEAM (Erlang VMen) som et OS i seg selv. Systemet ditt består av din app og en rekke andre apper. Disse andre appene, er gjerne biblioteker, som kan ha egen config og som kan definere sine egne prosesser. Om du lurer på hva en prosess er, så jeg skrev litt om det i [Elixir: Prosesser og meldinger](/blogg/2020-03-message-passing-beam/)

Det er altså vanlig at hver app har sin config, og når vi starter opp, så vil det jo være hendig å logge all config for hele systemet, alle appene, ikke bare din egen. 

Logging skjer altså i oppstart av din app, den er da ikke "started". Så for å få med den, legger vi den først til i lista:
`[{my_app, "Top level"}] ++ Application.started_applications()`.


## Resultatet?

Jeg har jo da knapt nevnt Phoenix. Men dette er altså defacto web rammeverk i Elixir. Og om en starter helt tomt prosjekt, så genereres en god del config for deg. Om du følger [hello world instruksjonene](https://hexdocs.pm/phoenix/up_and_running.html), og du starter appen ser du følgende:

```
Erlang/OTP 23 [erts-11.1] [source] [64-bit] [smp:8:8] [ds:8:8:10] [async-threads:1] [hipe] [dtrace]

[info] Running HelloWeb.Endpoint with cowboy 2.8.0 at 0.0.0.0:4000 (http)
[info] Access HelloWeb.Endpoint at http://localhost:4000
Interactive Elixir (1.10.4) - press Ctrl+C to exit (type h() ENTER for help)
iex(1)>
webpack is watching the files…

[hardsource:a1b34fc1] Using 1 MB of disk space.
[hardsource:a1b34fc1] Tracking node dependencies with: package-lock.json.
[hardsource:a1b34fc1] Reading from cache a1b34fc1...
Hash: ab15e3ac13a137397d57
Version: webpack 4.41.5
Time: 129ms
Built at: 09/27/2020 12:19:51 PM
                Asset       Size  Chunks             Chunk Names
       ../css/app.css   10.7 KiB     app  [emitted]  app
       ../favicon.ico   1.23 KiB          [emitted]
../images/phoenix.png   13.6 KiB          [emitted]
        ../robots.txt  202 bytes          [emitted]
               app.js   13.5 KiB     app  [emitted]  app
Entrypoint app = ../css/app.css app.js
[0] multi ./js/app.js 28 bytes {app} [built]
    + 5 hidden modules
```

Altså ingen informasjon om config. Koden beskrevet i denne posten kan [lastes ned i sin helhet her](https://gist.github.com/stoyle/a62a6511f9ebe18564119dff963ebebf), 
og ved da å putte en kodelinje i din Application.start `ConfigLogger.log_all_applicaion_config(:hello)`
så får du altså med følgende:  

```
Erlang/OTP 23 [erts-11.1] [source] [64-bit] [smp:8:8] [ds:8:8:10] [async-threads:1] [hipe] [dtrace]

[info] My configuration is [
  hello: [
    {Hello.Repo,
     [
       username: {:username, "postgres"},
       password: {:password, "p*****"},
       database: {:database, "hello_dev"},
       hostname: {:hostname, "localhost"},
       show_sensitive_data_on_connection_error: true,
       pool_size: 10
     ]},
    {:ecto_repos, [Hello.Repo]},
    {HelloWeb.Endpoint,
     [
       url: [host: {:host, "localhost"}],
       secret_key_base: {:secret_key_base, "/*****"},
       render_errors: [
         view: HelloWeb.ErrorView,
         accepts: ["html", "json"],
         layout: false
       ],
       pubsub_server: Hello.PubSub,
       live_view: [signing_salt: {:signing_salt, "q*****"}],
       http: [port: 4000],
       debug_errors: true,
...
```

Litt for mange linjer her til å vise alt, det er _mye_ config i en phoenix app. Nå har du full kontroll på hvordan systemet ditt er konfigurert.

## Bonus-logging

Håper du er allerede er overbevist om at det å logge config i oppstart er en god ide. Men, er det andre ting som er nyttig å logge også mon tro?

#### Git SHA

Det å vite hvilken versjon av systemet som logger er alltid nyttig. Så hvorfor ikke bare logge Git short SHA også? Gjør det i det minste i oppstart, men legg det gjerne på alle loggmeldinger.

Det er sjeldent vi kan følge med på feil når de skjer. Så det å ha Git SHA rett i loggmeldingen, vil gjøre det helt klart hvilken versjon av systemet feilen kom fra.

#### PID

PID i Erlang betyr en veldig spesiell type, men her snakker jeg om OS prosessens PID. Dette er nyttig å logge lokalt, da du lett kan identifisere prosessen i forskjellige kontekster. Har du PID, kan du enklere drepe prosessen, teste graceful shutdown med SIGTERM, eller finne ut om det er prosessen din som sluker CPU.

Dette er også nyttig i produksjon, spesielt om du kjører i docker. Om du vil at graceful shutdown skal fungere _må_ prosessen din kjøre på PID 1. Det er dessverre lett å gjøre dette feil i en Dockerfile.


I oppstarten av vår Elixir/Phoenix app, logger vi config med maskerte secrets, og PID og Git SHA.

```elixir
Logger.info("#{:amoi} has started successfully on pid #{:os.getpid()} git-sha is #{git_sha}")
ConfigLogger.log_all_applicaion_config(:amoi)
```

Hva logger du i oppstart av din app?
