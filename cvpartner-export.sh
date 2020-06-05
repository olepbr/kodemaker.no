#!/bin/sh

if [ ! -f ./secret.envrc ]; then
  echo "Cvpartner authorization token er ikke satt opp."
  echo "Følg beskrivelsen i filen  ./secret-sample.envrc"
  exit
fi

if [ -z "${CVPARTNER_AUTHORIZATION_TOKEN}" ] ||
   [ "${CVPARTNER_AUTHORIZATION_TOKEN}" == "hemmelig" ]; then
  echo "Miljøvariabel CVPARTNER_AUTHORIZATION_TOKEN er ikke satt opp"
  echo "Følg beskrivelsen i filen  ./secret-sample.envrc"
  exit
fi

if [ $# -eq 0 ]; then
  echo "Usage: $0 [<name-as-in-email>* | 'all']"
  echo "Example: $0 kolbjorn"
  exit
fi

echo ""
echo "Generering av cv-er fra kodemaker.no til cvpartner.no startet..."
echo "Du kan følge med på genereringen på http://kodemaker.cvpartner.no :)"
echo ""

lein cvpartner-export $* 2>cvpartner.error.txt
