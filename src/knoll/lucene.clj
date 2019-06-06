(ns knoll.lucene
  (:require [cheshire.core :refer [decode encode]]
            [clojure.java.io :refer [resource]]
            #_[org.httpkit.client :as http]
            [clj-http.client :as http]
            #_[clucy.core :as clucy]))

#_(def index (clucy/disk-index ""))
