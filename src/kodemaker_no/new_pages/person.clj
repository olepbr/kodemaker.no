(ns kodemaker-no.new-pages.person
  (:require [ui.icons :as icons]))

(def presence-base-urls
  {:twitter "https://twitter.com/"
   :linkedin "https://www.linkedin.com"
   :stackoverflow "https://stackoverflow.com/"
   :github "https://github.com/"})

(def presence-order [:linkedin :stackoverflow :twitter :github])

(defn prep-presence-links [presence]
  (->> (for [[k v] (select-keys presence presence-order)]
         [k (str (presence-base-urls k) v)])
       (sort-by #(.indexOf presence-order (first %)))
       (map (fn [[k url]]
              {:href url
               :target "_blank"
               :icon (icons/icon k)}))))
