:title Lag ditt eget rammeverk
:author magnar
:tech [:arkitektur :design]
:published 2021-04-21

:blurb

Ja, det er en oppfordring. Jeg mener for guds skyld ikke at du skal lage et nytt
rammeverk som andre kan bruke, men lag et til deg selv. Sett opp et stilas som
passer til det du bygger.

Men ikke med en gang.

:body

Ja, det er en oppfordring. Jeg mener for guds skyld ikke at du skal lage et nytt
rammeverk som andre kan bruke, men lag et til deg selv. Sett opp et stilas som
passer til det du bygger.

Men ikke med en gang.

## Du trenger det ikke

> "A framework is a product with the business logic removed, but all of the assumptions left in."
> -- [tef](https://programmingisterrible.com/post/65781074112/devils-dictionary-of-programming)

Det er overraskende hvor lite mikkmakk man trenger for å lage software. Enda mer
overraskende er hvor mye mikkmakk som ofte blir dratt med i oppstart av et
prosjekt.

Jeg har vært med på mer enn ett "første arkitekturmøte" som koker ned til
"hvilken konstellasjon av rammeverk skal vi bruke?" Nei! Det der er ikke
arkitektur. Du trenger det ikke. Stopp.

## Don't call us, we'll call you

Dra gjerne inn biblioteker -- nyttig kode som du kaller på eget initiativ -- men
styr unna rammeverk. Du vet, de store maskineriene med antagelser som du putter
koden din inn i. Som kaller koden din på et eller annet tidspunkt, ikke bestemt
av deg.

Du vil ikke tro hvor langt man kommer med en main-metode.

Det er noe veldig befriende med en trestruktur av kode hvor du *kan se* den
øverste noden - inngangsfunksjonen som får alt annet til å svinge. Det er en
ganske annen opplevelse enn å ha løsrevne løvnoder av kode som et rammeverk
kaller etter innfallsmetoden.

Begynn å lage features på denne måten. Helt fristilt fra rammeverk. Bare god
gammeldags kode som gjør greier. Funksjoner som kaller andre funksjoner. Du skal
se at du kommer langt.

## Det er godt helt til det er vondt

På et tidspunkt så vokser kanskje applikasjonen din seg så stor at dette
happy-go-lucky-opplegget begynner å knirke. Det gnisser litt. Man får noen
skrubbsår.

Dette er et vakkert øyeblikk.

Det er nå du får lov til å lage ditt eget rammeverk.

Og det er så deilig. For du vet hva du trenger! Du har eksempler! Mange helt
konkrete features du ønsker deg! Alt det du manglet i det første
arkitekturmøtet. Halleluja!

Det er en ganske artig oppgave å løse, men enda bedre: Du har fortsatt full
kontroll. Du kan bygge ut rammeverket ved behov. Du trenger ikke ta hensyn *over
hodet* til andres behov og ønsker. Alle assumptions er dine egne.

Det er ganske fint.

*PS!*

*Jeg lurte på om jeg skulle legge ved noen eksempler på slik stilaskode som jeg
selv har hatt brukt for opp gjennom årene. Men jeg vet jo ikke hva du trenger.
Det er jo hele poenget! Du må bygge ditt eget stilas, etter dine egne mål. Etter
prosjektets behov. Det er da det blir bra. Ikke sant?*
