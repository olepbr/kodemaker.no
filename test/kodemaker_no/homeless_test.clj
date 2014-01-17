(ns kodemaker-no.homeless-test
  (:require [kodemaker-no.homeless :refer :all]
            [test-with-files.core :refer [with-files tmp-dir]]
            [midje.sweet :refer :all]))

(fact (remove-vals {:a 1, :b nil, :c 3} nil?) => {:a 1, :c 3})

(fact
 (nil-if-blank "a") => "a"
 (nil-if-blank "") => nil
 (nil-if-blank nil) => nil)

(fact (rename-keys {"a" 1, "b" 2} #(.toUpperCase %)) => {"A" 1, "B" 2})

(fact (update-vals {"a" 1, "b" 2} inc) => {"a" 2, "b" 3})

(fact (with-html-transform "<html><body><h1></h1></body></html>"
        [:h1] (fn [node] (assoc node :content "Hello!")))

      => "<html><body><h1>Hello!</h1></body></html>")

(fact (hiccup-symbol-matches? :p :p.class) => true
      (hiccup-symbol-matches? :p.class :p) => false
      (hiccup-symbol-matches? :.class :p.class.more) => true
      (hiccup-symbol-matches? :p.more.class :p.class.more) => true)

(fact (hiccup-find :p.image
                   [:html
                    [:body
                     [:p.img "No"]
                     [:p.image "Yes 1"]
                     [:p.images "No"]
                     [:div.image
                      [:p.image "Yes 2"]]]])
      => (list [:p.image "Yes 1"]
               [:p.image "Yes 2"]))
