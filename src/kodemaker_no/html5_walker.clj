(ns kodemaker-no.html5-walker
  (:refer-clojure :exclude [replace find])
  (:import [ch.digitalfondue.jfiveparse Document Element Node Parser Selector NodeMatcher]))

(defn create-matcher [path]
  (.toMatcher
   (reduce (fn [selector element-kw]
             (-> selector
                 .withChild
                 (.element (name element-kw))))
           (-> (Selector/select)
               (.element (name (first path))))
           (next path))))

(defn replace [html path->f]
  (let [parser (Parser.)
        doc (.parse parser html)]
    (doseq [[path f] path->f]
      (doseq [node (.getAllNodesMatching doc (create-matcher path))]
        (f node)))
    (.getOuterHTML doc)))

(defn find [html path]
  (.getAllNodesMatching (.parse (Parser.) html) (create-matcher path)))

(comment

  (replace
   "<body>Hello world!<a href=\"foo\">Heisann <div>på deg</div></a></body>"
   {[:a] (fn [node] (.setAttribute node "href" "www.vg.no"))
    [:div] (fn [node] (.setInnerHTML node "jallaballa"))})

  (def doc (.parse (Parser.) "<h2><a href=\"#\">hei</a></h2>"))

  (def el (first
           (.getAllNodesMatching doc (create-matcher [:h2]))))

  (.getTextContent el)

  (= "a" (.getNodeName (first (.getChildNodes el))))

  (.getAttribute el "class")
  (.getInnerHTML el)

  (some-> el (.getAttribute "class") keyword)

  (.getOuterHTML el)

  (.getOuterHTML doc)


  )
