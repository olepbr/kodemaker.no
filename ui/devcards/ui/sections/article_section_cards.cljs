(ns ui.sections.article-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections.article-section :as section]))

(defcard top-aligned
  (section/render
   {:article {:alignment :front
              :title "Hva er Kubernetes"
              :content [:div.text
                        [:p "Kubernetes er Google sitt alt-burde-egentlig-være-en-container-prosjekt. Google har i de siste 10-15 årene kjørt alle greiene sine i containers, i noe de kaller for Google Borg. Kubernetes er en alternativ implementasion av Borg som er open source, laget av de samme folkene og har mange av de samme konseptene."]
                        [:p "Google har også etter hvert tatt i bruk Kubernetes selv på nye ting."]
                        [:p "Kubernetes gjør at det blir relativt plug-and-play å gjøre ting som tidligere kun var mulig med proprietære cloud-løsninger, som auto-skalering av instanser avhengig av lasten på systemet."]]
              :aside-title "Foredrag"
              :aside (e/video-thumb {:img "/devcard_images/to-the-cloud.jpg"
                                     :tags "JAVA, SCALA"
                                     :url "#"
                                     :title "To the cloud"})}}))

(defcard bottom-aligned
  (section/render
   {:article {:alignment :back
              :title "Hva er Kubernetes"
              :content (e/blockquote
                        {:quote
                         "Kodemaker tok en idé til ferdig løsning på kort tid, og de har vært en viktig ekstern bidragsyter i utviklingen av vårt konsept Oche. De har jobbet godt sammen med flere andre aktører i et hektisk prosjekt.

De er flinke, sier hva de mener og lager det vi ønsker. Softwaren de har laget
har fungert knirkefritt siden åpningen. Vi har et veldig godt inntrykk av hele
Kodemaker, og de fremstår som en dyktig, jovial og humørfylt gjeng."})
              :aside (e/round-media {:image "/devcard_images/person.png"
                                     :title "Geir Oterhals"
                                     :lines ["Prosjektleder, Oche Dart"
                                             "+47 992 18 320"]})}}))
