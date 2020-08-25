(ns kodemaker-no.new-pages.profile-page
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.new-pages.open-source :as oss]
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

(defn video-grid [items]
  (e/tango-grid
   (map
    (fn [{:keys [image tech-list url title]} style video-class grid-class]
      {:class grid-class
       :content (e/video-thumb
                 {:class (str style " " video-class)
                  :img (str "/" style "/" image)
                  :tags (e/tech-tags {:class "tags"
                                      :techs (take 5 tech-list)})
                  :url url
                  :title title})})
    (take 4 items)
    ["video-thumb-rouge" "video-thumb-chocolate" "video-thumb-chocolate" "video-thumb-rouge"]
    ["curtain curtain-short-right" nil nil "curtain curtain-short-top"]
    [nil nil "hide-below-600" "hide-below-600"])))

(defn by-preferred-techs [person tech-f xs]
  (let [tech-order (->> (:person/preferred-techs person)
                        (sort-by :list/idx)
                        (map :list/ref))
        indifferent (count (:person/preferred-techs person))]
    (sort-by #(let [idx (.indexOf tech-order (tech-f %))]
                (if (<= 0 idx)
                  idx
                  indifferent)) xs)))

(defn open-source-contributions [person]
  (->>
   (concat (sort-by :list/idx (:person/open-source-projects person))
           (sort-by :list/idx  (:person/open-source-contributions person)))
   (group-by oss/significant-tech)
   (sort-by (comp - count second))
   (by-preferred-techs person (comp :db/ident first))
   (map (fn [[tech xs]]
          {:tech tech
           :projects (remove :oss-project/contribution? xs)
           :contributions (filter :oss-project/contribution? xs)}))))

(defn oss-project-link [{:oss-project/keys [url name]}]
  [:a {:href url} name])

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

       (when-let [[uno dos] (seq (sort-by :list/idx (:person/hobbies person)))]
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
          :contents [(video-grid
                      (->> (sort-by :presentation/date presentations)
                           reverse
                           (map (fn [pres]
                                  {:image (:presentation/thumb pres)
                                   :tech-list (h/unwrap-ident-list pres :presentation/tech-list)
                                   :title (:presentation/title pres)
                                   :url (or (:page/uri pres)
                                            (:presentation/video-url pres))}))))]})

       ;; Screencasts

       (when-let [screencasts (seq (:person/screencasts person))]
         {:kind :titled
          :title "Screencasts"
          :contents (for [screencast (take 3 (sort-by :list/idx screencasts))]
                      (let [url (:screencast/url screencast)]
                        (e/illustrated-teaser
                         {:title (:screencast/title screencast)
                          :tags (e/tech-tags {:techs (take 5 (h/unwrap-ident-list screencast :screencast/tech-list))
                                              :class "tags"})
                          :url url
                          :illustration (:screencast/illustration screencast)
                          :content (f/to-html (:screencast/blurb screencast))
                          :link (when url {:text "Se screencast" :href url})})))})

       ;; Sideprosjekter

       (when-let [side-projects (seq (:person/side-projects person))]
         {:kind :titled
          :title "Sideprosjekter"
          :contents (for [side-project (take 3 (sort-by :list/idx side-projects))]
                      (let [url (:side-project/url side-project)]
                        (e/illustrated-teaser
                         {:title (:side-project/title side-project)
                          :tags (e/tech-tags {:techs (take 5 (h/unwrap-ident-list side-project :side-project/tech-list))
                                              :class "tags"})
                          :url url
                          :illustration (:side-project/illustration side-project)
                          :content (f/to-html (:side-project/description side-project))
                          :link (when url {:text (:side-project/link-text side-project) :href url})})))})

       ;; Open source

       (when-let [oss-techs (seq (open-source-contributions person))]
         {:kind :titled
          :title "Open source"
          :contents (for [{:keys [tech projects contributions]} oss-techs]
                      (e/teaser
                       {:title [:h3.h4 (:tech/name tech)]
                        :content [:ul
                                  (for [project projects]
                                    [:li "Utviklet " (oss-project-link project)
                                     ". " (f/markdown (:oss-project/description project))])
                                  (when (seq contributions)
                                    [:li "Har bidratt til "
                                     (f/comma-separated (map oss-project-link contributions))])]}))})

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

  (open-source-contributions person)

  (into {} (first (:person/open-source-contributions person)))


  (map kodemaker-no.render-new-page/render-section (:sections (create-page person)))


  )
