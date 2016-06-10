(defproject kodemaker-no "0.1.0-SNAPSHOT"
  :description "Statisk generering av kodemaker.no"
  :url "http://nye.kodemaker.no"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [optimus "0.18.5"]
                 [optimus-img-transform "0.2.0"]
                 [stasis "2.3.0" :exclusions [org.clojure/clojure]]
                 [ring "1.4.0"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.6"]
                 [mapdown "0.2.1"]
                 [me.raynes/cegdown "0.1.1"]
                 [prismatic/schema "1.1.1"]
                 [clj-time "0.11.0"]
                 [org.clojure/core.memoize "0.5.9"]
                 [clygments "0.1.1"]
                 [prone "1.1.1"]]
  :jvm-opts ["-Xmx768M"
             "-Djava.awt.headless=true"]
  :ring {:handler kodemaker-no.web/app
         :port 3333}
  :aliases {"build-site" ["run" "-m" "kodemaker-no.web/export"]}
  :profiles {:dev {:dependencies [[print-foo "1.0.2"]]
                   :plugins [[lein-ring "0.9.7"]]
                   :source-paths ["dev" "config"]
                   :test-paths ^:replace []}
             :test {:dependencies [[midje "1.8.3"]
                                   [test-with-files "0.1.1"]
                                   [flare "0.2.9"]]
                    :injections [(require 'flare.midje)
                                 (flare.midje/install!)]
                    :plugins [[lein-midje "3.2"]]
                    :source-paths ["config"]
                    :test-paths ["test"]
                    :resource-paths ["test/resources"]}})
