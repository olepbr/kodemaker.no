(ns kodemaker-no.render-page
  (:require [clojure.string :as str]
            [kodemaker-no.formatting :refer [to-html]]
            [kodemaker-no.layout :refer [with-layout]]
            [kodemaker-no.markup :as markup]
            [kodemaker-no.render-old-page :as old]))

(defn- render-illustrated-column [{:keys [id title illustration illustration-url body]}]
  [:div.bd.iw {:id id}
   (when title
     [:h2.offset [:span.offset-content title]])
   [:div.line
    [:div.unit.text-adornment
     [:p (when illustration
           (if illustration-url
             [:a {:href illustration-url}
              [:img {:src illustration}]]
             [:img {:src illustration}]))]]
    [:div.lastUnit
     (to-html body)]]])

(defn- render-reference [{:keys [id img url logo name phone title body class]}]
  (let [quote (str "«" (str/trim body) "»")]
    [:div.bd.iw {:class class :id id}
     [:div.ref.mod
      [:div.ref-w
       [:div.ref-img [:img {:src img}]]
       [:div.ref-txt [:p (to-html quote)]]
       [:div.ref-card
        [:div.ref-logo [:img {:src logo}]]
        [:div.ref-info.tight
         [:h5 name]
         [:p title [:br] phone]]]]
      [:div.ref-txt-2
       [:div.ref-txt-2-wrap
        (to-html quote)]]]]))

(defn- render-centered-column [{:keys [id title body]}]
  [:div.bd.iw {:id id}
   [:div.centered-column
    (when title [:h3.mbl.xlarge.hns title])
    (to-html body)]])

(defn render-contact-form [{:keys [id body button placeholder]}]
  [:div.inline-form.mbxxxl {:id id}
   [:div.bd.iw
    [:form {:action "/send-mail"
            :method "POST"}
     (to-html body)
     [:div.mod
      [:div.line
       [:div.unit.r-1of2
        [:input.input {:type "text", :name "kontakt", :placeholder placeholder}]]
       [:div.lastUnit
        [:input.btn.mtn.mll {:type "submit" :value button}]]]]]]])

(defn- render-grid-unit [url ^String img & [size]]
  [:div.gridUnit {:class (if (= "2x" size) "r-3-2" "r-6-4")}
   [:a.gridContent.linkBlock.tight.fpp {:href url}
    [:span.block.mbs
     {:class (when (.startsWith img "/photos/people/") "framed")}
     [:img {:src img}]]]])

(defn- render-grid [{:keys [id content]}]
  [:div.bd.iw {:id id}
   [:div.grid
    (->> content
         (str/split-lines)
         (map #(apply render-grid-unit (str/split % #" +"))))]])

(defn- render-ginormous-aside [{:keys [id aside body]}]
  [:div.bd.iw {:id id}
   [:hr]
   [:div.line
    [:div.unit.s-1of3.hide-lt-460
     [:p {:class (str "hn ginormous ginormous-" (count aside))} aside]]
    [:div.lastUnit
     (to-html body)]]
   [:hr]])

(defn- render-mega-quote [{:keys [id title]}]
  [:div.bd.iw {:id id}
   [:div.xxlarge.hns.mod.mega-quote "&ndash; "
    (markup/strip-paragraph (to-html title))]])

(defn render-section [section]
  [:div.body
   (case (:type section)
     "illustrated-column" (render-illustrated-column section)
     "centered-column" (render-centered-column section)
     "mega-heading" [:h1.hn.pth {:id (:id section)} (:title section)]
     "mega-quote" (render-mega-quote section)
     "reference" (render-reference section)
     "contact-form" (render-contact-form section)
     "grid" (render-grid section)
     "ginormous-aside" (render-ginormous-aside section)
     nil (:body section)

     (throw (ex-info (str "Unknown section type " (:type section)) section)))])

(defn render-page [page request]
  (cond
    (:sections page) (with-layout request page
                       (map render-section (:sections page)))
    (:layout page) (with-layout request page (:body page))
    :default (old/render-page page request)))
