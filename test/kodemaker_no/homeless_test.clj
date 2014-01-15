(ns kodemaker-no.homeless-test
  (:require [kodemaker-no.homeless :refer :all]
            [test-with-files.core :refer [with-files tmp-dir]]
            [midje.sweet :refer :all]))

(with-files [["/texts/banana.txt" "Banan"]
             ["/texts/apple.txt" "Eple"]
             ["/texts/fruit.txt" "Frukt"]
             ["/texts/irrelevant.md" "Ikke med"]]

  (fact (set (slurp-files (str tmp-dir "/texts") #"\.txt$"))
        => #{"Banan" "Eple" "Frukt"}))

(fact (remove-nil-vals {:a 1, :b nil, :c 3}) => {:a 1, :c 3})

(fact
 (nil-if-blank "a") => "a"
 (nil-if-blank "") => nil
 (nil-if-blank nil) => nil)
