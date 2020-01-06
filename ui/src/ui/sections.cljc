(ns ui.sections
  (:require [ui.elements :as e]
            [ui.layout :as l]))

(defn article-section [{:keys [articles class] :as params}]
  [:div.section.article-section
   {:className class
    :style (l/stylish {} params)}
   [:div.content
    (map e/article articles)]])

(defn banner-section [{:keys [logo text] :as params}]
  [:div.section
   {:style (l/stylish {} params)}
   [:div.content.tac.banner-ws
    (when logo
      [:img.banner-logo {:src logo}])
    (e/h1 {} text)]])

(defn bruce-section [{:keys [title text link image-front image-back] :as params}]
  [:div.section.bruce
   {:style (l/stylish {} params)}
   [:div.content
    [:div.gutter.gutter-l
     [:div.bruce-header (l/header)]
     [:div.bruce-content
      (e/h0 {} title)
      [:p.text text]
      (e/arrow-link link)]
     [:div.bruce-image-front
      [:img {:src image-front}]]
     [:div.bruce-image-back
      [:img {:src image-back}]]]]])

(defn grid-section-content [{:keys [items grid-type]}]
  [:div.content
   ((case grid-type
      :box-grid e/box-grid
      :card-grid e/card-grid) items)])

(defn grid-section [params]
  [:div.section.grid-section
   {:style (l/stylish {} params)}
   (grid-section-content params)])

(defn grid-header-section [params]
  [:div.section.grid-header-section
   {:style (l/stylish {} params)}
   [:div.content.header-section
    (l/header)]
   [:div.grid-section
    (grid-section-content params)]])

(def github-icon
  [:svg {:viewBox "0 0 24 24"}
   [:path {:d "M12 0C5.374 0 0 5.373 0 12c0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23A11.509 11.509 0 0112 5.803c1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576C20.566 21.797 24 17.3 24 12c0-6.627-5.373-12-12-12z"}]])

(def linkedin-icon
  [:svg {:viewBox "0 0 24 24"}
   [:path {:d "M19 0H5a5 5 0 00-5 5v14a5 5 0 005 5h14a5 5 0 005-5V5a5 5 0 00-5-5zM8 19H5V8h3v11zM6.5 6.732c-.966 0-1.75-.79-1.75-1.764s.784-1.764 1.75-1.764 1.75.79 1.75 1.764-.783 1.764-1.75 1.764zM20 19h-3v-5.604c0-3.368-4-3.113-4 0V19h-3V8h3v1.765c1.396-2.586 7-2.777 7 2.476V19z"}]])

(def stackoverflow-icon
  [:svg {:viewBox "0 0 24 24"}
   [:path {:d "M15 21H5v-2h10v2zm6-11.665L19.379 0l-1.993.346 1.62 9.335L21 9.335zm-5.964 6.937l-9.746-.975-.186 2.016 9.755.879.177-1.92zm.538-2.587l-9.276-2.608-.526 1.954 9.306 2.5.496-1.846zm1.204-2.413L8.481 6.408 7.452 8.151l8.298 4.865 1.028-1.744zm1.866-1.467l-5.339-7.829-1.672 1.14 5.339 7.829 1.672-1.14zM16 14v8H4v-8H2v10h16V14h-2z"}]])

(def twitter-icon
  [:svg {:viewBox "0 0 24 24"}
   [:path {:d "M24 4.557a9.83 9.83 0 01-2.828.775 4.932 4.932 0 002.165-2.724 9.864 9.864 0 01-3.127 1.195 4.916 4.916 0 00-3.594-1.555c-3.179 0-5.515 2.966-4.797 6.045A13.978 13.978 0 011.671 3.149a4.93 4.93 0 001.523 6.574 4.903 4.903 0 01-2.229-.616c-.054 2.281 1.581 4.415 3.949 4.89a4.935 4.935 0 01-2.224.084 4.928 4.928 0 004.6 3.419A9.9 9.9 0 010 19.54a13.94 13.94 0 007.548 2.212c9.142 0 14.307-7.721 13.995-14.646A10.025 10.025 0 0024 4.557z"}]])

