(ns kodemaker-no.new-pages.frontpage)

(defn create-page []
  {:sections [{:kind :bruce
               :title "Engasjert miljø - erfarne utviklere"
               :text "Vi er ansvarlige, engasjerte og uformelle konsulenter. Vi er selvgående, trygge på våre meninger og utfordrer gjerne det eksisterende. Er du en av de som vet at et fåtall dyktige utviklere kan utgjøre en stor forskjell, så ta gjerne kontakt med oss."
               :link {:text "Ta kontakt"
                      :href "/kontakt/"}
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
                 :text "Vi har interessante og utfordende kunder. Sammen har vi utviklet spennende nye løsninger."
                 :link {:text "Se referanser"
                        :href "/referanser/"}}
                {:icon {:type :science/robot-1 :height 79}
                 :title "Hvem er kodemakerne?"
                 :text "Vi er erfarne, sultne og sosiale konsulenter som liker å bryne oss på de vanskeligere oppgavene."
                 :link {:text "Se kodemakere"
                        :href "/folk/"}}
                {:icon {:type :computer/laptop-1 :height 79}
                 :title "Jobbe hos oss"
                 :text "Det er ikke ofte vi ansetter nye Kodemakere, men kanskje du er vår neste kollega?"
                 :link {:text "Se jobb"
                        :href "/jobbe-hos-oss/"}}]}
              {:kind :vertigo
               :title "Kunnskap og innsikt"
               :text "I Kodemaker sitter vi på mye kunnskap og erfaring innen et bredt
    spekter av forretningsområder og teknologi. Vi liker å dele kunnskap, og er stadig ute etter ny lærdom selv. Vi har pleid å holde en del foredrag på konferanser og lignende, men i nåsitutasjonen er kanskje blogging det enkleste. Kanskje du finner noe interessant?"
               :link {:text "Les blogger"
                      :href "/blogg/"}
               :image "/foto/christin-foredrag.jpg"
               :image-center "53% 23%"}
              {:kind :pønt
               :portrait-1 {:img "/rouge-portrait/foto/ashild-f.jpg"
                            :href "/ashild/"
                            :title "Åshild Thorrud"}
               :portrait-2 {:img "/chocolate-portrait/foto/kristian-f.jpg"
                            :href "/kristian/"
                            :title "Kristian Frøhlich"}
               :top-triangle {:img "/rouge-triangle/foto/magnus-f.jpg"
                              :href "/magnus/"
                              :title "Magnus Rundberget"}
               :bottom-triangle {:img "/chocolate-triangle/foto/sjogutta.jpg"
                                 :href "/folk/"
                                 :title "Alle sammen"}
               :top-circle {:img "/rouge-circle/foto/finn-f3.jpg"
                            :href "/finn/"
                            :title "Finn Johnsen"}
               :bottom-circle {:img "/chocolate-circle/foto/blyanter2.jpg"
                               :href "/blogg/"
                               :title "Bloggen vår"}}
              {:kind :footer}]})
