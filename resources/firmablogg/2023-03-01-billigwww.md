:title 25 kroner
:author finn
:published 2023-03-01
:tech [clojurescript platformio]

:blurb
Din egen webserver på splitter ny hardware til 25 kroner.

:body

Du får ikke mye til 25 kroner. Men du kan få din egen webserver på en bitte liten microkontroller.

For vanlig web-visningen er det nettleseren som gjør de tunge løftene, og en web-server som skal servere statisk web til litt personlig bruk - kan egentlig da være en datakraft-pingle. Den skal jo bare gi filer.

La oss kombinere to verdner, et høynivå og moderne webprosjekt med ClojureScript - med lavere nivå på en microkontroller. Hvorfor ikke...? Litt C++ er ikke noe å være redd for og jeg vedder 25 kroner på at du kommer til å forstå det aller meste dersom du har vært borti ett eller annet c-like språk.

Dette er et glimrende utgangspunkt til å bruke å web som interface videre hvis du ønsker å lese sensorer, styre motorer, kommunisere med radio eller bluetooth - for å nevne noe du kan gjøre med microkontrollere i arduino-verden. Og hvis du kun vil ha en webserver i lomma, så er det fint også, og det er dette vi skal gjøre her.

## Aller først: bevis på at jeg ikke lyger om 25 kroner
<img src="/images/blogg/2023-03-01-billigwww/alixpress.png" width="550" style="margin: 0 10px 10px 0;">
<img src="/images/blogg/2023-03-01-billigwww/min.jpg" width="450" style="margin: 0 10px 10px 0;">

Jeg bruker her en D1 mini ESP8266.

## platformio

