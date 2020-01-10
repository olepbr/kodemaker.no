(ns kodemaker-no.new-pages.tech-page
  (:require [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h :refer [map-vals max-by]]
            [ui.elements :as e]))

(defn get-main-aside [merged-presentations]
  (when-let [pres (some->> merged-presentations
                           (filter :presentation/thumb)
                           (max-by :presentation/date))]
    [:div.hide-below-1000
     (e/video-thumb
      {:img (str "/rouge-duotone/" (:presentation/thumb pres))
       :tags (e/people-tags {:prefix "Av"
                             :class "tags"
                             :people (:presentation/people pres)})
       :url (or (:page/uri pres)
                (:presentation/video-url pres))
       :title (:presentation/title pres)})]))

(defn presentation-uri [pres]
  (or (:page/uri pres)
      (:presentation/video-url pres)))

(defn merge-presentations [presentations]
  (-> (h/select-first-keys presentations
                         #{:presentation/thumb
                           :presentation/date
                           :presentation/video-url
                           :presentation/title
                           :page/uri})
      (assoc :presentation/people (keep :person/_presentations presentations))))

(defn merge-recommendations [recommendations]
  (-> (h/select-first-keys recommendations
                           #{:recommendation/title
                             :recommendation/url
                             :recommendation/description
                             :recommendation/link-text})
      (assoc :recommendation/people (keep :person/_recommendations recommendations))))

(defn create-page [tech]
  (let [merged-presentations (->> (:presentation/_techs tech)
                                  (group-by presentation-uri)
                                  vals
                                  (map merge-presentations))
        merged-recommendations (->> (:recommendation/_techs tech)
                                    (group-by :recommendation/url)
                                    vals
                                    (map merge-recommendations))]
    {:sections
     (->>
      [{:kind :header :background :chablis}
       {:kind :tech-intro
        :title (:tech/name tech)
        :logo (:tech/illustration tech)
        :article {:content [:div.text
                            (f/to-html (:tech/description tech))]
                  :alignment :front
                  :aside (get-main-aside merged-presentations)}
        :pønt [{:kind :greater-than
                :position "top -410px right 60vw"}
               {:kind :dotgrid
                :position "top -110px left 80vw"}]}

       ;; Anbefalinger

       (when (seq merged-recommendations)
         {:kind :titled
          :title "Våre anbefalinger"
          :contents (for [recommendation (->> merged-recommendations
                                              (sort-by (juxt (comp - count :recommendation/people)
                                                             (comp - count :recommendation/description)))
                                              (take 5))]
                      (e/teaser
                       (cond-> {:title (:recommendation/title recommendation)
                                :tags (e/people-tags {:prefix "Anbefalt av"
                                                      :people (:recommendation/people recommendation)
                                                      :class "tags"})
                                :url (:recommendation/url recommendation)
                                :content (f/to-html (:recommendation/description recommendation))}
                         (:recommendation/link-text recommendation)
                         (assoc :link {:text (:recommendation/link-text recommendation)
                                       :href (:recommendation/url recommendation)}))))})

       {:kind :footer}]
      (remove nil?)
      (map (fn [color section]
             (assoc section :background color))
           (cycle [:chablis :blanc])))}))

(comment

  (require '[datomic-type-extensions.api :as d])
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))


  (->>
   (for [[e file] (d/q '[:find ?e ?file
                         :where
                         [?e :presentation/video-url _ ?tx]
                         [?tx :tx-source/file-name ?file]] db)]
     (let [pres (d/entity db e)]
       [file
        (:presentation/video-url pres)
        (:presentation/thumb pres)
        (:presentation/title pres)]))
   (group-by first)
   (map-vals #(mapv (comp vec next) %)))




  )
