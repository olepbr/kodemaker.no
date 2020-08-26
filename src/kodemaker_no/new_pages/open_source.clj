(ns kodemaker-no.new-pages.open-source
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.markup :as m]))

(defn proglang [project]
  (->> (h/unwrap-ident-list project :oss-project/tech-list)
       (filter #(= :proglang (:tech/type %)))
       first))

(defn format-project [project]
  [:li.text.inline-text
   [:a {:href (:oss-project/url project)} (:oss-project/name project)]
   " - "
   (m/strip-paragraph (f/to-html (:oss-project/description project)))])

(defn format-contribution [project]
  [:a {:href (:oss-project/url project)}
   (:oss-project/name project)])

(comment

  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))
  (def magnar (d/entity db [:db/ident :person/magnar]))

  (into {} (first (:person/open-source-contributions magnar)))
  (into {} (proglang (first (:person/open-source-contributions magnar))))

)
