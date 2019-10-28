(ns ui.sections.banner-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.sections.banner-section :as section]))

(defcard
  (section/render
   {:text "Kubernetes"
    :logo "/devcard_images/kubernetes.png"}))
