:title Respect my https_proxy!
:author nils
:tech [:bash]
:published 2021-09-22

:blurb

Hva gjør du når du sitter bak en proxy og verktøyet ditt ikke spiller på lag med proxyer?

:body

Noen kunder jeg har jobbet for har av ulike grunner en proxy som all internett-trafikk må gå gjennom. 

Enn så irriterende det er så er det som regel bare å finne de riktige innstillingene i IDEA, browser og operativsystem, så er saken biff.

Som utvikler som er avhengig av shellet ditt så setter du miljøvariablene `http_proxy` og `https_proxy`, evt med uppercase også for å være sikker. 
Dette er variabler som de aller fleste cli-verktøy respekterer. 

Men hva gjør du når cli-verktøyet ditt simpelthen ikke respekterer noen av disse?


## Finn problemet

Først må du jo verifisere at det faktisk er proxyen som er problemet. 

Som regel er symptomet at verktøyet bruker lang tid og til slutt kaster opp en feilmelding om noe nettverksgreier. I mitt tilfelle er det et `xcodebuild` som gir meg problemer:

```
$ make ios-testflight
...

2020-11-12 13:09:42.467 xcodebuild[79720:562922] Progress 100 %: Completed App Store operation.
error: exportArchive: App Store Connect Operation Error. An error occurred uploading to the App Store.
Error Domain=WorkerErrorDomain Code=-10000 "error: App Store Connect Operation Error. An error occurred uploading to the App Store." UserInfo={NSLocalizedDescription=error: App Store Connect Operation Error. An error occurred uploading to the App Store.}
 EXPORT FAILED 
make: *** [ios-testflight] Error 70
```

Så `xcodebuild` har problemer. Nå starter jeg prosessen på nytt, og når det henger bruker jeg `netstat` til å finne ut hvor problemet ligger:

```
$ netstat -an|grep SYN

tcp4 0 0 10.0.16.193.61011 17.110.232.68.443 SYN_SENT
```

Hah. Så der er det noe, en prosess som har sendt en SYN request og ikke får svar.  Etter litt etterforskning så finner jeg at foreign-address, `17.110.232.68` er en ip-addresse som Apple eier. Jeg har funnet synderen. Nå må jeg finne den eksakte prosessen som henger. 

Local address er `10.0.16.193.61011`, bestående av ip og port. For å finne hvilken prosess som bruker port `61011` bruker jeg `lsof` og får meg en pen overraskelse:

``` 
$ lsof -i :61011
COMMAND ....
/Applications/Xcode.app/Contents/SharedFrameworks/ContentDeliveryServices.framework/Versions/A/itms/java/bin/java ... -[bunchofprops] -jar some.jar 
``` 
(output er redigert)

Hah. Så Xcodebuild bruker altså en bundlet versjon av __java__ for å laste opp appen min til appstore og programmet følger ikke konvensjonene for å respektere `https_proxy`!!! (Av alle versjoner av jdk har Apple valgt jdk 14.0.2 pr. i dag)

## Løsningen
Så skal jeg modifisere jarfila til å respektere `https_proxy`? Nææ, det høres litt slitsomt ut, dessuten har jeg ikke kildekoden til xcode-bibliotekene. Men siden macOS er unix har jeg en liten hack som kan funke likevel. 

`java` er en executable fil så jeg omdøper filen til `java.real` og lager en erstatnings-fil, en tekstfil jeg kaller `java`, som er et bash-script med en liten `chmod a+x`: 

```
!/bin/bash
/Applications/Xcode.app/Contents/SharedFrameworks/ContentDeliveryServices.framework/Versions/A/itms/java/bin/java.real -Dhttps.proxyHost=$https_proxy $*
```

Og se så. Ved å bruke `-Dhttps.proxy` parameteret til java tvinger jeg nå _alle_ programmer som blir started med denne jdken til å bruke proxyen min. 

Tilmed Apple må bite i støvet og respektere min `https_proxy`. 

Happy hacking!
