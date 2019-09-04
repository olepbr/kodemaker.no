(ns kodemaker-no.images
  (:require [fivetonine.collage.util :as util]
            [fivetonine.collage.core :as collage]
            [optimus.digest :as digest]
            [optimus.paths :refer [filename-ext just-the-path just-the-filename]]
            [optimus.assets.creation :refer [last-modified]]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]))

(def tmp-path (System/getProperty "java.io.tmpdir"))

(defn- create-folders [path]
  (.mkdirs (.getParentFile (io/file path))))

(defn cache-path [config spec]
  (str tmp-path
       (digest/sha-1 (pr-str (if (:cacheable-urls? config)
                               (update spec :resource slurp)
                               (update spec :resource #(.getPath %)))))
       "."
       (name (:ext spec))))

(defn crop-image [image options]
  (if (= options :square)
    (let [w (.getWidth image)
          h (.getHeight image)
          min-dimension (min w h)
          cropped-offset (- (/ (max w h) 2) (/ min-dimension 2))]
      (recur image {:offset (if (> w h) [cropped-offset 0] [0 cropped-offset]) :size [min-dimension min-dimension]}))
    (collage/crop image
                  (-> options :offset first)
                  (-> options :offset second)
                  (-> options :size first)
                  (-> options :size second))))

(defn prepare-jpg-for-retina [size]
  (cond-> size
    (:width size) (update :width * 2)
    (:height size) (update :height * 2)
    (< 200 (:width size)) (assoc :progressive true)
    :always (assoc :quality 0.3)))

(defn transform-image [{:keys [resource size style cache-path ext]}]
  (create-folders cache-path)
  (let [size (if (= :jpg ext) (prepare-jpg-for-retina size) size)]
    (-> (util/load-image resource)
        (cond->
            (:crop style) (crop-image (:crop style))
            (:scale size) (collage/scale (:scale size))
            (or (:width size) (:height size)) (collage/resize :width (:width size) :height (:height size))
            (:grayscale style) (collage/grayscale (:grayscale style))
            (:duotone style) (collage/duotone (:from (:duotone style)) (:to (:duotone style)))
            (:circle style) collage/circle
            (:triangle style) (collage/triangle (:triangle style))
            (:rotate style) (collage/rotate (:rotate style))
            (= :jpg ext) (util/save cache-path
                                    :quality (get size :quality 1)
                                    :progressive (get size :progressive false))
            (= :png ext) (util/save cache-path)))))

(defn get-ext [file styles]
  (if (or (contains? styles :circle)
          (contains? styles :triangle))
    "png"
    (last (re-find #"\.([^\.]+)$" file))))

(defn content-hash [file size style {:keys [sizes styles cacheable-urls? resource-path]}]
  (digest/sha-1
   (str (pr-str (get sizes size))
        (pr-str (get styles style))
        (if cacheable-urls?
          (slurp (io/resource (str resource-path "/" file)))
          (str resource-path "/" file)))))

(defn url-to [config file size style]
  (format "/%s/%s/%s/%s/%s.%s"
          (:prefix config)
          (name size)
          (name style)
          (content-hash file size style config)
          (second (re-find #"(.+)\.[^\.]+$" file))
          (get-ext file (get (:styles config) style))))

(defn realize-url [config url]
  (let [[_ size style file] (re-find #"/([^/]+)/([^/]+)/(.+)$" url)]
    (url-to config file (keyword size) (keyword style))))

(def path-re #"/([^\/]+)/([^\/]+)/([^\/]+)/([^\/]+)/(.+)\.([^\/]+)")

(defn image-spec [url]
  (let [[_ _ size style _ filename ext] (re-find path-re url)]
    {:size (keyword size)
     :style (keyword style)
     :filename filename
     :ext (keyword ext)
     :url url}))

(defn inflate-spec [{:keys [size style filename ext url]} {:keys [sizes styles resource-path] :as config}]
  (when (nil? (get sizes size))
    (throw (Exception. (format "Unknown size class \"%s\" in URL \"%s\", use one of %s" size url (pr-str sizes)))))
  (when (nil? (get styles style))
    (throw (Exception. (format "Unknown style class \"%s\" in URL \"%s\", use one of %s" style url (pr-str styles)))))
  (when-not (contains? #{:png :jpg} ext)
    (throw (Exception. (format "Unknown extension \"%s\" in URL \"%s\", use png or jpg" ext url))))
  (let [path (str resource-path "/" filename)
        jpg-file (io/resource (str path ".jpg"))
        png-file (io/resource (str path ".png"))]
    (when (and jpg-file png-file)
      (throw (Exception. (format "Found both %s.jpg and %s.png, unable to select input. Please make sure there is only one file under this name" path path))))
    (when (and (nil? jpg-file) (nil? png-file))
      (throw (Exception. (format "Found neither %s.jpg nor %s.png, unable to select input." path path))))
    (let [spec {:size (get sizes size)
                :style (get styles style)
                :ext ext
                :resource (or jpg-file png-file)}]
      (assoc spec :cache-path (cache-path config spec)))))

(defn cached? [spec]
  (-> (:cache-path spec)
      io/file
      .exists))

(defn serve-image [req opt]
  (let [spec (inflate-spec (image-spec (:uri req)) opt)]
    (when-not (cached? spec)
      (transform-image spec))
    {:status 200
     :body (io/file (:cache-path spec))}))

(defn image-req? [req {:keys [prefix]}]
  (and (= :get (:request-method req))
       (= prefix (second (re-find path-re (:uri req))))))

(defn wrap-images [handler & [opt]]
  (fn [req]
    (if (image-req? req opt)
      (serve-image req opt)
      (handler req))))

(comment
  (def image-asset-config
    {:prefix "image-assets"
     :styles {:round {:circle {}
                      :duotone {:from [0 0 0]
                                :to [120 180 255]}}}
     :sizes {:small {:w 100 :h 75}}
     :resource-path "public"})

  (.getPath (io/resource "public/favicon.ico"))

  (def url "/image-assets/small/round/fc1fcf02a8ba26f296a98079c105ed6bbb69f3b2/photos/blog/christian-johansen.png")

  (digest/sha-1 "/image-assets/small/round/fc1fcf02a8ba26f296a98079c105ed6bbb69f3b2/photos/blog/christian-johansen.png")

  (image-req? {:request-method :get :uri url} image-asset-config)
  (serve-image {:uri url} image-asset-config)

  (-> (inflate-spec (image-spec url) image-asset-config)
      temp-path
      io/file
      .exists)

  {:size {:w 100, :h 75},
   :style {:circle {}, :duotone {:from [0 0 0], :to [120 180 255]}},
   :ext :png,
   :resource "..."}

  )
