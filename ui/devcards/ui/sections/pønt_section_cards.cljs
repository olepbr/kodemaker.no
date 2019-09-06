(ns ui.sections.pønt-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.sections.pønt-section :as section]))

(defcard
  (section/render
   {:portrait-1 "/devcard_images/pønt6.jpg"
    :portrait-2 "/devcard_images/pønt1.jpg"
    :top-triangle "/devcard_images/pønt5.png"
    :bottom-triangle "/devcard_images/pønt4.png"
    :top-circle "/devcard_images/pønt3.png"
    :bottom-circle "/devcard_images/pønt2.png"}))
