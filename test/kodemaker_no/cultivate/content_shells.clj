(ns kodemaker-no.cultivate.content-shells)

(defn content [m]
  (merge {:people {}
          :tech {}
          :articles {}
          :references {}
          :raw-pages {}
          :tech-names {}
          :blog-posts {}
          :video-overrides {}
          :tech-types {}
          :employers {}}
         m))

(defn tech [m]
  (merge {:name "!"
          :description "!"
          :type :proglang}
         m))

(defn person [m]
  (merge {:name ["!"]
          :title "!"
          :start-date "!"
          :description "!"
          :phone-number "!"
          :email-address "!"
          :presence {}}
         m))

(defn project [m]
  (merge {:name "!"
          :logo "!"
          :description "!"
          :illustration "/path"
          :awesomeness 0}
         m))

(defn recommendation [m]
  (merge {:link {:url "http://example.com" :text "!"}
          :title "!"
          :blurb "!"
          :tech []}
         m))
