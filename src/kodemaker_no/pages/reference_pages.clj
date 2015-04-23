(ns kodemaker-no.pages.reference-pages
  (:require [clojure.string :as str]
            [kodemaker-no.formatting :refer [to-html]]
            [kodemaker-no.homeless :refer [update-vals rename-keys]]))

(defn- render-participant [[id text] people]
  (let [person ((keyword id) people)]
    [:div.line
     [:div.unit.s-1of3.hide-lt-460
      [:p.centered-face.phm [:img.framed {:src (:side-profile-near (:photos person))}]]]
     [:div.lastUnit
      [:h1.hn.mbn
       [:a {:href (:url person)}
        (:first-name person)]]
      [:p.mts text]]]))

(defn- render-participants [section people]
  {:body [:div.bd.iw
          (when (:title section)
            [:h2.offset [:span.offset-content (:title section)]])
          (-> (:content section)
              (str/split #"\n\n")
              (->> (partition 2)
                   (map #(render-participant % people))))]})

(defn- render-reference-meta [{:keys [title body team-size factoid-1 factoid-2]}]
  (when (< 6 (Integer/parseInt team-size))
    (throw (ex-info "Only 6 team members will fit in the current layout"
                    {:solution "Some CSS/design work needs to be done."})))
  {:body
   [:div.bd.iw
    [:div.ref-meta.mod.line
     [:div.ref-meta-left [:div.white
                          [:h5 title]
                          (to-html body)]]
     [:div.ref-meta-right
      [:div.peeps.white.hide-lt-660
       (repeat (Integer/parseInt team-size) [:img {:src "/icons/team-member.svg"}])]
      [:div.mod.mbs factoid-1 [:br] factoid-2]]]]})

(defn- reference-page [sections people]
  {:title (:page-title (first sections))
   :sections (->> sections
                  (map #(case (:type %)
                          "participants" (render-participants % people)
                          "reference-meta" (render-reference-meta %)
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
