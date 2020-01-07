(ns kodemaker-no.markup
  (:require [clojure.string :as str]
            [hiccup.core :as hiccup]))

(defn render-link [link]
  [:a.nowrap {:href (:url link)} (:text link)])

(defn link-if-url [{:keys [url name]}]
  (if url
    [:a {:href url} name]
    name))

(defn prepend-to-paragraph [html node]
  (str/replace html #"^<p>" (str "<p>" (hiccup/html node))))

(defn append-to-paragraph [html node]
  (str/replace html #"</p>$" (str (hiccup/html node) "</p>")))

(defn strip-paragraph [s]
  (let [s (str/trim s)]
    (subs s 3 (- (count s) 4))))
