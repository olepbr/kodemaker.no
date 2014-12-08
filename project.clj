(defproject kodemaker-no "0.1.0-SNAPSHOT"
  :description "Statisk generering av kodemaker.no"
  :url "http://nye.kodemaker.no"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [optimus "0.14.2"]
                 [optimus-img-transform "0.2.0"]
                 [stasis "2.1.1" :exclusions [org.clojure/clojure]]
                 [ring "1.2.1"]
                 [hiccup "1.0.4"]
                 [enlive "1.1.5"]
                 [mapdown "0.2.1"]
                 [me.raynes/cegdown "0.1.1"]
                 [prismatic/schema "0.2.0"]
                 [clj-time "0.6.0"]
                 [org.clojure/core.memoize "0.5.6"]
                 [clygments "0.1.1"]
                 [prone "0.6.1"]]
  :jvm-opts ["-Xmx768M"
             "-Djava.awt.headless=true"]
  :ring {:handler kodemaker-no.web/app
         :port 3333}
  :aliases {"build-site" ["run" "-m" "kodemaker-no.web/export"]}
  :profiles {:dev {:dependencies [[print-foo "0.4.2"]]
                   :plugins [[lein-ring "0.8.10"]]
                   :source-paths ["dev" "config"]
                   :test-paths ^:replace []}
             :test {:dependencies [[midje "1.6.3"]
                                   [test-with-files "0.1.0"]]
                    :plugins [[lein-midje "3.1.3"]]
                    :source-paths ["config"]
                    :test-paths ["test"]
                    :resource-paths ["test/resources"]}})
