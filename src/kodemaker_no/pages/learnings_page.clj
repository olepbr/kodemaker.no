(ns kodemaker-no.pages.learnings-page
  (:require [clojure.java.io :as io]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.markup :as markup]))

(defn- render-item [{:keys [url title tech by blurb duration price participants]}]
  (list
   [:h3 (if url [:a {:href url} title] title)]
   (f/render-tech-bubble tech by)
   [:p blurb]
   (when duration [:p.mvn [:strong "Varighet: "] duration])
   (when price [:p.mvn [:strong "Pris: "] price])
   (when participants [:p.mvn [:strong "Deltakere: "] (format "Minimum %s, maks %s"
                                                              (:min participants)
                                                              (:max participants))])))

(defn learnings-page [{:keys [business-presentations workshops videos screencasts]}]
  {:title "Lærelysten? Vi deler gjerne!"
   :sections [{:body [:div.bd.iw
                      [:p "I Kodemaker sitter vi på mye kunnskap og erfaring
                           innen et bredt spekter av forretningsområder og
                           teknologi. Dette ønsker vi å dele med deg slik at vi
                           sammen kan bli gode, og levere IT-prosjekter vi begge
                           ønsker å skryte av og vise frem. I tillegg til
                           kostnadsfrie "
                           [:a {:href "#ekspertdager"} "ekspertdager for våre kunder"]
                           " selger vi "
                           [:a {:href "#kurs"} "workshops og kurs"]
                           " og "
                           [:a {:href "#foredrag"} "foredrag"] ". Kanskje du vil
                           prøve før du kjøper? Sjekk ut "
                           [:a {:href "#konferanser"} "video av tidligere foredrag"] " og "
                           [:a {:href "#screencasts"} "våre screencasts"] "."]]}
              {:type "illustrated-column"
               :title "Ekspertdager"
               :illustration "/forside/team.jpg"
               :id "ekspertdager"
               :body [:div.bd.iw
                      [:p "Har du leid én Kodemaker har du tilgang til hele
                           gjengen. Vi har jevnlig kontakt og sparrer med
                           hverandre for å løse hverdagens floker. I tillegg har
                           vi såkalte ekspertdager — lån en annen Kodemaker til
                           ditt prosjekt en dag for en konkret problemstilling,
                           helt uten ekstra kostnad."]
                      [:p "Snuser prosjektet ditt på Elm til frontenden sin?
                           Book en ekspertdag med " [:a {:href "/magnus/"} "Magnus"]
                           " for en flyvende start. Har dere utfordringer i
                           AWS? Be om en dag med " [:a {:href "/kristian/"} "Kristian"]
                           " eller " [:a {:href "/christian/"} "Christian"] ".
                           Trenger teamet hjelp til å komme igang med Ansible? "
                           [:a {:href "/kjetil/"} "Kjetil"] " ordner opp. Har du
                           sett et foredrag med en Kodemaker som syntes relevant
                           for prosjektet ditt? Spør om en ekspertdag."]
                      [:p "Ekspertdager brukes også aktivt av Kodemakerne selv
                           der de øyner en mulighet for aksellerert læring.
                           Dette kommer både oss og deg som kunde til gode."]]}
              {:type "illustrated-column"
               :title "Workshop og kurs"
               :illustration "/forside/foredrag.jpg"
               :id "kurs"
               :body [:div.bd.iw
                      [:p "Skal prosjektet ditt i gang med noe nytt kan det være
                           greit med en workshop eller et kurs for å få alle opp
                           på samme nivå på kort tid. Våre workshops og kurs
                           holdes i dine lokaler. Prisene under gjelder
                           Oslo-regionen. Vi holder workshop og kurs i resten av
                           landet også, " [:a {:href "/kontakt/"} "ta kontakt for pris"] "."]
                      (map render-item workshops)]}
              {:type "illustrated-column"
               :title "Foredrag"
               :illustration "/forside/foredrag.jpg"
               :id "foredrag"
               :body [:div.bd.iw
                      [:p "Har du sett et foredrag av en Kodemaker du gjerne
                           skulle vist frem i din bedrift? Trenger dere en
                           presentasjon av en spesifikk teknologi for å vurdere
                           hva dere skal bruke videre? Kunne dere trenge litt
                           inspirasjon i prosjektet? Vi kommer gjerne og holder
                           et foredrag. Vi setter av en time i etterkant til
                           diskusjon. Er du allerede kunde av oss? Da får du
                           disse foredragene " [:strong "gratis"] ". Hva venter
                           du på?"]
                      (map render-item business-presentations)]}
              {:type "illustrated-column"
               :title "Foredrag fra konferanser"
               :illustration "/forside/foredrag.jpg"
               :id "konferanser"
               :body (map render-item videos)}
              {:type "illustrated-column"
               :title "Screencasts"
               :illustration "/forside/screencast.jpg"
               :id "screencasts"
               :body (map render-item screencasts)}]})
