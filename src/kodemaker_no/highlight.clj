(ns kodemaker-no.highlight
  (:require [clygments.core :as pygments]
            [kodemaker-no.html5-walker :as html5-walker]
            [clojure.string :as str]))

(defn- extract-code
  "Pulls out just the highlighted code, removing needless fluff and
  stuff from the Pygments treatment."
  [highlighted]
  (.getInnerHTML (first (html5-walker/find highlighted [:pre]))))

(defn highlight
  "Extracts code from the node contents, and highlights it according
  to the given language (extracted from the node's class name)."
  [node]
  (let [lang (some-> node (.getAttribute "class") not-empty keyword)
        code (cond-> (.getInnerHTML node)
               (= :html lang) (-> (str/replace "&lt;" "<")
                                  (str/replace "&gt;" ">")))]
    ;; Certain code samples (like a 14Kb HTML string embedded in JSON) trips up
    ;; Pygments (too much recursion). When that happens, skip highlighting
    (try
      (.setInnerHTML node
                     (-> code
                         (pygments/highlight (or lang "text") :html)
                         (extract-code)))
      (catch Exception _))))

(def skip-pygments?
  (= (System/getProperty "kodemaker.skip.pygments") "true"))

(defn maybe-highlight-node [node]
  "Parsing and highlighting with Pygments is quite resource intensive,
   on the order of adding 20 seconds to the full test run. This way we
   can disable the pygments by setting JVM_OPTS=\"-Dspid.skip.pygments=true\""
  (when-not skip-pygments?
    (highlight node)))

(defn add-hilite-class [node]
  (.setAttribute node "class" "codehilite"))
