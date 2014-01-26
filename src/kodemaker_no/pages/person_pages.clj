(ns kodemaker-no.pages.person-pages
  (:require [kodemaker-no.formatting :refer [to-html comma-separated]]
            [kodemaker-no.markup :refer [render-link]]
            [hiccup.core :as hiccup]
            [clojure.string :as str]))

(defn- link-to-tech [tech]
  (if (:url tech)
    [:a {:href (:url tech)} (:name tech)]
    (:name tech)))

(defn prepend-to-paragraph [html node]
  (str/replace html #"^<p>" (str "<p>" (hiccup/html node))))

(defn append-to-paragraph [html node]
  (str/replace html #"</p>$" (str (hiccup/html node) "</p>")))

(defn- render-recommendation [rec]
  (list [:h3 (:title rec)]
        (when-not (empty? (:tech rec))
          [:p.near.cookie-w [:span.cookie (interpose " " (map link-to-tech (:tech rec)))]])
        (append-to-paragraph (to-html :md (:blurb rec))
                             (list " " (render-link (:link rec))))))

(defn- render-recommendations [recs person]
  (list [:h2 (str (:genitive person) " Anbefalinger")]
        (map render-recommendation recs)))

(defn- render-hobby [hobby]
  [:div.bd
   [:h3.mtn (:title hobby)]
   (prepend-to-paragraph (to-html :md (:description hobby))
                         (if (:url hobby)
                           [:a.illu {:href (:url hobby)} [:img {:src (:illustration hobby)}]]
                           [:img.illu {:src (:illustration hobby)}]))])

(defn- render-hobbies [hobbies _]
  (list [:h2 "Snakker gjerne om"]
        (map render-hobby hobbies)))

(defn- inline-list [label nodes]
  (list [:strong label]
        (comma-separated nodes)
        "<br>"))

(defn- render-tech [tech _]
  [:p
   (when-let [favs (:favorites-at-the-moment tech)]
     (inline-list "Favoritter for tiden: " (map link-to-tech favs)))
   (when-let [more (:want-to-learn-more tech)]
     (inline-list "Vil lære mer: " (map link-to-tech more)))])

(defn- render-presentation [pres]
  [:div.media
   [:a.img.thumb.mts {:href (or (-> pres :urls :video)
                                (-> pres :urls :slides)
                                (throw (Exception. (str "Missing url to video or slides in presentation " (:title pres)))))}
    [:img {:src (:thumb pres)}]]
   [:div.bd
    [:h4.mtn (:title pres)]
    [:p (:blurb pres)
     (when-let [url (-> pres :urls :video)] (list " " [:a.nowrap {:href url} "Se video"]))
     (when-let [url (-> pres :urls :slides)] (list " " [:a.nowrap {:href url} "Se slides"]))
     (when-let [url (-> pres :urls :source)] (list " " [:a.nowrap {:href url} "Se koden"]))]]])

(defn- render-presentations [presentations person]
  (list [:h2 (str (:genitive person) " Foredrag")]
        (map render-presentation presentations)))

(defn- render-aside [person]
  [:div.tight
   [:h4 (:full-name person)]
   [:p
    (:title person) "<br>"
    [:span.nowrap (:phone-number person)] "<br>"
    [:a {:href (str "mailto:" (:email-address person))}
     (:email-address person)]]])

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
          (maybe-include person :presentations render-presentations))})

(defn person-pages [people]
  (into {} (map (juxt :url #(partial person-page %)) people)))
