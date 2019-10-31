(ns ui.sections.profile-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.sections.profile-section :as section]))

(defcard
  (section/render
   {:full-name "Trygve M. Amundsen"
    :image "/devcard_images/trygve.jpg"
    :title "Systemutvikler"
    :mobile "+47 92 06 18 19"
    :mail "trygve@kodemaker.no"
    :description [:div.text
                  [:p "Trygve har lang og solid erfaring innen faget systemutvikling. Han er entusiastisk opptatt av fagområdet, tilegner seg raskt ny kunnskap og følger nøye med på nyvinninger. Han evner å finne enkle løsninger på kompliserte problemstillinger. Han er resultatorientert og målrettet og leverer høy kvalitet. Han trives utmerket i det kunnskapsrike fagmiljøet i Kodemaker, og kommuniserer godt med kunder og kolleger."]]}))
