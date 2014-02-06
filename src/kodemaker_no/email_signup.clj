(ns kodemaker-no.email-signup)

(defn render-email-signup [{:keys [list-id heading button-text blurb]}]
  (list
   [:h4 heading]
   [:p blurb]
   [:form {:action (str "http://kodemaker.us3.list-manage.com/subscribe/post?u=4ebbe7240d7b3e43134812e43&amp;id=" list-id)
           :method "post"}
    [:fieldset.mod
     [:input {:type "hidden" :name (str "b_4ebbe7240d7b3e43134812e43_" list-id)}]
     [:label {:for "email"} "E-post "]
     [:input {:type "email" :name "email" :id "email"}]
     [:input {:type "submit" :value button-text}]]]))
