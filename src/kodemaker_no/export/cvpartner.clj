(ns kodemaker-no.export.cvpartner
  (:require [cheshire.core :refer :all]
            [clj-http.client :as http]
            [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [kodemaker-no.atomic :as atomic]
            [kodemaker-no.homeless :as h]
            [kodemaker-no.ingest :as ingest]
            [kodemaker-no.new-pages.cv-page :as cv]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; config stuff

(def authorization-token (System/getenv "CVPARTNER_AUTHORIZATION_TOKEN"))
(def site "https://kodemaker.cvpartner.com")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; datomic stuff

(defn- find-all-person-ids [db]
  (d/q '[:find [?e ...] :where [?e :person/email-address]] db))

(defn- find-person-by-email [db email]
  ; TODO must be a way to simplify d/entity .... d/q
  (d/entity db (d/q '[:find ?e . :in $ ?email :where [?e :person/email-address ?email]] db email)))

(defn- find-all-people [db]
  (->> (find-all-person-ids db)
       (map #(d/entity db %))))


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

(defn- find-cvuser-by-email [email]
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
  (let [user-id (:user_id cvp-user)
        cv-id (:default_cv_id cvp-user)]
    (do
      ;(delete-cv-part :courses user-id cv-id)
      (delete-cv-part :certifications user-id cv-id)
      (delete-cv-part :educations user-id cv-id)
      (delete-cv-part :key_qualifications user-id cv-id)
      (delete-cv-part :languages user-id cv-id)
      (delete-cv-part :presentations user-id cv-id)
      (delete-cv-part :project_experiences user-id cv-id)
      (delete-technologies user-id cv-id)
      )))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; cv data generation stuff

(defn- trim-newlines [s]
  (if (nil? s) s (str/replace s #"\n\s+" "\n")))

(defn- str-or-nil [label value]
  (if value
    (str label value)
    nil))

(defn- generate-project-experience-skills [db project]
  (let [tech-refs (:project/techs project)
        techs (map (partial d/entity db) tech-refs)]
    (map (fn [tech] {:tags {:no (:tech/name tech)}}) techs)))

(defn- generate-certification [certification]
  {:name             {:no (:name certification)}
   :organizer        {:no (:institution certification)}
   :long_description {:no (str/join "\n"
                                    (filter identity
                                            [(str-or-nil "Url: " (:url certification))
                                             (str-or-nil "Certificate-name: " (:text (:certificate certification)))
                                             (str-or-nil "Certificate-url: " (:url (:certificate certification)))]))}
   :year             (:year certification)})

(defn- generate-education [education]
  {:school    {:no (:institution education)}
   :degree    {:no (:subject education)}
   :year_from (first (:years education))
   :year_to   (last (:years education))})

(defn- generate-key-qualifications [db person]
  [
   {:label            {:no "Beskrivelse, person"}
    :long_description {:no (trim-newlines (:person/description person))}
    :tag_line         {:no ""}}
   {:label            {:no "Beskrivelse, cv"}
    :long_description {:no (trim-newlines (:cv/description person))}
    :tag_line         {:no ""}}
   {:label            {:no "Nøkkelkvalifikasjoner"}
    :long_description {:no (:person/title person)}
    :key_points       (->> (:person/qualifications person)
                           (sort-by :list/idx)
                           (map (fn [qual] {:name {:no (:qualification/text qual)}})))}
   {:label      {:no "Prefererte teknologier"}
    :key_points (map (fn [tech-ref]
                       {:name {:no (:tech/name (d/entity db tech-ref))}})
                     (h/entity-seq (:person/preferred-techs person)))}
   {:label      {:no "Bruker på jobben"}
    :key_points (map (fn [tech-ref]
                       {:name {:no (:tech/name (d/entity db tech-ref))}})
                     (map :list/ref (:person/using-at-work person)))}
   {:label      {:no "Favoritter for tiden"}
    :key_points (map (fn [tech-ref]
                       {:name {:no (:tech/name (d/entity db tech-ref))}})
                     (map :list/ref (:person/favorites-at-the-moment person)))}
   {:label      {:no "Vil lære mer av"}
    :key_points (map (fn [tech-ref]
                       {:name {:no (:tech/name (d/entity db tech-ref))}})
                     (map :list/ref (:person/want-to-learn-more person)))}
   {:label      {:no "Open source bidrag"}
    :key_points (map (fn [osc]
                       {:name             {:no (:oss-project/name osc)}
                        :long_description {:no (str/join
                                                 "\n"
                                                 (filter identity
                                                         [(str-or-nil "Url: " (:oss-project/url osc))
                                                          (str-or-nil "Techs: " (map (fn [tech-ref]
                                                                                       {:name {:no (:tech-name (d/entity db tech-ref))}})
                                                                                     (:oss-project/tech-list osc)))]))}})
                     (:person/open-source-contributions person))}])


(defn- generate-language [language]
  {:name  {:no (:language language)}
   :level {:no (str/join "\n"
                         (filter identity
                                 [(str-or-nil "Orally: " (:orally language))
                                  (str-or-nil "Written: " (:written language))]))}})

(defn- generate-project [db project]
  {:customer                  {:no (:project/customer project)}
   :description               {:no (:project/summary project)}
   :long_description          {:no (trim-newlines (:project/description project))}
   :year_from                 (first (:project/years project))
   :year_to                   (last (:project/years project))
   :project_experience_skills (generate-project-experience-skills db project)
   :disabled                  false})

(defn- generate-presentation [presentation]
  {:description      {:no (:presentation/title presentation)}
   :long_description {:no (str/join "\n"
                                    (filter identity
                                            [(str-or-nil "Description: " (:presentation/description presentation))
                                             (str-or-nil "Event-name: " (:presentation/event-name presentation))
                                             (str-or-nil "Event-url: " (:presentation/event-url presentation))
                                             (str-or-nil "Source-url: " (:presentation/source-url presentation))
                                             (str-or-nil "Slides-url: " (:presentation/slides-url presentation))]))}
   :month            (.getMonthValue (:presentation/date presentation))
   :year             (.getYear (:presentation/date presentation))})

(defn- generate-technologies [db person]
  (for [[category techs] (cv/compile-cv-techs person)]
    {:category {:no (:tech-category/label category)}
     :uncategorized false
     :technology_skills (map (fn [tech] {:tags {:no (:tech/name tech)}}) techs)}))

(defn generate-cv [db person]
  {:telefon             (:person/phone-number person)
   :twitter             (:twitter (:person/presence person))
   :certifications      (map generate-certification (:person/certifications person))
   :educations          (map generate-education (:person/education person))
   :key_qualifications  (generate-key-qualifications db person)
   :presentations       (map generate-presentation (:person/presentations person))
   :project_experiences (map (partial generate-project db) (:person/projects person))
   :technologies        (generate-technologies db person)
   :languages           (map generate-language (:person/languages person))})

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

(defn- create-or-update-user [user]
  (http-post {:user user} (api :users)))

(defn- update-cv [cvp-user cv]
  (let [import-json-url ((api :import-json) (:user_id cvp-user) (:default_cv_id cvp-user))]
    (post-multipart import-json-url cv)))

(defn- export-cv [db company-config person]
  (let [cv (generate-cv db person)
        email (:person/email-address person)
        cvp-user (find-cvuser-by-email email)]
    (println "Creating cv for" email)
    (try (if cvp-user
           (do
             (println "  Removing existing cv...")
             (cleanup-cv cvp-user)
             (println "  Creating new...")
             (update-cv cvp-user cv))
           (-> person
               (println "  Create new user...")
               (generate-user company-config)
               (create-or-update-user)
               (println "  Creating cv...")
               (update-cv cv)))
         (println "  Successfully created cv for " email)
         (catch Exception e
           (println "==> Failed creating cv for" email ". See error file for details.")
           (.println *err* (str "==> Failed creating cv for " email ": " e))
           ;(println "Person data is:" person)
           ;(println "Generated cv is:" cv)
           ))))

(defn cvpartner-export [& args]
  "To be used from shell script"
  (let [conn (atomic/create-database (str "datomic:mem://" (d/squuid)))]
    (ingest/ingest-all conn "resources")
    (let [db (d/db conn)
          names (vec args)
          company-config (get-company-config)
          people (if (some #{"all"} names)
                   (->> (find-all-people db)
                        (remove :person/quit?)
                        (filter :person/profile-active?)
                        ;(drop 12)                           ; TODO testing
                        ;(take 2)                            ; TODO testing
                        )
                   (->> names
                        (map (fn [name]
                               (let [email-address (str name "@kodemaker.no")
                                     person (find-person-by-email db email-address)]
                                 (if (:db/id person)
                                   person
                                   (println "==> Could not find person with email address " email-address)))))
                        (filter identity)))]
      (doseq [person people] (export-cv db company-config person)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; REPL stuff
(comment

  ;;;;;;;;;;;;;;;;;
  ; To run in repl:

  ; first start datomic database
  (start)

  ; then load this file in REPL
  ; (intellij: shift-cmd-L)

  ; finally set namespace for access to functions
  (ns kodemaker-no.export.cvpartner)

  ; set datomic
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  ;;;;;;;;;;;;;;;;;
  ; useful commands

  ; export all
  (map str (export-all-cvs db))

  ; get test person from datomic
  (def trygve (d/entity db :person/trygve))
  (def test-person (d/entity db :person/olga))
  (generate-cv db test-person)
  (generate-technologies db test-person)

  ; need company for create user
  (http-get (api :companies))

  ; all person ids
  (d/q '[:find ?e :where [?e :person/full-name]] db)

  ; cvpartner data here
  (def test-user (find-cvuser-by-email "trygve@kodemaker.no"))
  (def test-cv (http-get ((api :cvs) (:_id test-user) (:default_cv_id test-user))))

  ; datomic queries
  (d/q '[:find [(pull ?e [*]) ...] :where [?e :person/given-name "Trygve"]] db)
  (d/q '[:find [(pull ?e [*]) ...] :where [?e :person/email-address "olga@kodemaker.no"]] db)

  )
