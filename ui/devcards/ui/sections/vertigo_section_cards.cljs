(ns ui.sections.vertigo-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

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
