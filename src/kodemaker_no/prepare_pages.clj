(ns kodemaker-no.prepare-pages
  (:require [clojure.string :as str]
            [kodemaker-no.formatting :refer [to-id-str]]
            [kodemaker-no.highlight :refer [maybe-highlight-node maybe-add-hilite-class]]
            [kodemaker-no.homeless :refer [update-vals]]
            [kodemaker-no.render-page :refer [render-page]]
            [net.cgrand.enlive-html :refer [sniptest]]
            [optimus.link :as link]))

(defn- optimize-path-fn [request]
  (fn [src]
    (or (not-empty (link/file-path request src))
        (throw (Exception. (str "Asset not loaded: " src))))))

(defn- try-optimize-path [request path]
  (or (not-empty (link/file-path request path))
      path))

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
            ;; use optimized images
            [:img] #(update-in % [:attrs :src] (optimize-path-fn request))

            ;; use optimized links, if possible
            [:a] #(update-in % [:attrs :href] (partial try-optimize-path request))

            ;; give every h2 an anchor link for linkability
            [:h2] add-anchor

            ;; Syntax highlight fenced code blockse
            [:pre :code] maybe-highlight-node
            [:pre] maybe-add-hilite-class))

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
