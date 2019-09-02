(ns ui.typo-cards
  (:require [devcards.core :refer-macros [defcard]]
            [sablono.core :refer [html]]
            [ui.typo :as typo]))

(defcard h1
  (html (typo/h1 "Hello there world")))
