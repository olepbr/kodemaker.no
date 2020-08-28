(ns kodemaker-no.new-pages.references-page
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [ui.elements :as e]))

(defn create-page [page]
  (let [db (d/entity-db page)
        references (->> (d/q '[:find [?e ...]
                               :in $
                               :where
                               [?e :reference/page-title]]
                             db)
                        (map #(d/entity db %))
                        (remove #(= "hidden" (:reference/priority %)))
                        (sort-by #(:reference/priority % "99999")))]
    {:title "Våre referanser"
     :sections
     [(let [{:reference/keys [image half-circle-portrait blurb quote sections logo company href] :as reference}
            (first references)]
        {:kind :references-intro
         :title quote
         :image (some->> half-circle-portrait (str "/bottom-rouge-half-circle"))
         :logo {:image logo
                :title company
                :href href}
         :link {:text "Les mer"
                :href (:page/uri reference)}
         :content blurb})
      {:kind :hyrule
       :contents (for [{:reference/keys [image signee-name signee-title signee-phone
                                         portrait blurb quote sections logo company href] :as reference}
                       (next references)]
                   (e/attributed-content {:title [:h2.h4 [:a {:href (:page/uri reference)} quote]]
                                          :person {:image (some->> portrait (str "/vcard-small"))
                                                   :lines [[:strong signee-name]
                                                           signee-title
                                                           signee-phone]}
                                          :logo {:image logo
                                                 :title company
                                                 :href href}
                                          :link {:text "Les mer"
                                                 :href (:page/uri reference)}
                                          :content (e/blockquote
                                                    {:quote blurb})}))}
      {:kind :footer}]}))

(comment
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  (create-page
   (d/entity db :person/nils))

  )
