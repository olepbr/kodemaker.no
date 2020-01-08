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
       :contents [[:p.text.mbm
                   [:a {:href "/java/"} "Java"]
                   ", Scala, "
                   [:a {:href "/clojure/"} "Clojure"]
                   ", JavaScript, Groovy, "
                   [:a {:href "/clojurescript"} "ClojureScript"]
                   ", Ruby, Python, C++, Node.js, TypeScript, "
                   [:a {:href "/vbscript/"} "VBScript"]]]}
      {:title "Utviklingsverktøy"
       :contents [[:p.text.mbm "Maven, " [:a {:href "/emacs/"} "Emacs"] ", IntelliJ IDEA"]]}
      {:title "Versjonskontroll"
       :contents [[:p.text.mbm "CVS, Subversion, Git"]]}]}))
