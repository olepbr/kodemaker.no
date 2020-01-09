(ns kodemaker-no.images
  (:require [imagine.core :as imagine]))

(def rouge-duotone
  [:duotone [255 82 75] [255 255 255]])

(def chocolate-au-lait-duotone
  [:duotone [89 30 30] [255 255 255]])

(def image-asset-config
  {:prefix "image-assets"
   :resource-path "public"
   :disk-cache? true
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
              :origin-y :bottom}]]}

    :bruce-back
    {:transformations
     [[:resize {:smallest 774}]
      [:crop {:preset :square}]
      chocolate-au-lait-duotone
      [:circle]
      [:triangle :lower-right]
      [:crop {:width 666
              :height 666
              :origin [:right :bottom]}]]}

    :video-thumb-rouge
    {:transformations [[:fit {:width 640 :height 360 :scale-up? true :offset-x :left :offset-y :bottom}]
                       rouge-duotone]}

    :video-thumb-chocolate
    {:transformations [[:fit {:width 640 :height 360 :scale-up? true :offset-x :left :offset-y :bottom}]
                       chocolate-au-lait-duotone]}

    :rouge-duotone
    {:transformations [rouge-duotone]}

    :mega-banner
    {:transformations
     [chocolate-au-lait-duotone]
     :width 1400
     :retina-optimized? true}

    :vertigo
    {:height 850
     :retina-optimized? true}

    :hobby-square
    {:transformations [[:fit {:width 1200 :height 1200 :scale-up? true}]
                       rouge-duotone]
     :width 600
     :retina-optimized? true}

    :rouge-portrait
    {:transformations [[:fit {:width 670 :height 800 :scale-up? true}]
                       rouge-duotone]
     :width 335
     :retina-optimized? true}

    :chocolate-portrait
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
              :origin [:right :top]}]]}

    :chocolate-circle
    {:transformations
     [[:fit {:width 900 :height 900}]
      [:crop {:preset :square}]
      chocolate-au-lait-duotone
      [:circle]
      [:triangle :lower-left]
      [:crop {:width 775
              :origin [:left :bottom]}]]}

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
    {:transformations [[:fit {:width 380 :height 380 :scale-up? true}]
                       [:crop {:preset :square}]
                       chocolate-au-lait-duotone
                       [:circle]
                       [:crop {:width 380 :height 190 :origin-y :bottom}]]}

    :big-bottom-half-circle
    {:transformations [[:fit {:width 600 :height 600 :scale-up? true}]
                       [:crop {:preset :square}]
                       chocolate-au-lait-duotone
                       [:circle]
                       [:crop {:width 600 :height 300 :origin-y :bottom}]]}

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
