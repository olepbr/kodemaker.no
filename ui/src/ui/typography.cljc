(ns ui.typography
  (:require [clojure.string :as str]))

(defn el [el params text]
  [(get params :element el) {:className (name el)} text])

(def h1 (partial el :h1))
(def h2 (partial el :h2))
(def h3 (partial el :h3))
(def h4 (partial el :h4))
(def h5 (partial el :h5))

;; TODO: This probably don't belong in the "typography" ns
(defn blockquote [params content]
  [:blockquote {:className "blockquote"}
   (let [sentences (str/split content #"\n\n")
         sentences (concat [(str "«" (first sentences))] (rest sentences))]
     (map
      (fn [sentence] [:p {:className "p"} sentence])
      (concat (take (dec (count sentences)) sentences) [(str (last sentences) "»")])))])
