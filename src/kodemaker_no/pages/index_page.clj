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
       (remove :quit?)
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
  {:body [:div.bd.iw.mvxl.large
          [:div.center
           (->> people
                (map-indexed render-cloud-person)
                (interpose [:span " " [:span "&nbsp;"]]))]]}) ;; two spaces between names

(defn- create-obstacle-header [{:keys [title-1 title-2]}]
  {:body
   [:div.obstacle-header.mtl.rel
    [:div.bd.iw
     [:div.centered-column
      [:h3.mbl.mtm.xxlarge.hns
       [:span.nowrap title-1] " " [:span.nowrap title-2]]]
     [:div#obs-hd-0]
     [:div#obs-hd-1]
     [:div#obs-hd-2]
     [:div#obs-hd-3]
     [:div#obs-hd-4]
     [:div#obs-hd-5]
     [:div#obs-hd-6]
     [:div#obs-hd-7]
     [:div#obs-hd-8]]]})

(defn- update-section [people section]
  (case (:type section)
    "person-cloud" (create-people-cloud people)
    "obstacle-header" (create-obstacle-header section)
    section))

(defn index-page [people]
  (let [sorted-peeps (->> people
                          (remove :administration?)
                          (sort compare-by-start-date)
                          (reverse))]
    {:title {:h1 (str (num-consultants people) " kvasse konsulenter")
             :arrow (:url (first sorted-peeps))}
     :sections (->> (io/resource "index.md")
                    slurp
                    mapdown/parse
                    (map #(update-section sorted-peeps %)))}))
