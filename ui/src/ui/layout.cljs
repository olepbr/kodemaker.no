(ns ui.layout
  (:require [clojure.string :as str]))

(defn logo []
  [:img {:src "/img/logo.svg"}])

(def pønt-sizes
  {:greater-than "347px 693px"
   :dotgrid "auto"})

(defn add-pønt [style pønt]
  (let [unknown (remove pønt-sizes (map :kind pønt))]
    (when (seq unknown)
      (throw (ex-info (str "Unknown pønt kinds: " unknown) {}))))
  (-> style
      (assoc :background-repeat "no-repeat")
      (assoc :background-position
             (str/join ", " (map :position pønt)))

      (assoc :background-image
             (->> (for [{:keys [kind]} pønt]
                    (str "url(/img/pønt/" (name kind) ".png)"))
                  (str/join ", ")))

      (assoc :background-size
             (str/join ", " (map (comp pønt-sizes :kind) pønt)))))