Først må vi ha [platformio](https://platformio.org/) installert. Det er enkelt å installere fra den offentlige dokumentasjonen deres. Du kan bruke en hvilken som helst arduino-kompatibel microkontroller som platformio støtter. Platformio støtter [det meste](https://docs.platformio.org/en/latest/boards/index.html).

La oss lage et nytt prosjekt. 

```shell
$ mkdir pingla
$ cd pingla 
$ pio project init --board d1_mini --project-option="platform=espressif8266" 
```

### Nå har vi fått et bittelite tomt prosjekt
```s
$ tree
.
├── include
│   └── README
├── lib
│   └── README
├── platformio.ini
├── src
└── test
    └── README

4 directories, 4 files
```

Hvis du har installert VSCode og platformio, kan du åpne dette prosjektet. Vi fortsetter uansett med rein kommandolinje her.


## ClojureScript
**TLDR; vi lager en <em>index.html</em>, kompilerer en <em>main.js</em> fra ClojureScript.**

Vi trenger altså to kataloger, en for ClojureScript-prosjektet; **cljs/**, og en target-katalog som skal opp til dingsen vår; **data/www/**.


**cljs/deps.edn:**
```clojure
{:deps {org.clojure/clojurescript {:mvn/version "1.11.54"}}}
```


**cljs/resources/public/index.html:**
```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <script src="main.js"></script>
    </head>
    <body>
        <script>
            pingla.main.start()
        </script>
    </body>
</html>
```

**cljs/pingla/main.cljs**:
```clojure
(ns pingla.main)

(defn ^:export start []
  (js/alert "Hei fra cljs!"))
```


ClojureScript lager vi nå kun en popup med js/alert, målet er bare å dette til å snurre. Vi bruker en prosjektstruktur som gjør dette enkelt å innføre Figwheel i etterkant.
### Makefile
Merk at Makefile er på root i prosjektet, så for å kompilere ClojureScript så må vi gjøre litt directory -akrobatikk. 

```makefile

build-cljs:
	rm -Rf data/www/*
	cd cljs; clj -M -m cljs.main --optimizations advanced -c pingla.main; cd .. ;
	mv cljs/out/main.js data/www/
	cp cljs/resources/public/index.html data/www/

clean:
    rm -Rf cljs/out
    rm data/www
    
```


## C++
TLDR; Vi lager et eget wifi-nett og starter en webserver på 192.168.4.1 som server de to filene i data/www/

### Prosjektavhengighet i platformio
Noen har tatt bryet med å skrive en web-server vi kan bruke, så legg til dette i platformio.ini 

```ini
lib_deps = 
    ottowinter/ESPAsyncWebServer-esphome@^3.0.0
    ESP8266WiFi
 ```

### boilerplate for setup() og loop()

Dette er standard hello world for Arduino.

**src/main.cpp**
```c++
#include <Arduino.h>
void setup() {
    Serial.begin(9600); 
    Serial.println("Setup");
}
void loop() {
    Serial.println("Loop");
}
```

Vi må kompilere (build), laste opp executable (upload) og koble oss til (monitor) for å lese output, så trenger vi disse kommandoene i Makefile

```makefile
build:
	pio run -e d1_mini

upload:
	pio run -e d1_mini --target upload

monitor:
	pio device monitor

bum: build upload monitor
```

Jeg liker å ha et kort alias som kjører alle 3, og står du igjen med at Loop spammer ned skjermen din

```shell
$ make bum
[...]
Loop
Loop
Loop
Loop
Loop
Loop
Loop
Loop
Loop
Loop
Loop
Loop
Loop
Loop
Loop
Loop
Loop
Loop
```

### http server
Nå skal vi lime alt sammen og lage wifi, websever og levere filene våre fra frontend-prosjektet. 

Det er dårlig praksis å fylle opp **main.cpp** med masse greier, så vi kaller bare setup() i WEB fra main.cpp og gjør alt arbeidet i web.cpp.

**main.cpp:**
```cpp
#include <Arduino.h>
#include "web.h"

void setup() {
    Serial.begin(9600); 
    Serial.println("Setup");
    WEB::setup();
}

void loop() {}
```

**src/web.h:** 

```cpp
#ifndef web_h
#define web_h
namespace WEB {
    void setup();
}
#endif
```

**src/web.cpp:**

```cpp
#include <Arduino.h>
#ifdef ESP32
#include <WiFi.h>
#include <AsyncTCP.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#include <ESPAsyncTCP.h>
#endif
#include "ESPAsyncWebServer.h"
#include "LittleFS.h"

namespace WEB {
    AsyncWebServer server(80);

    const char* ssid = "pingla";
    const char* password = "pingla";
    IPAddress local_IP(192,168,4,1);
    IPAddress gateway(192,168,4,254);
    IPAddress subnet(255,255,255,0);

    void notFound(AsyncWebServerRequest *request) {
        request->send(404, "text/plain", "Not found");
    }

    void setup() {
        Serial.print("Setting soft-AP configuration ... ");
        Serial.println(WiFi.softAPConfig(local_IP, gateway, subnet) ? "Ready" : "Failed!");
        Serial.print("Setting soft-AP ... ");
        Serial.println(WiFi.softAP(ssid) ? "Ready" : "Failed!");

        Serial.print("Soft-AP IP address = ");
        Serial.println(WiFi.softAPIP());
        LittleFS.begin();

        server.on("/", HTTP_GET, [](AsyncWebServerRequest *request){
            File file = LittleFS.open("/www/index.html", "r");
            request->send(200, "text/html;charset=utf-8", file.readString());
            file.close();
        });

        server.on("/main.js", HTTP_GET, [](AsyncWebServerRequest *request){
            File file = LittleFS.open("/www/main.js", "r");
            request->send(200, "text/javascript; charset=utf-8", file.readString());
            file.close();
        });
        server.onNotFound(notFound);
        server.begin();
        Serial.print("Pingla er klar");
    }
}
```

### LittleFS
LittleFS er filsystemet på dingsen vår, og må uploades separat fra executable-en. Det fine med dette er at vi ikke trenger å røre programmet hvis det kun er endringer på web-filene vice versa.

```makefile
upload-files:
	pio run -e d1_mini -t buildfs 
	pio run -e d1_mini -t uploadfs 
```

```shell
$ make upload-files
[...]
Writing at 0x00028000... (73 %)
Writing at 0x0002c000... (80 %)
Writing at 0x00030000... (86 %)
Writing at 0x00034000... (93 %)
Writing at 0x00038000... (100 %)
Wrote 328208 bytes (238682 compressed) at 0x00000000 in 3.1 seconds (effective 837.4 kbit/s)...
Hash of data verified.

Leaving...
Hard resetting via RTS pin...
========================================================= [SUCCESS] Took 5.49 seconds =========================================================
```

```shell
$ make bum
[...]
Setting soft-AP ... Ready
Soft-AP IP address = 192.168.4.1
Pingla er klar

```

<img src="/images/blogg/2023-03-01-billigwww/screenshot.jpg" width="600" style="margin: 0 10px 10px 0;">

## Arbeidsflyten videre
Nå er vi ferdige med fundamentet, og det fungerer som du ser på screenshotten over. 

I tillegg er det kjekt med en liten ekstra kort kommando som bygger cljs og uploader til mikrokontrolleren i en og samme operasjon. **$make cu**
```makefile
# [...]

upload-files:
	pio run --target buildfs --environment d1_mini 
	pio run --target uploadfs --environment d1_mini

bum: build upload monitor

cu: build-cljs upload-files
```
Nå har vi **$make bum** som bygger og laster opp endringer på back-end, og **$make cu** som bygger og laster opp endringer i front-end. Korte kommandoer gjør hverdagen bedre.

Prosjektet finner du i helhet [på github](https://github.com/finnjohnsen/pingla).
