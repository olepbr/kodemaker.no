(ns kodemaker-no.pages.form-page
  (:require [kodemaker-no.formatting :refer [to-html]]
            [kodemaker-no.render-page :refer [render-body]]
            [kodemaker-no.homeless :refer [update-in-existing]]))

(defn form-page [articles]
  (let [contact (articles "/kontakt.md")]
    {:title "Hva kan vi hjelpe deg med?"
     :body (list
            [:p
             "Vi tar utviklingsoppdrag, lager workshops for deg, eller holder kurs og foredrag."]
            [:p "For å hjelpe oss plukke ut oppdrag vi har tro på, vil vi at du
            fyller ut dette skjemaet. Vi tar kontakt med
            deg så snart vi har mulighet. Stort sett vil du høre fra oss innen to
            arbeidsdager."]
            [:form.form.mod {:action "/send-mail"
                             :method "POST"}
             [:label "Skriv litt om planene dine"]
             [:textarea.input {:rows 8, :name "tekst"}]
             [:div.line
              [:div.unit.r-1of2
               [:label "Når ønsker du å starte?"]
               [:input.input {:type "text", :name "oppstart"}]]
              [:div.lastUnit
               [:label "Hvilket omfang ser du for deg?"]
               [:input.input {:type "text", :name "omfang"}]]]
             [:label "Hvordan får vi tak i deg?"]
             [:input.input {:type "text", :name "kontakt"}]
             [:div
              [:button.btn {:type "submit"} "Send skjema"]]]
            [:h2 "&nbsp;"]
            [:h1.hn (:title contact)]
            [:div.bd
             (render-body (-> contact
                              (update-in-existing [:lead] to-html)
                              (update-in-existing [:aside] to-html)
                              (update-in-existing [:body] to-html)))])}))
