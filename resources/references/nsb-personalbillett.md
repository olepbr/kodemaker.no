--------------------------------------------------------------------------------
:page-title NSB Personalbillett
:type reference
:logo /logos/nsb.png
:company NSB
:img /references/bengt-lyng.jpg
:name Bengt Lyng
:phone +47 924 33 836
:title Avdelingssjef, NSB Fellestjenester IT
:techs [:tomcat :git :maven :wicket :javascript :spring :java :bootstrap :hibernate :mssql]
:quote Kodemaker har bidratt med utallige råd og idéer
:priority z
:body

Kodemaker leverte ny teknisk løsning på kortere tid enn vi
forventet, og vi fikk derfor også utviklet vesentlig mer enn vi
først antok. Kodemaker løste oppgaven med minimal administrasjon. De
er kunnskapsrike og løsningsorienterte fagfolk, og ikke minst var de
flinke til å involvere og diskutere løsningene underveis med oss som
oppdragsgiver.

Anbefales sterkt!

--------------------------------------------------------------------------------
:type about
:title Administrering av personalbilletter
:body

Funksjonalitet i en 10 år gammel sentral applikasjon måtte løftes til tidsriktig
teknisk nivå, og samtidig utvikles med ny tiltrengt funksjonalitet. Kodemaker
ble valgt til å løse oppgaven i konkurranse med andre rammeavtaleleverandører.

Personalbillett er en applikasjon som brukes internt av administratorer i NSB.
De ordner rabatterte billetter til 30 000 ansatte i NSB og assosierte selskaper
som Nettbuss, Jernbaneverket, Flytoget, og ROM Eiendom.

--------------------------------------------------------------------------------
:type reference-meta
:title NSB Personalbillett
:body

En ny internapplikasjon for personalbilletter ble bygget opp av to Kodemakere
i team. Utviklingen foregikk hos NSB over en 8 måneders periode.

:team-size 2
:factoid-1 2 Kodemakere
:factoid-2 1200 timer / 08.2013-03.2014

--------------------------------------------------------------------------------
:type illustrated-column
:title Godt kjente teknologier
:body

Applikasjonen ble utviklet på nytt fra grunnen av. Backend var Java kjørende på
Jetty og [Tomcat](/tomcat/), med [Spring](/spring/), [Hibernate](/hibernate/),
Liquibase og [MS SQL Server](/mssql/). Frontenden ble laget ved hjelp av
[Wicket](/wicket/), [JavaScript](/javascript/) og [Twitter Bootstrap](/bootstrap/).

--------------------------------------------------------------------------------
:type grid
:content

/tomcat/                           /photos/tech/tomcat.svg
/git/                              /photos/tech/git.svg
/maven/                            /photos/tech/maven.svg 2x
/wicket/                           /photos/tech/wicket.svg
/javascript/                       /photos/tech/js.svg
/spring/                           /photos/tech/spring.png 2x
/java/                             /photos/tech/java.svg
/bootstrap/                        /photos/tech/bootstrap.svg
/hibernate/                        /photos/tech/hibernate.svg
/mssql/                            /photos/tech/mssql.png

--------------------------------------------------------------------------------
:type illustrated-column
:body

I tillegg til å gjenskape den gamle funksjonaliteten i en ny og mer
brukervennlig drakt, så utviklet vi en del ny funksjonalitet.

Den gamle applikasjonen var en stand-alone applikasjon, mens den nye er bedre
integrert med et databarehus som fungerer som datakilde for personaldata, med
MSAD/LDAP for pålogging, og ikke minst med det sentrale LISA billettsystemet
som er NSBs sentrale billettsystem.

--------------------------------------------------------------------------------
:type participants
:title Kodemakere hos NSB
:content

per

Per startet alene på prosjektet. I samråd med produkteier etablerte han
skjelettet for den nye applikasjonen. Han satt opp et effektivt og
automatiseringsvennlig utviklingsmiljø. Han implementerte en editor for
forretningsregler, generering av billettrettigheter, og batchoverføring av
billettrettigheter NSB’s sentrale billettsystem.

alf-kristian

Alf Kristian jobbet mest med hovedskjermbildene til systemet, det vil si søk og
vedlikehold av personalbiletter. Fokuset hans var å gjøre disse skjermbildene så
brukervennlige som mulig, og hadde derfor daglig kontakt med brukerne av
systemet.

--------------------------------------------------------------------------------
:type illustrated-column
:title Minimalistisk og pragmatisk prosess
:body

Vi jobbet i en kontinuerlig flyt - ingen faste møter eller seremonier. Ting ble
avklart med oppdragsgiver og brukere ved behov. Vi brukte en virtuell lappetavle
i Trello. Funksjonell spesifikasjon og arkitektur dokumenterte vi i Confluence.

Allerede etter et par dager var første skjelett av applikasjonen klar for
visning til kunden. Etterhvert la vi ut en ny versjon av applikasjonen på
testserver hver uke.

--------------------------------------------------------------------------------
