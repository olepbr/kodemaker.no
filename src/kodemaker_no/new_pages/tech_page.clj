(ns kodemaker-no.new-pages.tech-page)

(defn create-page [tech]
  {:sections [{:kind :banner
               :text (:tech/name tech)
               :logo (:tech/illustration tech)}
              {:kind :footer}]})
