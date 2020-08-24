(ns kodemaker-no.new-pages.profile-page
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.new-pages.person :as person]
            [ui.elements :as e]))

(defn unwrap-idents [entity k]
  (map (partial d/entity (d/entity-db entity)) (k entity)))

(def technology-categories
  [[:person/favorites-at-the-moment "Favoritter for tiden:"]
   [:person/using-at-work "Bruker på jobben:"]
   [:person/want-to-learn-more "Vil lære mer:"]])

(defn any-technology-categories? [person]
  (boolean (some (fn [[k _]] (seq (k person))) technology-categories)))

(defn create-page [person]
  (let [cv-uri (:page/uri (:cv/_person person))]
    {:title (:person/full-name person)
   :sections
   (->>
    [{:kind :profile
      :full-name (:person/full-name person)
      :image (:person/profile-page-picture person)
      :title (:person/title person)
      :mobile (:person/phone-number person)
      :mail (:person/email-address person)
         :cv (when cv-uri
               {:text "Se full CV"
                :url cv-uri})
      :description (f/markdown (:person/description person))
      :presence (person/prep-presence-links (:person/presence person))
      :pønt [{:kind :greater-than
              :position "top -270px left 12%"}
             {:kind :dotgrid
              :position "bottom -150px right -150px"}]}

     ;; Teknologi

     (when (any-technology-categories? person)
       {:kind :enumeration
        :title "Teknologi"
        :categories (->> (for [[k label] technology-categories]
                           {:label label
                            :items (for [tech (unwrap-idents person k)]
                                     {:text (:tech/name tech)
                                      :href (:page/uri tech)})})
                         (remove (comp empty? :items)))
        :pønt [{:kind :dotgrid
                :position "top -344px right -150px"}]})

     ;; Anbefalinger

     (when-let [recommendations (seq (:person/recommendations person))]
       {:kind :titled
        :title (str (f/genitive-name (:person/given-name person)) " anbefalinger")
        :contents (for [recommendation (take 3 (sort-by :list/idx recommendations))]
                    (e/teaser
                     (cond-> {:title (:recommendation/title recommendation)
                              :tags (e/tech-tags {:techs (take 5 (h/unwrap-ident-list recommendation :recommendation/tech-list))
                                                  :class "tags"})
                              :url (:recommendation/url recommendation)
                              :content (f/to-html (:recommendation/description recommendation))}
                       (:recommendation/link-text recommendation)
                       (assoc :link {:text (:recommendation/link-text recommendation)
                                     :href (:recommendation/url recommendation)}))))})

     ;; Snakker gjerne om

     (when-let [[uno dos] (seq (:person/hobbies person))]
       (let [hobby-1 {:title (:title uno)
                      :content (f/to-html (:description uno))
                      :image (str "/big-bottom-half-circle" (:illustration uno))}
             hobby-2 (when dos
                       {:title (:title dos)
                        :content (f/to-html (:description dos))
                        :image (str "/hobby-square" (:illustration dos))})]
         {:kind :hip
          :title (str (:person/given-name person) " snakker gjerne om")
          (if hobby-2 :left :single) hobby-1
          :right hobby-2}))

     ;; Bloggposter

     (when-let [blog-posts (seq (:blog-post/_author person))]
       {:kind :titled
        :title "Bloggposter"
        :contents (for [blog-post (take 3 (reverse (sort-by :blog-post/published blog-posts)))]
                    (let [url (or (:page/uri blog-post)
                                  (:blog-post/external-url blog-post))]
                      (e/teaser
                         {:title (:blog-post/title blog-post)
                          :tags (e/tech-tags {:techs (take 5 (h/unwrap-ident-list blog-post :blog-post/tech-list))
                                              :class "tags"})
                          :url url
                          :content (f/to-html (:blog-post/blurb blog-post))
                          :link (when url {:text "Les artikkel" :href url})})))})

       ;; Foredrag (videoer)

       (when-let [presentations (->> (:person/presentations person)
                                     (filter :presentation/thumb)
                                     (filter :presentation/video-url)
                                     (sort-by :list/idx)
                                     seq)]
         {:kind :titled
          :title "Foredrag"
          :contents [(e/tango-grid
                      (map
                       (fn [pres style video-class grid-class]
                         {:class grid-class
                          :content (e/video-thumb
                                    {:class (str style " " video-class)
                                     :img (str "/" style "/" (:presentation/thumb pres))
                                     :tags (e/tech-tags {:class "tags"
                                                         :techs (take 5 (h/unwrap-ident-list pres :presentation/tech-list))})
                                     :url (or (:page/uri pres)
                                              (:presentation/video-url pres))
                                     :title (:presentation/title pres)})})
                       (take 4 (reverse (sort-by :presentation/date presentations)))
                       ["video-thumb-rouge" "video-thumb-chocolate" "video-thumb-chocolate" "video-thumb-rouge"]
                       ["curtain curtain-short-right" nil nil "curtain curtain-short-top"]
                       [nil nil "hide-below-600" "hide-below-600"]))]})

       ;; Prosjekter

       (when-let [projects (->> (:person/projects person)
                                (sort-by :list/idx)
                                seq)]
         {:kind :titled
          :title "Prosjekter"
          :contents (let [num (count projects)
                          to-show (if cv-uri 3 8)
                          show-more-link? (and cv-uri (< to-show num))]
                      (->
                       (for [project (take to-show projects)]
                         [:div
                          [:div.h6.b (:project/customer project)]
                          (e/tech-tags {:class "tags"
                                        :techs (take 5 (h/unwrap-ident-list project :project/tech-list))})
                          [:div.mts
                           (f/to-html (:project/description project))]])
                       vec
                       (cond-> show-more-link?
                         (conj (e/arrow-link {:text "Se flere prosjekter"
                                              :href (str cv-uri "#prosjekter")})))))})

       ;; Referanser

       (when-let [endorsements (->> (:person/endorsements person)
                                    (sort-by :list/idx)
                                    seq)]
         {:kind :titled
          :title "Referanser"
          :contents (let [num (count endorsements)
                          to-show (if cv-uri 3 8)
                          show-more-link? (and cv-uri (< to-show num))]
                      (->
                       (for [endorsement (take to-show endorsements)]
                         [:div
                          [:div.h6.b (:author endorsement)]
                          [:div.text-s.annotation.mts (:title endorsement)]
                          [:div.mts.text
                           (f/to-html (str "«" (:quote endorsement) "»"))]])
                       vec (cond-> show-more-link?
                             (conj (e/arrow-link {:text "Se flere referanser"
                                                  :href (str cv-uri "#anbefalinger")})))))})


       {:kind :footer}]
      (remove nil?)
      (map (fn [color section]
             (assoc section :background color))
           (cycle [:blanc :chablis])))}))

(comment
  (def conn (:datomic/conn integrant.repl.state/system))
  (def db (d/db conn))

  (def person (d/entity db :person/magnar))

  (map kodemaker-no.render-new-page/render-section (:sections (create-page person)))


  )
