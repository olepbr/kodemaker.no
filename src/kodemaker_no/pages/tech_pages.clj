(ns kodemaker-no.pages.tech-pages
  (:require [kodemaker-no.formatting :refer [to-html comma-separated]]
            [kodemaker-no.markup :as markup]))

(defn- link-to-person [person]
  [:a {:href (:url person)} (:name person)])

(defn- render-recommendation [{:keys [title by blurb link]}]
  (list [:h3 title]
        [:p.near.cookie-w [:span.cookie "Anbefalt av " (comma-separated (map link-to-person by))]]
        (markup/append-to-paragraph
         (to-html blurb)
         (list " " (markup/render-link link)))))

(defn- render-recommendations [recommendations _]
  (list [:h2 "Våre Anbefalinger"]
        (map render-recommendation recommendations)))

(defn- render-presentation [{:keys [urls title thumb by blurb]}]
  [:div.media
   [:a.img.thumb.mts {:href (or (:video urls)
                                (:slides urls)
                                (throw (Exception. (str "Missing url to video or slides in presentation " title))))}
    [:img {:src thumb}]]
   [:div.bd
    [:h4.mtn title
     [:span.shy.tiny.nowrap " av " (comma-separated (map link-to-person by))]]
    [:p blurb
     (when-let [url (:video urls)] (list " " [:a.nowrap {:href url} "Se video"]))
     (when-let [url (:slides urls)] (list " " [:a.nowrap {:href url} "Se slides"]))
     (when-let [url (:source urls)] (list " " [:a.nowrap {:href url} "Se koden"]))]]])

(defn- render-presentations [presentations _]
  (list [:h2 "Våre Presentasjoner"]
        (map render-presentation presentations)))

(defn- maybe-include [tech kw f]
  (when (kw tech)
    (f (kw tech) tech)))

(defn- tech-page [tech]
  {:title (:name tech)
   :illustration (:illustration tech)
   :lead (to-html (:description tech))
   :body (list
          (maybe-include tech :recommendations render-recommendations)
          (maybe-include tech :presentations render-presentations))})

(defn tech-pages [techs]
  (into {} (map (juxt :url #(partial tech-page %)) techs)))
