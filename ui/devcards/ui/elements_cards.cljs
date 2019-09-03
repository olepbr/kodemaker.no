(ns ui.elements-cards
  (:require [devcards.core :refer-macros [defcard]]
            [sablono.core :refer [html]]
            [ui.elements :as e]))

(defcard arrow-link
  (html (e/arrow-link {:text "Se mer"
                       :href "https://wwww.kodemaker.no"})))
