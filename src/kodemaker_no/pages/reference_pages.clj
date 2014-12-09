(ns kodemaker-no.pages.reference-pages
  (:require [clojure.string :as str]
            [kodemaker-no.homeless :refer [update-vals rename-keys]]))

(defn- render-participant [[id text] people]
  (let [person ((keyword id) people)]
    [:div.line
     [:div.unit.s-1of3.hide-lt-460
      [:p.centered-face.phm [:img.framed {:src (:side-profile-near (:photos person))}]]]
     [:div.lastUnit
      [:h1.hn.mbn
       [:a {:href (:url person)}
        (:full-name person)]]
      [:p.mts text]]]))

(defn- render-participants [section people]
  {:body [:div.bd.iw
          (when (:title section)
            [:h2.offset [:span.offset-content (:title section)]])
          (-> (:content section)
              (str/split #"\n\n")
              (->> (partition 2)
                   (map #(render-participant % people))))]})

(defn- reference-page [sections people]
  {:title (:page-title (first sections))
   :sections (->> sections
                  (map #(if (= "participants" (:type %))
                          (render-participants % people)
                          %)))})

(defn- reference-url [path]
  (str "/referanser"
       (if (= path "/index.md")
         "/index.html"
         (str/replace path #"\.md$" "/"))))

(defn reference-pages [references people]
  (-> references
      (rename-keys reference-url)
      (update-vals #(partial reference-page % people))))
