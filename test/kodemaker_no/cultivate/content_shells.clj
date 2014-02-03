(ns kodemaker-no.cultivate.content-shells)

(defn content [m]
  (merge {:people {}
          :tech {}
          :projects {}
          :articles {}
          :tech-names {}
          :blog-posts {}}
         m))

(defn tech [m]
  (merge {:name "!"
          :description "!"}
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
          :illustration "!"
          :awesomeness 0}
         m))

(defn recommendation [m]
  (merge {:link {:url "!" :text "!"}
          :title "!"
          :blurb "!"
          :tech []}
         m))
