(ns kodemaker-no.images
  (:require [imagine.core :as imagine]))

(def rouge-duotone
  [:duotone [255 82 75] [255 255 255]])

(def chocolate-au-lait-duotone
  [:duotone [89 30 30] [255 255 255]])

(def image-asset-config
  {:prefix "image-assets"
   :resource-path "public"
   :transformations

   {:vcard-small
    {:transformations [[:fit {:width 184 :height 184}]
                       [:crop {:preset :square}]]
     :retina-optimized? true
     :width 92}

    :vcard-medium
    {:transformations [[:fit {:width 240 :height 240}]
                       [:crop {:preset :square}]]
     :retina-optimized? true
     :width 120}

    :bruce-front
    {:transformations
     [[:resize {:smallest 774}]
      [:crop {:preset :square}]
      rouge-duotone
      [:circle]
      [:triangle :lower-left]
      [:crop {:width 666
              :height 666
              :offset-y :bottom}]]}

    :bruce-back
    {:transformations
     [[:resize {:smallest 774}]
      [:crop {:preset :square}]
      chocolate-au-lait-duotone
      [:circle]
      [:triangle :lower-right]
      [:crop {:width 666
              :height 666
              :offset-x :right
              :offset-y :bottom}]]}

    :rouge-duotone
    {:transformations [rouge-duotone]}

    :mega-banner
    {:transformations
     [chocolate-au-lait-duotone
      ;;rouge-duotone
      ]
     :width 1400
     :retina-optimized? true}

    :vertigo
    {:height 850
     :retina-optimized? true}

    :rouge-portrait-pønt
    {:transformations [[:fit {:width 670 :height 800}]
                       rouge-duotone]
     :width 335
     :retina-optimized? true}

    :chocolate-portrait-pønt
    {:transformations [[:fit {:width 670 :height 800}]
                       chocolate-au-lait-duotone]
     :width 335
     :retina-optimized? true}

    :rouge-circle
    {:transformations
     [[:fit {:width 900 :height 900}]
      [:crop {:preset :square}]
      rouge-duotone
      [:circle]
      [:triangle :upper-right]
      [:crop {:width 775
              :offset-x :right
              :offset-y :top}]]}

    :chocolate-circle
    {:transformations
     [[:fit {:width 900 :height 900}]
      [:crop {:preset :square}]
      chocolate-au-lait-duotone
      [:circle]
      [:triangle :lower-left]
      [:crop {:width 775
              :offset-x :left
              :offset-y :bottom}]]}

    :chocolate-triangle
    {:transformations [[:fit {:width 220 :height 220}]
                       [:crop {:preset :square}]
                       chocolate-au-lait-duotone
                       [:triangle :lower-left]]}

    :rouge-triangle
    {:transformations [[:fit {:width 220 :height 220}]
                       [:crop {:preset :square}]
                       rouge-duotone
                       [:triangle :upper-right]]}

    :bottom-half-circle
    {:transformations [[:fit {:width 380 :height 380}]
                       [:crop {:preset :square}]
                       chocolate-au-lait-duotone
                       [:circle]
                       [:crop {:width 380 :height 190 :offset-y :bottom}]]}

    :rouge-triangle-medium
    {:transformations [[:fit {:width 380 :height 380}]
                       [:crop {:preset :square}]
                       rouge-duotone
                       [:triangle :lower-left]]}

    :profile-medium
    {:transformations [[:fit {:width 600 :height 800}]]
     :retina-optimized? true
     :width 300}}})

(defn url-to [transform file-path]
  (imagine/url-to image-asset-config transform file-path))
