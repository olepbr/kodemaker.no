(ns kodemaker-no.prepare-pages
  (:require [kodemaker-no.render-page :refer [render-page]]
            [kodemaker-no.homeless :refer [update-vals]]
            [net.cgrand.enlive-html :refer [sniptest]]
            [optimus.link :as link]))

(defn- optimize-path-fn [request]
  (fn [src]
    (or (not-empty (link/file-path request src))
        (throw (Exception. (str "Asset not loaded: " src))))))

(defn- replace-reference-tag [{:keys [attrs content]}]
  (let [{:keys [img url logo name phone title]} attrs]
    {:tag :div
     :attrs {:class "media"}
     :content [{:tag :img
                :attrs {:src img
                        :class "img thumb mts"}}
               {:tag :div
                :attrs {:class "bd"}
                :content [{:tag :a
                           :attrs {:href url
                                   :class "linkBlock right mod mts logo"}
                           :content [{:tag :img
                                      :attrs {:src logo}}]}
                          {:tag :h4
                           :attrs {:class "mtn"}
                           :content [name
                                     {:tag :a
                                      :attrs {:href (str "tel:" phone)
                                              :class "nowrap"}
                                      :content phone}]}
                          {:tag :p
                           :attrs {:class "near"}
                           :content title}
                          {:tag :p
                           :attrs {:class "near"}
                           :content [{:tag :q
                                      :content content}]}
                          {:tag :p
                           :content [{:tag :a
                                      :attrs {:href url}
                                      :content "Se referansen"}]}]}]}))

(defn- tweak-pages [html request]
  (sniptest html

            ;; use optimized images
            [:img] #(update-in % [:attrs :src] (optimize-path-fn request))

            ;; implement <reference> tag
            [:reference] replace-reference-tag))

(defn prepare-page [get-page request]
  (-> (get-page)
      (render-page request)
      (tweak-pages request)))

(defn prepare-pages [pages]
  (update-vals pages #(partial prepare-page %)))
