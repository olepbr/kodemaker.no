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
  (let [{:keys [img url logo name phone title]} attrs
        quote (str "«" (str/trim (first content)) "»")]
    {:tag :div
     :attrs {:class "ref mod"}
     :content [{:tag :div
                :attrs {:class "ref-w"}
                :content [{:tag :div
                           :attrs {:class "ref-img"}
                           :content [{:tag :img
                                      :attrs {:src img}}]}
                          {:tag :div
                           :attrs {:class "ref-txt"}
                           :content [{:tag :p
                                      :content quote}]}
                          {:tag :div
                           :attrs {:class "ref-card"}
                           :content [{:tag :div
                                      :attrs {:class "ref-logo"}
                                      :content [{:tag :img
                                                 :attrs {:src logo}}]}
                                     {:tag :div
                                      :attrs {:class "ref-info tight"}
                                      :content [{:tag :h4
                                                 :content name}
                                                {:tag :p
                                                 :content [title
                                                           {:tag :br}
                                                           phone]}]}]}]}
               {:tag :div
                :attrs {:class "ref-txt-2"}
                :content [{:tag :p
                           :content quote}]}]}))

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
            ;; implement <reference> tag
            [:reference] replace-reference-tag

            ;; implement <megalist> tag
            [:megalist] replace-megalist-tag

            ;; use optimized images
            [:img] #(update-in % [:attrs :src] (optimize-path-fn request))))

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
