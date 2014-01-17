(ns kodemaker-no.pages-test
  (:require [kodemaker-no.pages :refer :all]
            [midje.sweet :refer :all]))

(fact "The pages generated are based on given contents."
      (-> {:people [{:url "/magnars.html"}
                    {:url "/finnjoh.html"}]
           :articles {"/kompetanse.adoc" ""
                      "/systemer.adoc" ""}}
          get-pages keys set)

      => #{"/mennesker.html"
           "/magnars.html"
           "/finnjoh.html"
           "/kompetanse.html"
           "/systemer.html"})

(fact "Colliding urls are not tolerated."

      (get-pages {:people [], :articles {"/mennesker.html" ""}})
      => (throws Exception "URL conflicts between :article-pages and :general-pages: #{\"/mennesker.html\"}")

      (get-pages {:people [{:url "/magnars.html"}
                           {:url "/finnjoh.html"}]
                  :articles {"/magnars.adoc" ""
                             "/finnjoh.adoc" ""}})
      => (throws Exception "URL conflicts between :person-pages and :article-pages: #{\"/magnars.html\" \"/finnjoh.html\"}"))
