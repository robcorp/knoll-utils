(ns knoll.db
  (:require [clojure.string :as str]
            [korma.core
             :refer [defentity fields group order raw select set-fields
                     subselect update where]
             :rename {update upd}
             :as k]
            [korma.db
             :refer [default-connection defdb create-db]
             :as kd]
            #_[org.httpkit.client :as http]
            [clj-http.client :as http]
            [com.rpl.specter :as spctr]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as st]
            #_[orchestra.spec.test :as st]))


(s/def ::env-key #{::dev2 ::staging ::prod})

(defonce ^:private common-db-spec {:classname "oracle.jdbc.driver.OracleDriver"
                                   :subprotocol "oracle"
                                   :naming {:keys str/upper-case
                                            :fields str/upper-case}
                                   :make-pool? true})
            
(defonce config {::dev2 {::url "http://knldev2wcsapp1a.knoll.com:7003"
                         ::db (create-db (merge common-db-spec {:subname "thin:@knldev2wcsdb1.knoll.com:1521:WCSDEV2"
                                                                :user "ORAWCDEV11"
                                                                :password "ORAWCDEV11"}))}
                 ::staging {::url "http://knlprdwcsmgt1.knoll.com:7003"
                            ::db (create-db (merge common-db-spec {:subname "thin:@knlprdwcsdb.knoll.com:1521:WCSPRDG"
                                                                   :user "ORAPRDWCSMGT2"
                                                                   :password "knolloraprdwcsmgt2"}))}
                 ::prod {::url "http://knlprdwcsapp1.knoll.com:8080"
                         ::db (create-db (merge common-db-spec {:subname "thin:@knlprdwcsdb.knoll.com:1521:WCSPRDG"
                                                                :user "ORAWCDLV"
                                                                :password "knollorawcdlv"}))}})

(defn connect [env]
  #_{:pre [(s/valid? ::env-key env)]}
  (default-connection (-> config env ::db)))

(s/fdef connect
  :args (s/cat :env-keyword ::env-key))

(connect ::staging)

(defn limitn [n]
  (raw (str "ROWNUM <= " n)))

(defn count* []
  (raw "count(*) as COUNT"))

(defentity systemusers)
(defentity product_c
  #_(entity-fields :id :name :description :subtype))

(defentity product_a
  #_(entity-fields :id :name :description :attributetype :subtype))

(defentity product_c_mungo)
(defentity knoll_news_c)

;;(select systemusers)
;;(select systemusers (fields :id :username))
;;(select systemusers (where {:username "rhouser"}))
;;(select systemusers (where (raw "ROWNUM = 1")))
;;(select systemusers (fields (raw "count(*) as COUNT")))

(defentity systemevents)

(defn show-customindexevents [env]
  (connect env)
  (select systemevents
          (where {:eventname [in ["CustomIndexEvent" "CustomTextileIndexEvent"]]})))

(defentity knollluceneindexjobqueue)

(defn show-index-queue [env]
  (connect env)
  (select knollluceneindexjobqueue
          (fields :assettype (raw "count(*) as COUNT"))
          (where {:index_status [in ["added" "updated" "update_failed"]]})
          (group :assettype)
          (order :assettype)))

(s/def ::index-q-record (s/keys :req-un [::ASSETTYPE ::COUNT]))
(s/def ::ASSETTYPE string?)
(s/def ::COUNT #(instance? java.math.BigDecimal %))
(s/def ::index-q-records (s/coll-of ::index-q-record))

(s/fdef show-index-queue
  :args (s/cat :env ::env-key)
  :ret ::index-q-records)


(defn enable-customindexevent [env flag]
  (connect env)
  (upd systemevents
          (set-fields {:enabled (if flag 1M 0M)})
          (where {:eventname "CustomIndexEvent"}))
  (show-customindexevents env))

(defn reindex-now [env]
  (connect env)
  (let [custom-index-event (first (select systemevents (where {:eventname "CustomIndexEvent"})))
        url (str (-> config env ::url) "/cs/" (:TARGET custom-index-event) "?" (:PARAMS custom-index-event))]
    (future (http/get url))))

(defn get-knolltextile-for-fabricid [env fabid]
  (connect env)
  (select product_c
          (where {:id [in (subselect product_c_mungo
                                     (fields :cs_ownerid)
                                     (where (and
                                             {:cs_attrid 1334871353607M}
                                             {:stringvalue fabid})))]})))

