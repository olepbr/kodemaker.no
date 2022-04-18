(ns kodemaker-no.new-pages.blog
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h]
            [ui.elements :as e]
            [ui.icons :as icons])
  (:import java.time.format.DateTimeFormatter))

(defn format-date [date]
  (.format (DateTimeFormatter/ofPattern "dd.MM.yyyy") date))

(defn author [post]
  (d/entity (d/entity-db post) (:blog-post/author post)))

(defn techs [post]
  (h/unwrap-ident-list post :blog-post/tech-list))

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

(defn active-posts-by-published [db posts]
  (->> posts
       (map first)
       (active-posts db)
       (sort-by :blog-post/published)
       reverse))

(defn blog-posts-by-published [db]
  (->> (d/q '[:find ?e
              :in $
              :where
              [?e :blog-post/published ?p]]
            db)
       (active-posts-by-published db)))

(defn relevant-posts
  "Picks posts that share at least one tech with the reference post. Returns a
  list of posts sorted by the most relevant first (most shared techs). Ties are
  weighted in favor of posts with the same author."
  [post]
  (let [tech (:blog-post/techs post)
        author (:blog-post/author post)
        db (d/entity-db post)]
    (when-not (empty? tech)
      (->> (d/q '[:find [?e ...]
                  :in $ ?url [?tech ...]
                  :where
                  [?e :blog-post/techs ?tech]
                  (not [?e :page/uri ?url])]
                db (:page/uri post) tech)
           (active-posts db)
           (map (fn [p]
                  [(cond-> (* 2 (count (set/intersection tech (:blog-post/techs p))))
                     (= (:blog-post/author p) author) inc)
                   p]))
           (sort-by (comp - first))
           (map second)))))

(defn adjacent-posts [xs x]
  (loop [newer nil
         [curr & xs] xs]
    (cond
      (nil? curr) nil
      (= x curr) (remove nil? [newer (first xs)])
      :default (recur curr xs))))

