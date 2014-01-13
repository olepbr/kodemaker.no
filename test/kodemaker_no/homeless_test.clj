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
