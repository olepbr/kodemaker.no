(ns kodemaker-no.pages.course-page
  (:require [clojure.java.io :as io]
            [kodemaker-no.formatting :refer [to-html comma-separated link-to-person]]
            [kodemaker-no.markup :as markup]))

(defn render-tech-bubble [tech by]
  (when-not (empty? tech)
    [:p.near.cookie-w
     [:span.cookie
      (comma-separated (map link-to-person by)) " om "
      (comma-separated (map markup/link-if-url tech))]]))

(defn- render-video [video]
  (list
   [:h3 [:a {:href (:url video)} (:title video)]]
   (render-tech-bubble (:tech video) (:by video))
   [:p (:blurb video)]))

(defn course-page [videos]
  {:title "Lærelysten? Vi deler gjerne!"
   :sections [{:body [:div.bd.iw [:p "Bli med på foredrag, kurs, eller workshop, eller len deg tilbake og se en screencast."]]}
              {:type "illustrated-column"
               :title "Foredrag"
               :illustration "/forside/foredrag.jpg"
               :body (map render-video videos)}
              {:type "illustrated-column"
               :title "Screencasts"
               :illustration "/forside/screencast.jpg"
               :body (slurp (io/resource "screencasts.md"))}]})
