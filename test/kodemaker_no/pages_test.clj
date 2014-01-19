(ns kodemaker-no.pages-test
  (:require [kodemaker-no.pages :refer :all]
            [midje.sweet :refer :all]))

(fact "The pages generated are based on given contents."
      (-> {:people [{:url "/magnars/"}
                    {:url "/finnjoh/"}]
           :articles {"/kompetanse.adoc" ""
                      "/systemer.adoc" ""}}
          get-pages keys set)

      => #{"/mennesker/"
           "/magnars/"
           "/finnjoh/"
           "/kompetanse/"
           "/systemer/"})

(fact "Colliding urls are not tolerated."

      (get-pages {:people [], :articles {"/mennesker/" ""}})
      => (throws Exception "URL conflicts between :article-pages and :general-pages: #{\"/mennesker/\"}")

      (get-pages {:people [{:url "/magnars/"}
                           {:url "/finnjoh/"}]
                  :articles {"/magnars.adoc" ""
                             "/finnjoh.adoc" ""}})
      => (throws Exception "URL conflicts between :person-pages and :article-pages: #{\"/magnars/\" \"/finnjoh/\"}"))
