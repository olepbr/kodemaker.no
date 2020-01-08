(ns kodemaker-no.new-pages.blog
  (:require [datomic-type-extensions.api :as d]
            [ui.elements :as e]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.formatting :as f]
            [ui.sections :as sections]
            [clojure.set :as set])
  (:import java.time.format.DateTimeFormatter))

(defn format-date [date]
  (.format (DateTimeFormatter/ofPattern "dd.MM.yyyy") date))

(defn author [post]
  (d/entity (d/entity-db post) (:blog-post/author post)))

(defn techs [post]
  (let [db (d/entity-db post)]
    (map #(d/entity db %) (:blog-post/tech post))))

(defn small-teaser [post]
  (e/teaser
   {:title (:blog-post/title post)
    :annotation (format-date (:blog-post/published post))
    :url (:page/uri post)}))

(defn active-posts [db post-eids]
  (->> post-eids
       (map #(d/entity db %))
       (filter :page/uri)
       (remove :blog-post/archived?)
       (filter :blog-post/published)))

(defn blog-posts-by-published [db]
  (->> db
       (d/q '[:find ?e
              :in $
              :where
              [?e :blog-post/published ?p]])
       (map first)
       (active-posts db)
       (sort-by :blog-post/published)
       reverse))

(defn relevant-posts [post]
  (let [tech (:blog-post/tech post)
        author (:blog-post/author post)
        db (d/entity-db post)]
    (->> (d/q '[:find [?e ...]
                :in $ ?url [?tech ...]
                :where
                [?e :blog-post/tech ?tech]
                (not [?e :page/uri ?url])]
              db (:page/uri post) tech)
         (active-posts db)
         (map (fn [p]
                [(cond-> (* 2 (count (set/intersection tech (:blog-post/tech p))))
                   (= (:blog-post/author p) author) inc)
                 p]))
         (sort-by (comp - first))
         (map second))))

(defn adjacent-posts [xs x]
  (loop [newer nil
         [curr & xs] xs]
    (cond
      (nil? curr) nil
      (= x curr) (remove nil? [newer (first xs)])
      :default (recur curr xs))))

(defn related-posts [post]
  (let [latest (blog-posts-by-published (d/entity-db post))
        relevant (relevant-posts post)
        [next-post previous-post] (adjacent-posts latest post)]
    (->> (concat (take 3 relevant)
                 [next-post previous-post]
                 (take 4 latest))
         (remove nil?)
         (h/distinct-by :db/id)
         (remove #(= (:db/id %) (:db/id post)))
         (take 3)
         (sort-by :blog-post/published)
         reverse)))

(comment
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))
  (def post (d/entity db [:page/uri "/blogg/2019-10-javascript-sets/"]))
  (def post (d/entity db [:page/uri "/blogg/2019-10-cljss/"]))
  (def post (d/entity db [:page/uri "/blogg/2019-12-quic/"]))
  (def post (d/entity db [:page/uri "/blogg/javascript-coercion/"]))
  (def post (d/entity db [:page/uri "/blogg/2019-09-terningene-er-kastet/"]))

  (map :page/uri (related-posts post))
)

(def icons
  {:twitter sections/twitter-icon
   :linkedin sections/linkedin-icon
   :stack-overflow sections/stackoverflow-icon
   :github sections/github-icon})

(defn create-post-page [{:blog-post/keys [published updated title body] :as blog-post}]
  {:sections
   [{:kind :header
     :background :chablis}
    {:kind :container
     :content (e/simple-article
               {:title title
                :annotation (str "Publisert "
                                 (format-date published)
                                 (when updated
                                   (str ", sist oppdatert " (format-date updated))))
                :content (f/to-html body)})
     :pønt [{:kind :greater-than
             :position "top -440px left 90px"}
            {:kind :descending-line
             :position "top -540px right 90px"}]}
    {:kind :container
     :class "container-section-tight"
     :content (let [author (author blog-post)]
                (e/round-media
                 {:image (str "/vcard-small" (h/profile-picture author))
                  :title (:person/full-name author)
                  :lines [(:person/title author)
                          (:person/email-address author)]}))}
    {:kind :definitions
     :definitions (->> [(when-let [links (seq (:blog-post/discussion-links blog-post))]
                          {:title "Diskusjon"
                           :contents (->> links
                                          (sort-by :list/idx)
                                          (map #(e/teaser {:title (:text %)
                                                           :icon (icons (:icon %))
                                                           :url (:url %)})))})
                        {:title "Mer fra bloggen"
                         :contents (map small-teaser (related-posts blog-post))}]
                       (remove nil?))}
    {:kind :footer}]})

(defn blog-post-teaser [post]
  {:kind :article
   :class "article-section-tight"
   :articles
   [{:alignment :content
     :title (:blog-post/title post)
     :href (:page/uri post)
     :annotation (format-date (:blog-post/published post))
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
    (->> (blog-posts-by-published db)
         (map blog-post-teaser)
         (map (fn [color section]
                (assoc section :background color))
              (cycle [:blanc :blanc-rose])))
    [{:kind :footer}])})
