(ns kodemaker-no.pages
  (:require [optimus.link :as link]
            [kodemaker-no.layout :refer [with-layout render-page]]
            [kodemaker-no.asciidoc :as adoc]
            [kodemaker-no.people :as people]
            [kodemaker-no.homeless :refer [slurp-files]]
            [clojure.java.io :as io]))

(defn- url-for-person [person]
  (str "/" (people/id person) ".html"))

(defn render-person [request person]
  [:div.gridUnit.r-4-3-2
   [:a.photoframe.gridContent.linkBlock {:href (url-for-person person)}
    [:div.paspartur.tiny
     [:img.photo {:src (link/file-path request (str "/photos/" (people/id person) "/side-profile.jpg")
                                       :fallback "/photos/unknown/side-profile.jpg")}]
     [:span.linkish (people/full-name person)]
     [:span.title (:title person)]]]])

(defn all-people [request]
  {:body (with-layout request (str (count people/consultants) " kvasse konsulenter")
           [:div.body
            [:div.bd
             [:div.grid
              (->> people/everyone
                   (sort-by :order >)
                   (map (partial render-person request)))]
             [:p "Det er menneskene som betyr noe. Hos oss legger vi vekt på å bygge relasjoner og sørge for at de sosiale båndene er sterke. Hvis man trives på fritiden og føler at man har havnet i riktig firma, så vil man også fungere bedre i jobben som konsulent. Dette vil også kundene merke."]]])})

(defn- person-page [person request]
  {:body (with-layout request (people/full-name person)
           [:div.body])})

(defn article-pages []
  (->> (slurp-files "resources/articles/" #"\.adoc$")
       (map adoc/parse-article)
       (map (juxt :url #(partial render-page %)))
       (into {})))

(defn general-pages []
  {"/mennesker.html" all-people})

(defn people-pages []
  (into {} (map (juxt url-for-person #(partial person-page %)) people/everyone)))

(defn get-pages []
  (merge (people-pages)
         (article-pages)
         (general-pages)))
