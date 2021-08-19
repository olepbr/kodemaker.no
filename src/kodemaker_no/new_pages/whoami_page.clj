(ns kodemaker-no.new-pages.whoami-page
  (:require [clojure.java.io :as io]
            [ui.layout :as l]))

(defn render-section [_]
  [:div#whoami-wrapper
   [:div#whoami-section {:style (l/stylish {} {:background :chablis})}
    [:div#whoami-header (l/header-section {})]
    [:div#whoami-body.section
     [:div.content
      [:div#illustration
       (slurp (io/resource "whoami.svg"))]
      [:div.mtm.tac "Ja, hvem er Kodemaker?"]
      [:div#quotes
       [:div.whoami-q.scroll--none.tac
        [:div "Vi fikk et eksternt firma (Eggs) til å intervjue våre kunder, og her er et utdrag av tilbakemeldingene:"]]
       [:div.whoami-q.scroll--erfaring-on
        [:div "«De setter seg raskt inn i nye fagfelt.»"]]
       [:div.whoami-q.scroll--erfaring-on
        [:div "«Kodemaker har ikke et B-lag.»"]]
       [:div.whoami-q.scroll--erfaring-on
        [:div "«De har businessforståelse. De snakker med forretning mer enn en vanlig utvikler.»"]]
       [:div.whoami-q.scroll--ansvarlig-on
        [:div "«Kodemaker føler eierskap og tenker langsiktig selv om de skal dra om 3 måneder.»"]]
       [:div.whoami-q.scroll--ansvarlig-on
        [:div "«De lager en løsning og bygger opp et system som man ikke får problemer med senere.»"]]
       [:div.whoami-q.scroll--ansvarlig-on
        [:div "«De tar mye ansvar og er sikre på seg selv.»"]]
       [:div.whoami-q.scroll--utfordrer-on
        [:div "«Det er alltid faren med konsulenter at de bukker og neier. KM utfordrer det vedtatte.»"]]
       [:div.whoami-q.scroll--utfordrer-on
        [:div "«Vi vil ha konsulenter som kan si oss midt i mot. Vi vil ikke ha stille ‘code monkeys’.»"]]
       [:div.whoami-q.scroll--utfordrer-on
        [:div "«Vi ser etter konsulenter som har litt push back og har litt meninger. Viktig at vi blir utfordret.»"]]
       #_[:div.whoami-q.scroll--bidrar-on
        [:div "«»"]]
       [:div.whoami-q.scroll--engasjert-on
        [:div "«De går inn i noe med interesse og positivitet. De har tro på det de lager.»"]]
       [:div.whoami-q.scroll--engasjert-on
        [:div "«De er superengasjerte i kodebiten.»"]]
       [:div.whoami-q.scroll--uformell-on
        [:div "«Vi kan slappe av med de. Vi kan tulle.»"]]
       [:div.whoami-q.scroll--uformell-on
        [:div "«Omgjengelige, sosiale og uhøytidlige.»"]]
       [:div.whoami-q.scroll--orden-on
        [:div "«Ryddige kontrakter, kan stole på dem.»"]]
       [:div.whoami-q.scroll--orden-on
        [:div "«Lite byråkrati. Ikke så mye admin.»"]]
       [:div.whoami-q.scroll--orden-on
        [:div "«Lett å kontakte. Svarer fort. Fint med én person å forholde seg til.»"]]
       [:div.whoami-q.scroll--none.tac {:style {:max-width "600px" :margin-left "auto" :margin-right "auto"}}
        [:div "Vil du vite mer om hva kundene mener, så kan du ta en titt på "
         [:a.link.nbr {:href "/referanser/"} "våre referanser"] ". "
         "Eller sjekk ut "
         [:a.link.nrb {:href "/folk/"} "folka som jobber her"] "."]]]]]
    [:div#whoami-footer (l/footer {})]
    [:script (slurp (io/resource "public/scripts/whoami.js"))]]])

(defn create-page [_]
  {:title "Hvem er kodemakerne?"
   :sections [{:kind :whoami}]})
