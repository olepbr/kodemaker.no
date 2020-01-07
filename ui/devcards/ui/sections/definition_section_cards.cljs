(ns ui.sections.definition-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard definition-section
  (sections/definition-section
   {:definitions
    [{:title "Diskusjon"
      :contents [(e/teaser
                  {:title "Twitter'n"
                   :icon sections/twitter-icon
                   :url "https://twitter.com"})
                 (e/teaser
                  {:title "Linker'n"
                   :icon sections/linkedin-icon
                   :url "https://www.linkedin.com"})
                 (e/teaser
                  {:title "StackOverflyt"
                   :icon sections/stackoverflow-icon
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
