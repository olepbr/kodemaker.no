(ns kodemaker-no.ingestion.tech
  (:require [kodemaker-no.homeless :as h]))

(def tech-keys
  {:db/ident :id
   :tech/name :name
   :tech/description :description
   :tech/illustration :illustration
   :tech/site :site
   :tech/ad :ad})

(def qualify-tech-kw (partial h/qualify "tech"))

(defn create-tx [file-name tech]
  [(-> tech
       (h/keep-vals tech-keys)
       (update :db/ident qualify-tech-kw))])

(defn is-page? [tech]
  (or (:tech/description tech)
      (< 5 (+ (* 10 (count (:presentation/_techs tech)))
              #_(* 6 (count (:presentation-product/_techs tech)))
              (* 5 (count (:screencast/_techs tech)))
              (* 5 (count (:blog-post/_techs tech)))
              (* 2 (count (:side-project/_techs tech)))
              (* 1 (count (:recommendation/_techs tech)))))))

(defn page [tech]
  {:db/ident (:db/ident tech)
   :page/uri (str "/" (name (:db/ident tech)) "/")
   :page/kind :page.kind/tech})

(defn create-tech-category-tx [_ categories]
  (mapcat (fn [[type {:keys [label idx techs parent]}]]
            (let [tech-type (h/qualify "tech-category" type)]
              (conj
               (for [id techs]
                 {:db/ident (qualify-tech-kw id)
                  :tech/type tech-type})
               (cond-> {:db/ident tech-type}
                 label (assoc :tech-category/label label)
                 parent (assoc :tech-category/parent (h/qualify "tech-category" parent))
                 idx (assoc :list/idx idx))))) categories))

(defn create-tech-name-tx [file-name id->name]
  (for [[id tech-name] id->name]
    {:db/ident (qualify-tech-kw id)
     :tech/name tech-name}))
