# kodemaker.no

Våre nye nettsider kommer til verden.

## Provisjonering

Vi bruker [Ansible](www.ansibleworks.com) for å sette opp serveren.
Hvis du sitter på OSX er det så enkelt som `brew install ansible`. Da
får du `1.3.4` eller nyere, noe du også trenger.

### Teste lokalt

Du kan bruke [Vagrant](http://www.vagrantup.com/) og
[VirtualBox](https://www.virtualbox.org/) for å sette opp en virtuell
blank Ubuntu server lokalt.

```sh
cd provisioning/devbox
vagrant plugin install vagrant-vbguest
vagrant up
echo "\n192.168.33.44 local.kodemaker.no" | sudo tee -a /etc/hosts
```

Legg til din public key i `provisioning/keys`, og føy den til listen
under `Setup authorized_keys for users who may act as deploy user`
tasken i `provisioning/bootstrap.yml`.

Gå så tilbake til `provisioning/` og:

```sh
ansible-playbook -i hosts.ini bootstrap.yml --private-key=~/.vagrant.d/insecure_private_key -u vagrant --sudo
```

Nå kan du `ssh deploy@local.kodemaker.no` og se deg omkring. Sudo
passord er `kodemaker`.

### Provisjonere en server

Så, du har en fersk og fresk CentOS server som vil bli kodemaker.no.
Legg den til i `provisioning/hosts.ini` under `[new-servers]`. Du kan
ta bort `192.168.33.44`, den brukes bare for lokal testing.

Forhåpentligvis har du testet lokalt, og dermed ligger allerede din
public key i `provisioning/keys`.

Så gjenstår det bare å gå til `provisioning/` katalogen og inkantere:

```sh
ansible-playbook -i hosts.ini bootstrap.yml --user root --ask-pass
```

#### Øhh, det gikk ikke helt bra

- Nei, du mangler kanskje `sshpass` lokalt hos deg? Det er bare en yum
  eller apt unna. Eller hvis du er på OSX:

  ```sh
  brew install https://raw.github.com/eugeneoden/homebrew/eca9de1/Library/Formula/sshpass.rb
  ```

- Eller kanskje det mangler `python-apt` på serveren? Da må du SSHe inn og:

  ```sh
  apt-get update
  apt-get install python-apt
  ```

- Noen bokser kommer uten noe særlig byggetools i det heletatt:

  ```sh
  yum update e2fsprogs-libs e2fsprogs e2fsprogs-devel
  yum install e2fsprogs-libs e2fsprogs e2fsprogs-devel
  yum install wget gcc gcc-c++ flex bison make bind bind-libs bind-utils openssl openssl-devel perl quota libaio libcom_err-devel libcurl-devel gd zlib-devel zip unzip libcap-devel cronie bzip2 db4-devel cyrus-sasl-devel perl-ExtUtils-Embed autoconf automake libtool which
  ```
