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

(defn- render-reference-quote [{:keys [photo author title quote email phone]} url logo]
  [:div.media
   (when photo [:img.img.thumb.mts {:src photo}])
   [:div.bd
    [:a.linkBlock.right.mod.mts.logo {:href url}
     [:img {:src logo}]]
    [:h4.mtn author (when phone (list " " [:a.nowrap {:href (str "tel:" phone)} phone]))]
    (when title [:p.near title])
    [:p.near [:q quote]]
    [:p [:a {:href url} "Se referansen"]]]])

(defn- render-reference [{:keys [name url description logo reference]}]
  (if reference
    (render-reference-quote reference url logo)
    (render-our-reference-description name description url logo)))

(defn- render-person [{:keys [url photos full-name title]}]
  [:div.gridUnit.r-8-6-4
   [:a.photoframe.gridContent.linkBlock {:href url}
    [:span.thinMount.tiny.block
     [:img.mbs {:src (:side-profile-tiny photos)}]
     ]]])

(defn- num-consultants [people]
  (->> people
       (remove :administration?)
       (count)))

(defn- compare-by-start-date [a b]
  (compare (:start-date a)
           (:start-date b)))

(defn index-page [people data]
  {:body (list
          [:div.grid (->> people
                          (remove :administration?)
                          (sort compare-by-start-date)
                          (reverse)
                          (map render-person))]
          [:h1.hn (str (num-consultants people) " kvasse konsulenter")]
          (map render-reference (:references data))
          [:h1.hn "KODE. FORTERE. TRYGGERE."]
          [:div.bigProse
           (to-html (slurp (io/resource "index.md")))])})

