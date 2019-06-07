(ns knoll.cart
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [cheshire.core :refer [decode encode]] ; json encoding/decoding
            [clojure.java.io :refer [resource]]
            [org.httpkit.client :as http]
            [com.rpl.specter :refer [ALL select transform] :as spctr]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as st]
            [clojure.spec.gen.alpha :as gen]
            #_[orchestra.spec.test :as st]
            [clojure.pprint :refer [pprint print-table]]))


(defonce conn (mg/connect {:host "knlecomprd2.knoll.com" :port 27017}))
(defonce persistent-cart-db (mg/get-db conn "persistent_cart"))

(def soldToGutta (mc/find-maps persistent-cart-db "cart" {:soldToLastName "Gutta"}))
