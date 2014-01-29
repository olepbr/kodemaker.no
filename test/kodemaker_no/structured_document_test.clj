(ns kodemaker-no.structured-document-test
  (:require [kodemaker-no.structured-document :refer :all]
            [midje.sweet :refer :all]))

(fact
 (read-doc ":title abc") => {:title "abc"}
 (read-doc "
:title abc
:desc def") => {:title "abc", :desc "def"})

(fact
 (read-doc "
:::section1
abc
:::section2
def
ghi") => {:section1 "abc", :section2 "def\nghi"})

(fact
 (read-doc "
:title abc

:::section1

def

:illustration ghi

:::section2") => {:title "abc", :section1 "def", :illustration "ghi"})

;; empty sections are excluded
