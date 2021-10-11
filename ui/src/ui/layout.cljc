(ns ui.layout
  (:require [clojure.string :as str]
            [ui.elements :as e]))

(defn logo [{:keys [width]}]
  [:img {:alt "Kodemaker logo" :src "/img/logo.svg" :width width}])

(def pønt-infos
  {:greater-than {:ext ".svg" :size "650px 1300px"}
   :greater-than-small {:ext ".svg" :size "450px 900px"}
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
                      (str "url(/img/p-nt/" (name kind) ext ")"))
                    (str/join ", ")))

        (assoc :background-size
               (str/join ", " (map :size pønt))))))

(defn stylish [style {:keys [background pønt]}]
  (cond-> (assoc style :background-color (str "var(--" (name (or background :blanc)) ")"))
    pønt (add-pønt pønt)))

(def menu-items
  [{:href "/folk/" :text "Folk"}
   {:href "/blogg/" :text "Blogg"}
   #_{:href "/kurs/" :text "Lær"}
   {:href "/jobbe-hos-oss/" :text "Jobb"}
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
  [:div.header
   [:a {:title "Hjem" :href "/"} (logo {:width 176})]
   [:div.menu-toggler.clickable.h5 {:onclick "var mm = document.getElementById('mobile-menu'); mm.style.display='block'; mm.ontouchmove = function (e) { e.preventDefault(); };"}
    "Meny"]
   [:div#mobile-menu {:style {:display "none"}}
    [:div.close-menu.clickable {:onclick "document.getElementById('mobile-menu').style.display='none'"}
     "× Lukk"]
    [:ul
     (for [item menu-items]
       [:li.mts (e/arrow-link item)])]]
   [:ul.inline-menu.nav-list.h5 {:role "navigation" :aria-label "Hovedmeny"}
    (for [{:keys [href text]} menu-items]
      [:li [:a {:href href} text]])]])

(defn header-section [section]
  [:div.section.header-section
   {:style (stylish {} section)}
   [:div.content
    (header)]])

(defn footer [section]
  [:div.section {:style (stylish {} (assoc section :pønt [{:kind :less-than
                                                           :position "right -300px top -480px"}]))}
   [:div.content
    [:div.footer
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
      [:div (e/arrow-link {:text "Personvern" :href "/personvern/"})]
      [:div (e/arrow-link {:text "Miljøfyrtårn" :href "/miljofyrtarn/"})]]]]])
