(ns kodemaker-no.prepare-pages
  (:require [clojure.string :as str]
            [hiccup.core :refer [html]]
            [html5-walker.core :as html5-walker]
            [imagine.core :as imagine]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.highlight :as hl]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.render-page :as render]
            [optimus.link :as link]))

(defn- optimize-path-fn [image-asset-config request]
  (fn [src]
    (try
      (let [[url skigard] (str/split src #"#")]
        (str
         (or (not-empty (link/file-path request url))
             (imagine/realize-url image-asset-config url)
             (throw (Exception. (str "Asset not loaded: " url))))
         (some->> skigard (str "#"))))
      (catch Exception e
        (throw (ex-info "Failed to optimize path" {:src src} e))))))

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

(defn replace-urls-fn [f]
  (fn [style]
    (when style
      (str/replace style #"url\((.+?)\)"
                   (fn [[_ url]]
                     (str "url(" (f url) ")"))))))

(defn replace-path-fn [f]
  (fn [path]
    (str/replace path #"(\S+)(\s+\S+)?"
               (fn [[_ path suffix]]
                 (str (f path) suffix)))))

(defn replace-paths-fn [f]
  (fn [paths]
    (when paths
      (str/join ", "
                (map (replace-path-fn f)
                     (str/split paths #",\s*"))))))

(defn update-img-attrs [node f]
  (update-attr node "src" f)
  (when (.getAttribute node "srcset")
    (update-attr node "srcset" (replace-paths-fn f))))

(defn- tweak-page-markup [html image-asset-config request]
  (try
    (html5-walker/replace-in-document
     html
     {
      ;; use optimized images
      [:img] #(update-img-attrs % (optimize-path-fn image-asset-config request))
      [:head :meta] #(if (and (= (.getAttribute % "property") "og:image") (not (.getAttribute % "content")))
                      (update-attr % "content" (optimize-path-fn image-asset-config request)))
      [:.w-style-img] #(update-attr % "style" (replace-urls-fn (optimize-path-fn image-asset-config request)))
      [:.section] #(update-attr % "style" (replace-urls-fn (optimize-path-fn image-asset-config request)))
      [:video :source] #(update-attr % "src" (optimize-path-fn image-asset-config request))

      ;; use optimized svgs
      [:svg :use] #(replace-attr % "href" "xlink:href" (optimize-path-fn image-asset-config request))

      [:h2] add-anchor

      ;; use optimized links, if possible
      [:a] #(update-attr % "href" (partial fix-links request))

      ;; Syntax highlight fenced code blocks
      [:pre :code] hl/maybe-highlight-node
      [:pre] hl/add-hilite-class})
    (catch Exception e
      (throw (ex-info "Error while tweaking page markup"
                      {:image-asset-config image-asset-config
                       :request (dissoc request :optimus-assets)}
                      e)))))

(defn post-process-page [html image-asset-config request]
  (-> html
      (tweak-page-markup image-asset-config request)))

(defn prepare-page [image-asset-config get-page request]
  (-> (get-page)
      (render/render-page request)
      (tweak-page-markup image-asset-config request)))

(defn prepare-pages [pages image-asset-config]
  (h/update-vals pages #(partial prepare-page image-asset-config %)))
