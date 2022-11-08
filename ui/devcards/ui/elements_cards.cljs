(ns ui.elements-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.icons :as icons]))

(defcard tech-tags
  (e/tech-tags {:prefix "Laget med"
                :class "tags"
                :techs [{:tech/name "Java" :page/uri "#"}
                        {:tech/name "Spring" :page/uri "#"}
                        {:tech/name "Maven" :page/uri "#"}]}))

(defcard straight-tech-tags
  (e/tech-tags {:prefix "Om"
                :techs [{:tech/name "Java" :page/uri "#"}
                        {:tech/name "Spring" :page/uri "#"}
                        {:tech/name "Maven" :page/uri "#"}]}))
(defcard people-tags
  (e/people-tags {:prefix "Av"
                  :class "tags"
                  :people [{:person/given-name "Jon" :page/uri "#"}
                           {:person/given-name "Garfield" :page/uri "#"}
                           {:person/given-name "Cthulhu" :page/uri "#"}]}))

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

(defcard teaser
  (e/teaser {:title "Analyse av værdata"
             :tags "av Nils, Stig og Stig"
             :url "#"
             :content "Et system for å samle inn og organisere data om vær og bruke dataene for å finne korrelasjoner med andre tidsserier. Skrevet for det meste i go og deployet på google cloud platformen med kubernetes."
             :link {:text "Les artikkel"
                    :href "#"}}))

(defcard illustrated-teaser
  (e/illustrated-teaser
   {:title "Analyse av værdata"
    :tags "av Nils, Stig og Stig"
    :url "#"
    :illustration "/devcard_images/weather.png"
    :content "Et system for å samle inn og organisere data om vær og bruke dataene for å finne korrelasjoner med andre tidsserier. Skrevet for det meste i go og deployet på google cloud platformen med kubernetes."
    :link {:text "Les artikkel"
           :href "#"}}))

(defcard video-thumb
  (e/video-thumb {:img "/devcard_images/to-the-cloud.jpg"
                  :tags "JAVA, SCALA"
                  :url "#"
                  :title "To the cloud"}))

(defcard round-media
  (e/round-media {:image "/devcard_images/person.png"
                  :title "Geir Oterhals"
                  :lines ["Prosjektleder, Oche Dart"
                          "+47 992 18 320"]}))

(defcard vert-round-media
  (e/vert-round-media {:image "/devcard_images/magnus.jpg"
                       :lines ["Systemutvikler"
                               "+47 992 18 320"
                               "magnus@kodemaker.no"]}))

