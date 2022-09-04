(defproject kodemaker-no "0.1.0-SNAPSHOT"
  :description "Statisk generering av kodemaker.no"
  :url "http://nye.kodemaker.no"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/data.xml "0.0.7"]
                 [com.nextjournal/beholder "1.0.0"]
                 [optimus "1.0.0-rc3"]
                 [optimus-img-transform "0.3.1" :exclusions [fivetonine/collage]]
                 [cjohansen/imagine "2020.08.25"]
                 [stasis "2.5.0"]
                 [ring "1.7.1"]
                 [hiccup "1.0.5"]
                 [mapdown "0.2.1"]
                 [com.vladsch.flexmark/flexmark-all "0.50.42"]
                 [prismatic/schema "1.1.12"]
                 [clj-time "0.15.2"]
                 [org.clojure/core.memoize "0.7.2"]
                 [clygments "2.0.0"]
                 [prone "2019-07-08"]
                 [cjohansen/dumdom "2019.09.05-1"]
                 [com.datomic/datomic-free "0.9.5697"]
                 [datoms-differ "2019-08-26"]
                 [datomic-type-extensions "2019-09-04"]
                 [java-time-dte "2018-04-18"]
                 [java-time-literals "2018-04-06"]
                 [html5-walker "2020-01-08"]
                 [clj-http "3.10.1"]
                 [cheshire "5.10.0"]]
  :jvm-opts ["-Xmx768M"
             "-Djava.awt.headless=true"]
  :ring {:handler repl/ring-app
         :init repl/init-app-for-ring!
         :port 3334}
  :aliases {"build-new-site" ["run" "-m" "kodemaker-no.web/export-new"]
            "cvpartner-export" ["run" "-m" "kodemaker-no.export.cvpartner/cvpartner-export"]}
  :profiles {:dev {:dependencies [[hiccup-find  "1.0.0"]
                                  [integrant "0.7.0"]
                                  [integrant/repl "0.3.1"]
                                  [midje "1.9.9"]
                                  [test-with-files "0.1.1"]
                                  [enlive "1.1.6"]
                                  [flare "0.2.9"]]
                   :repl-options {:init-ns repl}
                   :injections [(require 'flare.midje)
                                (flare.midje/install!)]
                   :plugins [[lein-ring "0.12.5"]
                             [lein-ancient "0.6.15"]
                             [lein-midje "3.2.1"]]
                   :source-paths ["dev" "config" "ui/src"]
                   :resource-paths ["ui/resources" "test/resources"]
                   :test-paths ["test"]}})
