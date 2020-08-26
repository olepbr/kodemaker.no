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
                        (sort-by #(:reference/priority % "99999")))]
    {:title "Våre referanser"
     :sections
     [{:kind :header}
      {:kind :hyrule
       :pønt [{:kind :greater-than
               :position "top -400px left -450px"}]
       :contents (map-indexed
                  (fn [idx {:reference/keys [image signee-name signee-title signee-phone page-title
                                             portrait blurb quote sections logo company href] :as reference}]
                    (e/attributed-content {:title (if (= 0 idx)
                                                    [:h1.h3.mw500 quote]
                                                    [:h2.h4 quote])
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
                                                     {:quote blurb})}))
                  references)}
      {:kind :footer}]}))

(comment
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  (create-page
   (d/entity db :person/nils))

  )
