(ns ui.typography-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]))

(defcard h1
  (e/h1 {} "Et unikt team av seniortviklere"))

(defcard h2-acting-as-h1
  (e/h1 {:element :h2} "Et unikt team av seniortviklere"))

(defcard h2
  (e/h2 {} "Artikler og innsikt"))

(defcard h1-acting-as-h2
  (e/h2 {:element :h1} "Artikler og innsikt"))

(defcard h3
  (e/h3 {} "Vi har levert"))

(defcard h1-acting-as-h3
  (e/h3 {:element :h1} "Vi har levert"))

(defcard h4
  (e/h4 {} "Se hvem vi er på laget"))

(defcard h1-acting-as-h4
  (e/h4 {:element :h1} "Se hvem vi er på laget"))

(defcard h4-light
  [:h4.h4-light "Se hvem vi er på laget"])

(defcard h5
  (e/h5 {} "Kontakt"))

(defcard h6
  (e/h6 {} "Kontakt"))

(defcard h1-acting-as-h5
  (e/h5 {:element :h1} "Kontakt"))

(defcard paragraph
  [:p {} "To Kodemakere står bak det teknologiske ved Oche sitt automatiserte konsept for dartspill. Bildeanalyse, spillutvikling, grensesnitt og oppsett/overvåkning av maskiner og utstyr."])

(defcard large-text
  [:p.text-l {} "The text, she is very big"])

(defcard small-text
  [:p.text-s {} "The text, she is not so big"])

