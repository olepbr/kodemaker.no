(ns kodemaker-no.formatting
  (:require [clojure.string :as str]
            [me.raynes.cegdown :as md]))

(defn no-widows [s]
  "Avoid typographic widows by adding a non-breaking space between the
   last two words."
  (str/replace s #" ([^ ]{1,6})$" "&nbsp;$1"))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn to-html [_ s]
  (md/to-html s pegdown-options))
