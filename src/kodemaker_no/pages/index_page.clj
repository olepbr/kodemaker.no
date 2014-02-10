(ns kodemaker-no.pages.index-page
  (:require [kodemaker-no.formatting :refer [to-html]]))

(defn- render-form [{:keys [title address subject button action]}]
  (list
   [:h2 title]
   [:form.form.mod {:action (:url action)
                    :method (:method action)}
    [:label address]
    [:input.input {:type "text"}]
    [:label subject]
    [:textarea.input {:rows 4}]
    [:div
     [:button.btn {:type "submit"} button]]]))

(defn- render-intro [{:keys [title text]}]
  (list
   [:h2 title]
   (to-html text)))

(defn- render-face [{:keys [url name photo]}]
  [:a.linkBlock {:href url}
   [:span.inverse.fpt.linkish name]
   [:img.fpf {:src photo}]])

(defn- render-our-reference-description [name description url logo]
  (list
   [:a.linkBlock.right.mod.mtl.logo {:href url}
    [:img {:src logo}]]
   [:h3 name]
   [:p description " "
    [:a.nowrap {:href url} "Se referansen"]]))

(defn- render-reference-quote [{:keys [photo author title quote email phone]} url logo]
  [:div.media
   (when photo [:img.img.thumb.mts {:src photo}])
   [:div.bd
    [:a.linkBlock.right.mod.mts.logo {:href url}
     [:img {:src logo}]]
    [:h4.mtn author]
    (when title [:p.near title])
    [:p.near [:q quote]]
    [:p [:a {:href url} "Se referansen"]]]])

(defn- render-reference [{:keys [name url description logo reference]}]
  (if reference
    (render-reference-quote reference url logo)
    (render-our-reference-description name description url logo)))

(defn index-page [data]
  {:body (list
          [:div.line.bbl
           [:div.unitRight.r-2of3
            [:div.bd.rel.fface
             (render-face (first (shuffle (:faces data))))]]
           [:div.lastUnit
            (render-form (:form data))]]
          (render-intro (:intro data))
          (map render-reference (:references data))
          [:p.mtn [:a {:href "/referanser/"} "Se alle våre referanser"]])})
