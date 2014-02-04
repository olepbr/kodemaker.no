(ns kodemaker-no.pages.people-page
  (:require [kodemaker-no.homeless :as compare*]))

(defn- render-person [{:keys [url photos full-name title]}]
  [:div.gridUnit.r-4-3-2
   [:a.photoframe.gridContent.linkBlock {:href url}
    [:span.mount.tiny.block
     [:img.mbs {:src (:side-profile photos)}]
     [:span.linkish full-name]
     [:span.title title]]]])

(defn- num-consultants [people]
  (->> people
       (remove :administration?)
       (count)))

(defn- compare-by-admin-and-start-date [a b]
  (or (compare* (:administration? b)
                (:administration? a))
      (compare* (:start-date a)
                (:start-date b))
      0))

(defn all-people [people]
  {:title (str (num-consultants people) " kvasse konsulenter")
   :body (list
          [:div.grid (->> people
                          (sort compare-by-admin-and-start-date)
                          (reverse)
                          (map render-person))]
          [:p "Det er menneskene som betyr noe. Hos oss legger vi vekt på å bygge relasjoner og sørge for at de sosiale båndene er sterke. Hvis man trives på fritiden og føler at man har havnet i riktig firma, så vil man også fungere bedre i jobben som konsulent. Dette vil også kundene merke."])})
