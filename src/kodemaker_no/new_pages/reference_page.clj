(ns kodemaker-no.new-pages.reference-page
  (:require [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :refer [map-vals max-by]]
            [ui.elements :as e]))

(defn create-page [{:reference/keys [image signee-name signee-title signee-phone page-title
                                     portrait blurb]}]
  {:title page-title
   :sections
   [{:kind :header
     :pønt [{:kind :descending-line
             :position "left 33% top 0"}
            {:kind :descending-line
             :position "left 80vw top 0"}]}
    {:kind :widescreen
     :image (str "/mega-banner/" image)
     :alt (format "%s, %s" signee-name signee-title)}
    {:kind :article
     :article {:alignment :back
               :content (e/blockquote {:quote blurb})
               :aside (e/round-media {:image (str "/vcard-small/" portrait)
                                      :title signee-name
                                      :lines [signee-title
                                              signee-phone]})}}
    {:kind :footer}]})
