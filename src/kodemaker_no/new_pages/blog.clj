(ns kodemaker-no.new-pages.blog
  (:require [datomic-type-extensions.api :as d]
            [ui.elements :as e]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.formatting :as f])
  (:import java.time.format.DateTimeFormatter))

(defn create-post-page [blog-post]
  {:sections [{:kind :footer}]})

(defn blog-posts [db]
  (->> db
       (d/q '[:find ?e
              :in $
              :where
              [?e :blog-post/published ?p]])
       (map first)
       (map #(d/entity db %))
       (filter :page/uri)
       (remove :blog-post/archived?)
       (sort-by :blog-post/published)
       reverse))

(defn author [post]
  (d/entity (d/entity-db post) (:blog-post/author post)))

(defn techs [post]
  (let [db (d/entity-db post)]
    (map #(d/entity db %) (:blog-post/tech post))))

(defn blog-post-teaser [post]
  {:kind :article
   :class "article-section-tight"
   :articles
   [{:alignment :content
     :title (:blog-post/title post)
     :href (:page/uri post)
     :annotation (.format (DateTimeFormatter/ofPattern "dd.MM.yyyy") (:blog-post/published post))
     :content [:div
               [:div.text.mbm (f/to-html (:blog-post/blurb post))]
               [:p (e/arrow-link {:text "Les artikkelen"
                                  :title (:blog-post/title post)
                                  :href (:page/uri post)})]]
     :aside (let [author (author post)]
              (e/round-media
               {:image (str "/vcard-small" (h/profile-picture author))
                :title (:person/full-name author)
                :lines [(e/tech-tags {:prefix "Om"
                                      :techs (techs post)})]}))}]})

(defn create-index-page [db]
  {:sections
   (concat
    [{:kind :header
      :bg-color :blanc
      :pønt [{:kind :descending-line
              :position "left 33% top 0"}
             {:kind :descending-line
              :position "left 80vw top 0"}]}]
    (->> (blog-posts db)
         (map blog-post-teaser)
         (map (fn [color section]
                (assoc section :background color))
              (cycle [:blanc :blanc-rose])))
    [{:kind :footer}])})
