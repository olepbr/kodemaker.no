(ns kodemaker-no.formatting-test
  (:require [kodemaker-no.formatting :refer :all]
            [midje.sweet :refer :all]))

(fact "We don't want any short typographic widows, but long ones are fine."
      (no-widows "Hello there, how are you?") => "Hello there, how are&nbsp;you?"
      (no-widows "Hello there, are you Slartibartfast?") => "Hello there, are you Slartibartfast?")

(fact "We process markdown."
      (to-html :md "# hi\nhow are you") => "<h1>hi</h1><p>how are you</p>"
      (to-html :md "```clojure\n(+ 3 3)\n```") => "<pre><code class=\"clojure\">(+ 3 3)\n</code></pre>")

(fact (comma-separated ["1"]) => ["1"]
      (comma-separated ["1" "2"]) => ["1" " og " "2"]
      (comma-separated ["1" "2" "3"]) => ["1" ", " "2" " og " "3"]
      (comma-separated ["1" "2" "3" "4"]) => ["1" ", " "2" ", " "3" " og " "4"])
