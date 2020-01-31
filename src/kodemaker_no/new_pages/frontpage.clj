(ns kodemaker-no.new-pages.frontpage)

(defn create-page []
  {:sections [{:kind :bruce
               :title "Et unikt team av seniorutviklere"
               :text "Vi håndplukker erfarne konsulenter som er selvgående og
                      trygge på sine meninger. Våre folk har sterkt fokus på
                      kompetanseoverføring i team og tar komplekse oppgaver med
                      tunge integrasjoner."
               :link {:text "Hvem er Kodemaker?"
                      :href "/jobb/"}
               :image-front "/bruce-front/foto/folk.jpg"
               :image-back "/bruce-back/foto/arduino.jpg"}
              {:kind :seymour
               :background :blanc-rose
               :pønt [{:kind :greater-than
                       :position "bottom -550px left -310px"}
                      {:kind :ascending-line
                       :position "top -500px right -440px"}]
               :seymours
               [{:icon {:type :science/chemical :height 79}
                 :title "Referanser"
                 :text "Vi har kun erfarne konsulenter med oss som liker å bryne seg på vanskelige oppgaver."
                 :link {:text "Se referanser"
                        :href "/referanser/"}}
                {:icon {:type :science/robot-1 :height 79}
                 :title "Se hvem vi har på laget"
                 :text "Vi har kun erfarne konsulenter med oss som liker å bryne seg på vanskelig oppgaver."
                 :link {:text "Våre ansatte"
                        :href "/folk/"}}
                {:icon {:type :computer/laptop-1 :height 79}
                 :title "Kurs og workshops"
                 :text ""
                 :link {:text "Vi kan tilby"
                        :href "/kurs/"}}]}
              {:kind :vertigo
               :title "Artikler og innsikt"
               :text "I Kodemaker sitter vi på mye kunnskap og erfaring innen et bredt
    spekter av forretningsområder og teknologi. Dette ønsker vi å dele med deg
    slik at vi sammen kan bli gode, og levere IT-prosjekter vi begge ønsker å
    skryte av og vise frem."
               :link {:text "Se mer"
                      :href "/jobb/"}
               :image "/foto/nils-1.jpg"}
              {:kind :pønt
               :portrait-1 "/rouge-portrait/foto/christian-1.jpg"
               :portrait-2 "/chocolate-portrait/foto/christian-1.jpg"
               :top-triangle "/rouge-triangle/foto/christian-1.png"
               :bottom-triangle "/chocolate-triangle/foto/christian-1.png"
               :top-circle "/rouge-circle/foto/christian-1.png"
               :bottom-circle "/chocolate-circle/foto/christian-1.png"}
              {:kind :footer}]})
