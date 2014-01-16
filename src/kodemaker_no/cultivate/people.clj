(ns kodemaker-no.cultivate.people)

(defn- add-str [person]
  (assoc person :str (-> person :id str (subs 1))))

(defn- add-url [person]
  (assoc person :url (str "/" (:str person) ".html")))

(defn- add-full-name [person]
  (assoc person :full-name (str (:first-name person) " "
                                (when-let [middle (:middle-name person)]
                                  (str middle " "))
                                (:last-name person))))

(defn- add-photos [person]
  (assoc person :photos
         {:side-profile (str "/photos/" (:str person) "/side-profile.jpg")
          :half-figure (str "/photos/" (:str person) "/half-figure.jpg")}))

(defn- cultivate-person [person]
  (-> person
      add-str
      add-url
      add-full-name
      add-photos))

(defn cultivate-people [content]
  (update-in content [:people] #(map cultivate-person %)))
