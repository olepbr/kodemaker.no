(ns kodemaker-no.export.cvpartner
  (:require [clojure.string :as str]
            [clj-http.client :as http]
            [cheshire.core :refer :all]
            [java-time-literals.core :as jte]
            [datomic-type-extensions.api :as d]
            [clojure.string :as str]
            [kodemaker-no.new-pages.cv-page :as page]
            [kodemaker-no.new-pages.person :refer [prefer-techs]])
  (:import java.time.LocalDate))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; config stuff

(def authorization-token "hemmelig api key til cvpartner")
(def site "https://kodemaker.cvpartner.com")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; datomic stuff

(defn- db-pull-by-id [db id]
  (d/pull db '[*] id))

(defn- find-all-person-ids [db]
  (map #(first %1) (d/q '[:find ?e :where [?e :person/full-name]] db)))

(defn- find-all-persons [db]
  (->> (find-all-person-ids db)
       (map (partial db-pull-by-id db))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; api stuff

(def authorization-header {"Authorization" (str "Token token=" authorization-token)})

(def api
  {:companies   (str site "/api/v1/countries")
   :users       (str site "/api/v1/users")
   :cvs         (fn [user-id cv-id] (str/join "/" [site "api/v3/cvs" user-id cv-id]))
   :cv-part     (fn [user-id cv-id part-name id] (str/join "/" [site "api/v3/cvs" user-id cv-id part-name id]))
   :import-json (fn [user-id cv-id] (str/join "/" [site "api/v1/cvs" user-id cv-id "import_json"]))})

(defn- post-multipart [_url body]
  "Really a specialized version for posting cvs to the import_json api.
  Worked around a bug? in clj-http and/or apache-httpclient to be able to set filename
  when content is not a file."
  (let [byteArray (.getBytes (generate-string body))
        url _url
        ;url "http://localhost:8080"                        ; see what we send with: >$ nc -l 8080
        ]
    (http/post url {:as        :json
                    :multipart [{:name      "json[file]"
                                 :mime-type "application/json"
                                 :encoding  "UTF-8"
                                 :content   byteArray}]
                    :headers   authorization-header})))


(defn- http-get [url]
  (:body (http/get url {:as :json :headers authorization-header})))

(defn- http-post [body url]
  (let [json-body (generate-string body)]
    (:body (http/post url {:as :json :content-type :json :body json-body :headers authorization-header}))))

(defn- http-delete [url]
  (http/delete url {:headers authorization-header}))

(defn- http-get-or-nil [url]
  (let [response (http/get url {:as :json :headers authorization-header :throw-exceptions false})
        status (:status response)]
    (cond
      (<= 200 status 299) (:body response)
      (= 404 status) nil
      :else (do
              (println "Failed while getting url " url)
              (prn response)
              (throw (Exception. "Error"))))))

(defn- find-user-by-email [email]
  (http-get-or-nil (str (api :users) "/find?email=" email)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; cv cleanup stuff

(defn- delete-technologies [user-id cv-id]
  "Similar to delete-cv-part, but cannot delete ;uncategorized technologies"
  (let [part-name :technologies
        cvp-cv (http-get ((api :cvs) user-id cv-id))]
    (doseq [part (filter #(not (:uncategorized %)) (get cvp-cv part-name))]
      (http-delete ((api :cv-part) user-id cv-id (name part-name) (:_id part))))))

(defn- delete-cv-part [part-name user-id cv-id]
  "Delete cv part such as project_experiences, courses etc.
   A part must be a list where each item hava an '_id' attribute"
  (let [cvp-cv (http-get ((api :cvs) user-id cv-id))]
    (doseq [part (get cvp-cv part-name)]
      (http-delete ((api :cv-part) user-id cv-id (name part-name) (:_id part))))))

(defn- cleanup-cv [cvp-user]
  "The api is so constructed that one must manually remove cv, one part at a time"
  (let [cvp-cv (http-get ((api :cvs) (:user_id cvp-user) (:default_cv_id cvp-user)))
        user-id (:user_id cvp-user)
        cv-id (:default_cv_id cvp-user)]
    (println "Cleaning up cv for" (:email cvp-user))
    (do
      ;(delete-cv-part :courses user-id cv-id)
      (delete-cv-part :certifications user-id cv-id)
      (delete-cv-part :educations user-id cv-id)
      (delete-cv-part :key_qualifications user-id cv-id)
      (delete-cv-part :presentations user-id cv-id)
      (delete-cv-part :project_experiences user-id cv-id)
      (delete-technologies user-id cv-id)
      )))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; cv data generation stuff

(defn- str-or-nil [label value]
  (if value
    (str label value)
    nil))

(defn- generate-tech [tech]
  {:tags {:no (:tech/name tech)}})

(defn- pull-tech [db tech-ref]
  (db-pull-by-id db (:db/id tech-ref)))

(defn- all-techs-by-type [db person]
  "NOTE: Almost identical to page/all-techs, but still different"
  (->> (concat (:person/using-at-work person)
               (:person/innate-skills person)
               (page/side-project-techs person)
               (page/blog-post-techs person)
               (page/screencast-techs person)
               (page/presentation-techs person)
               (page/open-source-techs person)
               (page/project-techs person))
       (remove (or (set (:person/exclude-techs person)) #{}))
       frequencies
       (sort-by (comp - second))
       (map first)
       (person/prefer-techs (:person/preferred-techs person))
       (map (partial pull-tech db))
       (group-by :tech/type)))

(defn- generate-project-experience-skills [db project]
  (let [tech-refs (:project/techs project)
        techs (map (partial pull-tech db) tech-refs)]
    (map generate-tech techs)))

(defn- generate-certification [certification]
  {:name             {:no (:name certification)}
   :organizer        {:no (:institution certification)}
   :long_description {:no (str/join "\n"
                                    (filter identity
                                            [(str-or-nil "Url:" (:url certification))
                                             (str-or-nil "Certificate-name:" (:text (:certificate certification)))
                                             (str-or-nil "Certificate-url:" (:url (:certificate certification)))]))}
   :year             (:year certification)})

(defn- generate-education [education]
  {:school    {:no (:institution education)}
   :degree    {:no (:subject education)}
   :year_from (first (:years education))
   :year_to   (last (:years education))})

(defn- generate-key-qualifications [db person]
  [
   {:label            {:no "Beskrivelse, person"}
    :long_description {:no (:person/description person)}
    :tag_line         {:no ""}}
   {:label            {:no "Beskrivelse, cv"}
    :long_description {:no (:cv/description person)}
    :tag_line         {:no ""}}
   {:label            {:no "Nøkkelkvalifikasjoner"}
    :long_description {:no (:person/title person)}
    :key_points       (map (fn [qual] {:name {:no qual}}) (:person/qualifications person))}
   {:label      {:no "Prefererte teknologier"}
    :key_points (map (fn [tech-ref]
                       {:name {:no (:tech/name (pull-tech db tech-ref))}})
                     (:person/preferred-techs person))}
   {:label      {:no "Bruker på jobben"}
    :key_points (map (fn [tech-ref]
                       {:name {:no (:tech/name (pull-tech db tech-ref))}})
                     (:person/using-at-work person))}
   {:label      {:no "Favoritter for tiden"}
    :key_points (map (fn [tech-ref]
                       {:name {:no (:tech/name (pull-tech db tech-ref))}})
                     (:person/favorites-at-the-moment person))}
   {:label      {:no "Vil lære mer av"}
    :key_points (map (fn [tech-ref]
                       {:name {:no (:tech/name (pull-tech db tech-ref))}})
                     (:person/want-to-learn-more person))}
   {:label      {:no "Open source bidrag"}
    :key_points (map (fn [osc]
                       {:name             {:no (:oss-project/name osc)}
                        :long_description {:no (str/join
                                                 "\n"
                                                 (filter identity
                                                         [(str-or-nil "Url:" (:oss-project/url osc))
                                                          (str-or-nil "Techs:" (map (fn [tech-ref]
                                                                                      {:name {:no (:tech-name (pull-tech db tech-ref))}})
                                                                                    (:oss-project/tech-list osc)))
                                                          ]))}})
                     (:person/open-source-contributions person))}])


(defn- generate-project [db project]
  {:customer                  {:no (:project/customer project)}
   :description               {:no (:project/summary project)}
   :long_description          {:no (:project/description project)}
   :year_from                 (first (:project/years project))
   :year_to                   (last (:project/years project))
   :project_experience_skills (generate-project-experience-skills db project)
   :disabled                  false})

(defn- generate-presentation [presentation]
  {:description      {:no (:presentation/title presentation)}
   :long_description {:no (str/join "\n"
                                    (filter identity
                                            [(str-or-nil "Description:" (:presentation/description presentation))
                                             (str-or-nil "Event-name:" (:presentation/event-name presentation))
                                             (str-or-nil "Event-url:" (:presentation/event-url presentation))
                                             (str-or-nil "Source-url:" (:presentation/source-url presentation))
                                             (str-or-nil "Slides-url:" (:presentation/slides-url presentation))]))}
   :month            (.getMonthValue (:presentation/date presentation))
   :year             (.getYear (:presentation/date presentation))})

(defn- generate-technologies [db person]
  (for [[tech-type techs] (all-techs-by-type db person)]
    {:category          {:no (or (page/tech-labels tech-type) tech-type)}
     :uncategorized     (nil? tech-type)
     :technology_skills (map (fn [tech] {:tags {:no (:tech/name tech)}}) techs)}))


(defn- generate-cv [db person]
  {:telefon             (:person/phone-number person)
   :twitter             (:twitter (:person/presence person))
   :certifications      (map generate-certification (:person/certifications person))
   :educations          (map generate-education (:person/education person))
   :key_qualifications  (generate-key-qualifications db person)
   :presentations       (map generate-presentation (:person/presentations person))
   :project_experiences (map (partial generate-project db) (:person/projects person))
   :technologies        (generate-technologies db person)
   })

(defn- generate-user [person company-config]
  {:country_id (:country-id company-config)
   :company_id (:company-id company-config)
   :office_id  (:office-id company-config)
   :email      (:person/email-address person)
   :deactivate false
   :role       "internationalmanager"
   :name       (:person/full-name person)})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Basic logic

(defn- get-company-config []
  (let [companies (http-get (api :companies))
        kodemaker (first companies)                         ; We have access to only one company
        country-id (:country_id (first (:offices kodemaker)))]
    {:country-id country-id
     :company-id "5ea83fbf0d5a2501a3ea8ce2"                 ; from another user?
     :office-id  "5ea83fcb9039f30e1ce2047d"}))              ; from another user?

(defn create-or-update-user [user]
  (http-post {:user user} (api :users)))

(defn- update-cv [cvp-user cv]
  (let [import-json-url ((api :import-json) (:user_id cvp-user) (:default_cv_id cvp-user))]
    (post-multipart import-json-url cv)))

(defn- export-cv [db company-config person]
  (let [cv (generate-cv db person)
        email (:person/email-address person)
        cvp-user (find-user-by-email email)]
    (println "Creating cv for" email)
    (try (if cvp-user
           (do
             (cleanup-cv cvp-user)
             (update-cv cvp-user cv))
           (-> person
               (generate-user company-config)
               (create-or-update-user)
               (update-cv cv)))
         (str "Created cv for " email)
         (catch Exception e
           (println "Failed creating cv for" email ": " e)
           (println "Person data is:" person)
           (println "Generated cv is:" cv)
           (str "Failed creating cv for " email)))))

(defn export-all-cvs [db]
  (let [company-config (get-company-config)]
    (->> (find-all-persons db)
         (filter #(not (:person/quit? %)))
         (filter :person/profile-active?)
         (drop 25)                                          ; TODO testing
         (take 1)                                           ; TODO testing
         (run! (partial export-cv db company-config)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; REPL stuff
(comment
  (start)

  ; After (start), load this file in REPL

  (ns kodemaker-no.export.cvpartner)

  ; export all
  (map str (export-all-cvs db))

  ; trygve local here
  (def trygve (d/pull db '[*] 17592186045673))

  (generate-technologies db trygve)

  ; datomic
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  ; All person ids
  (d/q '[:find ?e :where [?e :person/full-name]] db)

  ; Need company for create user
  (http-get (api :companies))

  ; trygve cvpartner here
  (find-user-by-email (:person/email-address trygve))
  (http-get (str/join "/" [(api :users) "5ea84086af75491055e94423"]))

  (generate-cv trygve)

  (update-cv trygve)
  (export-cv (get-company-config) 17592186045673)


  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ; Old stuff

  ;
  (d/q '[:find [(pull ?e [*]) ...] :where [?e :person/given-name "Trygve"]] db)
  (d/q '[:find [(pull ?e [*]) ...] :where [?e :person/email-address "trygve@kodemaker.no"]] db)
  ; NOT: (d/q '[:find [(pull ?e [*]) ...] :where [?e :id 17592186045673]] db)

  )
