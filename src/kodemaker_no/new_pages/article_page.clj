(ns kodemaker-no.new-pages.article-page
  (:require [datomic-type-extensions.api :as d]
            [ui.elements :as e]
            [kodemaker-no.formatting :as f]))

(defn create-page [article]
  {:title (:article/title article)
   :sections
   [{:kind :header}
    (if (= :simple (:article/layout article))
      {:kind :container
       :content (e/simple-article
                 {:title (:article/title article)
                  :content [:div
                            (->> [(:article/lead article)
                                  (:article/body article)]
                                 (remove empty?)
                                 (map f/markdown))]})}
      {:kind :article
       :pønt [{:kind :ascending-line
               :position "top 0 left -400px"}]
       :articles (assoc-in
                  (->> [(:article/lead article)
                        (:article/body article)]
                       (remove empty?)
                       (mapv (fn [s] {:content (f/markdown s)})))
                  [0 :mecha-title] (:article/title article))})
    {:kind :footer}]})
