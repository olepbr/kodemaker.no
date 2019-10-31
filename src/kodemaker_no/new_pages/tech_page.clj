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
                          :tags (:person/given-name (:person/_presentations pres))
                          :url (:page/uri pres)
                          :title (:presentation/title pres)}))))))

;; TODO: fix to nye foredrag av Christin uten thumb

(defn create-page [tech]
  {:sections
   [{:kind :header}
    {:kind :banner
     :text (:tech/name tech)
     :logo (:tech/illustration tech)}
    {:kind :article
     :article (-> {:title (str "Hva er " (:tech/name tech))
                   :content [:div.text
                             (f/to-html (:tech/description tech))]
                   :alignment :front}
                  (add-main-aside tech))}
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
