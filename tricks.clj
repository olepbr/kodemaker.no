;; Find info about images

(defn find-info [file]
  (let [img (img/load-image file)]
    [(.getPath file) (.floatValue (/ (.getWidth img) (.getHeight img)))]))

(defn w-n-h [file]
  (let [img (img/load-image file)]
    [(.getWidth img) (.getHeight img)]))

(->> (io/as-file "resources/public/photos")
     .listFiles seq
     (filter #(.isDirectory %))
     (mapcat #(.listFiles %))
     (filter #(re-find #"\.jpg$" (.getName %)))
     (filter #(= "half-figure.jpg" (.getName %)))
     (map w-n-h)
     (frequencies))

(.floatValue (/ 4096 5236)) ; 0.7822766
