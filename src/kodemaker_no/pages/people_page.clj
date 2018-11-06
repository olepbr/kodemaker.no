(ns kodemaker-no.pages.people-page
  (:require [kodemaker-no.cultivate.people :as people]))

(defn- render-person [{:keys [url photos full-name title]}]
  [:div.gridUnit.r-4-3
   [:a.gridContent.linkBlock.tight.fpp {:href url}
    [:span.block.framed.mbs [:img {:src (:side-profile-near photos)}]]
    [:span.linkish full-name]]])

(defn- compare-by-start-date [a b]
  (compare (:start-date a)
           (:start-date b)))

(defn people-page [people]
  (let [people (people/sorted-profiles people)]
    {:title {:h1 (str (count people) " blide mennesker")
             :arrow (:url (first people))}
     :body [:div.grid
            (map render-person people)]}))
