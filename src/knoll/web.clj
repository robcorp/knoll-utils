(ns knoll.web
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [itsy.core :refer [crawl extract-all]]
            [cemerick.url :refer [url]]))

(defn url-path->filename [url-str]
  (let [url-path (-> url-str url :path)
        name (.. url-path
            (replaceFirst "/" "")
            (replace "/" "-"))]
    (str name ".txt")))

(def url-count (atom 0))

(defn find-0fe8-handler [{:keys [url body]}]
  (swap! url-count inc)
  (when (zero? (mod @url-count 50))
    (reset! url-count 0)
    (println))
  (if (.contains body "0fe8")
    (let [dir "/Users/robcorp/temp/"
          file (url-path->filename url)]
      (spit (str dir file) body)
      (println)
      (println url file))
    (print "."))
  (Thread/sleep 500))

(defn print-url-handler [{:keys [url body]}]
  (println url)
  (Thread/sleep 500))

(defn knoll-dot-com-filter [url-set]
  (->> url-set
       (remove #(or (.contains (.toLowerCase %) "javascript:")
                    (.contains % "mailto:")
                    (.contains % "tel:")
                    (.contains % "/css/")
                    (.contains % "/story/")
                    (.contains % "/document/")
                    (.contains % "/nkdc/")
                    (.contains % "/pdf.js/")
                    #_(.contains % "knolltextileproductdetail")))
       #_(filter #(or (.startsWith % "https://www.knoll.com/shop/")
                    (.startsWith % "https://www.knoll.com/discover-knoll/")
                    (.startsWith % "https://www.knoll.com/product/")
                    (.startsWith % "https://www.knoll.com/knollnewsdetail/")))
       set))

(defn base-extractor
  [original-url body]
  (when body
    (let [candidates1 (->> (re-seq #"href=\"([^\"]+)\"" body)
                           (map second)
                           (remove #(or (= % "/")
                                        (.startsWith % "#")))
                           set)
          candidates2 (->> (re-seq #"href='([^']+)'" body)
                           (map second)
                           (remove #(or (= % "/")
                                        (.startsWith % "#")))
                           set)
          url-regex #"https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]*[-A-Za-z0-9+&@#/%=~_|]"
          candidates3 (re-seq url-regex body)
          all-candidates (set
                          (map #(str/replace % #"\s" "")
                               (concat candidates1
                                       candidates2
                                       candidates3)))
          fq (set (filter #(.startsWith % "http")
                          all-candidates))
          ufq (set/difference all-candidates fq)
          fq-ufq (map #(str (url original-url %)) ufq)
          all (set (concat fq fq-ufq))]

      all)))

(defn make-custom-extractor [filter-fn]
  (fn [original-url body]
    (filter-fn (base-extractor original-url body))))

(defn remaining-urls [c]
  (-> c :state :url-queue count))

(def crawl-opts
  (atom {
         :url "https://www.knoll.com/"
         :handler #'print-url-handler #_#'find-0fe8-handler
         :workers 5
         :url-limit 1000
         :url-extractor (make-custom-extractor knoll-dot-com-filter)
         :http-opts {}
         :host-limit true
         :polite? true}))

(def crawler (crawl @crawl-opts))

