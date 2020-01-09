(ns ui.sections.definition-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.icons :as icons]
            [ui.sections :as sections]))

(defcard definition-section
  (sections/definition-section
   {:definitions
    [{:title "Diskusjon"
      :contents [(e/teaser
                  {:title "Twitter'n"
                   :icon icons/twitter
                   :url "https://twitter.com"})
                 (e/teaser
                  {:title "Linker'n"
                   :icon icons/linkedin
                   :url "https://www.linkedin.com"})
                 (e/teaser
                  {:title "StackOverflyt"
                   :icon icons/stackoverflow
                   :url "https://stackoverflow.com"})
                 (e/teaser
                  {:title "En helt ukjent lenke"
                   :url "https://www.facebook.com"})]}
     {:title "Mer fra bloggen"
      :contents [(e/teaser
                  {:title "Devops! Dev? Ops!"
                   :annotation "30.10.2019"
                   :url "/blogg/devops-dev-ops/"})
                 (e/teaser
                  {:title "Inputvalidering i Kotlin med Arrow"
                   :annotation "23.10.2019"
                   :url "/blogg/kotlin/"})
                 (e/teaser
                  {:title "Jeg velger Windows, det beste utviklingsmiljøet (for meg)"
                   :annotation "16.10.2019"
                   :url "/blogg/windoze/"})]}]}))

(defcard titled-definition-section
  (sections/definition-section
    {:title "Teknologi"
     :definitions
     [{:title "Programmeringsspråk"
       :contents [[:p.text
                   [:a {:href "/java/"} "Java"]
                   ", Scala, "
                   [:a {:href "/clojure/"} "Clojure"]
                   ", JavaScript, Groovy, "
                   [:a {:href "/clojurescript"} "ClojureScript"]
                   ", Ruby, Python, C++, Node.js, TypeScript, "
                   [:a {:href "/vbscript/"} "VBScript"]]]}
      {:title "Utviklingsverktøy"
       :contents [[:p.text "Maven, " [:a {:href "/emacs/"} "Emacs"] ", IntelliJ IDEA"]]}
      {:title "Versjonskontroll"
       :contents [[:p.text "CVS, Subversion, Git"]]}]}))

(defcard titled-grouped-definition-section
  (sections/definition-section
    {:title "Prosjekter"
     :definitions
     [{:type :separator
       :category "Arbeidsgiver"
       :title "Kodemaker Systemutvikling AS"}
      {:title (list "Mnemonic" [:br] "2019")
       :contents [[:h4.h6 [:em "Frontend-programmering"]]
                  [:div.text
                   [:p "Jobbet som frontend-programmerer på diverse applikasjoner knyttet til Mnemonic sitt Argus system."]]
                  [:p.text-s.annotation.mts "JavaScript, TypeScript, React"]]}
      {:title (list "Animalia" [:br] "2018-2019")
       :contents [[:h4.h6 [:em "API og klient for klauvskjæring"]]
                  [:div.text
                   [:p "Laget API for å tilby og motta data fra terminaler som brukes av klauvskjærere i norske fjøs. I all hovedsak snakk om en Clojure webserver som snakker med en database. Utviklet også en web-klient for internt bruk/administrasjon."]]
                  [:p.text-s.annotation.mts "Clojure, ClojureScript, Twitter Bootstrap, React, H2, Oracle, Openshift"]]}]}))

(defcard titled-definition-section-with-tight-lists
  (sections/definition-section
    {:title "Sertifiseringer og kurs"
     :definitions
     [{:title "2004"
       :contents [[:ul.dotted.dotted-tight
                   [:li "Sun Certified Enterprise Architect (SCEA)"]
	           [:li "Sun Certified Java Developer (SCJD)"]
	           [:li "Sun Certified Business Component Developer (SCBCD)"]]]}]}))

(defcard untitled-definition-section
  (sections/definition-section
    {:title "Andre faglige bidrag"
     :definitions
     [{:contents [[:h4.h6 [:em "Artikkel: Introduksjon til bildeanalyse"]]
                  [:p "Bildeanalyse, kunsten å lese informasjon fra bilder, er
                  en artig og utfordrende del av IT-faget. Man kan gjøre
                  tekstgjenkjenning, bygge automatiserte industriprosesser, lage
                  nymotens dartspill og en hel del andre greier. Her beskriver
                  vi en måte man kan komme i gang på og forklarer noen av de
                  enkleste prinsippene som kan være kjekke å kunne."]]}]}))

(defcard complex-title
  (sections/definition-section
    {:title "Anbefalinger"
     :definitions
     [{:type :complex-title
       :title [:div
               [:div.mbs [:img.img.round-img {:src "/devcard_images/person.png"}]]
               [:h3.h4-light "Sissel Irene Monso Sveum"]
               [:p "Områdeleder, FINN.no"]]
       :contents [[:h4.h6 [:em "Artikkel: Introduksjon til bildeanalyse"]]
                  [:p "Bildeanalyse, kunsten å lese informasjon fra bilder, er
                  en artig og utfordrende del av IT-faget. Man kan gjøre
                  tekstgjenkjenning, bygge automatiserte industriprosesser, lage
                  nymotens dartspill og en hel del andre greier. Her beskriver
                  vi en måte man kan komme i gang på og forklarer noen av de
                  enkleste prinsippene som kan være kjekke å kunne."]]}]}))
