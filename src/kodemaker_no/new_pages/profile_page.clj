(ns kodemaker-no.new-pages.profile-page
  (:require [kodemaker-no.formatting :as f]))

(def presence-base-urls
  {:twitter "https://twitter.com/"
   :linkedin "https://www.linkedin.com"
   :stackoverflow "https://stackoverflow.com/"
   :github "https://github.com/"})

(defn fix-presence [presence]
  (into {}
        (for [[k v] presence]
          [k (str (presence-base-urls k) v)])))

(defn create-page [person]
  {:sections
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
    {:kind :footer}]})
