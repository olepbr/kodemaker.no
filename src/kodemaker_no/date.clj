(ns kodemaker-no.date
  (:require [clj-time.coerce :refer [to-local-date to-date-time]]
            [clj-time.core :as time]
            [clj-time.format :refer [parse formatters unparse formatter]]
            [clojure.string :as str]))

(defn parse-ymd "Ensure consistent parsing of dates" [date-str]
  (to-local-date (parse (formatters :year-month-day) date-str)))

(defn format-dmy "Ensure consistent formatting of dates" [date]
  (unparse (formatter "dd.MM.yyyy") (to-date-time date)))

(def months
  {"may" "mai"
   "oct" "okt"
   "dec" "des"})

(defn clever-date [date relative-to]
  (let [days (time/in-days (time/interval (to-date-time relative-to) (to-date-time date)))]
    (case days
      0 "I dag"
      1 "I morgen"
      (let [datetime (to-date-time date)
            day (unparse (formatter "d.") datetime)
            month-str (str/lower-case (unparse (formatter "MMM") datetime))]
        (str day " " (or (months month-str) month-str)))))) ; Should really use Locale...

(defn within? [from until date]
  (and (or (= from date) (time/after? date from))
       (or (= until date) (time/before? date until))))

(defn in-weeks [date weeks]
  (time/plus date (time/weeks weeks)))
