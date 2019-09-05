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
  [{:href "/folk/" :text "Folk"}
   {:href "/blogg/" :text "Blogg"}
   {:href "/kurs/" :text "Lær"}
   {:href "/jobb-hos-oss/" :text "Jobb"}
   {:href "/kontakt/" :text "Kontakt"}])

(defn menu [& [{:keys [position]}]]
  [:div.menu {:style {:position (or position "fixed")}}
   [:div.menu-close-button.clickable.h5 "Lukk"]
   [:ul.nav-list.text-l {:role "navigation" :aria-label "Hovedmeny"}
    (for [{:keys [href text]} menu-items]
      [:li
       (e/arrow-link {:text text
                      :size :large
                      :href href})])]])

(defn header []
  [:div.header {}
   (logo {:width 176})
   [:div.menu-toggler.clickable.h5 "Meny"]
   [:ul.inline-menu.nav-list.h5 {:role "navigation" :aria-label "Hovedmeny"}
    (for [{:keys [href text]} menu-items]
      [:li [:a {:href href} text]])]])

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
