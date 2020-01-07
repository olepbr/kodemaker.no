(ns ui.sections.profile-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

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
