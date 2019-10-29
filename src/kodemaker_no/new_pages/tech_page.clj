(ns kodemaker-no.new-pages.tech-page
  (:require [kodemaker-no.formatting :as f]))

(defn create-page [tech]
  {:sections [{:kind :header}
              {:kind :banner
               :text (:tech/name tech)
               :logo (:tech/illustration tech)}
              {:kind :intro
               :article {:title (str "Hva er " (:tech/name tech))
                         :text (f/to-html (:tech/description tech))}}
              {:kind :footer}]})
