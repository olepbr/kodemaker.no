(ns kodemaker-no.formatting
  (:require [clojure.string :as str]
            [me.raynes.cegdown :as md]))

(defn no-widows [s]
  "Avoid typographic widows by adding a non-breaking space between the
   last two words."
  (str/replace s #" ([^ ]{1,6})$" "&nbsp;$1"))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough :quotes :smarts])

(defn to-html [s]
  (md/to-html s pegdown-options))

(defn comma-separated [coll]
  (drop 1 (interleave (into (list " og " "")
                            (repeat (dec (count coll)) ", "))
                      coll)))

(defn- consecutive? [[before after]]
  (= (inc before) after))

(defn- consecutive-years [years]
  (let [pairs (partition-all 2 1 years)
        consecutive (cons (first years)
                          (->> pairs
                               (take-while consecutive?)
                               (map second)))
        remains (->> pairs (drop-while consecutive?) flatten distinct rest)]
    (if (seq remains)
      (concat [consecutive] (consecutive-years remains))
      [consecutive])))

(defn- year-range-str [[first-year & rest]]
  (if rest
    (str first-year "-" (last rest))
    first-year))

(defn year-range [years]
  (str/join ", " (map year-range-str (consecutive-years years))))
