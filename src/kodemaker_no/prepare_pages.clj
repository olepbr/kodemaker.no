(ns kodemaker-no.prepare-pages
  (:require [clojure.string :as str]
            [kodemaker-no.homeless :refer [update-vals]]
            [kodemaker-no.render-page :refer [render-page]]
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
                                      :content (str/trim (first content))}]}
                          {:tag :p
                           :content [{:tag :a
                                      :attrs {:href url}
                                      :content "Se referansen"}]}]}]}))

(defn- to-megalist-item [[title text]]
  {:tag :p
   :attrs {:class "m-item"}
   :content [{:tag :strong
              :attrs {:class "m-title"}
              :content [{:tag :span
                         :content (:content title)}
                        {:tag :span
                         :attrs {:class "m-dot"}
                         :content "."}]}
             {:tag :span
              :attrs {:class "m-text"}
              :content (:content text)}]})

(defn- replace-megalist-tag [{:keys [content]}]
  {:tag :div
   :attrs {:class "megalist"}
   :content (->> content
                 (remove string?)
                 (partition 2)
                 (map to-megalist-item))})

(defn- tweak-pages [html request]
  (sniptest html
            ;; use optimized images
            [:img] #(update-in % [:attrs :src] (optimize-path-fn request))

            ;; implement <reference> tag
            [:reference] replace-reference-tag

            ;; implement <megalist> tag
            [:megalist] replace-megalist-tag))

(defn- use-norwegian-quotes [html]
  (-> html
   (str/replace "“" "«")
   (str/replace "”" "»")))

(defn prepare-page [get-page request]
  (-> (get-page)
      (render-page request)
      (tweak-pages request)
      use-norwegian-quotes))

(defn prepare-pages [pages]
  (update-vals pages #(partial prepare-page %)))
