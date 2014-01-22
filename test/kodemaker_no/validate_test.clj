(ns kodemaker-no.validate-test
  (:require [kodemaker-no.validate :refer :all]
            [kodemaker-no.content :refer [load-content]]
            [midje.sweet :refer :all]))

(fact
 "All data is validated."

 (validate-content (load-content))

 "Validation OK" => truthy)
