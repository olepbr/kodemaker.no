(ns kodemaker-no.pages.people-page
  (:require [kodemaker-no.layout :refer [with-layout]]
            [optimus.link :as link]))

(defn- render-person [request person]
  [:div.gridUnit.r-4-3-2
   [:a.photoframe.gridContent.linkBlock {:href (:url person)}
    [:div.paspartur.tiny
     [:img.photo {:src (link/file-path request (-> person :photos :side-profile)
                                       :fallback "/photos/unknown/side-profile.jpg")}]
     [:span.linkish (:full-name person)]
     [:span.title (:title person)]]]])

(defn- num-consultants [people]
  (->> people
       (remove :administration?)
       (count)))

(defn all-people [people request]
  {:body
   (with-layout request (str (num-consultants people) " kvasse konsulenter")
     [:div.body
      [:div.bd
       [:div.grid
        (->> people
             (sort-by :order >)
             (map (partial render-person request)))]
       [:p "Det er menneskene som betyr noe. Hos oss legger vi vekt på å bygge relasjoner og sørge for at de sosiale båndene er sterke. Hvis man trives på fritiden og føler at man har havnet i riktig firma, så vil man også fungere bedre i jobben som konsulent. Dette vil også kundene merke."]]])})
