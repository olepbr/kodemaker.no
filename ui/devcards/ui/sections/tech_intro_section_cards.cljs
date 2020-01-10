(ns ui.sections.tech-intro-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.icons :as icons]
            [ui.sections :as sections]))

(defcard tech-intro-section
  (sections/tech-intro-section
   {:title "Kubernetes"
    :logo "/devcard_images/kubernetes.png"
    :article {:content [:div.text
                        [:p "Kubernetes er Google sitt alt-burde-egentlig-være-en-container-prosjekt. Google har i de siste 10-15 årene kjørt alle greiene sine i containers, i noe de kaller for Google Borg. Kubernetes er en alternativ implementasion av Borg som er open source, laget av de samme folkene og har mange av de samme konseptene."]
                        [:p "Google har også etter hvert tatt i bruk Kubernetes selv på nye ting."]
                        [:p "Kubernetes gjør at det blir relativt plug-and-play å gjøre ting som tidligere kun var mulig med proprietære cloud-løsninger, som auto-skalering av instanser avhengig av lasten på systemet."]]
              :alignment :front
              :aside [:div.hide-below-1000
                      (e/video-thumb
                       {:img "/devcard_images/to-the-cloud.jpg"
                        :tags "JAVA, SCALA"
                        :url "#"
                        :title "To the cloud"})]}
    :pønt [{:kind :greater-than
            :position "top -410px right 60vw"}
           {:kind :dotgrid
            :position "top -110px left 80vw"}]}))
