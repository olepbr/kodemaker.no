(ns kodemaker-no.cultivate.projects
  (:require [kodemaker-no.cultivate.util :as util]
            [kodemaker-no.homeless :refer [update-vals]]))

(defn- add-url [project]
  (assoc project :url (util/url project)))

(defn- cultivate-project [content project]
  (->> project
       add-url))

(defn cultivate-projects [content]
  (update-vals (:projects content) (partial cultivate-project content)))
