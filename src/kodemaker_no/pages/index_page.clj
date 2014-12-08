(ns kodemaker-no.pages.index-page
  (:require [clojure.java.io :as io]
            [kodemaker-no.formatting :refer [to-html]]
            [mapdown.core :as mapdown]))

(defn- render-person [{:keys [url photos full-name title]}]
  [:div.gridUnit.r-4-3
   [:a.gridContent.linkBlock.tight.fpp {:href url}
    [:span.block.framed.mbs [:img {:src (:side-profile-near photos)}]]
    [:span.linkish full-name]]])

(defn- num-consultants [people]
  (->> people
       (remove :administration?)
       (count)))

(defn- compare-by-start-date [a b]
  (compare (:start-date a)
           (:start-date b)))

(defn render-cloud-person [i person]
  [:a {:href (:url person)
       :class (str
               "hn nowrap "
               (when (= 1 (mod i 2)) "black"))}
   (:full-name person)])

(defn- create-people-cloud [people]
  {:body [:div.bd.iw.mtxl.large
          [:div.center
           (->> people
                (map-indexed render-cloud-person)
                (interpose [:span " " [:span "&nbsp;"]]))]]}) ;; two spaces between names

(defn- insert-person-cloud [people section]
  (if (= "person-cloud" (:type section))
    (create-people-cloud people)
    section))

(defn index-page [people]
  (let [sorted-peeps (->> people
                          (remove :administration?)
                          (sort compare-by-start-date)
                          (reverse))]
    {:title {:h1 (str (num-consultants people) " kvasse konsulenter")
             :arrow (:url (first sorted-peeps))}
     :full-width true
     :sections (->> (io/resource "index.md")
                    slurp
                    mapdown/parse
                    (map #(insert-person-cloud sorted-peeps %)))}))
