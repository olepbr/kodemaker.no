(ns ui.elements
  (:require [clojure.string :as str]))

(defn el [el params text]
  [(get params :element el)
   {:className (str (name el)
                    (when-let [cn (:className params)]
                      (str " " cn)))} text])

(def h1 (partial el :h1))
(def h2 (partial el :h2))
(def h3 (partial el :h3))
(def h4 (partial el :h4))
(def h5 (partial el :h5))

(defn text [text]
  [:div.text
   (for [sentence (str/split text #"\n\n")]
     [:p.p sentence])])

(defn blockquote [{:keys [quote]}]
  [:blockquote.blockquote.text {}
   (let [sentences (str/split quote #"\n\n")
         sentences (concat [(str "«" (first sentences))] (rest sentences))]
     (map
      (fn [sentence] [:p {:className "p"} sentence])
      (concat (take (dec (count sentences)) sentences) [(str (last sentences) "»")])))])

(defn arrow [{:keys [width]}]
  [:div.arrow [:div.arrow-head]])

(def size-classes
  {:large "text-l"})

(defn arrow-link [{:keys [text href size] :as params}]
  [:a.ib {:href href
          :className (size-classes size)}
   text (arrow params)])

(defn icon [{:keys [type width height]}]
  [:svg {:view-box "0 0 24 24"
         :width width
         :height height}
   [:use {:xlink-href (str "/icons/" (namespace type) "/" (name type) ".svg#icon")
          :style {"--svg_color" "var(--rouge)"}}]])

(defn seymour [params]
  [:div.seymour
   [:div.seymour-top
    (icon (:icon params))
    (h4 {} (:title params))
    [:p (:text params)]]
   [:div.seymour-bottom
    (arrow-link (:link params))]])

(def media-image-sizes
  {"vcard-small" 92
   "vcard-medium" 120})

(defn round-media [{:keys [image title lines className image-class]}]
  [:div.round-media {:className className}
   [:div.media-element
    (let [image-class (or image-class "vcard-small")]
      [:img.img {:className (str "image-style-" (or image-class "vcard-small"))
                 :width (media-image-sizes image-class)
                 :src (str image)}])]
   [:div.media-content
    (when title (h4 {:className "title"} title))
    [:p (interpose [:br] lines)]]])

(defn vert-round-media [params]
  (round-media (assoc params
                      :className "vert-round-media"
                      :image-class "vcard-medium")))

(defn article [{:keys [title sub-title content aside image alignment]}]
  [:div.article {:className (str "article-" (name (or alignment :balanced)))}
   [:div.article-content {}
    (when title
      (h3 {:element :h2} title))
    (when sub-title
      (h4 {:element :h3} sub-title))
    content]
   [:div.article-aside {}
    (when image
      [:img.img {:className (cond
                              (= :front alignment) "image-style-bottom-half-circle"
                              :default "image-style-rouge-triangle-medium")
                 :src image}])
    aside]])