(defn profile-section [{:keys [full-name title mobile mail description image presence cv] :as params}]
  [:div.section {:style (l/stylish {} params)}
   [:div.content.header-section
    (l/header)]
   [:div.content.profile-section.mbxl
    [:div.profile-title
     [:h1.h1 full-name]
     [:h5.h5.mbm title]]
    [:div.profile-image [:img.img {:src image}]]
    [:div.profile-cv
     (e/arrow-link {:text (:text cv)
                    :href (:url cv)})]
    [:div.profile-contact
     [:div [:a {:href (str "tel:" mobile)} mobile]]
     [:div [:a {:href (str "mailto:" mail)} mail]]
     [:div.profile-icons.mtm
      (when-let [url (:linkedin presence)]
        [:a {:href url :target "_blank"} linkedin-icon])
      (when-let [url (:stackoverflow presence)]
        [:a {:href url :target "_blank"} stackoverflow-icon])
      (when-let [url (:twitter presence)]
        [:a {:href url :target "_blank"} twitter-icon])
      (when-let [url (:github presence)]
        [:a {:href url :target "_blank"} github-icon])]]
    [:div.profile-desc
     description]]])

(defn pønt-section [{:keys [portrait-1 portrait-2 top-triangle bottom-triangle top-circle bottom-circle] :as params}]
  [:div.section.pønt-section {:style (l/stylish {} params)}
   [:div.content
    [:div.gutter.gutter-xl
     [:div.pønt-item.portrait-1
      [:img.img.image-style-chocolate-triangle {:src portrait-1}]]

     [:div.pønt-item.top-triangle
      [:img.img.image-style-rouge-triangle {:src top-triangle}]]

     [:div.pønt-item.bottom-circle
      [:img.img.image-style-chocolate-circle-pønt {:src bottom-circle}]]

     [:div.pønt-item.portrait-2
      [:img.img.image-style-rouge-triangle {:src portrait-2}]]

     [:div.pønt-item.top-circle
      [:img.img.image-style-rouge-circle-pønt {:src top-circle}]]

     [:div.pønt-item.bottom-triangle
      [:img.img.image-style-chocolate-triangle {:src bottom-triangle}]]]]])

(defn seymour-section [{:keys [seymours] :as params}]
  [:div.section {:style (l/stylish {} params)}
   [:div.content.whitespaceorama
    [:div.trigrid
     (for [seymour seymours]
       [:div
        (e/seymour seymour)])]]])

(defn titled-section [{:keys [title contents] :as params}]
  [:div.section.titled-section {:style (l/stylish {} params)}
   [:div.content
    [:div.titled-title
     [:h3.h3 title]]
    [:div.titled-content
     (interpose [:div.mbl] contents)]]])

(defn enumeration-section [{:keys [title categories] :as params}]
  [:div.section.enumeration-section {:style (l/stylish {} params)}
   [:div.content
    [:h3.h3 title]
    [:div.enum-cats
     (for [category categories]
       [:div.enum-cat
        [:div.enum-label (:label category)]
        [:div.enum-items
         (e/comma-separated
          (for [item (:items category)]
            (if (:href item)
              [:a.link {:href (:href item)} (:text item)]
              (:text item))))]])]]])

(defn vertigo-section [{:keys [title text link image] :as params}]
  [:div.section.vertigo
   {:style (l/stylish {} params)}
   [:div.content
    [:div.gutter.gutter-l.grid
     {:style (l/add-pønt {} [{:kind :less-than
                              :position "right -300px top -410px"}])}
     [:div.vertigo-media
      [:div.inner-media
       [:img.img.image-style-vertigo {:src image}]]]
     [:div.vertigo-content
      [:div.inner-content
       (e/h2 {} title)
       [:p.text text]
       (e/arrow-link link)]]]]])

(defn widescreen-section [{:keys [image alt] :as params}]
  [:div.section.widescreen
   {:style (l/stylish {} params)}
   [:div.content
    [:div.content-l
     [:img.img {:src image :alt alt}]]]])
