(ns kodemaker-no.pages-test
  (:require [kodemaker-no.pages :refer :all]
            [midje.sweet :refer :all]))

(fact "The pages generated are based on given contents."
      (-> {:people {:magnars {:url "/magnars/"}
                    :finnjoh {:url "/finnjoh/"}}
           :tech {:react {:url "/react/"}}
           :articles {"/kompetanse.adoc" ""
                      "/systemer.adoc" ""}}
          create-pages keys set)

      => #{"/mennesker/"
           "/magnars/"
           "/finnjoh/"
           "/react/"
           "/kompetanse/"
           "/systemer/"})

(fact "Colliding urls are not tolerated."

      (create-pages {:people {}, :articles {"/mennesker/" ""}})
      => (throws Exception "URL conflicts between :article-pages and :general-pages: #{\"/mennesker/\"}")

      (create-pages {:people {:magnars {:url "/magnars/"}
                              :finnjoh {:url "/finnjoh/"}}
                     :tech {}
                     :articles {"/magnars.adoc" ""
                                "/finnjoh.adoc" ""}})
      => (throws Exception "URL conflicts between :person-pages and :article-pages: #{\"/magnars/\" \"/finnjoh/\"}"))
