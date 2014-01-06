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

- `cd provisioning/devbox`
- `vagrant plugin install vagrant-vbguest`
- `vagrant up`
- `echo "\n192.168.33.44 local.kodemaker.no" | sudo tee -a /etc/hosts`
