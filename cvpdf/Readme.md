# Tooling for å lage PDF av CV-ene

I bygget kjører vi et node-script som bruker puppeteer til å "printe" CV-sidene
til PDF-er på disk. Dette er pakket inn i en Docker container for å kunne kjøre
uten ytterligere avhengigheter i CI-miljøet.

For å bygge imaget:

```sh
docker build -t 575778107697.dkr.ecr.eu-west-1.amazonaws.com/html2pdf:$(git rev-parse --short=10 HEAD) .
```

Publiser til AWS:

```sh
$(aws ecr get-login --region eu-west-1 --no-include-email --profile kodemaker)
docker push 575778107697.dkr.ecr.eu-west-1.amazonaws.com/html2pdf:$(git rev-parse --short=10 HEAD)
```

Oppdater så build.sh til å bruke det nye imaget. Voila!
