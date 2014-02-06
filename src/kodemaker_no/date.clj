(ns kodemaker-no.date
  (:require [clj-time.core :as time]
            [clj-time.format :refer [parse formatters unparse formatter]]
            [clj-time.coerce :refer [to-local-date to-date-time]]
            [clojure.string :as str]))

(defn parse-ymd "Ensure consistent parsing of dates" [date-str]
  (to-local-date (parse (formatters :year-month-day) date-str)))

(defn format-dmy "Ensure consistent formatting of dates" [date]
  (unparse (formatter "dd.MM.yyyy") (to-date-time date)))

(defn clever-date [date relative-to]
  (let [days (time/in-days (time/interval (to-date-time relative-to) (to-date-time date)))]
    (case days
      0 "I dag"
      1 "I morgen"
      (str/replace (str/lower-case (unparse (formatter "d. MMM") (to-date-time date))) "c" "s")))) ; Should really use Locale...

(defn within? [from until date]
  (and (or (= from date) (time/after? date from))
       (or (= until date) (time/before? date until))))

(defn in-weeks [date weeks]
  (time/plus date (time/weeks weeks)))
