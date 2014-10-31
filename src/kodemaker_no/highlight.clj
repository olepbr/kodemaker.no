(ns kodemaker-no.highlight
  (:require [clygments.core :as pygments]
            [net.cgrand.enlive-html :refer [sniptest html-resource select]]))

(defn- extract-code
  "Pulls out just the highlighted code, removing needless fluff and
  stuff from the Pygments treatment."
  [highlighted]
  (-> highlighted
      java.io.StringReader.
      html-resource
      (select [:pre])
      first
      :content))

(defn highlight
  "Extracts code from the node contents, and highlights it according
  to the given language (extracted from the node's class name)."
  [node]
  (let [code (->> node :content (apply str))
        lang (->> node :attrs :class keyword)]
    {:tag :code
     ;; Certain code samples (like a 14Kb HTML string embedded in JSON) trips up
     ;; Pygments (too much recursion). When that happens, skip highlighting
     :content (try
                (-> code
                    (pygments/highlight (or lang "text") :html)
                    (extract-code))
                (catch Exception e code))}))

(def skip-pygments?
  (= (System/getProperty "kodemaker.skip.pygments") "true"))

(defn maybe-highlight-node [node]
  "Parsing and highlighting with Pygments is quite resource intensive,
   on the order of adding 20 seconds to the full test run. This way we
   can disable the pygments by setting JVM_OPTS=\"-Dspid.skip.pygments=true\""
  (if-not skip-pygments?
    (highlight node)
    node))

(defn maybe-add-hilite-class [node]
  (assoc-in node [:attrs :class] "codehilite"))
