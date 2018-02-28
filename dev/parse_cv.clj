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
                [label (if (= label :experience-since)
                         (parse-year value)
                         value)])))
       (into {})))

(defn parse-period [period]
  (cond
    (re-find #"\d+\s*-\s*$" period) [(parse-year period) :ongoing]
    (re-find #"\d+\s*-\s*\d+$" period) (let [numbers (map parse-year (str/split period #"-"))]
                                         (range (first numbers) (inc (last numbers))))
    :default [(parse-year period)]))

(defn parse-project [raw]
  (let [[customer period raw-desc] (map hf/hiccup-string (hf/hiccup-find [:td] raw))
        [description raw-tech] (str/split raw-desc #"Teknologi:")]
    {:customer customer
     :years (parse-period period)
     :description (.trim description)
     :tech (when raw-tech
             (->> (str/split raw-tech #",")
                  (map #(.trim %))
                  (mapv #(or (techs %) (-> (.trim %)
                                           .toLowerCase
                                           (str/replace #" " "-")
                                           keyword)))))}))

(defn parse-open-source-projects [section]
  (->> (hf/hiccup-find [:li] section)
       (filter #(re-find #"^Utviklet" (hf/hiccup-string %)))
       (mapv #(let [[_ _ _ link description] %]
                {:url (:href (second link))
                 :name (nth link 2)
                 :description (str/replace description #"\s*med [^\.]+\.\s*" "")}))))

(defn parse-education [raw]
  (let [[institution period subject] (map hf/hiccup-string (hf/hiccup-find [:td] raw))]
    {:institution institution
     :years (parse-period period)
     :subject subject}))

(defn parse-endorsements [section]
  (mapv (fn [author quote]
          {:author author
           :quote (-> quote
                      (str/replace #"^\s*\"|\"\s*$" "")
                      .trim)})
        (map hf/hiccup-string (hf/hiccup-find [:h3] section))
        (map hf/hiccup-string (hf/hiccup-find [:blockquote] section))))

(defn ensure-cols [n rows]
  (filter #(= n (count (hf/hiccup-find [:td] %))) rows))

(defn parse-cv [markup]
  (let [oss-section (find-section markup #"open source")]
    (merge
     {:name (str/split (html-str markup [:#name]) #" ")
      :title (html-str markup [:#title])
      :phone-number (html-str markup [:#phone])
      :email-address (html-str markup [:#mail])
      :qualifications (->> markup
                           (hf/hiccup-find [:#kvalifikasjoner])
                           (hf/hiccup-find [:li])
                           (mapv hf/hiccup-string))
      :projects (->> (find-section markup #"^prosjekter$")
                     (hf/hiccup-find [:.projects.details :tbody :tr])
                     (ensure-cols 3)
                     (mapv parse-project))
      :open-source-projects (parse-open-source-projects oss-section)
      :education (->> (find-section markup #"^utdannelse$")
                      (hf/hiccup-find [:.projects.details :tbody :tr])
                      (ensure-cols 3)
                      (mapv parse-education))
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
