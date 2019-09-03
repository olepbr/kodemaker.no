(ns ui.layout
  (:require [clojure.string :as str]))

(defn logo []
  [:img {:src "/img/logo.svg"}])

(def pønt-infos
  {:greater-than {:ext ".svg" :size "650px 1300px"}
   :less-than {:ext ".svg" :size "650px 1300px"}
   :descending-line {:ext ".svg" :size "650px"}
   :ascending-line {:ext ".svg" :size "650px"}
   :dotgrid {:ext ".png" :size "auto"}})

(defn add-pønt [style pønt]
  (let [unknown (remove pønt-infos (map :kind pønt))]
    (when (seq unknown)
      (throw (ex-info (str "Unknown pønt kinds: " unknown) {}))))
  (let [pønt (for [p pønt]
               (merge (get pønt-infos (:kind p)) p))]
    (-> style
        (assoc :background-repeat "no-repeat")
        (assoc :background-position
               (str/join ", " (map :position pønt)))

        (assoc :background-image
               (->> (for [{:keys [kind ext]} pønt]
                      (str "url(/img/pønt/" (name kind) ext ")"))
                    (str/join ", ")))

        (assoc :background-size
               (str/join ", " (map :size pønt))))))