(defcard blockquote
  (e/blockquote
   {:quote
    "Kodemaker tok en idé til ferdig løsning på kort tid, og de har vært en viktig ekstern bidragsyter i utviklingen av vårt konsept Oche. De har jobbet godt sammen med flere andre aktører i et hektisk prosjekt.

De er flinke, sier hva de mener og lager det vi ønsker. Softwaren de har laget
har fungert knirkefritt siden åpningen. Vi har et veldig godt inntrykk av hele
Kodemaker, og de fremstår som en dyktig, jovial og humørfylt gjeng."}))

(defcard centered-article
  (e/article
   {:content (e/blockquote
              {:quote "Kodemaker tok en idé til ferdig løsning på kort tid, og
de har vært en viktig ekstern bidragsyter i utviklingen av vårt konsept Oche. De
har jobbet godt sammen med flere andre aktører i et hektisk prosjekt.

De er flinke, sier hva de mener og lager det vi ønsker. Softwaren de har laget
har fungert knirkefritt siden åpningen. Vi har et veldig godt inntrykk av hele
Kodemaker, og de fremstår som en dyktig, jovial og humørfylt gjeng."})
    :aside (e/round-media
            {:image "/devcard_images/person.png"
             :title "Geir Oterhals"
             :lines ["Prosjektleder, Oche Dart"
                     "+47 992 18 320"]})
    :alignment :balanced}))

(defcard article-content-aligned-aside
  (e/article
   {:title "En tittel"
    :content (e/blockquote
              {:quote "Kodemaker tok en idé til ferdig løsning på kort tid, og
de har vært en viktig ekstern bidragsyter i utviklingen av vårt konsept Oche. De
har jobbet godt sammen med flere andre aktører i et hektisk prosjekt.

De er flinke, sier hva de mener og lager det vi ønsker. Softwaren de har laget
har fungert knirkefritt siden åpningen. Vi har et veldig godt inntrykk av hele
Kodemaker, og de fremstår som en dyktig, jovial og humørfylt gjeng."})
    :aside (e/round-media
            {:image "/devcard_images/person.png"
             :title "Geir Oterhals"
             :lines ["Prosjektleder, Oche Dart"
                     "+47 992 18 320"]})
    :alignment :content}))

(defcard top-aside-article
  (e/article
   {:content [:div.text [:p "Oche tilbyr en helt ny opplevelse av dart pakket
   inn i et gjennomført sosialt konsept for den gode opplevelsen med
   vennegjengen eller jobben. Det er tradisjonell dart i form av pilkast på en
   fysisk dartskive, men derfra og inn er alt annerledes. Alle kast blir
   automatisk registrert i datasystemet, og din score og plassering blir
   automatisk beregnet og annonsert på stor skjerm kast for kast. Det gir en
   helt annen opplevelse for hele gjengen, og det blir kjapt rom for kjappe
   kommentarer og økt forventningspress."]
              [:p "Kodemaker har vært med å lage en del forskjellige typer spill, og da blir det
opp til den enkelte å finne sin favoritt. Her vil det være rom for å enkelt
utvikle nye varianter, og ikke minst kan det bli gøy med turneringer. Kodemaker
har jobbet tett sammen med designere fra Eggs og en maskiningeniør fra Not A
Number. Sammen har vi laget en på alle plan spennende og velfungerende løsning
for dette fysiske spillkonseptet."]]
    :image "/devcard_images/half-circle.png"
    :alignment :front}))

(defcard bottom-aside-article
  (e/article
   {:title "Bildeanalyse, spillutvikling og uvant maskinpark"
    :content [:div.text [:p "Utrolig variert og utfordrende IT-prosjekt.
   Kodemaker har jobbet med bildeanalyse, spillutvikling, grensesnitt og
   oppsett/overvåkning av maskiner og utstyr. Grensesnittet er utviklet i
   samarbeid med Eggs Design, som har stått for visuelt design og hjelp på
   frontend-utvikling. Hardware og oppsett av fysisk utstyr er utviklet i
   samarbeid med Alan Gorman - Not a Number AS, som har designet lys,
   kameraplassering med stativ, piler og alle andre mekaniske egenskaper."]
              [:p "Prosjektet startet med en konseptfase hvor det ble utviklet
en Proof of Concept for selve kamerateknologien. Deretter har vi gradvis bygget
ut med optimalisering av nøyaktighet på kalibrering i parallell med utvikling av
spill, grensesnitt og støtteverktøy/overvåkning."]]
    :image "/devcard_images/dart-triangle.png"
    :alignment :back}))

(defcard article-with-sub-title
  (e/article
   {:sub-title "Eivind B Waaler"
    :content [:div.text [:p "Eivind startet jobben med bildeanalyse og har utviklet
    selve motoren for kalibrering av kameraer og gjenkjenning av piler. Han har
    også gjort en del videreutvikling på selve spillmotoren, samt masse
    frontend-utvikling for å ferdigstille design/grensesnitt mot slutten av
    prosjektet. Eivind var også ansvarlig for alt det fysiske - fra valg/oppsett
    av kameraer og annen hardware som datamaskiner, lysstyring og skjermer."]]
    :aside (e/vert-round-media {:image "/devcard_images/magnus.jpg"
                                :lines ["Systemutvikler"
                                        "+47 992 18 320"
                                        "magnus@kodemaker.no"]})
    :alignment :balanced}))

(defcard annotated-article
  (e/article
   {:title "En superinteressant bloggartikkel"
    :content [:div.text [:p "Eivind startet jobben med bildeanalyse og har utviklet
    selve motoren for kalibrering av kameraer og gjenkjenning av piler. Han har
    også gjort en del videreutvikling på selve spillmotoren, samt masse
    frontend-utvikling for å ferdigstille design/grensesnitt mot slutten av
    prosjektet. Eivind var også ansvarlig for alt det fysiske - fra valg/oppsett
    av kameraer og annen hardware som datamaskiner, lysstyring og skjermer."]]
    :annotation "23.10.2019"}))

(defcard simple-article
  (e/simple-article
   {:title "En rett-frem artikkel som bruker all bredde den får"
    :annotation "Publisert 02.02.2020"
    :content [:div.text.article-text
              [:p
               "Denne innholdsblokka er ikke bare " [:code ".text"]
               ", neida, den er også " [:code ".article-text"] ", en dedikert
               tekst-klasse som blant annet brukes i blogg-innlegg, som får
               flotte seg med litt ekstra white-space, og litt annen størrelse
               på headinger."]
              [:h2 "Se bare på meg"]
              [:p "Du ville kanskje ikke gjettet at overskriften over var en h2?
              Galskap, den ser jo ut som en h3, og bare sjekk all luften over
              den. Elegant. Luksuriøst."]]}))

(defcard attributed-content
  (e/attributed-content {:title [:h2.h4 "Kodemaker tok idé til ferdig løsning på kort tid"]
                         :person {:image "/devcard_images/person.png"
                                  :lines [[:strong "Geir Oterhals"]
                                          "Prosjektleder, Oche Dart"
                                          "+47 992 18 320"]}
                         :logo {:image "/devcard_images/oche.svg"
                                :title "Oche"
                                :href "https://ochedart.com/"}
                         :link {:text "Les mer"
                                :href "#"}
                         :content (e/blockquote
                                   {:quote
                                    "Kodemaker tok en idé til ferdig
                                           løsning på kort tid, og de har vært
                                           en viktig ekstern bidragsyter i
                                           utviklingen av vårt konsept Oche. De
                                           har jobbet godt sammen med flere
                                           andre aktører i et hektisk prosjekt.
                                           De er flinke, sier hva de mener og
                                           lager det vi ønsker. Softwaren de har
                                           laget har fungert knirkefritt siden
                                           åpningen. Vi har et veldig godt
                                           inntrykk av hele Kodemaker, og de
                                           fremstår som en dyktig, jovial og
                                           humørfylt gjeng."})}))

(defcard curtain-left
  (e/curtain
   {:content [:img.img {:width "300" :src "/devcard_images/profile.jpg"}]
    :side :left}))

(defcard curtain-right
  (e/curtain
   {:content [:img.img {:width "300" :src "/devcard_images/profile.jpg"}]
    :side :right}))

(defcard illustrated
  (e/illustrated
   {:image "/devcard_images/profile.jpg"
    :title "Justin Moore"
    :lines ["Systemutvikler"
            "+47 934 17 480"
            "christin@kodemaker.no"]}))

(defcard illustrated-left
  (e/illustrated
   {:image "/devcard_images/profile.jpg"
    :title "Justin Moore"
    :curtain :left
    :lines ["Systemutvikler"
            "+47 934 17 480"
            "christin@kodemaker.no"]}))

(defcard illustrated-right
  (e/illustrated
   {:image "/devcard_images/profile.jpg"
    :title "Justin Moore"
    :curtain :right
    :lines ["Systemutvikler"
            "+47 934 17 480"
            "christin@kodemaker.no"]}))

(defcard icon-row
  (e/icon-link-row
   {:links [{:href "https://www.linkedin.com/in/christianjohansen"
             :target "_blank"
             :icon (icons/icon :linkedin)}
            {:href "https://twitter.com/cjno"
             :target "_blank"
             :icon (icons/icon :twitter)}
            {:href "https://mastodon.social/@christian@mastodon.duderinos.no"
             :target "_blank"
             :icon (icons/icon :mastodon)}
            {:href "https://github.com/cjohansen"
             :target "_blank"
             :icon (icons/icon :github)}]}))
