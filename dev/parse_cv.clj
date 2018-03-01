(ns parse-cv
  (:require [clojure.string :as str]
            [hiccup-find.core :as hf]
            [kodemaker-no.content :as content]
            [pl.danieljanus.tagsoup :as tags]))

(defn get-techs []
  (let [content (content/load-content)]
    (->> (concat (map (fn [{:keys [id name]}]
                        [name id]) (vals (:tech content)))
                 (map (fn [[tech name]]
                        [name tech]) (:tech-names content)))
         (into {}))))

(defonce techs (get-techs))

(defn html-str [markup selector]
  (->> markup
       (hf/hiccup-find selector)
       (hf/hiccup-string)))

(defn find-section [markup header]
  (->> (hf/hiccup-find [:section] markup)
       (filter #(re-find header
                         (-> (hf/hiccup-find [:header] %)
                             first
                             hf/hiccup-string
                             .toLowerCase)))
       first))

(def label->key
  {"Født" :born
   "Sivil status" :relationship-status
   "Utdanning" :education-summary
   "Erfaring" :experience-since})

(defn parse-year [s]
  (if (re-find #"\d+\.\d+" s)
    (Integer/parseInt (re-find #"\d+" (second (str/split s #"\."))))
    (Integer/parseInt (re-find #"\d+" s))))

(defn parse-personals [dtdd]
  (->> (partition 2 dtdd)
       (map (fn [[dt dd]]
              (let [label (label->key (hf/hiccup-string dt))
                    value (hf/hiccup-string dd)]
                [label (if (#{:experience-since :born} label)
                         (parse-year value)
                         value)])))
       (into {})))

(defn parse-period [period]
  (cond
    (re-find #"\d+\s*-\s*$" period) [(parse-year period) :ongoing]
    (re-find #"\d+\s*-\s*\d+$" period) (let [numbers (map parse-year (str/split period #"-"))]
                                         (into [] (range (first numbers) (inc (last numbers)))))
    :default [(parse-year period)]))

(defn parse-project [[customer period raw-desc]]
  (let [[description raw-tech] (str/split (:text raw-desc) #"Teknologi:")]
    {:customer (:text customer)
     :years (parse-period (:text period))
     :description (.trim description)
     :tech (when raw-tech
             (->> (str/split raw-tech #",")
                  (map #(.trim %))
                  (mapv #(or (techs %) (-> (.trim %)
                                           .toLowerCase
                                           (str/replace #" " "-")
                                           (str/replace #"[\(\)]" "")
                                           keyword)))))}))

(defn parse-col [td]
  (if-let [link (hf/hiccup-find [:a] td)]
    {:url (-> link first second :href)
     :link-text (hf/hiccup-string link)
     :text (hf/hiccup-string td)}
    {:text (hf/hiccup-string td)}))

(defn parse-tbody-rows [root]
  (->> root
       (hf/hiccup-find [:tbody :tr])
       (filter #(= 3 (count (hf/hiccup-find [:td] %))))
       (map #(map parse-col (hf/hiccup-find [:td] %)))))

(defn parse-open-source-projects [section]
  (if-let [lis (seq (hf/hiccup-find [:li] section))]
    (->> (hf/hiccup-find [:li] section lis)
         (filter #(re-find #"^Utviklet" (hf/hiccup-string %)))
         (mapv #(let [[_ _ _ link description] %]
                  {:url (:href (second link))
                   :name (nth link 2)
                   :description (str/replace description #"\s*med [^\.]+\.\s*" "")})))
    (->> (parse-tbody-rows section)
         (map (fn [[project year description]]
                {:url (:url project)
                 :name (:link-text project)
                 :description (:text description)}))
         (filter #(not (empty? (:name %)))))))

(defn parse-education [[institution period subject]]
  {:institution (:text institution)
   :years (parse-period (:text period))
   :subject (:text subject)})

(defn parse-endorsements [section]
  (mapv (fn [author quote]
          {:author author
           :quote (-> quote
                      (str/replace #"^\s*\"|\"\s*$" "")
                      .trim)})
        (map hf/hiccup-string (hf/hiccup-find [:h3] section))
        (map hf/hiccup-string (hf/hiccup-find [:blockquote] section))))

(defn qualifications-section [markup]
  (or (seq (hf/hiccup-find [:#kvalifikasjoner] markup))
      (find-section markup #"kvalifikasjoner")))

(defn parse-presentation [[title event year]]
  {:title (:text title)
   :event (:text event)
   :urls {:video (or (:url title) (:url event))}
   :date (:text year)})

(defn parse-cv [markup]
  (let [oss-section (find-section markup #"open source")]
    (merge
     {:name (str/split (html-str markup [:#name]) #" ")
      :title (html-str markup [:#title])
      :phone-number (html-str markup [:#phone])
      :email-address (html-str markup [:#mail])
      :qualifications (->> (qualifications-section markup)
                           (hf/hiccup-find [:li])
                           (mapv hf/hiccup-string))
      :projects (->> (find-section markup #"^prosjekter$")
                     parse-tbody-rows
                     (mapv parse-project))
      :open-source-projects (parse-open-source-projects oss-section)
      :education (->> (find-section markup #"^utdannelse$")
                      parse-tbody-rows
                      (mapv parse-education))
      :presentations (->> (find-section markup #"foredrag")
                          parse-tbody-rows
                          (mapv parse-presentation))
      :endorsements (parse-endorsements (find-section markup #"^anbefalinger$"))}
     (parse-personals (->> (hf/hiccup-find [:#personal :dl] markup)
                           first
                           (drop 2))))))

(defn fetch-and-parse [id]
  (->> (format "https://www.kodemaker.no/cv/%s/" id)
       tags/parse
       parse-cv))

(defn dump-cv [id]
  (with-open [w (clojure.java.io/writer (format "resources/cv-dump/%s.edn" id))]
    (binding [*out* w]
      (clojure.pprint/write (fetch-and-parse id)))))
