(ns kodemaker-no.new-pages.tech-page
  (:require [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :refer [map-vals max-by]]
            [ui.elements :as e]))

(defn get-main-aside [tech]
  (when-let [pres (some->> (:presentation/_techs tech)
                           (filter :presentation/thumb)
                           (max-by :presentation/date))]
    [:div.hide-below-1000
     (e/video-thumb
      {:img (str "/rouge-duotone/" (:presentation/thumb pres))
       :tags (e/people-tags {:prefix "Av"
                             :class "tags"
                             :people [(:person/_presentations pres)]})
       :url (:page/uri pres)
       :title (:presentation/title pres)})]))

(defn create-page [tech]
  {:sections
   (->>
    [{:kind :header :background :chablis}
     {:kind :tech-intro
      :title (:tech/name tech)
      :logo (:tech/illustration tech)
      :article {:content [:div.text
                          (f/to-html (:tech/description tech))]
                :alignment :front
                :aside (get-main-aside tech)}
      :pønt [{:kind :greater-than
              :position "top -410px right 60vw"}
             {:kind :dotgrid
              :position "top -110px left 80vw"}]}

     {:kind :footer}]
    (remove nil?))})

(comment

  (require '[datomic.api :as d])
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))


  (->>
   (for [[e file] (d/q '[:find ?e ?file
                         :where
                         [?e :presentation/video-url _ ?tx]
                         [?tx :tx-source/file-name ?file]] db)]
     (let [pres (d/entity db e)]
       [file
        (:presentation/video-url pres)
        (:presentation/thumb pres)
        (:presentation/title pres)]))
   (group-by first)
   (map-vals #(mapv (comp vec next) %)))




  )
