(ns ui.layout
  (:require [clojure.string :as str]
            [ui.elements :as e]))

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
        (assoc :overflow "hidden")
        (assoc :background-repeat "no-repeat")
        (assoc :background-position
               (str/join ", " (map :position pønt)))

        (assoc :background-image
               (->> (for [{:keys [kind ext]} pønt]
                      (str "url(/img/pønt/" (name kind) ext ")"))
                    (str/join ", ")))

        (assoc :background-size
               (str/join ", " (map :size pønt))))))

(defn footer []
  [:div.footer {:style (add-pønt {} [{:kind :less-than
                                      :position "right -340px top -480px"}])}
   [:div [:div {:style {:width "179px"}} (logo)]]
   [:div
    [:div "Kodemaker Systemutvikling AS"]
    [:div "Munkedamsveien 3b"]
    [:div "0161 OSLO"]]
   [:div
    [:div "Orgnr. 982099595"]
    [:div "+47 22 82 20 80"]
    [:div "kontakt@kodemaker.no"]]
   [:div
    (e/arrow-link {:text "Personvern"})]])
