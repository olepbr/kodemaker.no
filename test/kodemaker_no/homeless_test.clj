(ns kodemaker-no.homeless-test
  (:require [kodemaker-no.homeless :refer :all]
            [test-with-files.core :refer [with-files tmp-dir]]
            [midje.sweet :refer :all]))

(fact (remove-vals nil? {:a 1, :b nil, :c 3}) => {:a 1, :c 3})

(fact
 (nil-if-blank "a") => "a"
 (nil-if-blank "") => nil
 (nil-if-blank nil) => nil)
