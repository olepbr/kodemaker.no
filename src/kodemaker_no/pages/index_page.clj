(ns kodemaker-no.pages.index-page
  (:require [clojure.java.io :as io]
            [kodemaker-no.formatting :refer [to-html]]
            [mapdown.core :as mapdown]))

(defn- render-our-reference-description [name description url logo]
  (list
   [:a.linkBlock.right.mod.mtl.logo {:href url}
    [:img {:src logo}]]
   [:h3 name]
   [:p description " "
    [:a.nowrap {:href url} "Se referansen"]]))

(defn- render-reference-quote [{:keys [photo author title quote email phone]} url logo name]
  [:div.media
   (when photo [:img.img.thumb.mts {:src photo}])
   [:div.bd
    [:p.mtn [:q quote]]
    [:p "&mdash; " [:strong.nowrap author] ", " [:span.nowrap title] ", " [:a.nowrap {:href url} name]]]])

(defn- render-reference [{:keys [name url description logo reference]}]
  (if reference
    (render-reference-quote reference url logo name)
    (render-our-reference-description name description url logo)))

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

(defn- render-frontpage-section [section]
  (if (:full-width section)
    (to-html (:body section))
    (list (when (:title section)
            [:h2.offset [:span.offset-content (:title section)]])
          [:div.line
           [:div.unit.text-adornment
            [:p (when (:illustration section)
                  [:img {:src (:illustration section)}])]]
           [:div.lastUnit
            (to-html (:body section))]])))

(defn index-page [people]
  (let [sorted-peeps (->> people
                          (remove :administration?)
                          (sort compare-by-start-date)
                          (reverse))]
    {:title {:h1 (str (num-consultants people) " kvasse konsulenter")
             :arrow (:url (first sorted-peeps))}
     :bricks [{:url "/clojure/", :text "Clojure"}
              {:url "/java/", :text "Java"}
              {:url "/groovy/", :text "Groovy"}
              {:url "/javascript/", :text "JavaScript"}
              {:url "/ruby/", :text "Ruby"}
              {:url "/react/", :text "React"}
              {:url "/git/", :text "Git"}
              {:url "/gradle/", :text "Gradle"}]
     :body (list
            [:div.grid (map render-person sorted-peeps)]
            [:h1.hn.pth "Hva er raskeste veien i mål?"]
            [:div
             (->> (io/resource "index.md")
                  slurp
                  mapdown/parse
                  (map render-frontpage-section))])}))
