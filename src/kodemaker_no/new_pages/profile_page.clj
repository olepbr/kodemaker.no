(ns kodemaker-no.new-pages.profile-page
  (:require [clojure.set :as set]
            [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h]
            [ui.elements :as e]))

(def presence-base-urls
  {:twitter "https://twitter.com/"
   :linkedin "https://www.linkedin.com"
   :stackoverflow "https://stackoverflow.com/"
   :github "https://github.com/"})

(defn fix-presence [presence]
  (into {}
        (for [[k v] presence]
          [k (str (presence-base-urls k) v)])))

(defn unwrap-idents [entity k]
  (map (partial d/entity (d/entity-db entity)) (k entity)))

(def technology-categories
  [[:person/favorites-at-the-moment "Favoritter for tiden:"]
   [:person/using-at-work "Bruker på jobben:"]
   [:person/want-to-learn-more "Vil lære mer:"]])

(defn any-technology-categories? [person]
  (boolean (some (fn [[k _]] (seq (k person))) technology-categories)))

(defn create-page [person]
  {:sections
   (->>
    [{:kind :profile
      :full-name (:person/full-name person)
      :image (h/profile-picture person)
      :title (:person/title person)
      :mobile (:person/phone-number person)
      :mail (:person/email-address person)
      :cv {:text "Se full CV"
           :url (str "/cv/" (name (:db/ident person)) "/")}
      :description (f/markdown (:person/description person))
      :presence (fix-presence (:person/presence person))
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
                              :tags (e/tech-tags {:techs (unwrap-idents recommendation :recommendation/tech)
                                                  :class "tags"})
                              :url (:recommendation/url recommendation)
                              :content (f/to-html (:recommendation/description recommendation))}
                       (:recommendation/link-text recommendation)
                       (assoc :link {:text (:recommendation/link-text recommendation)
                                     :href (:recommendation/url recommendation)}))))})

     ;; Snakker gjerne om

     (when-let [hobbies (seq (:person/hobbies person))]
       {:kind :hip
        :title (str (:person/given-name person) " snakker gjerne om")
        :single (let [hobby (first hobbies)]
                  {:title (:title hobby)
                   :content (f/to-html (:description hobby))
                   :image (str "/big-bottom-half-circle" (:illustration hobby))})})

     ;; Bloggposter

     (when-let [blog-posts (seq (:blog-post/_author person))]
       {:kind :titled
        :title "Bloggposter"
        :contents (for [blog-post (take 3 (reverse (sort-by :blog-post/published blog-posts)))]
                    (let [url (or (:page/uri blog-post)
                                  (:blog-post/external-url blog-post))]
                      (e/teaser
                       {:title (:blog-post/title blog-post)
                        :tags (e/tech-tags {:techs (unwrap-idents blog-post :blog-post/tech)
                                            :class "tags"})
                        :url url
                        :content (f/to-html (:blog-post/blurb blog-post))
                        :link (when url {:text "Les artikkel" :href url})})))})

     {:kind :footer}]
    (remove nil?)
    (map (fn [color section]
           (assoc section :background color))
         (cycle [:blanc :chablis])))})

(comment
  (def conn (:datomic/conn integrant.repl.state/system))
  (def db (d/db conn))

  (def person (d/entity db :person/magnar))

  (map kodemaker-no.render-new-page/render-section (:sections (create-page person)))


  )
