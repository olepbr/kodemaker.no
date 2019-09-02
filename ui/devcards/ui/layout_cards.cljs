(ns ui.layout-cards
  (:require [ui.layout :as layout]
            [devcards.core :refer-macros [defcard]]
            [sablono.core :refer [html]]))

(defcard logo
  (html (layout/logo)))

(defcard pønt
  (html
   [:div
    {:style
     (-> {:background-color "#fffafa"
          :height "800px"
          :overflow "hidden"}
         (layout/add-pønt [{:kind :greater-than
                            :position "left -100px"}
                           {:kind :dotgrid
                            :position "right 422px"}]))}]))
