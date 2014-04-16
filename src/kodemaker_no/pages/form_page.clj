(ns kodemaker-no.pages.form-page
  (:require [kodemaker-no.formatting :refer [to-html]]))

(defn form-page []
  {:title "Hva kan vi hjelpe deg med?"
   :body (list
          [:form.form.mod {:action "/send-mail"
                           :method "POST"}
           [:label "Prosjektbeskrivelse:"]
           [:textarea.input {:rows 4, :name "tekst"}]
           [:label "Omtrentlig omfang:"]
           [:input.input {:type "text", :name "omfang"}]
           [:label "Ønsket oppstart:"]
           [:input.input {:type "text", :name "oppstart"}]
           [:label "Kontaktinformasjon:"]
           [:input.input {:type "text", :name "kontakt"}]
           [:div
            [:button.btn {:type "submit"} "Send skjema"]]])})
