(ns kodemaker-no.date
  (:require
   [clj-time.format :refer [parse formatters unparse formatter]]
   [clj-time.coerce :refer [to-local-date to-date-time]]))

(defn parse-ymd "Ensure consistent parsing of dates" [date-str]
  (to-local-date (parse (formatters :year-month-day) date-str)))

(defn format-dmy "Ensure consistent formatting of dates" [date]
  (unparse (formatter "dd.MM.yyyy") (to-date-time date)))
