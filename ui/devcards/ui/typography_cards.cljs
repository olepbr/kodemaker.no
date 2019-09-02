(ns ui.typography-cards
  (:require [devcards.core :refer-macros [defcard]]
            [sablono.core :refer [html]]
            [ui.typography :as typography]))

(defcard h1
  (html (typography/h1 "Hello there world")))
