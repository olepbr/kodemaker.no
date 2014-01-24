(ns kodemaker-no.markup)

(defn render-link [link]
  [:a.nowrap {:href (:url link)} (:text link)])
