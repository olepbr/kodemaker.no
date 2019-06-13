(ns kodemaker-no.pages.blog-pages
  (:require [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [clojure.string :as str]
            [kodemaker-no.cultivate.blog-posts :as blog]
            [kodemaker-no.date :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.markup :as markup]
            [kodemaker-no.pages.person-pages :as pp]
            [kodemaker-no.render-page :as r]))

(defn- published [blog-post]
  (d/format-dmy (:published blog-post)))

(defn- history [blog-post]
  (concat
   (list "Publisert "
         (d/format-dmy (:published blog-post)))
   (when-let [updated (:updated blog-post)]
     (list ", "
           [:a {:href (str "https://github.com/kodemaker/kodemaker.no/commits/master/resources/firmablogg"
                           (:file-path blog-post))} "sist oppdatert " (d/format-dmy updated)]))))

(defn- published? [blog-post now]
  (and (:published blog-post)
       (not (time/before? now (:published blog-post)))))

(defn- by-published [blog-posts]
  (->> blog-posts
       (sort-by :published)
       reverse))

(defn- published-posts [blog-posts & [now]]
  (let [now (or now (coerce/to-local-date (time/now)))]
    (->> blog-posts
         (filter #(published? % now))
         by-published)))

(defn- blog-post-li [blog-post]
  [:li.small (list [:a {:href (:path blog-post)} (:title blog-post)]
                   [:br]
                   [:span.shy (published blog-post)])])

(defn- blog-post-list [blog-posts]
  [:ul (map blog-post-li blog-posts)])

(defn- blog-post-lead [blog-post]
  [:h1.hn.mbs [:a {:href (:path blog-post)} (:title blog-post)]])

(defn- blog-post-body [blog-post]
  (f/to-html (:body blog-post)))

(defn- blog-post-aside [blog-post blog-posts]
  (when-let [other-posts (seq (remove #{blog-post} blog-posts))]
    (list [:h3 "Mer fra bloggen"]
          (->> other-posts
               by-published
               blog-post-list))))

(defn- author [{:keys [url first-name]}]
  [:a {:href url} first-name])

(defn- byline-text [blog-post]
  (cond
    (and (:author-person blog-post)
         (seq (:tech blog-post)))
    (list (author (:author-person blog-post))
          " om "
          (f/comma-separated (map markup/link-if-url (:tech blog-post))))

    (:author-person blog-post)
    (list "Av " (author (:author-person blog-post)))))

(defn- byline [blog-post]
  (when-let [text (byline-text blog-post)]
    [:p text]))

(defn- render-blog-post-teaser [blog-post]
  (list
   (blog-post-lead blog-post)
   [:div.line
    [:div.unit.s-1of3
     [:div.bd
      (when-let [img (:illustration blog-post)]
        [:a.block.mod {:href (:path blog-post)} [:img {:src img}]])
      [:p.shy (published blog-post)]
      (byline blog-post)]]
    [:div.lastUnit
     [:div.bd
      (f/to-html (:blurb blog-post))
      [:p [:a {:href (:path blog-post)} "Les hele"]]]]]))

(defn- disqus-script []
  (slurp (clojure.java.io/resource "public/scripts/blog-post.js")))

(defn legacy-blog-post-page [blog-post blog-posts]
  {:title (:title blog-post)
   :illustration (:illustration blog-post)
   :aside (list
           (when (seq (:presence blog-post))
             [:div.bd
              (pp/render-presence (:presence blog-post))])
           [:p.shy (published blog-post)]
           (byline blog-post)
           (blog-post-aside blog-post blog-posts))
   :body (list (blog-post-body blog-post)
               (when (:contact-form blog-post)
                 (r/render-contact-form
                  {:body (:contact-form blog-post)
                   :button (:contact-form-button blog-post)
                   :placeholder "E-post eller tlf"}))
               [:div#disqus_thread.mod]
               [:script (str "var disqus_identifier='" (:path blog-post) "';"
                             (disqus-script))])})

(defn blog-post-page [blog-post blog-posts]
  {:title (:title blog-post)
   :illustration (:illustration blog-post)
   :body (concat
          (list [:p.mbn.small (byline-text blog-post)]
                [:p.mtn.small (history blog-post)]
                (blog-post-body blog-post)
                [:h2 "Diskusjon"]
                [:p.mbn "Vi diskuterer gjerne hvor enn du finner oss. Ta kontakt!"]
                (when-let [presence (-> blog-post :author-person :presence)]
                  [:div.bd
                   (pp/render-presence (dissoc presence :cv))])
                [:div#disqus_thread.mod]
                [:script (str "var disqus_identifier='" (:path blog-post) "';"
                              (disqus-script))])
          (blog-post-aside blog-post blog-posts))})

(defn blog-post-pages [blog-posts & [page-fn]]
  (let [published (published-posts (vals blog-posts))
        blog-post-page (or page-fn blog-post-page)]
    (-> blog-posts
        (h/rename-keys blog/blog-post-path)
        (h/update-vals #(partial blog-post-page % published)))))

(defn blog-page [blog-posts]
  {:title {:head "Kodemakerbloggen"}
   :body (->> blog-posts
              published-posts
              (map render-blog-post-teaser)
              (interpose [:hr]))})
