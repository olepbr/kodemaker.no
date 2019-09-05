(ns ui.layout
  (:require [clojure.string :as str]
            [ui.elements :as e]))

(defn logo [{:keys [width]}]
  [:img {:src "/img/logo.svg" :width width}])

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

(def menu-items
  [{:url "/folk/" :text "Folk"}
   {:url "/blogg/" :text "Blogg"}
   {:url "/kurs/" :text "Lær"}
   {:url "/jobb-hos-oss/" :text "Jobb"}
   {:url "/kontakt/" :text "Kontakt"}])

(defn header []
  [:div.header {}
   (logo {:width 176})
   [:div.menu-toggler.h5 "Meny"]
   [:ul.inline-menu.h5 {:role "navigation" :aria-label "Hovedmeny"}
    (for [{:keys [url text]} menu-items]
      [:li [:a {:href url} text]])]])

(defn footer []
  [:div.footer {:style (add-pønt {} [{:kind :less-than
                                      :position "right -300px top -480px"}])}
   [:div.f-logo (logo {:width 176})]
   [:div.f-infos
    [:div.f-address
     [:div "Kodemaker Systemutvikling AS"]
     [:div "Munkedamsveien 3b"]
     [:div "0161 OSLO"]]
    [:div.f-contact
     [:div "Orgnr. 982099595"]
     [:div [:a {:href "tel:+4722822080"} "+47 22 82 20 80"]]
     [:div [:a {:href "mailto:kontakt@kodemaker.no"} "kontakt@kodemaker.no"]]]]
   [:div.f-links
    (e/arrow-link {:text "Personvern" :href "/personvern/"})]])
