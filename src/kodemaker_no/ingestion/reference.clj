(ns kodemaker-no.ingestion.reference
  (:require [clojure.string :as str]
            [kodemaker-no.homeless :as h])
  (:import java.time.LocalDate))

(def reference-keys
  {:reference/page-title :page-title
   :reference/image :img
   :reference/portrait :portrait
   :reference/half-circle-portrait :half-circle-portrait
   :reference/signee-name :name
   :reference/signee-phone :phone
   :reference/signee-title :title
   :reference/logo :logo
   :reference/company :company
   :reference/href :href
   :reference/techs :techs
   :reference/tech-list :techs
   :reference/quote :quote
   :reference/priority :priority
   :reference/blurb :body})

(defn- build-date [y m]
  (LocalDate/of (Integer/parseInt y) (Integer/parseInt m) 1))

(defn extract-scope [section]
  (let [[_ hours from-month from-year to-month to-year]
        (re-find #"(?:(\d+) timer / )?(\d\d)\.(\d\d\d\d)-(\d\d)\.(\d\d\d\d)" (:factoid-2 section))]
    (cond-> {}
      (and from-year from-month) (assoc :reference/project-start (build-date from-year from-month))
      (and to-year to-month) (assoc :reference/project-end (build-date to-year to-month))
      hours (assoc :reference/project-hours (Integer/parseInt hours)))))

(defn extract-team [section]
  (letfn [(add-member [team member]
            (if member
              (conj team (update member :project-participation/role str/trim))
              team))]
    (loop [[line & lines] (str/split (:content section) #"\n")
           member nil
           team []]
      (cond
        (nil? line) (add-member team member)

        (re-find #"^[^\s\.]+$" line)
        (recur
         lines
         {:project-participation/person {:db/ident (keyword "person" line)}}
         (add-member team member))

        :default
        (recur lines (if member
                       (update member :project-participation/role str "\n" line)
                       member) team)))))

(defn extract-grid [{:keys [content]}]
  (->> (str/split content #"\n")
       (remove empty?)
       (map-indexed
        #(let [[url image size] (str/split %2 #"\s+")]
           {:block/idx %1
            :block/url url
            :block/image image
            :block/size (if-let [[_ s] (re-find #"(\d+)x" (or size ""))]
                          (Integer/parseInt s)
                          1)}))))

(defn extract-section-data [inputs]
  (loop [sections []
         reference {}
         [input & inputs] (map #(update % :type keyword) inputs)]
    (if (nil? input)
      (assoc reference :reference/sections (vec (map-indexed #(assoc %2 :idx %1) sections)))
      (case (:type input)
        :reference
        (recur sections
               (-> input
                   (h/update-in-existing [:techs] read-string)
                   (h/keep-vals reference-keys)
                   (h/update-in-existing [:reference/techs] h/prep-techs)
                   (h/update-in-existing [:reference/tech-list] h/prep-tech-list))
               inputs)

        :reference-meta
        (recur (conj sections (dissoc input :team-size :factoid-1 :factoid-2))
               (merge reference (extract-scope input))
               inputs)

        :grid
        (recur (conj sections input) (assoc reference :reference/grid-blocks (extract-grid input)) inputs)

        :participants
        (recur (conj sections (dissoc input :content))
               (assoc reference :reference/team (extract-team input))
               inputs)

        (recur (conj sections input) reference inputs)))))

(defn create-tx [file-name reference-sections]
  [(-> (extract-section-data reference-sections)
       (assoc :page/uri (str "/referanser" (second (re-find #"references(.*).md" file-name)) "/"))
       (assoc :page/kind :page.kind/reference))])

(comment
  (let [file-name "references/oche-dart.md"]
    (create-tx file-name (h/slurp-mapdown-resource file-name)))

  (extract-scope {:factoid-2 "950 timer / 02.2014-08.2014"})
  (extract-scope {:factoid-2 "01.2017-09.2017"})

  (extract-team "

alf-kristian

Alf Kristian hadde opprinnelig hovedansvar for backend og server-rigg, men fokuset
ble etterhvert byttet om til mer frontend utvikling. Før dette så ikke Alf Kristian
på seg selv som noen \"frontend-fyr\", men dette var så moro at han gladelig jobber
med frontend i dag.

eivind

Eivind var fullstack-utvikler med fokus på frontend. Han jobbet mye med integrasjon
mot meglersystem samt adressesøk og prisestimat. I tillegg ble det mye flikking på
CSS og JavaScript for å få design og integrasjon til å henge perfekt sammen :)

")

)
