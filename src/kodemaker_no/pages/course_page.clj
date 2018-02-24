(ns kodemaker-no.pages.course-page
  (:require [clojure.java.io :as io]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.markup :as markup]))

(defn- render-item [{:keys [url title tech by blurb]}]
  (list
   [:h3 [:a {:href url} title]]
   (f/render-tech-bubble tech by)
   [:p blurb]))

(defn course-page [videos screencasts]
  {:title "Lærelysten? Vi deler gjerne!"
   :sections [{:body [:div.bd.iw [:p "Bli med på foredrag, kurs, eller workshop, eller len deg tilbake og se en screencast."]]}
              {:type "illustrated-column"
               :title "Foredrag"
               :illustration "/forside/foredrag.jpg"
               :body (map render-item videos)}
              {:type "illustrated-column"
               :title "Screencasts"
               :illustration "/forside/screencast.jpg"
               :body (map render-item screencasts)}]})
