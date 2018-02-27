(ns kodemaker-no.pages.cv-pages-test
  (:require [clj-time.core :as time]
            [hiccup.core :refer [html]]
            [kodemaker-no.pages.cv-pages :refer :all]
            [midje.sweet :refer :all]))

(def christian
  {:url "/christian/"
   :presence {:cv "christian"}
   :full-name "Christian Johansen"
   :experience-since 2002
   :use-new-cv? true
   :str "christian"})

(defn page [& {:as extras}]
  (((cv-pages [(merge christian extras)]) "/cv/christian/")))

(fact (-> (page) :title) => "Christian Johansen CV")

(fact (-> (page) :layout) => :cv)

(fact
 "Rendering the page does not cause trouble"
 (let [html (-> (page) :body html)]
   html => #"/photos/people/christian/side-profile-cropped.jpg"
   html => #"Erfaring"))
