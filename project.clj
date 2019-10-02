(defproject kodemaker-no "0.1.0-SNAPSHOT"
  :description "Statisk generering av kodemaker.no"
  :url "http://nye.kodemaker.no"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "0.2.6"]
                 [optimus "0.20.2"]
                 [optimus-img-transform "0.3.1" :exclusions [fivetonine/collage]]
                 [cjohansen/imagine "2019.10.02"]
                 [stasis "2.5.0"]
                 [ring "1.7.1"]
                 [hiccup "1.0.5"]
                 [mapdown "0.2.1"]
                 [me.raynes/cegdown "0.1.1"]
                 [prismatic/schema "1.1.12"]
                 [clj-time "0.15.2"]
                 [org.clojure/core.memoize "0.7.2"]
                 [clygments "2.0.0"]
                 [prone "2019-07-08"]
                 [cjohansen/dumdom "2019.09.05-1"]
                 [juxt/dirwatch "0.2.5"]
                 [com.datomic/datomic-free "0.9.5697"]
                 [datoms-differ "2019-08-26"]
                 [datomic-type-extensions "2019-09-04"]
                 [java-time-dte "2018-04-18"]
                 [java-time-literals "2018-04-06"]
                 [html5-walker "2019-10-01"]]
  :jvm-opts ["-Xmx768M"
             "-Djava.awt.headless=true"]
  :ring {:handler kodemaker-no.web/app
         :init kodemaker-no.web/init-app!
         :port 3333}
  :aliases {"build-site" ["run" "-m" "kodemaker-no.web/export"]}
  :profiles {:dev {:dependencies [[hiccup-find  "1.0.0"]
                                  [integrant "0.7.0"]
                                  [integrant/repl "0.3.1"]]
                   :repl-options {:init-ns repl}
                   :plugins [[lein-ring "0.12.5"]
                             [lein-ancient "0.6.15"]]
                   :source-paths ["dev" "config" "ui/src"]
                   :resource-paths ["ui/resources"]
                   :test-paths ^:replace []}
             :test {:dependencies [[midje "1.9.9"]
                                   [test-with-files "0.1.1"]
                                   [enlive "1.1.6"]
                                   [flare "0.2.9"]]
                    :injections [(require 'flare.midje)
                                 (flare.midje/install!)]
                    :plugins [[lein-midje "3.2.1"]]
                    :source-paths ["config" "ui/src"]
                    :test-paths ["test"]
                    :resource-paths ["test/resources" "ui/resources"]}})
