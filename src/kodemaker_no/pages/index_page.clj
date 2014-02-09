(ns kodemaker-no.pages.index-page)

(defn index-page []
  {:body (list
          [:div.line
           [:div.unitRight.r-2of3
            [:div.bd.rel
             [:a.linkBlock {:href "/andre/"}
              [:span.inverse.fpt.linkish "André Bonkowski"]
              [:img.fpf {:src "/photos/people/andre/side-profile-cropped.jpg"}]]]]
           [:div.lastUnit
            [:h2 "Kontakt oss"]
            [:form.form.mod
             [:label "Din e-post eller telefon:"]
             [:input.input {:type "text"}]
             [:label "Hva gjelder det?"]
             [:textarea.input {:rows 4}]
             [:div
              [:button.btn {:type "submit"} "Ta kontakt!"]]]]]
          [:h2 "Når du vil ha mer enn bare kode"]
          [:p "Våre gode kommunikasjonsevner, evne til å samarbeide samt at vi har et
               våkent øye for nye og bedre løsninger gjør at kundene opplever oss som
               viktige støttespillere. "])})
