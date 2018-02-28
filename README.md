# kodemaker.no

Våre nye nettsider kommer til verden.

## Teste lokalt

Skaff [leiningen](https://github.com/technomancy/leiningen#leiningen) om du ikke
har den. Sats på versjon 2.3+. Hvis du har en gammel versjon under 2.0 så funker
det garantert ikke. På OSX kan du hente den med homebrew: `brew update && brew
install leiningen`

Du må også ha JDK 1.7. Sjekk med `java -version`, ellers
[last ned her](http://docs.oracle.com/javase/7/docs/webnotes/install/index.html).

Gå så til rota av prosjektet, og

```shell
lein ring server
```

Voila!

## Hvordan skal mine data se ut?

Du finner din personlige datafil i `resources/people/`. Slik ser den ut:

```clj
(def Person
  {:id ID
   :name [Str]
   :title Str
   :start-date Str
   :description Str ;; Skrives i tredjeperson, alt annet i førsteperson
   (optional-key :cv/description) Str ;; Hvis du ønsker en annen/lengre beskrivelse på CV-en
   (optional-key :administration?) Boolean
   (optional-key :quit?) Boolean

   :phone-number Str
   :email-address Str

   :presence {(optional-key :cv) Str ;; Kodemaker cv id
              (optional-key :twitter) Str ;; brukernavn
              (optional-key :linkedin) Path ;; path til din offentlige side
              (optional-key :stackoverflow) Path ;; path til din offentlige side
              (optional-key :github) Str ;; brukernavn
              (optional-key :coderwall) Str} ;; brukernavn

   (optional-key :tech) {(optional-key :using-at-work) [ID]
                         (optional-key :favorites-at-the-moment) [ID]
                         (optional-key :want-to-learn-more) [ID]}

   (optional-key :recommendations) [{:title Str ;; Samme som tittel på det du lenker til
                                     :blurb Str ;; Litt om hvorfor du anbefaler
                                     :link {:url URL :text Str} ;; lenketekst av typen "Se foredraget" og "Les artikkelen"
                                     :tech [ID]}]

   (optional-key :hobbies) [{:title Str
                             :description Str
                             :illustration Path
                             (optional-key :url) URL}]

   (optional-key :side-projects) [{:title Str
                                   :description Str
                                   :illustration Path
                                   (optional-key :link) {:url URL :text Str}
                                   (optional-key :tech) [ID]}]

   (optional-key :blog-posts) [{:url URL
                                :title Str
                                :blurb Str
                                (optional-key :tech) [ID]}]

   (optional-key :presentations) [{:title Str ;; foredrag som du selv har holdt
                                   (optional-key :id) ID ;; brukes til å generere URL for video-presentasjoner
                                   :blurb Str
                                   :tech [ID]
                                   (optional-key :event) Str ;; Konferansenavn etc
                                   (optional-key :date) Date ;; iso-8601 yyyy-mm-dd
                                   :urls {(optional-key :video) URL
                                          (optional-key :slides) URL
                                          (optional-key :source) URL} ;; må ha minst en av disse URLene
                                   (optional-key :direct-link?) Boolean}] ;; true hvis det ikke skal embeddes video på kodemaker-sidene

   (optional-key :upcoming) [{:title Str ;; Kommende kurs eller presentasjoner
                              :description Str
                              :url URL ;; Link til feks din abstract hos konferansen
                              (optional-key :call-to-action) {:url URL :text Str} ;; Bruk denne til "Meld deg på kurs" o.l.
                              :tech [ID]
                              :location {:title Str :url URL} ;; Eks {:title "JavaZone (Oslo)", :url "http://javazone.no"}
                              :date Date}] ;; iso-8601 yyyy-mm-dd

   (optional-key :appearances) [{:title Str ;; Mindre profilerte kurs/workshops/foredrag til CV-en
                                 :event Str
                                 :date Date ;; iso-8601 yyyy-mm-dd
                                 :tech [ID]}]

   (optional-key :screencasts) [{:title Str ;; screencasts du selv har laget
                                 :blurb Str
                                 :tech [ID]
                                 (optional-key :launch-date) Date;; iso-8601 yyyy-mm-dd
                                 :url URL}]

   (optional-key :open-source-projects) [{:url URL
                                          :name Str
                                          :description Str
                                          :tech [ID]}] ;; sortert under første tech

   (optional-key :open-source-contributions) [{:url URL
                                               :name Str
                                               :tech [ID]}] ;; sortert under første tech

   (optional-key :projects) [{:customer Str
                              (optional-key :cv/customer) Str
                              (optional-key :summary) Str
                              (optional-key :employer) ID
                              (optional-key :description) Str
                              (optional-key :cv/description) Str
                              (optional-key :exclude-from-profile?) Boolean
                              :years [Num] ;; årstallene du jobbet der, typ [2013 2014]. [2018 0] for å beskrive et pågående prosjekt
                              :tech [ID]}] ;; hvilke tech jobbet du med? viktigst først

   (optional-key :endorsements) [{:author Str ;; anbefalinger, gjerne fra linkedin
                                  :quote Str
                                  (optional-key :title) Str ;; tittel, firma
                                  (optional-key :photo) Path}]

   ;; For CV-er
   (optional-key :born) Num
   (optional-key :relationship-status) Str
   (optional-key :education-summary) Str
   (optional-key :experience-since) Num
   (optional-key :qualifications) [Str]
   (optional-key :innate-skills) [ID] ;; Techs du vil ha lista på CV-en men som du ikke har tatt deg
                                      ;; bryet å knytte til et prosjekt av noe slag

   (optional-key :education) [{:institution Str ;; Utdanning
                               :years [Num]
                               :subject Str}]

   (optional-key :languages) [{:language Str
                               :orally (enum "Grunnleggende" "God" "Meget god" "Flytende" "Morsmål")
                               :written (enum "Grunnleggende" "God" "Meget god" "Flytende" "Morsmål")}]

   (optional-key :certifications) [{:name Str
                                    :year Num}]})
```

Legge merke til at dette er kode som kjører når siden bygges opp, slik
at du bør få grei tilbakemelding om du tråkker på utsiden.

**NB!** Kjør opp siden og se hvordan det blir før du commiter. Da får
du kjørte all programmatisk validering av datastrukturen, sjekket at
alle bilde-URLer finnes, og sett med øynene dine at det ble som du
hadde tenkt.

Eksempel på utfylte data finner du i [Magnars profil](resources/people/magnar.edn).

#### Datatyper

- `ID` er et :keyword som er lowercase uten sære tegn.
- `Path` er en streng som starter med `/` uten sære tegn.
- `URL` er en streng som starter med `http://` eller https og forøvrig er en URL.
- `Date` en er streng med dato på formatet `yyyy-MM-dd`
- `Num` er et tall

## Laste opp bilder

Bildene ligger i `resources/public`.

- `/logos` Logo til referanser: .png med bredde 290px. Husk å bruke [smushit](http://smushit.com). Evt [tinypng](https://tinypng.com/)
- `/thumbs/faces` Ansikt til referansepersoner: .jpg, proporsjon 3/4, gjerne 210x280
- `/illustrations/hobbies/` Illustrasjoner til hobbyer: .jpg med bredde 420px.
- `/illustrations/side-projects/` Illustrasjoner til sideprosjekter: .jpg med bredde 420px.
- `/references/` Illustrasjoner til forside-referanser: .jpg med bredde 1280x488.
- `/photos/tech/` Illustrasjoner til tech: .jpg med bredde 580px.
- `/photos/people/<person>/half-figure.jpg` Kodemaker stående: .jpg 580x741
- `/photos/people/<person>/side-profile.jpg` Kodemaker sittende: .jpg 620x485
- `/photos/people/<person>/side-profile-cropped.jpg` Kodemaker med kappa hode: .jpg 580x453

Hvis du ikke har Photoshop eller lignende, så kan du skalere bilder på
http://scaleyourimage.com/.

## Hva med fagsider?

De ligger i `resources/tech`.

```clj

(def Tech
  {:id ID
   :name Str
   :description Str
   :type (enum :proglang
               :vcs
               :methodology
               :devtools
               :library
               :framework
               :server
               :database
               :devops
               :os
               :frontend
               :specification
               :tool)
   (optional-key :illustration) Str
   (optional-key :site) URL
   (optional-key :ad) {:heading Str
                       :blurb Str
                       :link-text Str}})
```

[Se eksempel på tech](resources/tech/javascript.edn).

### Annonser på fagsider

Du kan legge til en liten annonse for å ta kontakt med oss i venstrekolonnen på
fagsidene. For JavaScript ser det ut som dette:

```clj
:ad {:heading "Ønsker du rådgivning om JavaScript?"
     :blurb "Vi har mange dyktige konsulenter som brenner for
             JavaScript. Vi kurser utviklerne dine og hjelper deg å ta
             de riktige valgene."
     :link-text "Ta kontakt"}
```

## Frittstående sider

I resources/articles ligger det noen
[markdown](http://daringfireball.net/projects/markdown/syntax)-filer. Disse blir
hver til sin egen side, og får URL lik filnavnet. Filen
resources/articles/kolbjorn-er-sjef.md blir tilgjengelig som
http://kodemaker.no/kolbjorn-er-sjef/

Disse "artiklene" skal inneholde noe meta-data. Som et minimum bør du ha med
`:title` og `:body`, men du kan også ha med `:illustration` (bilde som skal
vises øverst i venstrekolonnen), `:lead` (øverste del av hovedkolonnen) og
`:aside` (venstrekolonnen). Et minimalt eksempel følger, for ytterligere
eksempler, se eksisterende filer i `resources/articles`.

```md
:title Min supre side
:illustration /photos/people/kolbjorn/side-profile-cropped.jpg

:aside

Viktig med litt kjøtt i venstrekolonna.

:lead

Denne siden er helt super, lover. Denne delen kan bestå av flere avsnitt om du
så ønsker, ingen begrensning. Det er heller ingen forskjell visuelt på denne
delen fra den etterfølgende body-delen.

:body

## Dette er en markdown-heading

Body er bra greier altså.
```

## Blogg

Mye av innholdet i den gamle bloggen er borte, men det betyr bare bedre plass
til nye, gode innlegg. Blogg-poster finnes i `resources/blog/`. Som "artikler" er
dette en samling markdown-filer med litt meta-data i. Formatet på blogg-poster
er enda enklere enn artiklene, og illustreres best gjennom et eksempel. Se
forøvrig eksisterende innlegg i `resources/blog` for flere eksempler.

```md
:title Kommende Kodemaker – Alf Kristian Støyle
:published 2013-06-28
:illustration /photos/blog/alf-kristian-stoyle.jpg

:body

Det er over et år siden vi ansatte noen sist, men den som venter på noe godt…

Velkommen til Kodemaker!
```

For blogg-poster er kun `:illustration` valgfritt. URL-en til bloggpostene
genereres fra filnavnet, og prefikses med blogg/. Altså blir
`resources/blog/mitt-innlegg.md` til http://kodemaker.no/blogg/mitt-innlegg/

## CV-er

Det er nå mulig å få CV-en sin generert fra dataene i person-profilen din.
Ettersom de nye CV-ene byr på litt mindre personlig formattering og layout, og
nok ikke blir HELT lik som den gamle for de fleste, er den nye CV-en opt-in. Du
opter inn i person-profilen din med:

```edn
 :use-new-cv? true
```

Se [Christians profil](resources/people/christian.edn) for referanse. For at
CV-en skal se bra ut trenger du å fylle ut noen flere datapunkter enn det som
tidligere var nødvendig. Det meste er valgfritt, men det er anbefalt å
ihvertfall få på plass:

- `:experience-since` - Året du startet å jobbe
- `:education-summary` - En one-liner om utdanningen din til "visittkortet" på
  toppen
- `:qualifications` - En liste med kvalifikasjoner
- `:education` - Utdanning, i mer detalj
- `:languages` - Språk du snakker/skriver

I tillegg kan du vurdere å berike CV-en din med følgende felter:

- `:appearances` - Foredrag/workshops som ikke allerede er nevnt under
  `:presentations`. Krever mindre detaljer, og vises kun på CV-en. Perfekt for
  feks internforedrag (hos oss eller andre) osv.
- `:innate-skills` - Techs du vil ha listet opp på CV-en, men som du ikke har
  knyttet til noe spesifikt prosjekt.
- `:cv/description` - En alternativ beskrivelse som brukes i stedet for
  `:description` for CV-en.

Helt til slutt er det også mulig å legge til disse to feltene, men det er
kanskje mulig å diskutere hvor relevante de er:

- `:born`
- `:relationship-status`

## Provisjonering

Vi bruker [Ansible](www.ansibleworks.com) for å sette opp serveren.
Hvis du sitter på OSX er det så enkelt som `brew install ansible`. Da
får du `1.4.3` eller nyere, noe du også trenger.

### Sette opp din egen server lokalt

Du kan bruke [Vagrant](http://www.vagrantup.com/) og
[VirtualBox](https://www.virtualbox.org/) for å sette opp en virtuell
blank CentOS server lokalt.

```sh
cd provisioning/devbox
vagrant plugin install vagrant-vbguest
vagrant up
echo "\n192.168.33.44 local.kodemaker.no" | sudo tee -a /etc/hosts
```

Det er mulig du får en `An error occurred during installation of
VirtualBox Guest Additions. Some functionality may not work as
intended.` ... det er ikke stress. Bare "Window System drivers" som
ikke blir installert.

Deretter må du sette passord for root. Sudo passord er `kodemaker`:

```sh
vagrant ssh
sudo passwd root
```

Logg ut igjen.

Legg til din public key i `provisioning/keys`, og føy den til listen
under `Setup authorized_keys for users who may act as deploy user`
tasken i `provisioning/bootstrap.yml`.

Gå så tilbake til `provisioning/` og:

```sh
ansible-playbook -i hosts-local.ini bootstrap.yml --user root --ask-pass
```

Svar med passordet du lagde til root.

Den kjører en god stund, og så kan du `ssh deploy@local.kodemaker.no`
og se deg omkring.

Fortsett så til [Sette opp kodemaker.no](#neste-sette-opp-kodemakerno).

### Provisjonere en server

Så, du har en fresk og fersk CentOS server som vil bli kodemaker.no.
Legg den til i `provisioning/hosts.ini` under `[new-servers]`.

Forhåpentligvis har du testet lokalt, og dermed ligger allerede din
public key i `provisioning/keys`.

Så gjenstår det bare å gå til `provisioning/` katalogen og inkantere:

```sh
ansible-playbook -i hosts.ini bootstrap.yml --user root --ask-pass
```

#### Øhh, det gikk ikke helt bra

Nei, du mangler kanskje `sshpass` lokalt hos deg? Det er bare en yum
eller apt unna. Eller hvis du er på OSX:

```sh
brew install https://raw.github.com/eugeneoden/homebrew/eca9de1/Library/Formula/sshpass.rb
```

### Neste: Sette opp kodemaker.no

Først av alt må du sette opp mail-utsendingen:

```sh
cp provisioning/files/mail-config-sample.js provisioning/files/mail-config.js
```

Åpne `provisioning/files/mail-config.js` og legg til den hemmelige
nøkkelen på `apiUser`. Hvis du ikke har noen, så er det greit på en
lokal maskin. Da bare funker det ikke å sende mail. ;-) Du kan finne
eksisterende verdi ved å logge inn på kodemaker.no og gjøre
`cat mail-config.js`.

Når du bootstrapper, så vil root-login og passord-login bli disablet.
Så når vi nå skal sette opp kodemaker no, så må du fleske til med en
annen inkantasjon:

```sh
ansible-playbook -i hosts.ini setup-kodemaker.yml --ask-become-pass
```

Nå er det altså ikke SSH-passordet som brukes lenger - den bruker din
private key - men du må oppgi sudo-passordet. Dersom du ikke har gjort
noen endringer, så er det fortsatt `kodemaker`. Men hvis dette er en
offentlig server, så lønner det seg nok å gjøre den endringen. Logg
inn som `deploy` og `passwd`.

#### Bygg siten

Første gang du bygger tar det lang tid. Det kan være hyggelig å se at
den holder på med noe.

```sh
ssh deploy@local.kodemaker.no
./build-site.sh
```

Og så kan du besøke http://local.kodemaker.no i nettleseren din og
meske deg i de nye sidene våre.

Når du vil oppgradere, kan du be serveren om å bygge en ny versjon av
siten:

```sh
curl local.kodemaker.no/site/build
```

Den henter da altså fra github. Om du vil teste lokale endringer er
det mye greiere å få til med `lein ring server`.

### SSL

www.kodemaker.no serveres nå med SSL og HSTS-header. Sertifikatet
provisjoneres fra Let's Encrypt med certbot, og fornyes i en cron-jobb
ukentlig. Ettersom Varnish ikke støtter TLS så går nå trafikken først
til nginx på port 443, så til Varnish på port 6081, deretter varnish på
port 8001.

nginx svarer også på port 80, og dette er kun for å serve ACME challengen
som er en del av dansen for å sikre seg sertifikater fra Let's Encrypt.
All annen trafikk redirectes til HTTPS.

Dersom du kjører lokalt bør du sette `use_ssl=False` i `hosts-local.ini`,
ellers vil Ansible feile når det forsøkes å bestille sertifikat, ettersom
den prosessen krever at serveren du er på er tilgjengelig over internett
med det hostnamet du bestiller sertifikat for.

## Bidra til koden

Supert! Gjør gjerne det, men husk å kjøre testene:

```sh
lein with-profile test midje :autotest
```

Du kan få en innføring i hvordan koden fungerer i
[denne bloggposten](https://cjohansen.no/building-static-sites-in-clojure-with-stasis).
