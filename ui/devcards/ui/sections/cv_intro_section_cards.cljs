(ns ui.sections.cv-intro-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.sections :as sections]
            [ui.icons :as icons]))

(defcard cv-intro-section
  (sections/cv-intro-section
   {:image "/devcard_images/trygve.jpg"
    :friendly-name "Timothy"
    :full-name "Trygve M. Amundsen"
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
             :icon icons/github}]
    :experience "Systemutvikler med 20 års erfaring"
    :qualifications ["Fullstack, jobber like gjerne med backend som frontend"
                     "Erfaring med oppsett og drift av infrastruktur og CI/CD"
                     "Liker funksjonell programmering, enkle løsninger og små team"
                     "Skriver godt, liker å dokumentere, holder gjerne workshops, og bidrar til å løfte fagmiljøene han jobber i"]
    :quote {:text "En utvikler er ikke en utvikler. Det er stor forskjell pa effektiviteten og evne til a produsere kode med hoy kvalitet. Eivind er i den ypperste klasse i forhold til a omgjore krav til kode med hoy teknisk og funksjonell kvalitet."
            :source "Kjell Sverre Birkeland, Prosjektleder, Skatteetaten"}
    :description '([:p "Eivind har mange års erfaring som utvikler og arkitekt på diverse Java-baserte systemer. Han er pragmatisk og elsker å finne gode løsninger på kompliserte utfordringer. Eivind er flink til å se saker fra flere sider og streber etter å komme opp med enkle og elegante implementasjoner. Han gir seg aldri før feilen er funnet!"]
                   [:p "Eivind har erfaring med både fag- og personalledelse i tillegg til utvikling. Dette har gitt han mer forståelse for menneskene rundt seg, men også gjort han enda mer sikker på at det er utvikling han vil drive med."]
                   [:p "Etter å ha jobbet i ca. 10 år tok Eivind mastergrad ved UiO. Dette bidro til at han fikk øynene opp for alternative JVM-språk og funksjonell programmering. De siste årene har han også jobbet en god del med frontend - og mener selv å vite forskjellen på god og dårlig JavaScript-kode."]
                   [:p "Eivind er aktiv i fagmiljøet og har blant annet holdt presentasjon på mer enn 10 forskjellige JavaZone-konferanser."])
    :highlights [{:title "BN Bolig"
                  :text "Websider og selvbetjening for boligselgere. Grensesnitt og integrasjoner."
                  :href "/referanser/bn-bolig/"}
                 {:title "FINN.no"
                  :text "Utvikling av nye og eksisterende løsninger for betaling, ordre og produkt."}
                 {:title "Oche"
                  :text "Utvikling av nytt spillkonsept. Bildeanalyse, spillutvikling, grensesnitt og drift/monitorering."
                  :href "/referanser/oche/"}]}))
