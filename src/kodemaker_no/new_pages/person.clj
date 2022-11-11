(ns kodemaker-no.new-pages.person
  (:require [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.new-pages.open-source :as oss]
            [ui.elements :as e]
            [ui.icons :as icons]))

(defn mastodon-url [username]
  (let [[_ user server] (str/split username #"@")]
    (str "https://" server "/@" user)))

(def presence-url-fns
  {:mastodon mastodon-url})

(def presence-base-urls
  {:twitter "https://twitter.com/"
   :linkedin "https://www.linkedin.com"
   :stackoverflow "https://stackoverflow.com/"
   :github "https://github.com/"})

(def presence-order [:linkedin :stackoverflow :twitter :mastodon :github])

(defn prep-presence-links [presence]
  (->> (for [[k v] (select-keys presence presence-order)]
         [k (if-let [presence-url-fn (presence-url-fns k)]
              (presence-url-fn v)
              (str (presence-base-urls k) v))])
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

(defn open-source-projects [person]
  (concat
   (sort-by :list/idx (:person/open-source-projects person))
   (sort-by :list/idx (:person/open-source-contributions person))))

(defn prepare-open-source-projects [person]
  (when-let [projects (seq (open-source-projects person))]
    (let [db (d/entity-db person)
          by-techs (group-by (comp :db/ident oss/proglang) projects)]
      {:title "Bidrag til fri programvare"
       :techs
       (->> by-techs
            (sort-by (comp - count second))
            keys
            (prefer-techs (preferred-techs person))
            (map (fn [tech]
                   (let [projects (get by-techs tech)]
                     {:title (:tech/name (d/entity db tech))
                      :markup [:ul.dotted.dotted-tight
                               (map oss/format-project (filter :oss-project/description projects))
                               (when-let [contribs (->> projects
                                                        (remove :oss-project/description)
                                                        seq)]
                                 [:li.text
                                  "Har bidratt til "
                                  (e/comma-separated
                                   (map oss/format-contribution contribs))])]}))))})))
