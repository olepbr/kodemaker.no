(ns ui.elements
  (:require [clojure.string :as str]))

(defn el [el params text]
  [(get params :element el)
   {:className (str (name el)
                    (when-let [cn (:className params)]
                      (str " " cn)))} text])

(defn h0 [params text]
  [:h1.h0 params text])

(def h1 (partial el :h1))
(def h2 (partial el :h2))
(def h3 (partial el :h3))
(def h4 (partial el :h4))
(def h5 (partial el :h5))
(def h6 (partial el :h6))

(defn comma-separated [coll]
  (drop 1 (interleave (into (list " og " "")
                            (repeat (dec (count coll)) ", "))
                      coll)))

(defn enumerate-techs [techs]
  (comma-separated
   (for [tech (filter :tech/name techs)]
     (if (:page/uri tech)
       [:a.link {:href (:page/uri tech)}
        (:tech/name tech)]
       (:tech/name tech)))))

(defn tech-tags [{:keys [prefix techs class]}]
  (when (seq techs)
    [:span {:className class} prefix " "
     (enumerate-techs techs)]))

(defn people-tags [{:keys [prefix people class]}]
  (when (seq people)
    [:span {:className class} prefix " "
     (comma-separated (for [person (filter :person/given-name people)]
                        (if (:page/uri person)
                          [:a.link {:href (:page/uri person)}
                           (:person/given-name person)]
                          (:person/given-name person))))]))

