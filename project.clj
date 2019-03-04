(defproject kodemaker-no "0.1.0-SNAPSHOT"
  :description "Statisk generering av kodemaker.no"
  :url "http://nye.kodemaker.no"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "0.2.6"]
                 [optimus "0.20.2"]
                 [optimus-img-transform "0.2.0"]
                 [stasis "2.3.0" :exclusions [org.clojure/clojure]]
                 [ring "1.7.1"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.6"]
                 [mapdown "0.2.1"]
                 [me.raynes/cegdown "0.1.1"]
                 [prismatic/schema "1.1.1"]
                 [clj-time "0.11.0"]
                 [org.clojure/core.memoize "0.5.9"]
                 [clygments "0.1.1"]
                 [prone "1.6.1"]]
  :jvm-opts ["-Xmx768M"
             "-Djava.awt.headless=true"]
  :ring {:handler kodemaker-no.web/app
         :port 3333}
  :aliases {"build-site" ["run" "-m" "kodemaker-no.web/export"]}
  :profiles {:dev {:dependencies [[clj-tagsoup/clj-tagsoup "0.3.0" :exclusions [org.clojure/clojure]]
                                  [hiccup-find  "1.0.0"]]
                   :plugins [[lein-ring "0.12.5"]]
                   :source-paths ["dev" "config"]
                   :test-paths ^:replace []}
             :test {:dependencies [[midje "1.9.6"]
                                   [test-with-files "0.1.1"]
                                   [flare "0.2.9"]]
                    :injections [(require 'flare.midje)
                                 (flare.midje/install!)]
                    :plugins [[lein-midje "3.2.1"]]
                    :source-paths ["config"]
                    :test-paths ["test"]
                    :resource-paths ["test/resources"]}})
