(ns kodemaker-no.pages.people-page
  (:require [clojure.java.io :as io]
            [kodemaker-no.formatting :refer [to-html]]
            [mapdown.core :as mapdown]))

(defn- render-person [{:keys [url photos full-name title]}]
  [:div.gridUnit.r-4-3
   [:a.gridContent.linkBlock.tight.fpp {:href url}
    [:span.block.framed.mbs [:img {:src (:side-profile-near photos)}]]
    [:span.linkish full-name]]])

(defn- num-people [people]
  (->> people (remove :quit?) (count)))

(defn- compare-by-start-date [a b]
  (compare (:start-date a)
           (:start-date b)))

(defn people-page [people]
  (let [sorted-peeps (->> people
                          (remove :quit?)
                          (sort compare-by-start-date)
                          (reverse))]
    {:title {:h1 (str (num-people people) " blide mennesker")
             :arrow (:url (first sorted-peeps))}
     :body [:div.grid
            (map render-person sorted-peeps)]}))