(defn blockquote [{:keys [quote]}]
  [:blockquote.blockquote.text {}
   (let [sentences (str/split quote #"\n\n")
         sentences (concat [(str "«" (first sentences))] (rest sentences))]
     (map
      (fn [sentence] [:p {:className "p"} sentence])
      (concat (take (dec (count sentences)) sentences) [(str (last sentences) "»")])))])

(defn arrow [{:keys [width]}]
  [:div.arrow [:div.arrow-head]])

(def size-classes
  {:large "text-l"})

(defn arrow-link [{:keys [text href size title] :as params}]
  [:a.link.ib.skip-underline
   {:href href
    :title title
    :className (size-classes size)}
   text (arrow params)])

(def viewboxes
  {:custom/person "0 0 44 44"})

(defn icon [{:keys [type width height color]}]
  [:svg {:view-box (or (viewboxes type) "0 0 24 24")
         :width width
         :height height}
   [:use {:xlink-href (str "/icons/" (namespace type) "/" (name type) ".svg#icon")
          :style {"--svg_color" (str "var(--" (or (some-> color name) "rouge") ")")}}]])

(defn seymour [params]
  [:div.seymour
   [:div.seymour-top
    (icon (:icon params))
    (h4 {} (:title params))
    [:p (:text params)]]
   [:div.seymour-bottom
    (arrow-link (:link params))]])

(defn teaser [{:keys [link annotation tags content icon title url]}]
  [:div.teaser
   (if url
     [:a.link {:href url :className (when icon "icon-link")} icon title]
     title)
   (when annotation
     [:p.annotation.text-s annotation])
   (when tags
     [:div.tags.mvs tags])
   (when content [:div.text.mbm content])
   (when link (arrow-link link))])

(defn illustrated-teaser [{:keys [link annotation tags content icon title url illustration]}]
  [:div.teaser
   [:h3.h4-light
    (if url
      [:a.link {:href url :className (when icon "icon-link")} icon title]
      title)]
   (when annotation
     [:p.annotation.text-s annotation])
   [:div.illustrated-teaser
    [:div.teaser-content
     (when tags
       [:div.tags.mvs tags])
     (when content [:div.text.mts.mbm content])]
    (when illustration
      [:div.teaser-illustration.mts
       [:img.img {:src illustration}]])
    (when link
      [:div.teaser-link
       (arrow-link link)])]])

(defn video-thumb [params]
  [:div
   [:a.video-thumb {:href (:url params) :className (:class params)}
    [:img.img {:src (:img params)}]
    [:div.video-overlay [:div.video-indicator]]]
   [:p.tags.video-tags (:tags params)]
   [:p.text [:a.link {:href (:url params)}
             (:title params)]]])

(defn curtain-class [side]
  (str "curtain-" (or (some-> side name) "left")))

(defn curtain [{:keys [content side]}]
  [:span.curtain {:className (curtain-class side)}
   content])

(defn media [{:keys [title href content class image]}]
  [:div.media {:className class}
   (when (:src image)
     [:div.media-element {}
      (let [image [:img.img {:className (str "image-style-" (:type image)
                                             (when-let [side (:curtain image)]
                                               (str " " (curtain-class side))))
                             :src (:src image)}]]
        (if href
          [:a {:href href} image]
          image))])
   [:div.media-content
    (when title (h4 {:className "title"} (if href [:a {:href href} title] title)))
    content]])

(defn round-media [params]
  (media (assoc params
                :class "round-media"
                :content [:p (interpose [:br] (:lines params))]
                :image {:src (:image params)
                        :type "vcard-small"
                        :size 92})))

(defn vert-round-media [params]
  (media (assoc params
                :class "round-media vert-round-media"
                :content [:p (interpose [:br] (:lines params))]
                :image {:src (:image params)
                        :type "vcard-medium"
                        :size 120})))

(defn illustrated [params]
  (media (assoc params
                :class "illustrated"
                :content [:p (interpose [:br] (:lines params))]
                :image {:src (:image params)
                        :type "profile-medium"
                        :size 300
                        :curtain (:curtain params)})))

(defn article-header [{:keys [title href sub-title annotation]}]
  (->> (list
        (when title
          [:h2.h3 (if href
                    [:a {:href href} title]
                    title)])
        (when sub-title
          [:h3.h4 sub-title])
        (when annotation
          [:p.annotation.text-s annotation]))
       (remove nil?)))

(defn article [{:keys [content aside aside-title image alignment mecha-title
                       mecha-sub-title mecha-sub-title-style] :as article}]
  [:div.article-wrapper
   (when (or mecha-title mecha-sub-title)
     [:div.mbl
      (when mecha-title [:h1.h0 mecha-title])
      (when mecha-sub-title [:h2 {:className (or mecha-sub-title-style "h5")}
                             mecha-sub-title])])
   (when (= :content alignment)
     [:div.article {:className (str "article-align-" (name (or alignment :balanced)))}
      [:div.article-content
       (article-header article)]])
   [:div.article {:className (str "article-align-" (name (or alignment :balanced)))}
    [:div.article-content {}
     (when-not (= :content alignment)
       (article-header article))
     content]
    [:div.article-aside {}
     (when aside-title
       [:h2.h3 aside-title])
     (when image
       [:img.img {:className (cond
                               (= :front alignment) "image-style-bottom-half-circle"
                               :default "image-style-rouge-triangle-medium")
                  :src image}])
     aside]]])

(defn simple-article [{:keys [title tags annotation content]}]
  [:div.article-wrapper
   [:div.simple-article
    (when (or title tags annotation)
      [:div.mbl
       (when title [:h1.h1 title])
       (when tags [:div.tags.mvs tags])
       (when annotation [:p.annotation annotation])])
    [:div.text.article-text content]]])

(defn stats [{:keys [icon-type icon-count stats]}]
  [:div.stats {}
   (repeat icon-count
           (icon {:type icon-type
                  :color :chocolat-au-lait
                  :width 44}))
   (for [stat stats]
     [:p stat])])

(defn image-link [{:keys [href image alt]}]
  [:a {:href href} [:img.img {:src image :alt alt}]])

(defn box-grid [items]
  [:div.box-grid
   (for [{:keys [content size]} items]
     [:div.box-grid-item {:className (when size (str "box-grid-span-" size))}
      content])])

(defn card-grid [items]
  [:div.card-grid
   (for [{:keys [content]} items]
     [:div.card-grid-item content])])

(defn tango-grid [items]
  [:div.tango-grid {:style {:grid-gap "64px"}}
   (for [{:keys [content class]} items]
     [:div.tango-grid-item {:className class} content])])

(defn icon-link-row [{:keys [links class]}]
  [:div.icon-links {:className class}
   (for [{:keys [href title icon target]} links]
     [:a {:href href :target target :title title}
      icon])])

(defn attributed-content [{:keys [title person logo link content]}]
  [:div
   [:div.ac-title.mbm title]
   [:div.attributed-content
    [:div.ac-content
     content
     [:div.acc
      [:div.acc-link (arrow-link link)]
      [:div.acc-logo [:img.img {:src (:image logo)}]]]]
    [:div.ac-person
     (round-media person)
     [:div.ac-logo
      [:img.img {:src (:image logo)}]]]
    [:div.ac-link
     (arrow-link link)]]])

(defn google-map [{:keys [zoom lat lon title api-key map-marker-url]}]
  [:div
   [:div {:id "google-map" :style {:height "441px"}}]
   [:script
    (str "
var initMap = function () {
    var kmhq = new google.maps.LatLng(" lat ", " lon ");
    var map = new google.maps.Map(document.getElementById('google-map'), {
        center: kmhq,
        zoom: " zoom "
    });
    var marker = new google.maps.Marker({
        position: kmhq,
        map: map,
        title: '<address>" title "</address>',
        icon: '" map-marker-url "'
    });
};
")]
   [:script
    {:type "text/javascript"
     :src (str "https://maps.googleapis.com/maps/api/js?key=" api-key
               "&callback=initMap")}]])
