(ns kodemaker-no.new-pages.video-page
  (:require [datomic-type-extensions.api :as d]
            [kodemaker-no.formatting :as f]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.ingestion.video :as iv]
            [ui.elements :as e]))

(defn- create-embed-code [url]
  (let [{:keys [type id]} (iv/find-video url)]
    (case type
      :youtube [:div.video-embed
                [:iframe {:src (str "//www.youtube.com/embed/" id)
                          :frameborder "0"
                          :allowfullscreen true}]]
      :vimeo [:div.video-embed
              [:iframe {:src (str "//player.vimeo.com/video/" id "?title=0&amp;byline=0&amp;portrait=0")
                        :frameborder "0"
                        :allowfullscreen true}]]
      nil)))

(defn create-page [video]
  {:title (:video/title video)
   :sections
   [{:kind :header}
    {:kind :content
     :content [:div
               [:div.content (create-embed-code (:video/url video))]
               [:div.section.mts
                [:div.content
                 (e/tech-tags {:class "tags"
                               :techs (distinct (h/unwrap-ident-list video :video/tech-list))})]]]}
    {:kind :article
     :articles [{:alignment :content
                 :title (:video/title video)
                 :content [:div.text.em (f/to-html (:video/blurb video))]
                 :aside [:div
                         (for [id (:video/by video)]
                           (let [author (d/entity (d/entity-db video) id)]
                             [:div.mbm
                              (e/round-media
                               {:image (str "/vcard-small" (first (:person/portraits author)))
                                :title (:person/full-name author)
                                :href (:page/uri author)
                                :lines [(:person/title author)
                                        (:person/phone-number author)
                                        (:person/email-address author)]})]))]}]}
    {:kind :footer}]})
