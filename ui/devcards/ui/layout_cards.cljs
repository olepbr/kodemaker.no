(ns ui.layout-cards
  (:require [ui.layout :as layout]
            [dumdom.devcards :refer-macros [defcard]]))

(defcard logo
  (layout/logo {}))

(defcard header
  (layout/header))

(defcard footer
  (layout/footer))

(defcard pønt
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
                           :position "right -400px top"}]))}])

(defcard mer-pønt
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
                           :position "right top"}]))}])
