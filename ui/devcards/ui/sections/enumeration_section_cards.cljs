(ns ui.sections.enumeration-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard enumeration-section
  (sections/enumeration-section
   {:title "Teknologi"
    :categories [{:label "Favoritter for tiden:"
                  :items [{:text "Python" :href "/"}
                          {:text "ClojureScript" :href "/"}]}
                 {:label "Bruker på jobben:"
                  :items [{:text "Python" :href "/"}
                          {:text "OpenCV"}
                          {:text "Clojure" :href "/"}
                          {:text "ClojureScript" :href "/"}]}
                 {:label "Vil lære mer:"
                  :items [{:text "Clojure" :href "/"}
                          {:text "Emacs" :href "/"}
                          {:text "Rust"}
                          {:text "Go" :href "/"}
                          {:text "R"}
                          {:text "Machine learning"}
                          {:text "ClojureScript" :href "/"}]}]}))
