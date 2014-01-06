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
