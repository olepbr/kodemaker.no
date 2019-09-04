(ns kodemaker-no.prepare-pages
  (:require [clojure.string :as str]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.highlight :as hl]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.images :as images]
            [kodemaker-no.render-page :as render]
            [net.cgrand.enlive-html :as enlive]
            [optimus.link :as link]))

(defn- optimize-path-fn [image-asset-config request]
  (fn [src]
    (or (not-empty (link/file-path request src))
        (images/realize-url image-asset-config src)
        (throw (Exception. (str "Asset not loaded: " src))))))

(defn- try-optimize-path [request path]
  (or (not-empty (link/file-path request path))
      path))

(defn- fix-links [request path]
  (when-let [path (try-optimize-path request path)]
    (if (and (:base-url request)
             (str/starts-with? path "/")
             (not (str/starts-with? path "//")))
      (str (:base-url request) path)
      path)))

(defn- get-node-text [node]
  (let [text (-> node :content first)]
    (if (string? text) text (get-node-text text))))

(defn- wrap-in-anchor [content target]
  (cond
    (string? (first content))
    [{:tag :a
      :attrs {:class "anchor-link"
              :id target
              :href (str "#" target)}
      :content (into [{:tag :span
                       :attrs {:class "anchor-marker"}
                       :content "¶"}] content)}]

    (= :a (-> content first :tag))
    content

    :default
    (into [(update-in (first content) [:content] #(wrap-in-anchor % target))] (rest content))))

(defn- add-anchor [node]
  (update-in node [:content] #(wrap-in-anchor % (f/to-id-str (get-node-text node)))))

(defn- tweak-pages [html image-asset-config request]
  (enlive/sniptest
   html
   ;; use optimized images
   [:img] #(update-in % [:attrs :src] (optimize-path-fn image-asset-config request))

   ;; use optimized links, if possible
   [:a] #(update-in % [:attrs :href] (partial fix-links request))

   ;; give every h2 an anchor link for linkability
   [:h2] add-anchor

   ;; Syntax highlight fenced code blockse
   [:pre :code] hl/maybe-highlight-node
   [:pre] hl/maybe-add-hilite-class))

(defn- use-norwegian-quotes [html]
  (-> html
      (str/replace "“" "«")
      (str/replace "”" "»")))

(defn prepare-page [image-asset-config get-page request]
  (-> (get-page)
      (render/render-page request)
      (tweak-pages image-asset-config request)
      use-norwegian-quotes))

(defn prepare-pages [pages image-asset-config]
  (h/update-vals pages #(partial prepare-page image-asset-config %)))
