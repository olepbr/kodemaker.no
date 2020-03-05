(ns ui.sections.pønt-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard pønt-section
  (sections/pønt-section
   {:portrait-1 {:img "/devcard_images/pønt6.jpg"
                 :href "http://www.vg.no"
                 :title "Verdens Gang"}
    :portrait-2 {:img "/devcard_images/pønt1.jpg"
                 :href "http://www.vg.no"
                 :title "Verdens Gang"}
    :top-triangle {:img "/devcard_images/pønt5.png"
                   :href "http://www.vg.no"
                   :title "Verdens Gang"}
    :bottom-triangle {:img "/devcard_images/pønt4.png"
                      :href "http://www.vg.no"
                      :title "Verdens Gang"}
    :top-circle {:img "/devcard_images/pønt3.png"
                 :href "http://www.vg.no"
                 :title "Verdens Gang"}
    :bottom-circle {:img "/devcard_images/pønt2.png"
                    :href "http://www.vg.no"
                    :title "Verdens Gang"}}))
