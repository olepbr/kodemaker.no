(ns ui.sections.references-intro-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard references-intro-section
  (sections/references-intro-section
   {:title "Kodemaker tok en idé til ferdig løsning på kort tid"
    :image "/devcard_images/sjogutta2.png"
    :logo {:image "/devcard_images/oche.svg"
           :title "Oche"
           :href "https://ochedart.com/"}
    :link {:text "Les mer"
           :href "#"}
    :content "Kodemaker tok en idé til ferdig løsning på kort tid, og de har
              vært en viktig ekstern bidragsyter i utviklingen av vårt konsept
              Oche. De har jobbet godt sammen med flere andre aktører i et
              hektisk prosjekt. De er flinke, sier hva de mener og lager det vi
              ønsker. Softwaren de har laget har fungert knirkefritt siden
              åpningen. Vi har et veldig godt inntrykk av hele Kodemaker, og de
              fremstår som en dyktig, jovial og humørfylt gjeng."}))
