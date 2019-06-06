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

(s/def ::LeadTimeOption #{{:std "Standard Ship"}
                          {:quick "Quick Ship"}
                          {:three-week "3 Week Ship"}})
(s/def ::LeadTimeOptions (s/coll-of ::LeadTimeOption :distinct true :min-count 1 :max-count 3))
(s/def ::ProductTypeFamily #{"Seating" "Tables" "Storage" "Power & Data" "Work Tools & Accessories" "Screens & Communications Boards"})
(s/def ::ProductName string?)
(s/def ::ELFProduct (s/keys :req [::ProductName]))
(s/def ::ELFProducts (s/coll-of ::ELFProduct))

#_(pprint (gen/sample (s/gen ::LeadTimeOptions)))

