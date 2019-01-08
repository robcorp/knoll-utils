(ns knoll.essentialsfinishes
  (:require [cheshire.core :refer [decode encode]] ; json encoding/decoding
            [clojure.java.io :refer [resource]]
            [org.httpkit.client :as http]
            [com.rpl.specter :refer [ALL select transform] :as spctr] 
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as st]
            [clojure.spec.gen.alpha :as gen]
            #_[orchestra.spec.test :as st]
            [clojure.pprint :refer [pprint print-table]]))


(s/def ::ProductTypeFamily #{"Seating" "Tables" "Storage" "Power & Data" "Work Tools & Accessories" "Screens & Communication Boards"})
(s/def ::ProductType )
