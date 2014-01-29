(ns kodemaker-no.pages.person-pages
  (:require [kodemaker-no.formatting :refer [to-html comma-separated]]
            [kodemaker-no.markup :as markup]
            [hiccup.core :as hiccup]
            [clojure.string :as str]))

(defn render-tech-bubble [tech]
  (when-not (empty? tech)
    [:p.near.cookie-w
     [:span.cookie (interpose " " (map markup/link-if-url tech))]]))

(defn- render-recommendation [{:keys [title tech blurb link]}]
  (list [:h3 title]
        (render-tech-bubble tech)
        (markup/append-to-paragraph
         (to-html blurb)
         (list " " (markup/render-link link)))))

(defn- render-recommendations [recs person]
  (list [:h2 (str (:genitive person) " Anbefalinger")]
        (map render-recommendation (take 3 recs))))

(defn- render-hobby [{:keys [title description url illustration]}]
  [:div.bd
   [:h3.mtn title]
   (markup/prepend-to-paragraph
    (to-html description)
    (if url
      [:a.illu {:href url} [:img {:src illustration}]]
      [:img.illu {:src illustration}]))])

(defn- render-hobbies [hobbies _]
  (list [:h2 "Snakker gjerne om"]
        (map render-hobby hobbies)))

(defn- inline-list [label nodes]
  (list [:strong label]
        (comma-separated nodes)
        "<br>"))

(defn- render-tech [{:keys [favorites-at-the-moment want-to-learn-more]} _]
  [:p
   (when favorites-at-the-moment
     (inline-list "Favoritter for tiden: " (map markup/link-if-url favorites-at-the-moment)))
   (when want-to-learn-more
     (inline-list "Vil lære mer: " (map markup/link-if-url want-to-learn-more)))])

(defn- render-presentation [{:keys [urls title thumb blurb]}]
  [:div.media
   [:a.img.thumb.mts {:href (or (:video urls)
                                (:slides urls)
                                (throw (Exception. (str "Missing url to video or slides in presentation " title))))}
    [:img {:src thumb}]]
   [:div.bd
    [:h3.mtn title]
    [:p blurb
     (when-let [url (:video urls)] (list " " [:a.nowrap {:href url} "Se video"]))
     (when-let [url (:slides urls)] (list " " [:a.nowrap {:href url} "Se slides"]))
     (when-let [url (:source urls)] (list " " [:a.nowrap {:href url} "Se koden"]))]]])

(defn- render-presentations [presentations person]
  (list [:h2 (str (:genitive person) " Foredrag")]
        (map render-presentation presentations)))

(defn- render-endorsement [{:keys [photo author title project quote]}]
  [:div.media
   (when photo [:img.img.thumb.mts {:src photo}])
   [:div.bd
    [:h3.mtn author]
    (if title
      [:p.near title ", " (markup/link-if-url project)]
      [:p.near (markup/link-if-url project)])
    [:p [:q quote]]]])

(defn- render-endorsements [endorsements person]
  (list [:h2 (str (:genitive person) " Referanser")]
        (map render-endorsement endorsements)))

(defn- render-aside [{:keys [full-name title phone-number email-address]}]
  [:div.tight
   [:h4 full-name]
   [:p
    title "<br>"
    [:span.nowrap phone-number] "<br>"
    [:a {:href (str "mailto:" email-address)} email-address]]])

(defn- render-blog-post [post]
  (list
   [:h3 (:title post)]
   (render-tech-bubble (:tech post))
   [:p (:blurb post) " "
    [:a {:href (:url post)} "Les posten"]]))

(defn- render-blog-posts [posts person]
  (list
   [:h2 (str (:genitive person) " Bloggposter")]
   (map render-blog-post (take 3 posts))))

(defn- maybe-include [person kw f]
  (when (kw person)
    (f (kw person) person)))

(defn- person-page [person]
  {:title (:full-name person)
   :illustration (-> person :photos :half-figure)
   :lead [:p (:description person)]
   :aside (render-aside person)
   :body (list
          (maybe-include person :tech render-tech)
          (maybe-include person :recommendations render-recommendations)
          (maybe-include person :hobbies render-hobbies)
          (maybe-include person :blog-posts render-blog-posts)
          (maybe-include person :presentations render-presentations)
          (maybe-include person :endorsements render-endorsements))})

(defn person-pages [people]
  (into {} (map (juxt :url #(partial person-page %)) people)))
