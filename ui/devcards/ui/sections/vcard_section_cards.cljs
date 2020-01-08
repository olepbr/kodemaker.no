(ns ui.sections.vcard-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.sections :as sections]
            [ui.icons :as icons]))

(defcard vcard
  (sections/vcard-section
   {:image "/devcard_images/trygve.jpg"
    :friendly-name "Timothy"
    :full-name "Timothy Delgado"
    :title "Systemutvikler"
    :contact-lines ["982 19 322"
                    "eivind@kodemaker.no"]
    :links [{:title "Timothy på LinkedIn"
             :href "https://www.linkedin.com/in/timothydelgado"
             :icon icons/linkedin}
            {:title "Timothy på Stack Overflow"
             :href "https://www.stack-overflow.com/timothydelgado"
             :icon icons/stackoverflow}
            {:title "tdelgado på Twitter"
             :href "https://twitter.com/tdelgado"
             :icon icons/twitter}
            {:title "tgod på GitHub"
             :href "https://github.com/tgod"
             :icon icons/github}]}))
