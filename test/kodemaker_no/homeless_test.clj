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
