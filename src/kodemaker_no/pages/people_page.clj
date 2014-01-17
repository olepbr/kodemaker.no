(ns kodemaker-no.pages.people-page)

(defn- render-person [person]
  [:div.gridUnit.r-4-3-2
   [:a.photoframe.gridContent.linkBlock {:href (:url person)}
    [:div.paspartur.tiny
     [:img.photo {:src (-> person :photos :side-profile)}]
     [:span.linkish (:full-name person)]
     [:span.title (:title person)]]]])

(defn- num-consultants [people]
  (->> people
       (remove :administration?)
       (count)))

(defn all-people [people]
  {:title (str (num-consultants people) " kvasse konsulenter")
   :body (list
          [:div.grid (->> people
                          (sort-by #(:order % 0) >)
                          (map render-person))]
          [:p "Det er menneskene som betyr noe. Hos oss legger vi vekt på å bygge relasjoner og sørge for at de sosiale båndene er sterke. Hvis man trives på fritiden og føler at man har havnet i riktig firma, så vil man også fungere bedre i jobben som konsulent. Dette vil også kundene merke."])})
