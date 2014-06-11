(ns kodemaker-no.pages-test
  (:require [kodemaker-no.pages :refer :all]
            [midje.sweet :refer :all]))

(fact "The pages generated are based on given contents."
      (-> {:people {:magnars {:url "/magnars/"}
                    :finnjoh {:url "/finnjoh/"}}
           :tech {:react {:url "/react/"}}
           :projects {:finn-oppdrag {:url "/finn-oppdrag/"}}
           :articles {"/kompetanse.md" ""
                      "/systemer.md" ""}}
          create-pages keys set)

      => #{"/"
           "/magnars/"
           "/finnjoh/"
           "/react/"
           "/kompetanse/"
           "/systemer/"
           "/skjema/"
           "/blogg/"
           "/kurs/"})

(fact "Colliding urls are not tolerated."

      (create-pages {:people {}, :articles {"/skjema/" ""}})
      => (throws Exception "URL conflicts between :article-pages and :general-pages: #{\"/skjema/index.html\"}")

      (create-pages {:people {:magnars {:url "/magnars/"}
                              :finnjoh {:url "/finnjoh/"}}
                     :tech {}
                     :articles {"/magnars.md" ""
                                "/finnjoh.md" ""}})
      => (throws Exception "URL conflicts between :person-pages and :article-pages: #{\"/finnjoh/index.html\" \"/magnars/index.html\"}"))
