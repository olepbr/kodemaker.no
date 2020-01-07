(ns ui.sections.titled-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard titled-section
  (sections/titled-section
   {:title "Eivinds anbefalinger"
    :contents [(e/teaser
                {:title "The Log: What every software engineer should know about real-time data's unifying abstraction"
                 :tags "Kafka og Performance"
                 :url "#"
                 :content "Fantastisk bra artikkel om logg som sentralt system for integrasjon, konsistens og asynkron meldingsutveksling. Skrevet av mannen bak Kafka. Bør leses av alle som har interesse av arkitektur hos større bedrifter med flere systemer som skal snakke sammen."
                 :link {:text "Les artikkel"
                        :href "#"}})]}))
