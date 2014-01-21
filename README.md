# kodemaker.no

Våre nye nettsider kommer til verden.

## Teste lokalt

Skaff [leiningen](https://github.com/technomancy/leiningen#leiningen)
om du ikke har den.

Gå så til rota av prosjektet, og

```shell
lein ring server
```

Voila!

## Hvordan skal mine data se ut?

Enn så lenge er alt dette tentativt. Jeg må prøve meg litt fram. Men
datastrukturen blir omtrent slik, vil jeg tro:

```clj
(def Person
  {:id Keyword
   :name [Str]
   :title Str
   :start-date Str
   :description Str
   (optional-key :administration?) Boolean

   :phone-number Str
   :email-address Str

   :presence {(optional-key :cv) Str ;; Kodemaker cv id
              (optional-key :twitter) Str  ;; username
              (optional-key :linkedin) Str ;; path to public profile
              (optional-key :stackoverflow) Str ;; path to public profile
              (optional-key :github) Str ;; username
              (optional-key :coderwall) Str} ;; username

   (optional-key :tech) {:favorites-at-the-moment [Keyword]
                         (optional-key :want-to-learn-more) [Keyword]}

   (optional-key :blogs) [{:id Keyword
                           :name Str
                           :url Str
                           :theme Str ;; very short
                           :tech [Keyword]}]

   (optional-key :blog-posts) [{:url Str
                                :title Str
                                :blurb Str
                                :tech [Keyword]
                                :blog (either Keyword ;; :id from :blogs
                                              {:name Str
                                               :url Str})}]

   (optional-key :recommendations) [{:url Str
                                     :title Str
                                     :blurb Str
                                     :tech [Keyword]}]

   (optional-key :projects) [{:id Keyword
                              :customer Str
                              :description Str
                              :tech [Keyword]}]

   (optional-key :endorsements) [{:author Str
                                  :quote Str
                                  (optional-key :title) Str
                                  (optional-key :project) Keyword
                                  (optional-key :photo) Str}]})
```

Legge merke til at dette er kode som kjører når siden bygges opp, slik
at du bør få grei tilbakemelding om du tråkker på utsiden.

Eksempel på utfylte data finner du i [min profil](resources/people/magnar.edn).

## Laste opp bilder

Bildene ligger i `resources/public`. Info:

- `/logos` Logo til referanser: .png med bredde 290px. Husk å bruke [smushit](smushit.com).
- `/thumbs/faces` Ansikt til referansepersoner: .jpg, proporsjon 3/4, gjerne 210x280
- `/thumbs/videos` Utsnitt fra video: .jpg, proporsjon 4/3, gjerne 200x150
- `/photos/references/` Illustrasjoner til referanser: .jpg med bredde 580px.
- `/photos/tech/` Illustrasjoner til tech: .jpg med bredde 580px.
- `/photos/people/<person>/half-figure.jpg` Kodemaker stående: .jpg 580x741
- `/photos/people/<person>/side-profile.jpg` Kodemaker sittende: .jpg 620x485

Hvis du ikke har Photoshop eller lignende, så kan du skalere bilder på
http://scaleyourimage.com/.

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

Legg til din public key i `provisioning/keys`, og føy den til listen
under `Setup authorized_keys for users who may act as deploy user`
tasken i `provisioning/bootstrap.yml`.

Gå så tilbake til `provisioning/` og:

```sh
ansible-playbook -i hosts.ini bootstrap.yml --private-key=~/.vagrant.d/insecure_private_key -u vagrant --sudo
```

Nå kan du `ssh deploy@local.kodemaker.no` og se deg omkring. Sudo
passord er `kodemaker`.

Så kan du fortsette ned til
[Sette opp kodemaker.no](#neste-sette-opp-kodemakerno).

### Provisjonere en server

Så, du har en fresk og fersk CentOS server som vil bli kodemaker.no.
Legg den til i `provisioning/hosts.ini` under `[new-servers]`. Du kan
ta bort `192.168.33.44`, den brukes bare for lokal testing.

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

Når du bootstrapper, så vil root-login og passord-login bli disablet.
Så når vi nå skal sette opp kodemaker no, så må du fleske til med en
annen inkantasjon:

```sh
ansible-playbook -i hosts.ini setup-kodemaker.yml --user deploy --sudo --ask-sudo-pass
```

Nå er det altså ikke SSH-passordet som brukes lenger - den bruker din
private key - men du må oppgi sudo-passordet. Dersom du ikke har gjort
noen endringer, så er det fortsatt `kodemaker`. Men hvis dette er en
offentlig server, så lønner det seg nok å gjøre den endringen. Logg
inn som `deploy` og `passwd`.

#### Bygg og deploy siten

Du må ha en ganske ny versjon av
[leiningen](https://github.com/technomancy/leiningen#leiningen)
installert. Gå til rota av prosjektet, og:

```sh
lein build-site
cd provisioning
ansible-playbook -i hosts.ini deploy-kodemaker.yml --user deploy --sudo --ask-sudo-pass
```

Og nå kan du besøke http://local.kodemaker.no i nettleseren din og
meske deg i de nye sidene våre.

#### Funka ikke!

Okay,

- La du merke til at det er et nytt script `deploy-kodemaker.yml`?
  Pass på at du ikke kjører `setup-kodemaker.yml` en gang til istedet.

- Får du ` ERROR: synchronize is not a legal parameter in an Ansible
  task or handler` må du oppdatere din Ansible.

Fortsatt ikke bedre? Klag til meg, så legger jeg til fikser fortløpende.
