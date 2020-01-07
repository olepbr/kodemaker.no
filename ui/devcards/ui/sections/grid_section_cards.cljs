(ns ui.sections.grid-section-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [ui.elements :as e]
            [ui.sections :as sections]))

(defcard grid-section
  (sections/grid-section
   {:grid-type :box-grid
    :items [{:content
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

(def card-data
  {:image "/devcard_images/profile.jpg"
   :title "Justin Moore"
   :lines ["Systemutvikler"
           "+47 934 17 480"
           "christin@kodemaker.no"]})

(defcard grid-section-2
  (sections/grid-section
   {:grid-type :card-grid
    :items (->> [(assoc card-data :curtain :left)
                 card-data
                 card-data
                 card-data
                 card-data
                 (assoc card-data :curtain :right)]
                (map (fn [data] {:content (e/illustrated data)})))}))
