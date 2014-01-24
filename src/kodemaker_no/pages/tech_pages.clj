(ns kodemaker-no.pages.tech-pages
  (:require [kodemaker-no.formatting :refer [to-html comma-separated]]))

(defn- link-to-person [person]
  [:a {:href (:url person)} (:name person)])

(defn- render-recommendation [rec]
  (list [:h3 (:title rec)]
        [:p.near.cookie-w [:span.cookie "Anbefalt av " (comma-separated (map link-to-person (:recommended-by rec)))]]
        [:p (:blurb rec) " "
         [:a.nowrap {:href (:url rec)} "Les mer"]]))

(defn- render-recommendations [recs]
  (list [:h2 "Våre Anbefalinger"]
        (map render-recommendation recs)))

(defn- render-presentation [pres]
  [:div.media
   [:a.img.thumb.mts {:href (or (-> pres :urls :video)
                                (-> pres :urls :slides)
                                (throw (Exception. (str "Missing url to video or slides in presentation " (:title pres)))))}
    [:img {:src (:thumb pres)}]]
   [:div.bd
    [:h4.mtn (:title pres)
     [:span.shy.tiny.nowrap " av " (comma-separated (map link-to-person (:by pres)))]]
    [:p (:blurb pres)
     (when-let [url (-> pres :urls :video)] (list " " [:a.nowrap {:href url} "Se video"]))
     (when-let [url (-> pres :urls :slides)] (list " " [:a.nowrap {:href url} "Se slides"]))
     (when-let [url (-> pres :urls :source)] (list " " [:a.nowrap {:href url} "Se koden"]))]]])

(defn- render-presentations [pres]
  (list [:h2 "Våre Presentasjoner"]
        (map render-presentation pres)))

(defn- tech-page [tech]
  {:title (:name tech)
   :illustration (:illustration tech)
   :lead (to-html :md (:description tech))
   :body (list
          (when-let [xs (:recommendations tech)]
            (render-recommendations xs))
          (when-let [xs (:presentations tech)]
            (render-presentations xs)))})

(defn tech-pages [techs]
  (into {} (map (juxt :url #(partial tech-page %)) techs)))
