(ns kodemaker-no.prepare-pages
  (:require [clojure.string :as str]
            [hiccup.core :refer [html]]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.highlight :as hl]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.html5-walker :as html5-walker]
            [kodemaker-no.images :as images]
            [kodemaker-no.render-page :as render]
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

(defn- add-anchor [node]
  (when-not (= "a" (.getNodeName (first (.getChildNodes node))))
    (let [id-str (f/to-id-str (.getTextContent node))]
      (.setInnerHTML
       node
       (html
        [:a.anchor-link {:id id-str :href (str "#" id-str)}
         [:span.anchor-marker "¶"]
         (.getInnerHTML node)])))))

(defn update-attr [node attr f]
  (.setAttribute node attr (f (.getAttribute node attr))))

(defn replace-attr [node attr-before attr-after f]
  (.setAttribute node attr-after (f (.getAttribute node attr-before)))
  (.removeAttribute node attr-before))

(defn- tweak-pages [html image-asset-config request]
  (html5-walker/replace
   html
   {
    ;; use optimized images
    [:img] #(update-attr % "src" (optimize-path-fn image-asset-config request))

    ;; use optimized svgs
    [:svg :use] #(replace-attr % "href" "xlink:href" (optimize-path-fn image-asset-config request))

    [:h2] add-anchor

    ;; use optimized links, if possible
    [:a] #(update-attr % "href" (partial fix-links request))

    ;; Syntax highlight fenced code blocks
    [:pre :code] hl/maybe-highlight-node
    [:pre] hl/add-hilite-class}))

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
