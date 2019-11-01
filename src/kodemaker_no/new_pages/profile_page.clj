(ns kodemaker-no.new-pages.profile-page
  (:require [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [ui.elements :as e]))

(def presence-base-urls
  {:twitter "https://twitter.com/"
   :linkedin "https://www.linkedin.com"
   :stackoverflow "https://stackoverflow.com/"
   :github "https://github.com/"})

(defn fix-presence [presence]
  (into {}
        (for [[k v] presence]
          [k (str (presence-base-urls k) v)])))

(defn unwrap-idents [entity k]
  (map (partial d/entity (d/entity-db entity)) (k entity)))

(defn create-page [person]
  {:sections
   (->>
    [{:kind :profile
      :full-name (:person/full-name person)
      :image (str "/foto/profiles/" (name (:db/ident person)) ".jpg")
      :title (:person/title person)
      :mobile (:person/phone-number person)
      :mail (:person/email-address person)
      :cv {:text "Se full CV"
           :url (str "/cv/" (name (:db/ident person)) "/")}
      :description (f/markdown (:person/description person))
      :presence (fix-presence (:person/presence person))
      :pønt [{:kind :greater-than
              :position "top -270px left 12%"}
             {:kind :dotgrid
              :position "bottom -150px right -150px"}]}
     (when-let [recommendations (seq (:person/recommendations person))]
       {:kind :titled
        :title (str (f/genitive-name (:person/given-name person)) " anbefalinger")
        :content [:div
                  (for [recommendation recommendations]
                    (e/teaser
                     (cond-> {:title (:recommendation/title recommendation)
                              :tags (e/tech-tags {:techs (unwrap-idents recommendation :recommendation/tech)})
                              :url (:recommendation/url recommendation)
                              :text (:recommendation/description recommendation)}
                       (:recommendation/link-text recommendation)
                       (assoc :link {:text (:side-project/link-text recommendation)
                                     :href (:side-project/url recommendation)}))))]})
     {:kind :footer}]
    (remove nil?)
    (map (fn [color section]
           (assoc section :background color))
         (cycle [:blanc :blanc-rose])))})
