(ns ui.layout-cards
  (:require [ui.layout :as layout]
            [devcards.core :refer-macros [defcard]]
            [sablono.core :refer [html]]))

(defcard logo
  (html (layout/logo {})))

(defcard header
  (html (layout/header)))

(defcard footer
  (html (layout/footer)))

(defcard pønt
  (html
   [:div
    {:style
     (-> {:background-color "#fffafa"
          :height "800px"
          :overflow "hidden"}
         (layout/add-pønt [{:kind :greater-than
                            :position "-300px -400px"}
                           {:kind :dotgrid
                            :position "right 422px"}
                           {:kind :descending-line
                            :position "right -400px top"}]))}]))

(defcard mer-pønt
  (html
   [:div
    {:style
     (-> {:background-color "#fffafa"
          :height "800px"
          :overflow "hidden"}
         (layout/add-pønt [{:kind :less-than
                            :position "right -300px top -400px"}
                           {:kind :dotgrid
                            :position "-57px 120px"}
                           {:kind :ascending-line
                            :position "right top"}]))}]))
