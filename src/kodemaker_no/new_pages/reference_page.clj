(ns kodemaker-no.new-pages.reference-page
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [ui.elements :as e])
  (:import java.time.format.DateTimeFormatter))

(defmulti render-section (fn [db reference section] (:type section)))

(defmethod render-section :default [_ _ _] nil)

(def alignments
  {"front" :front
   "back" :back
   "balanced" :balanced})

(defn alignment [default {:keys [alignment]}]
  (or (alignments alignment) default))

(defn image-url [default-style {:keys [image image-style]}]
  (when image
    (format "/%s%s" (or default-style image-style) image)))

(defmethod render-section :about [_ _ {:keys [title sub-title body] :as section}]
  {:kind :article
   :class "responsive-article-dots"
   :articles [{:alignment (alignment :front section)
               :image (image-url "bottom-half-circle" section)
               :mecha-title title
               :mecha-sub-title sub-title
               :content (f/markdown body)}]})

(def mmYYYY (DateTimeFormatter/ofPattern "MM.YYYY"))

(defmethod render-section :reference-meta
  [db
   {:reference/keys [project-hours project-start project-end team]}
   {:keys [title body]}]
  {:kind :article
   :background :blanc-rose
   :articles
   [{:title title
     :content (f/to-html body)
     :aside (e/stats
             {:icon-type :custom/person
              :icon-count (count team)
              :stats [(let [developers (count team)]
                        (if (= 1 developers)
                          "Én Kodemaker"
                          (format "%s Kodemakere" (count team))))
                      (let [start (.format project-start mmYYYY)
                            end (.format project-end mmYYYY)]
                        (if project-hours
                          (format "%s timer / %s-%s" project-hours start end)
                          (format "%s-%s" start end)))]})}]})

(defmethod render-section :grid [db {:reference/keys [grid-blocks]} _]
  {:kind :grid
   :grid-type :box-grid
   :items (for [{:block/keys [url image size]} (sort-by :block/idx grid-blocks)]
            {:content (e/image-link {:href url :image image})
             :size size})})

(defmethod render-section :illustrated-column [_ _ {:keys [title sub-title body] :as section}]
  {:kind :article
   :articles [{:alignment (alignment :back section)
               :image (image-url "rouge-triangle-medium" section)
               :title title
               :sub-title sub-title
               :content (f/markdown body)}]})

(defmethod render-section :participants [db {:reference/keys [team]} {:keys [title]}]
  (let [articles
        (->> team
             (mapv (fn [{:project-participation/keys [person role]}]
                     (let [{:person/keys [given-name family-name title phone-number email-address portraits]}
                           (d/entity db [:db/ident person])]
                       {:sub-title (format "%s %s" given-name family-name)
                        :content (f/markdown role)
                        :alignment :back
                        :aside (e/vert-round-media
                                {:image (some->> portraits first (str "/vcard-small"))
                                 :lines [title
                                         phone-number
                                         email-address]})}))))]
    {:kind :article
     :articles (-> articles
                   (assoc-in [0 :mecha-sub-title] title)
                   (assoc-in [0 :mecha-sub-title-style] "h3"))}))

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
      :image (str "/mega-banner" image)
      :background :blanc-rose
      :alt (format "%s, %s" signee-name signee-title)}
     {:kind :article
      :background :blanc-rose
      :pønt [{:kind :ascending-line
              :position "top 0 left -400px"}]
      :articles [{:alignment :back
                  :content (e/blockquote {:quote blurb})
                  :aside (e/round-media {:image (some->> portrait (str "/vcard-small"))
                                         :title signee-name
                                         :lines [signee-title
                                                 signee-phone]})}]}]
    (keep (partial render-section (d/entity-db reference) reference) (sort-by :idx sections))
    [{:kind :footer}])})
