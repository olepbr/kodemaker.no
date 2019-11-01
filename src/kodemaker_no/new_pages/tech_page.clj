(ns kodemaker-no.new-pages.tech-page
  (:require [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :refer [map-vals max-by]]
            [ui.elements :as e]))

(defn add-main-aside [article tech]
  (let [pres (->> (:presentation/_tech tech)
                  (filter :presentation/thumb)
                  (max-by :presentation/date))]
    (cond-> {:title (str "Hva er " (:tech/name tech))
             :content [:div.text
                       (f/to-html (:tech/description tech))]
             :alignment :front}
      pres
      (-> (assoc :aside-title "Foredrag")
          (assoc :aside (e/video-thumb
                         {:img (str "/rouge-duotone/" (:presentation/thumb pres))
                          :tags (e/people-tags {:prefix "Av"
                                                :people [(:person/_presentations pres)]})
                          :url (:page/uri pres)
                          :title (:presentation/title pres)}))))))

(defn create-page [tech]
  {:sections
   [{:kind :header}
    {:kind :banner
     :text (:tech/name tech)
     :logo (:tech/illustration tech)}
    {:kind :article
     :articles [(-> {:title (str "Hva er " (:tech/name tech))
                     :content [:div.text
                               (f/to-html (:tech/description tech))]
                     :alignment :front}
                    (add-main-aside tech))]}
    (let [side-project (->> (:side-project/_tech tech)
                            shuffle
                            first)]
      {:kind :article
       :articles [{:title "Sideprosjekter"
                   :content (e/teaser (cond-> {:title (:side-project/title side-project)
                                               :tags (e/people-tags {:prefix "Av"
                                                                     :people [(:person/_side-projects side-project)]})
                                               :text (:side-project/description side-project)
                                               :url (:side-project/url side-project)}
                                        (:side-project/link-text side-project)
                                        (assoc :link {:text (:side-project/link-text side-project)
                                                      :href (:side-project/url side-project)})))
                   :image (str "/chocolate-triangle/" (:side-project/illustration side-project))}]
       :background :blanc-rose})
    {:kind :footer}]})

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
