(ns ui.elements-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]))

(defcard arrow-link
  (e/arrow-link {:text "Se mer"
                 :href "https://wwww.kodemaker.no"}))

(defcard big-arrow-link
  (e/arrow-link {:text "Se mer"
                 :size :large
                 :href "https://wwww.kodemaker.no"}))

(defcard icon
  (e/icon {:type :computer/laptop-1 :width 100}))

(defcard seymour
  (e/seymour {:icon {:type :science/robot-2 :height 79}
              :title "Referanser"
              :text "Vi har kun erfarne konsulenter med oss, som liker å bryne seg på vanskelige oppgaver."
              :link {:text "Se referanser"
                     :href "/referanser/"}}))

(defcard round-media
  (e/round-media {:image "/devcard_images/person.png"
                  :title "Geir Oterhals"
                  :lines ["Prosjektleder, Oche Dart"
                          "+47 992 18 320"]}))

(defcard blockquote
  (e/blockquote
   {:quote
    "Kodemaker tok en idé til ferdig løsning på kort tid, og de har vært en viktig ekstern bidragsyter i utviklingen av vårt konsept Oche. De har jobbet godt sammen med flere andre aktører i et hektisk prosjekt.

De er flinke, sier hva de mener og lager det vi ønsker. Softwaren de har laget
har fungert knirkefritt siden åpningen. Vi har et veldig godt inntrykk av hele
Kodemaker, og de fremstår som en dyktig, jovial og humørfylt gjeng."}))
