(ns ui.sections-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard article-section-top-aligned
  (sections/article-section
   {:articles [{:alignment :front
                :title "Hva er Kubernetes"
                :content [:div.text
                          [:p "Kubernetes er Google sitt alt-burde-egentlig-være-en-container-prosjekt. Google har i de siste 10-15 årene kjørt alle greiene sine i containers, i noe de kaller for Google Borg. Kubernetes er en alternativ implementasion av Borg som er open source, laget av de samme folkene og har mange av de samme konseptene."]
                          [:p "Google har også etter hvert tatt i bruk Kubernetes selv på nye ting."]
                          [:p "Kubernetes gjør at det blir relativt plug-and-play å gjøre ting som tidligere kun var mulig med proprietære cloud-løsninger, som auto-skalering av instanser avhengig av lasten på systemet."]]
                :aside-title "Foredrag"
                :aside (e/video-thumb {:img "/devcard_images/to-the-cloud.jpg"
                                       :tags "JAVA, SCALA"
                                       :url "#"
                                       :title "To the cloud"})}]}))

(defcard article-section-bottom-aligned
  (sections/article-section
   {:articles [{:alignment :back
                :content (e/blockquote
                          {:quote
                           "Kodemaker tok en idé til ferdig løsning på kort tid, og de har vært en viktig ekstern bidragsyter i utviklingen av vårt konsept Oche. De har jobbet godt sammen med flere andre aktører i et hektisk prosjekt.

De er flinke, sier hva de mener og lager det vi ønsker. Softwaren de har laget
har fungert knirkefritt siden åpningen. Vi har et veldig godt inntrykk av hele
Kodemaker, og de fremstår som en dyktig, jovial og humørfylt gjeng."})
                :aside (e/round-media {:image "/devcard_images/person.png"
                                       :title "Geir Oterhals"
                                       :lines ["Prosjektleder, Oche Dart"
                                               "+47 992 18 320"]})}]}))

(defcard article-section-with-mecha-title
  (sections/article-section
   {:articles [{:alignment :back
                :mecha-title "Oche"
                :mecha-sub-title "Dart på en ny måte"
                :content (e/blockquote
                          {:quote
                           "Kodemaker tok en idé til ferdig løsning på kort tid, og de har vært en viktig ekstern bidragsyter i utviklingen av vårt konsept Oche. De har jobbet godt sammen med flere andre aktører i et hektisk prosjekt.

De er flinke, sier hva de mener og lager det vi ønsker. Softwaren de har laget
har fungert knirkefritt siden åpningen. Vi har et veldig godt inntrykk av hele
Kodemaker, og de fremstår som en dyktig, jovial og humørfylt gjeng."})
                :aside (e/round-media {:image "/devcard_images/person.png"
                                       :title "Geir Oterhals"
                                       :lines ["Prosjektleder, Oche Dart"
                                               "+47 992 18 320"]})}]}))

(defcard banner-section
  (sections/banner-section
   {:text "Kubernetes"
    :logo "/devcard_images/kubernetes.png"}))

(defcard bruce-section
  (sections/bruce-section
   {:title "Et unikt team av señiorutviklere"
    :text "Vi håndplukker erfarne konsulenter som er selvgående og trygge på
    sine meninger. Våre folk har sterkt fokus på kompetanseoverføring i team og
    tar komplekse oppgaver med tunge integrasjoner."
    :link {:text "Jobb med oss"
           :href "/jobb/"}
    :image-front "/devcard_images/bruce-front.png"
    :image-back "/devcard_images/bruce-back.png"}))

(defcard grid-section
  (sections/grid-section
   {:grid-type :box-grid
    :items [{:content
             (e/image-link
              {:image "/devcard_images/opencv.jpg"
               :alt "OpenCV"
               :href "https://opencv.org/"})}
            {:content
             (e/image-link
              {:image "/devcard_images/clojure.svg"
               :alt "Clojure"
               :href "/clojure/"})}
            {:content
             (e/image-link
              {:image "/devcard_images/python-logo.png"
               :alt "Python"
               :href "/python/"})
             :size 2}
            {:content
             (e/image-link
              {:image "/devcard_images/cljs.svg"
               :alt "ClojureScript"
               :href "/clojurescript/"})}
            {:content
             (e/image-link
              {:image "/devcard_images/kubernetes.png"
               :alt "Kubernetes"
               :href "/kubernetes/"})}
            {:content
             (e/image-link
              {:image "/devcard_images/kubernetes.png"
               :alt "Kubernetes"
               :href "/kubernetes/"})}
            {:content
             (e/image-link
              {:image "/devcard_images/kubernetes.png"
               :alt "Kubernetes"
               :href "/kubernetes/"})}]}))

(def card-data
  {:image "/devcard_images/profile.jpg"
   :title "Justin Moore"
   :lines ["Systemutvikler"
           "+47 934 17 480"
           "christin@kodemaker.no"]})

(def colors ["red" "blue" "yellow" "purple"])

(defcard grid-section
  (sections/grid-section
   {:grid-type :card-grid
    :items (->> [(assoc card-data :curtain :left)
                 card-data
                 card-data
                 card-data
                 card-data
                 (assoc card-data :curtain :right)]
                (map (fn [data] {:content (e/illustrated data)})))}))

(defcard profile-section
  (sections/profile-section
   {:full-name "Trygve M. Amundsen"
    :image "/devcard_images/trygve.jpg"
    :title "Systemutvikler"
    :mobile "+47 92 06 18 19"
    :mail "trygve@kodemaker.no"
    :cv {:text "Se full CV"
         :url "#"}
    :description [:div.text
                  [:p "Trygve har lang og solid erfaring innen faget systemutvikling. Han er entusiastisk opptatt av fagområdet, tilegner seg raskt ny kunnskap og følger nøye med på nyvinninger. Han evner å finne enkle løsninger på kompliserte problemstillinger. Han er resultatorientert og målrettet og leverer høy kvalitet. Han trives utmerket i det kunnskapsrike fagmiljøet i Kodemaker, og kommuniserer godt med kunder og kolleger."]]
    :presence {:twitter "#"
               :github "#"
               :linkedin "#"}
    :pønt [{:kind :greater-than
            :position "top -270px left 12%"}
           {:kind :dotgrid
            :position "bottom -150px right -150px"}]}))

(defcard pønt-section
  (sections/pønt-section
   {:portrait-1 "/devcard_images/pønt6.jpg"
    :portrait-2 "/devcard_images/pønt1.jpg"
    :top-triangle "/devcard_images/pønt5.png"
    :bottom-triangle "/devcard_images/pønt4.png"
    :top-circle "/devcard_images/pønt3.png"
    :bottom-circle "/devcard_images/pønt2.png"}))

(defcard seymour-section
  (sections/seymour-section
   {:pønt [{:kind :greater-than
            :position "bottom -550px left -310px"}
           {:kind :ascending-line
            :position "top -500px right -440px"}]
    :seymours
    [{:icon {:type :science/chemical :height 79}
      :title "Referanser"
      :text "Det er fleske meg ikke dårlig hvor mange artige prosjekter vi har fått være med på."
      :link {:text "Se referanser"
             :href "/referanser/"}}
     {:icon {:type :science/robot-1 :height 79}
      :title "Sjekk ut hvem vi har på laget 'æ, guttær!"
      :text "Vi har kun erfarne konsulenter med oss som liker å bryne seg på vanskelig oppgaver."
      :link {:text "Våre ansatte"
             :href "/folk/"}}
     {:icon {:type :computer/laptop-1 :height 79}
      :title "Kurs og workshops"
      :text "Her er en ganske kort tekst."
      :link {:text "Vi kan tilby"
             :href "/kurs/"}}]}))

(defcard vertigo-section
  (sections/vertigo-section
   {:title "Artikler og innsikt"
    :text "I Kodemaker sitter vi på mye kunnskap og erfaring innen et bredt
    spekter av forretningsområder og teknologi. Dette ønsker vi å dele med deg
    slik at vi sammen kan bli gode, og levere IT-prosjekter vi begge ønsker å
    skryte av og vise frem."
    :link {:text "Se mer"
           :href "/jobb/"}
    :image "/devcard_images/vertigo.jpg"}))

(defcard widescreen-section
  (sections/widescreen-section
   {:image "/devcard_images/geir.jpg"
    :alt "Geir Oterhals på Oche"}))
