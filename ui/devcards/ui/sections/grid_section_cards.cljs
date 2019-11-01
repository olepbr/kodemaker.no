(ns ui.sections.grid-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.sections.grid-section :as section]
            [ui.elements :as e]))

(defcard grid-section
  (section/render
   {:items [{:content
             (e/image-link
              {:image "/devcard_images/opencv.jpg"
               :alt "OpenCV"
               :href "https://opencv.org/"})}
            {:content
             (e/image-link
              {:image "/devcard_images/clojure.svg"
               :alt "Clojure"
               :href "/clojure/"})}
            {:content
             (e/image-link
              {:image "/devcard_images/python-logo.png"
               :alt "Python"
               :href "/python/"})
             :size 2}
            {:content
             (e/image-link
              {:image "/devcard_images/cljs.svg"
               :alt "ClojureScript"
               :href "/clojurescript/"})}
            {:content
             (e/image-link
              {:image "/devcard_images/kubernetes.png"
               :alt "Kubernetes"
               :href "/kubernetes/"})}
            {:content
             (e/image-link
              {:image "/devcard_images/kubernetes.png"
               :alt "Kubernetes"
               :href "/kubernetes/"})}
            {:content
             (e/image-link
              {:image "/devcard_images/kubernetes.png"
               :alt "Kubernetes"
               :href "/kubernetes/"})}]}))
