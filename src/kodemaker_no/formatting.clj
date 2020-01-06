(ns kodemaker-no.formatting
  (:require [clojure.string :as str]
            [kodemaker-no.markup :as markup])
  (:import [com.vladsch.flexmark.ext.autolink AutolinkExtension]
           [com.vladsch.flexmark.ext.gfm.strikethrough StrikethroughExtension]
           [com.vladsch.flexmark.ext.tables TablesExtension]
           [com.vladsch.flexmark.ext.typographic TypographicExtension]
           [com.vladsch.flexmark.html HtmlRenderer]
           [com.vladsch.flexmark.parser Parser]
           [com.vladsch.flexmark.util.data MutableDataSet]))

(def flexmark-opts (-> (MutableDataSet.)
                       (.set Parser/EXTENSIONS [(AutolinkExtension/create)
                                                (StrikethroughExtension/create)
                                                (TablesExtension/create)
                                                (TypographicExtension/create)])))

(defn md-to-html [s]
  (->> (.parse (.build (Parser/builder flexmark-opts)) s)
       (.render (.build (HtmlRenderer/builder flexmark-opts)))))

(defn no-widows [s]
  "Avoid typographic widows by adding a non-breaking space between the
   last two words."
  (str/replace s #" ([^ ]{1,6})$" "&nbsp;$1"))

(defn comma-separated [coll]
  (drop 1 (interleave (into (list " og " "")
                            (repeat (dec (count coll)) ", "))
                      coll)))

(defn- consecutive? [[before after]]
  (or (= :ongoing after) (= (and (number? before) (inc before)) after)))

(defn- consecutive-years [years]
  (let [pairs (partition-all 2 1 years)
        consecutive (cons (first years)
                          (->> pairs
                               (take-while consecutive?)
                               (map second)))
        remains (->> pairs (drop-while consecutive?) flatten distinct rest)]
    (if (seq remains)
      (concat [consecutive] (consecutive-years remains))
      [consecutive])))

(defn- year-range-str [[first-year & rest]]
  (if rest
    (str first-year "-" (when-not (= :ongoing (last rest)) (last rest)))
    first-year))

(defn year-month-str [s]
  (when s
    (let [[year month] (str/split s #"-")]
      (str (format "%02d" (Integer/parseInt month)) "." year))))

(defn year-range [years]
  (str/join ", " (map year-range-str (consecutive-years years))))

(defn genitive-name [first-name]
  (str first-name
       (if (.endsWith first-name "s")
         "'"
         "s")))

(def norwegian-char-replacements
  {"æ" "e"
   "ø" "o"
   "å" "a"
   "Æ" "E"
   "Ø" "O"
   "Å" "A"})

(defn to-id-str [str]
  "Replaces all special characters with dashes, avoiding leading,
   trailing and double dashes."
  (-> (.toLowerCase str)
      (str/replace #"[æøåÆØÅ]" norwegian-char-replacements)
      (str/replace #"[^a-zA-Z0-9]+" "-")
      (str/replace #"-$" "")
      (str/replace #"^-" "")))

(defn min*
  "Like min, but takes a list - and 0 elements is okay."
  [vals]
  (when (seq vals) (apply min vals)))

(defn subs*
  "Like subs, but safe - ie, doesn't barf on too short."
  [s len]
  (if (> (count s) len)
    (subs s len)
    s))

(defn find-common-indent-column
  "Find the lowest number of spaces that all lines have as a common
   prefix. Except, don't count empty lines."
  [lines]
  (->> lines
       (remove empty?)
       (map #(count (re-find #"^ +" %)))
       (min*)))

(defn unindent-all
  "Given a block of code, if all lines are indented, this removes the
  preceeding whitespace that is common to all lines."
  [lines]
  (let [superflous-spaces (find-common-indent-column lines)]
    (map #(subs* % superflous-spaces) lines)))

(defn unindent-but-first
  "Given a block of code, if all lines are indented, this removes the
  preceeding whitespace that is common to all lines."
  [lines]
  (let [superflous-spaces (find-common-indent-column (drop 1 lines))]
    (concat (take 1 lines)
            (map #(subs* % superflous-spaces) (drop 1 lines)))))

(defn to-html [s]
  (if (string? s)
    (md-to-html (->> s
                     str/split-lines
                     unindent-but-first
                     (str/join "\n")))
    s))

(defn markdown [s]
  [:div.text (to-html s)])

(defn link-to-person [person]
  [:a {:href (:url person)} (:name person)])

(defn render-tech-bubble
  ([tech by]
   (when-not (empty? tech)
     [:p.near.cookie-w
      [:span.cookie
       (comma-separated (map link-to-person by)) " om "
       (comma-separated (map markup/link-if-url tech))]]))
  ([tech]
   (when-not (empty? tech)
     [:p.near.cookie-w
      [:span.cookie
       (comma-separated (map markup/link-if-url tech))]])))
