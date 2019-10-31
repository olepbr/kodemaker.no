(ns kodemaker-no.new-pages.reference-page
  (:require [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :refer [map-vals max-by]]
            [ui.elements :as e]))

(defmulti render-section (fn [reference section] (:type section)))

(defmethod render-section :default [_ _] nil)

(defmethod render-section :illustrated-column [reference {:keys [title body]}]
  {:kind :article
   :article {:alignment :front
             :content body}})

(defmethod render-section :participants [{:reference/keys [project-hours project-start project-end team]}
                                         {:keys [title]}]
  {:kind :article
   :article {:title title}
   :aside [:div
           (let [developers (count team)]
             (if (= 1 developers)
               [:p "Én Kodemaker"]
               [:p (str "%s Kodemakere" (count team))]))
           [:p (if project-hours
                 (format "%s / %s-%s" project-hours project-start project-end)
                 (format "%s-%s" project-start project-end))]]
   :background "var(--blanc-rose)"})

(defn create-page [{:reference/keys [image signee-name signee-title signee-phone page-title
                                     portrait blurb sections] :as reference}]
  {:title page-title
   :sections
   (concat
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
                                               signee-phone]})}}]
    (keep (partial render-section reference) (sort-by :idx sections))
    [{:kind :footer}])})
