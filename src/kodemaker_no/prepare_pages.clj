(ns kodemaker-no.prepare-pages
  (:require [clojure.string :as str]
            [kodemaker-no.formatting :refer [to-id-str]]
            [kodemaker-no.highlight :refer [highlight-code-blocks]]
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
                         :attrs {:class "m-title-text"}
                         :content (:content title)}
                        {:tag :span
                         :attrs {:class "m-dot"}
                         :content "."}]}
             {:tag :span
              :attrs {:class "m-text-wrap"}
              :content [{:tag :span
                         :attrs {:class "m-text"}
                         :content (:content text)}]}]})

(defn- replace-megalist-tag [{:keys [content]}]
  {:tag :div
   :attrs {:class "megalist"}
   :content (->> content
                 (remove string?)
                 (partition 2)
                 (map to-megalist-item))})

(defn- get-node-text [node]
  (let [text (-> node :content first)]
    (if (string? text) text (get-node-text text))))

(defn- wrap-in-anchor [content target]
  (if (string? (first content))
    [{:tag :a
      :attrs {:class "anchor-link"
              :id target
              :href (str "#" target)}
      :content (into [{:tag :span
                       :attrs {:class "anchor-marker"}
                       :content "¶"}] content)}]
    (into [(update-in (first content) [:content] #(wrap-in-anchor % target))] (rest content))))

(defn- add-anchor [node]
  (update-in node [:content] #(wrap-in-anchor % (to-id-str (get-node-text node)))))

(defn- tweak-pages [html request]
  (sniptest html
            ;; implement <reference> tag
            [:reference] replace-reference-tag

            ;; implement <megalist> tag
            [:megalist] replace-megalist-tag

            ;; use optimized images
            [:img] #(update-in % [:attrs :src] (optimize-path-fn request))

            ;; give every h2 an anchor link for linkability
            [:h2] add-anchor))

(defn- use-norwegian-quotes [html]
  (-> html
      (str/replace "“" "«")
      (str/replace "”" "»")))

(def skip-pygments?
  (= (System/getProperty "kodemaker.skip.pygments") "true"))

(defn- maybe-highlight-code-blocks [page]
  "Parsing and highlighting with Pygments is quite resource intensive,
   on the order of adding 20 seconds to the full test run. This way we
   can disable the pygments by setting JVM_OPTS=\"-Dspid.skip.pygments=true\""
  (if-not skip-pygments?
    (highlight-code-blocks page)
    page))

(defn prepare-page [get-page request]
  (-> (get-page)
      (render-page request)
      (tweak-pages request)
      use-norwegian-quotes
      maybe-highlight-code-blocks))

(defn prepare-pages [pages]
  (update-vals pages #(partial prepare-page %)))
