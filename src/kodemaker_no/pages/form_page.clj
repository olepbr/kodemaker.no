(ns kodemaker-no.pages.form-page
  (:require [kodemaker-no.formatting :refer [to-html]]
            [kodemaker-no.render-old-page :refer [render-body]]
            [kodemaker-no.homeless :refer [update-in-existing]]))

(defn form-page [articles]
  {:title "La oss ta en prat"
   :body (list
          [:p "Hva med å finne ut om vi er en god match for hverandre? Legg igjen
                 telefonnummer eller epost-adresse, så tar Kolbjørn kontakt med deg."]
          [:form.form.mod {:action "/send-mail"
                           :method "POST"}
           [:input.input {:type "text", :name "kontakt"}]
           [:div
            [:button.btn {:type "submit"} "Ja, la oss ta en prat, Kolbjørn"]]])})
