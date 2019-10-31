--------------------------------------------------------------------------------
:page-title BN Bolig
:type reference
:img /references/rune-strandli.jpg
:logo /logos/bnbolig.png
:name Rune Strandli
:phone +47 922 97 460
:title Chief Digital Officer, BN Bolig
:techs [:javascript :clojure :responsive-design :ansible :git]
:body

Kodemaker deltok med fullstackutviklere i et kryssfunksjonellt team hvor 
de utviklet integrasjonsplattform og digital kundefront til BN Bolig. 
Kodemaker er blant de faglig mest kompetente i bransjen, men det som kanskje 
var aller viktigst for oss var at de med sin erfaring, engasjement og smidighet 
dekket et stort spenn av tekniske behov/roller og at de på eget initiativ tok 
tak i ideer og oppgaver.

Vi var opptatt av å bygge et lite, agilt og selvorganisert team. Kodemaker har 
for oss bevist at et lite team med håndplukkede, høykompetente personer kan 
levere raskere og bedre.



--------------------------------------------------------------------------------
:type illustrated-column
:title Lansering av ny innovativ eiendomsmegler
:body

BN Bolig er en eiendomsmegler som ble startet på 3 måneder med ny, innovativ digital, kundefront. Dette for å kunne levere bedre tjenester til kjøpere, selgere og interessenter i boligsalgsprosesser.

Kodemaker, i samarbeid med Eggs Design, designet, utviklet og driftet løsningen i lanseringsperioden og de første månedene etterpå. 

Eggs og Kodemaker var med på alle delene av selskapets oppstart. Herunder det å jobbe med visjon, mål, profil, reklame-strategier med mere. BN Bolig ønsket å bli utfordret på eiendomsmeglerbransjens “satte sannheter”, som var en ekstra stor utfordring for prosjektets oppstart. I begynnelsen ble det lansert mange hårete mål.

Det å lansere på så kort tid krevde sterkt fokus fra alle designerne og programmerere. De første 3 månedene var hektiske. Eggs stilte med designere på alle nivåer, og folk som var gode på CSS. Kodemaker stod for programmeringen, frontend, backend, samt oppsett av servere og drifting. Selskapene samarbeidet veldig godt, og vi klarte sammen målsettingen om å “gå live” 31. mars.


--------------------------------------------------------------------------------
:type reference-meta
:title BN Bolig
:body

Tre Kodemakere i team leverte i starten av 2017 en digital kundefront på årets 3 første måneder. Dette for å kunne levere bedre tjenester til kjøpere, selgere og interessenter i boligsalgsprosesser. Prosjektet ferdigstilt og overlevert til kunden med to Kodemakere i sluttfasen.

:team-size 3
:factoid-1 3 (2) Kodemakere
:factoid-2 01.2017-09.2017

--------------------------------------------------------------------------------
:type illustrated-column
:body

bnbolig.no består av:

- Presentasjon av BN Bolig
- Automatisk prisestimat av vilkårlige boliger i Oslo
- Public visning av budrunder
- Innloggede sider for selger, som gir stor innsikt i salgsprosessen.

> "Her hadde vi dårlig tid, så det var viktig at vi rigget oss til slik at vi kjapt 
kunne levere. Eneste måten å få dette i land på var å ta full kontroll, og ha fullt 
ansvar. Devops all the way!"
> 
> -- <cite>Alf Kristian</cite>
--------------------------------------------------------------------------------

:type illustrated-column
:title Clojure var essensielt for backenden
:illustration /illustrations/references/bnbolig-front.png
:illustration-url http://bnbolig.no/
:body

BN Bolig hadde kjøpt et meglersystem hvor vi skulle integrere tungt med dette 
systemets API’er. Vi lagde en backend som sørget for sikker tilgang til dette 
API’et, i tillegg til flere andre API’er.

Da prosjektet i stor grad handlet om integrasjon med andre systemer, var det
viktig å ha en kodebase for backend, der det var lett å eksperimentere.

Clojure lot oss teste og ekperiementere med disse integrasjonene, uten forsinkelser.
Repl basert utvikling var helt essensielt for å kjapt få kontroll på hvordan 
integrasjonene fungerte. Backenden ble utstyrt med en Aleph/Netty async-server, 
backet med en PostgreSQL database. Webserver var nginx, for caching ble varnish 
benyttet, HAProxy for lastbalansering og blue-green deployment, og TLS sertifikater 
ble laget med Letsencrypt.

Løsningen ble opprinnelig driftet i Digital Ocean, der vi satt opp og driftet Linux 
servere. All provisjonering ble gjort via Ansible. Frontenden ble laget i 
JavaScript/ES6 og React.js, bygget med webpack og deployet med Shipit.js.
Vi la opp til å jobbe så smidig som mulig, med deployment til prod så ofte som 
mulig. Altså såkalt continuous delivery, både for frontend, backend og 
server-oppsett.

--------------------------------------------------------------------------------
:type grid
:content

/javascript/                       /photos/tech/js.svg
/clojure/                          /photos/tech/clojure.svg
/responsive-design/                /photos/tech/rwd.jpg 2x
/ansible/                          /photos/tech/ansible-red.svg
/git/                              /photos/tech/git-gray.svg

--------------------------------------------------------------------------------

:type participants
:title Kodemakere hos BN Bolig
:content

alf-kristian

Alf Kristian hadde opprinnelig hovedansvar for backend og server-rigg, men fokuset 
ble etterhvert byttet om til mer frontend utvikling. Før dette så ikke Alf Kristian 
på seg selv som noen "frontend-fyr", men dette var så moro at han gladelig jobber 
med frontend i dag. 

eivind

Eivind var fullstack-utvikler med fokus på frontend. Han jobbet mye med integrasjon 
mot meglersystem samt adressesøk og prisestimat. I tillegg ble det mye flikking på 
CSS og JavaScript for å få design og integrasjon til å henge perfekt sammen :)

trygve

Trygve kom inn som et friskt pust i vår frontend-utvikling, og uten ham hadde vi 
neppe klarte å levere etter den ambisiøse planen i tide. Hans tidligere og ferske 
erfaring med React og Redux, ga oss en flying start. Alltid plass i prosjekter for
en rolig, erfaren og produktiv ringrev.

--------------------------------------------------------------------------------
