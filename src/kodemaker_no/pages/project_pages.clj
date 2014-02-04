(ns kodemaker-no.pages.project-pages
  (:require [clojure.string :as str]
            [kodemaker-no.formatting :refer [comma-separated year-range]]
            [kodemaker-no.markup :refer [link-if-url]]))

(defn- render-person [{:keys [url thumb full-name description years]}]
  [:div.media
   [:a.img.thumb.mts {:href url}
    [:img {:src thumb}]]
   [:div.bd
    [:h4.mtn full-name " " [:span.tiny.shy (year-range years)]]
    [:p description]]])

(defn- compare* [a b]
  (let [result (compare a b)]
    (if (zero? result)
      nil
      result)))

(defn- compare-by-years [a b]
  (or (compare* (count (:years b))
                (count (:years a)))
      (compare* (apply min (:years a))
                (apply min (:years b)))
      0))

(defn- render-people [people project]
  (list [:h2 "Våre folk på saken"]
        (->> (:people project)
             (sort compare-by-years)
             (map render-person))))

(defn- render-endorsement [{:keys [photo author person title quote]}]
  [:div.media
   (when photo [:img.img.thumb.mts {:src photo}])
   [:div.bd
    [:h4.mtn author
     [:span.tiny " om "
      [:a {:href (:url person)} (:first-name person)]]]
    (when title [:p.near title])
    [:p [:q quote]]]])

(defn- render-endorsements [endorsements _]
  (list [:h2 "Referanser"]
        (map render-endorsement endorsements)))

(defn- render-tech [tech _]
  (list [:h3 "Teknologi"]
        [:p (comma-separated (map link-if-url tech)) "."]))

(defn- render-illustration [{:keys [site illustration]}]
  [:p [:a {:href site} [:img {:src illustration}]]])

(defn- strip-protocol [s]
  (str/replace s #"^[a-z]+://" ""))

(defn- render-site [site]
  [:p [:a {:href site} (strip-protocol site)]])

(defn- maybe-include [project kw f]
  (when (kw project)
    (f (kw project) project)))

(defn- project-page [project]
  {:title (:name project)
   :illustration (:logo project)
   :lead [:p (:description project)]
   :aside (list
           (cond
            (:illustration project) (render-illustration project)
            (:site project) (render-site (:site project))))
   :body (list
          (maybe-include project :tech render-tech)
          (maybe-include project :people render-people)
          (maybe-include project :endorsements render-endorsements))})

(defn project-pages [projects]
  (into {} (map (juxt :url #(partial project-page %)) projects)))
