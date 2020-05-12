(ns kodemaker-no.export.cvpartner
  (:require [clj-http.client :as http]
            [cheshire.core :refer :all]
            [datomic-type-extensions.api :as d]
            [clojure.string :as str]))

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
  {:companies           (str site "/api/v1/countries")
   :users               (str site "/api/v1/users")
   :cvs                 (fn [user-id cv-id] (str/join "/" [site "api/v3/cvs" user-id cv-id]))
   :project-experiences (fn [user-id cv-id project-id] (str/join "/" [site "api/v3/cvs" user-id cv-id "project_experiences" project-id]))
   :import-json         (fn [user-id cv-id] (str/join "/" [site "api/v1/cvs" user-id cv-id "import_json"]))})

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
              (prn (str "Failed while getting url " url))
              (prn response)
              (throw (Exception. "Error"))))))

(defn- find-user-by-email [email]
  (http-get-or-nil (str (api :users) "/find?email=" email)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; cv cleanup stuff

(defn- delete-all-project-experience [user-id cv-id]
  (let [cvp-cv (http-get ((api :cvs) user-id cv-id))]
    ;(run! (partial delete-project-experience user-id cv-id) (:project_experiences cvp-cv)))
    (doseq [project (:project_experiences cvp-cv)]
      (http-delete ((api :project-experiences) user-id cv-id (:_id project))))))

(defn- cleanup-cv [cvp-user]
  "The api is so constructed that one must manually remove cv, one part at a time"
  (let [cvp-cv (http-get ((api :cvs) (:user_id cvp-user) (:default_cv_id cvp-user)))]
    (prn "Cleaning up cv for " (:email cvp-user))
    (do
      ; + many more deletes
      (delete-all-project-experience (:user_id cvp-user) (:default_cv_id cvp-user)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; cv data generation stuff

(defn- generate-tech [tech]
  {:tags {:no (:tech/name tech)}})

(defn- generate-project-experience-skills [db project]
  (let [tech-refs (:project/techs project)
        techs (map #(db-pull-by-id db (:db/id %)) tech-refs)]
    (map generate-tech techs)))


(defn- generate-project [db project]
  {:customer                  {:no (:project/customer project)}
   :description               {:no (:project/summary project)}
   :long_description          {:no (:project/description project)}
   :year_from                 (first (:project/years project))
   :year_to                   (last (:project/years project))
   :project_experience_skills (generate-project-experience-skills db project)
   :disabled                  false})


(defn- generate-cv [db person]
  {:project_experiences (map (partial generate-project db) (:person/projects person))})

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
    (prn "Creating cv for " email)
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
           (str "Failed creating cv for " email)))))

(defn export-all-cvs [db]
  (let [company-config (get-company-config)]
    (->> (find-all-persons db)
         (filter #(not (:person/quit? %)))
         (filter :person/profile-active?)
         (drop 18)                                           ; TODO testing
         (take 2)                                           ; TODO testing
         (run! (partial export-cv db company-config)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; REPL stuff
(comment
  (start)

  (ns kodemaker-no.export.cvpartner)

  ; datomic
  (def conn (d/connect "datomic:mem://kodemaker"))
  (def db (d/db conn))

  ; All person ids
  (d/q '[:find ?e :where [?e :person/full-name]] db)

  ; Need company for create user
  (http-get (api :companies))

  ; trygve local here
  (def trygve (d/pull db '[*] 17592186045673))

  ; trygve cvpartner here
  (find-user-by-email (:person/email-address trygve))
  (http-get (str/join "/" [(api :users) "5ea84086af75491055e94423"]))

  (generate-cv trygve)

  (update-cv trygve)
  (export-cv (get-company-config) 17592186045673)

  (map str (export-all-cvs db))

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ; Old stuff

  ;
  (d/q '[:find [(pull ?e [*]) ...] :where [?e :person/given-name "Trygve"]] db)
  (d/q '[:find [(pull ?e [*]) ...] :where [?e :person/email-address "trygve@kodemaker.no"]] db)
  ; NOT: (d/q '[:find [(pull ?e [*]) ...] :where [?e :id 17592186045673]] db)

  )