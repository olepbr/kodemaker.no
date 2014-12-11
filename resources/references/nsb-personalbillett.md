--------------------------------------------------------------------------------
:page-title NSB Personalbillett
:type reference
:logo /logos/nsb.png
:img /references/bengt-lyng.jpg
:name Bengt Lyng
:phone +47 924 33 836
:title Avdelingssjef, NSB Fellestjenester IT
:body

Kodemaker leverte ny teknisk løsning på kortere tid enn vi
forventet, og vi fikk derfor også utviklet vesentlig mer enn vi
først antok. Kodemaker løste oppgaven med minimal administrasjon. De
er kunnskapsrike og løsningsorienterte fagfolk, og ikke minst var de
flinke til å involvere og diskutere løsningene underveis med oss som
oppdragsgiver.

Anbefales sterkt!

--------------------------------------------------------------------------------
:type illustrated-column
:title Administrering av personalbilletter
:body

Funksjonalitet i en 10 år gammel sentral applikasjon måtte løftes til tidsriktig
teknisk nivå, og samtidig utvikles med ny tiltrengt funksjonalitet. Kodemaker
ble valgt til å løse oppgaven i konkurranse med andre rammeavtaleleverandører.

--------------------------------------------------------------------------------
:type reference-meta
:title NSB Personalbillett
:body

En ny internapplikasjon for personalbilletter ble bygget opp av to Kodemakere
i team. Personalsjef hos NSB var kundekontakt.

:team-size 2
:factoid-1 2 Kodemakere
:factoid-2 1200 timer / 08.2013-03.2014

--------------------------------------------------------------------------------
:type illustrated-column
:body

Personalbillett er en applikasjon som brukes internt av administratorer i NSB.
De ordner rabatterte billetter til 30 000 ansatte i NSB og assosierte selskaper
som Nettbuss, Jernbaneverket, Flytoget, og ROM Eiendom.

--------------------------------------------------------------------------------
:type illustrated-column
:title Godt kjente teknologier
:body

Applikasjonen ble utviklet på nytt fra grunnen av. Backend var Java kjørende på
Jetty og Tomcat, med Spring, Hibernate, Liquibase og MS SQL Server. Frontenden
ble laget ved hjelpa av Wicket, JavaScript og Twitter Bootstrap.

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

alf-kristian

Nullam eu ante vel est convallis dignissim. Fusce suscipit, wisi nec facilisis
facilisis, est dui fermentum leo, quis tempor ligula erat quis odio. Nunc porta
vulputate tellus. Nunc rutrum turpis sed pede. Sed bibendum. Aliquam posuere.

per

Pellentesque condimentum, magna ut suscipit hendrerit, ipsum augue ornare nulla,
non luctus diam neque sit amet urna. Curabitur vulputate vestibulum lorem. Fusce
sagittis, libero non molestie mollis, magna orci ultrices dolor, at vulputate
neque nulla lacinia eros.

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
