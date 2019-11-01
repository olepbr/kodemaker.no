(ns ui.sections.grid-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.sections.grid-section :as section]))

(defcard grid-section
  (section/render
   {:items [{:image "/devcard_images/opencv.jpg"
             :alt "OpenCV"
             :href "https://opencv.org/"}
            {:image "/devcard_images/clojure.svg"
             :alt "Clojure"
             :href "/clojure/"}
            {:image "/devcard_images/python-logo.png"
             :alt "Python"
             :href "/python/"
             :size 2}
            {:image "/devcard_images/cljs.svg"
             :alt "ClojureScript"
             :href "/clojurescript/"}
            {:image "/devcard_images/kubernetes.png"
             :alt "Kubernetes"
             :href "/kubernetes/"}
            {:image "/devcard_images/kubernetes.png"
             :alt "Kubernetes"
             :href "/kubernetes/"}
            {:image "/devcard_images/kubernetes.png"
             :alt "Kubernetes"
             :href "/kubernetes/"}]}))
