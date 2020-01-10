(ns kodemaker-no.new-pages.person
  (:require [ui.icons :as icons]
            [kodemaker-no.homeless :as h]))

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

(defn prefer-techs [preferred techs]
  (if (seq preferred)
    (let [pref-count (count preferred)]
      (sort-by (fn [tech]
                 (let [idx (.indexOf preferred tech)]
                   ;; If there is a preference for this tech, use it's index in
                   ;; the sorted list of preferences. Otherwise, return the
                   ;; number of preferences, which will keep the prior sort
                   ;; order for techs for which there is no preference, and
                   ;; place them all after the preferred techs.
                   (if (<= 0 idx)
                     idx
                     pref-count))) techs))
    techs))

(defn preferred-techs [person]
  (h/entity-seq (:person/preferred-techs person)))
