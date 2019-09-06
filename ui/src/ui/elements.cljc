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

(defn blockquote [{:keys [quote]}]
  [:blockquote.blockquote {}
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

(defn round-media [{:keys [image title lines]}]
  [:div.round-media
   [:div.media-element
    [:img.img.image-style-small-round {:width 92 :src (str image)}]]
   [:div.media-content
    (h4 {:className "title"} title)
    [:p (interpose [:br] lines)]]])
