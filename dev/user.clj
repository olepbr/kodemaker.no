(ns user
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint pp print-table]]
            [clojure.reflect]
            [clojure.repl :refer :all]
            [clojure.set :as set]
            [clojure.string :as str]
            [hiccup.core :refer [html]]
            [kodemaker-no.content :refer [load-content]]
            [print.foo :refer :all]))

(defmacro dump-locals []
  `(clojure.pprint/pprint
    ~(into {} (map (fn [l] [`'~l l]) (reverse (keys &env))))))

(defn list-functions [o]
  (print-table
   (sort-by :name
            (filter :exception-types (:members (clojure.reflect/reflect o))))))
