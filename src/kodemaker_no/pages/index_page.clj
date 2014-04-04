(ns kodemaker-no.pages.index-page
  (:require [clojure.java.io :as io]
            [kodemaker-no.formatting :refer [to-html]]))

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
    [:span.block.framed.mbs [:img {:src (:side-profile-cropped photos)}]]
    [:span.linkish full-name]]])

(defn- num-consultants [people]
  (->> people
       (remove :administration?)
       (count)))

(defn- compare-by-start-date [a b]
  (compare (:start-date a)
           (:start-date b)))

(defn index-page [people data]
  {:title {:h1 (str (num-consultants people) " kvasse konsulenter")}
   :body (list
          [:div.grid (->> people
                          (remove :administration?)
                          (sort compare-by-start-date)
                          (reverse)
                          (map render-person))]
          [:h1.hn "Hva er raskeste veien i mål?"]
          [:div
           (to-html (slurp (io/resource "index.md")))])})
