(defproject kodemaker-no "0.1.0-SNAPSHOT"
  :description "Statisk generering av kodemaker.no"
  :url "http://nye.kodemaker.no"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [optimus "0.14.0"]
                 [stasis "0.2.0"]
                 [ring "1.2.1"]
                 [hiccup "1.0.4"]]
  :ring {:handler kodemaker-no.web/app
         :port 3333}
  :aliases {"build-site" ["run" "-m" "kodemaker-no.web/export"]}
  :profiles {:dev {:dependencies [[midje "1.6.0"]
                                  [print-foo "0.4.2"]]
                   :plugins [[lein-midje "3.1.3"]
                             [lein-ring "0.8.7"]]
                   :resource-paths ["config"]
                   :source-paths ["dev"]}})