(defn related-posts
  "Tries to pick n related posts to present as further reading. It's a bit
  involved, but here's our goals for this:

  1. It should be possible to eventually visit every post by following these
     links from post to post
  2. If there are relevant posts, like part 2, same topic etc, some of those
     should be included
  3. Most posts should have something new as related

  At the very least, include the post published before this one - if this is the
  first post, include the latest one published. This creates a circle, so one
  can reach every post. Fill the remaining spots with topically related posts,
  if possible, and pad out the remaining spots with the post published after
  this one, then just some newly published posts."
  [post & n]
  (let [n (or n 5)
        latest (blog-posts-by-published (d/entity-db post))
        relevant (relevant-posts post)
        [next-post previous-post] (adjacent-posts latest post)]
    (->> (concat (take (dec n) relevant)
                 [(or previous-post (first latest)) next-post]
                 (take (inc n) latest))
         (remove nil?)
         (h/distinct-by :db/id)
         (remove #(= (:db/id %) (:db/id post)))
         (take n)
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
  {:twitter icons/twitter
   :linkedin icons/linkedin
   :stack-overflow icons/stackoverflow
   :github icons/github})

(defn strip-text [text max-size]
  (let [s (str/trim text)
        size (count s)]
    (if (> size max-size)
      (str (subs s 0 max-size)
           (str "..."))
      s)))

(defn create-post-page [{:blog-post/keys [published updated title body blurb author author-picture] :as blog-post}]
  {:title title
   :metas [{:property "og:site"
             :content "Kodemaker"}
           {:property "og:type"
             :content "article"}
           {:property "og:description"
             :content (strip-text blurb 200)}
           {:property "article_published_time"
            :content (format-date published)}
           {:property "og:title"
             :content (strip-text title 70)}
           {:property "og:url"
            :content (:page/uri blog-post)}]
   :sections
   [{:kind :header
     :background :chablis}
    {:kind :container
     :content (e/simple-article
               {:title title
                :tags (e/tech-tags {:techs (techs blog-post)})
                :annotation (str "Publisert "
                                 (format-date published)
                                 (when updated
                                   (str ", sist oppdatert " (format-date updated))))
                :content (f/to-html body)})
     :pønt (->> [{:kind :dotgrid
                  :position "top -50px right 0"}
                 {:kind :descending-line
                  :position "top -240px right 0"}
                 {:kind :ascending-line
                  :position "top 0 right -50px"}]
                shuffle
                (take 1))}
    {:kind :container
     :class "container-section-tight"
     :content (when-let [author (author blog-post)]
                (e/round-media
                 {:image (str "/vcard-small" (:blog-post/author-picture blog-post))
                  :title (:person/full-name author)
                  :href (:page/uri author)
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

(defn published [post]
  (format-date (:blog-post/published post)))

(defn post-url [blog-post]
  (or (:page/uri blog-post)
      (:blog-post/external-url blog-post)))

(defn link-to-tech-blog [tech]
  (-> (into {} tech)
      (assoc :page/uri (format "/blogg/%s/" (name (:db/ident tech))))))

(defn blog-post-teaser [post]
  {:kind :article
   :class "article-section-tight"
   :articles
   [{:alignment :content
     :title (:blog-post/title post)
     :href (:page/uri post)
     :annotation (published post)
     :content [:div
               [:div.text.mbm (f/to-html (:blog-post/blurb post))]
               [:p (e/arrow-link {:text "Les artikkelen"
                                  :title (:blog-post/title post)
                                  :href (post-url post)})]]
     :aside (let [author (author post)]
              (e/round-media
               {:image (when (:blog-post/author-picture post) (str "/vcard-small" (:blog-post/author-picture post)))
                :title (:person/full-name author)
                :href (:page/uri author)
                :lines [(e/tech-tags {:prefix "Om"
                                      :techs (map link-to-tech-blog (techs post))})]}))}]})

(def pønts
  [{:kind :dotgrid
    :position "top 50px right -100px"}
   {:kind :descending-line
    :position "top -240px right 0"}
   {:kind :ascending-line
    :position "top 0 left -150px"}
   {:kind :greater-than
    :position "top -400px left -450px"}
   {:kind :less-than
    :position "top -400px right -450px"}])

(defn pønt-a-few [sections]
  (loop [sections (vec sections)
         idx 0
         skip (cycle (shuffle [2 3 4]))
         pønts (cycle (shuffle (concat pønts pønts)))]
    (if (<= (count sections) idx)
      sections
      (recur (assoc-in sections [idx :pønt] (take 1 pønts))
             (+ idx (first skip))
             (rest skip)
             (rest pønts)))))

(defn list-blog-posts [posts]
  (->> posts
       (map blog-post-teaser)
       (map (fn [color section]
              (assoc section :background color))
            (cycle [:blanc :blanc-rose]))
       pønt-a-few))

(defn create-index-page [db]
  {:title "Blogg"
   :sections
   (concat
    [{:kind :header
      :bg-color :blanc}]
    (list-blog-posts (blog-posts-by-published db))
    [{:kind :footer}])})

(defn tech-blog-posts-by-published [db tech]
  (->> (d/q '[:find ?e
              :in $ ?t
              :where
              [?e :blog-post/published ?p]
              [?e :blog-post/techs ?t]]
            db tech)
       (active-posts-by-published db)))

(defn try-pitch [tech content k label]
  (when (seq (k tech))
    (str label
         (when (< 0 (count (remove (k tech) content)))
           " og andre godsaker"))))

(defn tech-page-pitch [tech]
  (let [content (concat (:presentation/_techs tech)
                        (:screencast/_techs tech)
                        (:side-project/_techs tech)
                        (:recommendation/_techs tech))]
    (when (seq content)
      (str
       (or
        (try-pitch tech content :presentation/_techs "foredrag")
        (try-pitch tech content :screencast/_techs "screencasts")
        (try-pitch tech content :side-project/_techs "side-prosjekter")
        (try-pitch tech content :recommendation/_techs "anbefalinger"))
       (format " om %s" (:tech/name tech))))))

(defn create-category-index-page [db page]
  (let [tech (d/entity db [:db/ident (:blog-category/tech page)])]
    {:title (format "Blogg: %s" (:tech/name tech))
     :sections
     (concat
      [{:kind :header
        :bg-color :blanc}
       {:kind :tech-intro
        :title (:tech/name tech)
        :logo (:tech/illustration tech)
        :article {:content [:div.tac
                            [:p "Du ser nå noen av " [:a {:href "/blogg/"} "våre blogginnlegg"] "." ]
                            (when-let [pitch (tech-page-pitch tech)]
                              [:p "Vi kan også by på " [:a {:href (:page/uri tech)} pitch] "."])]
                  :alignment :front}
        :pønt [{:kind :greater-than
                :position "top -410px right 60vw"}
               {:kind :dotgrid
                :position "top -110px left 80vw"}]}]
      (->> (tech-blog-posts-by-published db (:db/ident tech))
           list-blog-posts)
      [{:kind :footer}])}))
