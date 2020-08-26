(ns kodemaker-no.new-pages.tech-page
  (:require [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h :refer [map-vals max-by]]
            [kodemaker-no.new-pages.blog :as blog]
            [ui.elements :as e]))

(defn presentation-uri [pres]
  (or (:page/uri pres)
      (:presentation/video-url pres)))

(defn change-1st-person-to-3rd [value entity person-lookup]
  (str/replace value #"\b[jJ]eg\b"
               (let [person (person-lookup entity)]
                 (str "[" (:person/given-name person) "](" (:page/uri person) ")"))))

(defn choose-and-change [choose change k & args]
  (fn [entities]
    (when-let [entity (choose entities)]
      {k (apply change (k entity) entity args)})))

(defn merge-presentations [presentations]
  (-> (h/select-keys-by presentations
                        {:presentation/thumb first
                         :presentation/date first
                         :presentation/video-url first
                         :presentation/title first
                         :presentation/description (choose-and-change first
                                                                      change-1st-person-to-3rd
                                                                      :presentation/description
                                                                      :person/_presentations)
                         :page/uri first})
      (assoc :presentation/people (keep :person/_presentations presentations))))

(defn merge-recommendations [recommendations]
  (-> (h/select-keys-by recommendations
                        {:recommendation/title first
                         :recommendation/url first
                         :recommendation/description first})
      (assoc :recommendation/people (keep :person/_recommendations recommendations))))

(defn recommendations-section [tech]
  (when-let [merged-recommendations (->> (:recommendation/_techs tech)
                                         (group-by :recommendation/url)
                                         vals
                                         (map merge-recommendations)
                                         seq)]
    {:kind :titled
     :title "Våre anbefalinger"
     :contents (for [recommendation (->> merged-recommendations
                                         (sort-by (juxt (comp - count :recommendation/people)
                                                        (comp - count :recommendation/description)))
                                         (take 5))]
                 (e/teaser
                  {:title (:recommendation/title recommendation)
                   :tags (e/people-tags {:prefix "Anbefalt av"
                                         :people (:recommendation/people recommendation)
                                         :class "tags"})
                   :url (:recommendation/url recommendation)
                   :content (f/to-html (:recommendation/description recommendation))}))}))

(defn classify-presentations [tech]
  (let [all-presentations (->> (:presentation/_techs tech)
                               (group-by presentation-uri)
                               vals
                               (map merge-presentations)
                               (filter #(< 80 (count (:presentation/description % ""))))
                               (sort-by :presentation/date)
                               (reverse)
                               seq)
        presentation-stars (->> all-presentations
                                (filter :presentation/thumb)
                                (filter :presentation/video-url))
        headliner (when (:tech/description tech)
                    (first presentation-stars))
        to-show-in-grid (take 4 (remove #{headliner} presentation-stars))]
    {:headliner headliner
     :to-show-in-grid to-show-in-grid
     :remaining (remove (into #{headliner} to-show-in-grid)
                        all-presentations)}))

(defn presentations-section [{:keys [to-show-in-grid remaining]}]
  (when (seq (concat to-show-in-grid remaining))
    {:kind :titled
     :title "Våre foredrag"
     :contents (->> [(when (seq to-show-in-grid)
                       (e/tango-grid
                        (map
                         (fn [pres style video-class]
                           {:content [:div
                                      (e/video-thumb
                                       {:class (str style " " video-class)
                                        :img (str "/" style "/" (:presentation/thumb pres))
                                        :tags (e/people-tags {:class "tags"
                                                              :people (:presentation/people pres)})
                                        :url (presentation-uri pres)
                                        :title (:presentation/title pres)})
                                      [:div.text (f/to-html (:presentation/description pres))]]})
                         to-show-in-grid
                         ["video-thumb-rouge" "video-thumb-chocolate" "video-thumb-chocolate" "video-thumb-rouge"]
                         ["curtain curtain-short-right" nil nil "curtain curtain-short-top"])))
                     (when (seq remaining)
                       [:div
                        (for [pres remaining]
                          [:div.mbm
                           [:a.link {:href (presentation-uri pres)}
                            (:presentation/title pres)]
                           [:div
                            (e/people-tags {:class "tags"
                                            :people (:presentation/people pres)})]
                           [:div.text
                            (f/to-html (:presentation/description pres))]])])]
                    (remove nil?))}))

(defn blog-post-sections [tech]
  (when-let [posts (->> (:blog-post/_techs tech)
                        (group-by blog/post-url)
                        vals
                        (apply concat)
                        (sort-by :blog-post/published)
                        reverse
                        seq)]
    (-> (vec (blog/list-blog-posts posts))
        (assoc-in [0 :articles 0 :mecha-title] "Bloggposter"))))

(defn create-page [tech]
  (let [presentations (classify-presentations tech)]
    {:title (:tech/name tech)
     :sections
     (->>
      (concat
       [{:kind :header :background :chablis}
        {:kind :tech-intro
         :title (:tech/name tech)
         :logo (:tech/illustration tech)
         :article {:content [:div.text
                             (f/to-html (:tech/description tech))]
                   :alignment :front
                   :aside (when-let [headliner (:headliner presentations)]
                            [:div.hide-below-1000
                             (e/video-thumb
                              {:img (str "/rouge-duotone/" (:presentation/thumb headliner))
                               :tags (e/people-tags {:prefix "Av"
                                                     :class "tags"
                                                     :people (:presentation/people headliner)})
                               :url (presentation-uri headliner)
                               :title (:presentation/title headliner)})])}
         :pønt [{:kind :greater-than
                 :position "top -410px right 60vw"}
                {:kind :dotgrid
                 :position "top -110px left 80vw"}]}

        (recommendations-section tech)
        (presentations-section presentations)]
       (blog-post-sections tech)
       [{:kind :footer}])
      (remove nil?)
      (map (fn [color section]
             (assoc section :background color))
           (cycle [:chablis :blanc])))}))

(comment

  (require '[datomic-type-extensions.api :as d])
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))
  (def tech (d/entity db :tech/clojure))

  (create-page tech)

  (->> (:presentation/_techs tech)
       (group-by presentation-uri)
       vals
       (map merge-presentations))

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
