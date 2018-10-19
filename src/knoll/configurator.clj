(ns knoll.configurator
  (:require [cheshire.core :refer [decode]]
            [clojure.java.io :refer [resource]]
            [org.httpkit.client :as http]
            [com.rpl.specter :as spctr :refer [select ALL MAP-KEYS]]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as st]
            [clojure.spec.gen.alpha :as gen]
            ))

;; Configurator Specs

(s/def ::configurator (s/keys :req-un [::catalog ::partNumber ::name ::description :configurator/price :configurator/listPrice
                                       ::width ::height ::depth ::cartonWeight ::cartonCount
                                       ::shippingLocation ::perUserQuantity :configurator/quickShip
                                       ::groups ::preconfigurations
                                       ::salePrice ::saleStartDate ::saleEndDate
                                       ::endsOn ::toutMessage]))
(s/def ::catalog string?)
(s/def ::partNumber string?)
(s/def ::name string?)
(s/def ::description (s/nilable string?))
(s/def :configurator/price double?)
(s/def :configurator/listPrice (s/nilable (s/double-in :min 0.0)))
(s/def ::width (s/double-in :min 0.0))
(s/def ::height (s/double-in :min 0.0))
(s/def ::depth (s/double-in :min 0.0))
(s/def ::cartonWeight (s/double-in :min 0.0))
(s/def ::cartonCount nat-int?)
(s/def ::shippingLocation string?)
(s/def ::perUserQuantity nat-int?)
(s/def :configurator/quickShip nat-int?)
(s/def ::preconfigurations (s/coll-of string?))
(s/def ::salePrice (s/nilable (s/double-in :min 0.0)))
(s/def ::saleStartDate (s/nilable string?))
(s/def ::saleEndDate (s/nilable string?))
(s/def ::endsOn (s/nilable string?))
(s/def ::group (s/keys :req-un [::sequence ::subGroup ::code ::optionGroupCode
                                :group/description ::historicalCutoff ::historicalSequence
                                ::displayOption ::choiceType ::tooltip ::choices]))
(s/def ::sequence (fn [x] (>= x 1)))
(s/def ::code string?)
(s/def :group/description string?)
(s/def ::optionGroupCode string?)
(s/def ::choiceType (s/nilable string?))
(s/def ::displayOpion #{"N" "Y"})
(s/def ::groups (s/coll-of ::group))
(s/def ::choice (s/keys :req-un [::number
                                 :choice/price
                                 :choice/listPrice
                                 :choice/description
                                 ::configurationNote
                                 ::subOptionGroupCode
                                 :choice/quickShip
                                 ::parentGroup
                                 ::parentChoice
                                 ::alt
                                 ::sampleCode
                                 ::sampleName
                                 ::displayOption
                                 ::defaultSelected
                                 ::optionGroupCode
                                 ::salePrice
                                 ::saleStartDate
                                 ::saleEndDate
                                 ::endsOn
                                 ]))
(s/def :choice/listPrice double?)
(s/def :choice/quickShip string?)
(s/def ::choices (s/coll-of ::choice))

;;;; Confgurator defs and miscellaneous utils

;; Read in the list of product names and partnumbers
(def product-partnums (-> (resource "basic-conf-product-names-and-partnumbers.json")
                          slurp
                          (decode true)))

;; get the list of all partnumbers
(def partnums (select [:RECORDS ALL :PARTNUMBER] product-partnums))

#_(def configurator-service-url "//knldev2wcsapp1a.knoll.com/configurator/parts/")
#_(def configurator-service-url "https://knlprdwcsmgt.knoll.com/configurator/parts/")
(def configurator-service-url "https://www.knoll.com/configurator/parts/")

(defn get-configurator-for [part]
  (decode
   (:body @(http/get (str configurator-service-url part)))
   true))

(def barcelona-stool (get-configurator-for "251Y"))
(def generation (get-configurator-for "111"))
(def pixel-console (get-configurator-for "KS21RG"))
(def regeneration-fully-upholstered (get-configurator-for "442"))
(def regeneration-by-knoll (get-configurator-for "441"))
(def regeneration-high-task (get-configurator-for "441H"))
(def platner-arm-chair (get-configurator-for "1725A"))
(def barcelona-couch (get-configurator-for "258LS"))

(def product-configs
  (list barcelona-stool
        generation
        pixel-console
        regeneration-fully-upholstered
        regeneration-by-knoll
        regeneration-high-task
        platner-arm-chair
        barcelona-couch))

; validate Configurator data for known products
#_(map #(s/valid? ::Configurator %) product-configs)

;; take 10 random partnumber, get their configurator data and validate each one against the spec
(->> partnums
     shuffle
     (take 10)
     (map get-configurator-for)
     (map (fn [c] [(:partNumber c) (s/valid? ::configurator c)]))
     (filter (fn [r] (false? (second r)))))
