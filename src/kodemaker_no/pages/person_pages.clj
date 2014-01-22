(ns kodemaker-no.pages.person-pages
  (:require [kodemaker-no.formatting :refer [to-html]]
            [hiccup.core :as hiccup]
            [clojure.string :as str]))

(defn- link-to-tech [tech]
  (if (:url tech)
    [:a {:href (:url tech)} (:name tech)]
    (:name tech)))

(defn- render-recommendation [rec]
  (list [:h3 (:title rec)]
        (when-not (empty? (:tech rec))
         [:p.near [:span.techs (interpose " " (map link-to-tech (:tech rec)))]])
        [:p (:blurb rec) " "
         [:a.nowrap {:href (:url rec)} "Les mer"]]))

(defn- render-recommendations [person recs]
  (list [:h2 (str (:genitive person) " Anbefalinger")]
        (map render-recommendation recs)))

(defn into-paragraph [html node]
  (str/replace html #"^<p>" (str "<p>" (hiccup/html node))))

(defn- render-hobby [hobby]
  [:div.bd
   [:h3.mtn (:title hobby)]
   (into-paragraph (to-html :md (:description hobby))
                   [:img.right {:src (:illustration hobby)}])])

(defn- render-hobbies [hobbies]
  (list [:h2 "Snakker gjerne om"]
        (map render-hobby hobbies)))

(defn- comma-separated [coll]
  (drop 1 (interleave (into (list " og " "")
                            (repeat (dec (count coll)) ", "))
                      coll)))

(defn- inline-list [label nodes]
  (list [:strong label]
        (comma-separated nodes)
        "<br>"))

(defn- render-tech [tech]
  [:p
   (when-let [favs (:favorites-at-the-moment tech)]
     (inline-list "Favoritter for tiden: " (map link-to-tech favs)))
   (when-let [more (:want-to-learn-more tech)]
     (inline-list "Vil lære mer: " (map link-to-tech more)))])

(defn- render-aside [person]
  [:div.tight
   [:h4 (:full-name person)]
   [:p
    (:title person) "<br>"
    [:span.nowrap (:phone-number person)] "<br>"
    [:a {:href (str "mailto:" (:email-address person))}
     (:email-address person)]]])

(defn- person-page [person]
  {:title (:full-name person)
   :illustration (-> person :photos :half-figure)
   :lead [:p (:description person)]
   :aside (render-aside person)
   :body (list
          (when-let [xs (:tech person)]
            (render-tech xs))
          (when-let [xs (:recommendations person)]
            (render-recommendations person xs))
          (when-let [xs (:hobbies person)]
            (render-hobbies xs)))})

(defn person-pages [people]
  (into {} (map (juxt :url #(partial person-page %)) people)))
