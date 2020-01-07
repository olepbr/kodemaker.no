(ns ui.sections.banner-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard banner-section
  (sections/banner-section
   {:text "Kubernetes"
    :logo "/devcard_images/kubernetes.png"}))
