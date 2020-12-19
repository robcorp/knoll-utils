(ns knoll.db
  (:require [clojure.string :as str]
            [korma.core :as k
             :refer [defentity fields group order raw select set-fields
                     subselect update where]
             :rename {update upd}]
            [korma.db :as kd
             :refer [default-connection create-db]]
            #_[org.httpkit.client :as http]
            [clj-http.client :as http]
            #_[com.rpl.specter :as spctr]
            [clojure.spec.alpha :as s]
            #_[clojure.spec.test.alpha :as st]
            #_[orchestra.spec.test :as st]
            [clojure.pprint :refer [print-table]]
            [clojure.core.async :as async :refer [chan >! <! go]]
            [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection])
  (:import (com.mchange.v2.c3p0 ComboPooledDataSource PooledDataSource)))


(s/def ::env-key #{::dev2 ::staging ::prod})


(comment
  "jdbc:as400://KNL400S;libraries=KNLEGQAV2;translate binary=true"
  "SBDUSER"
  "sbd2000"
  "com.ibm.as400.access.AS400JDBCDriver")

(def tpid-db-spec
  {:classname "com.ibm.as400.access.AS400JDBCDriver"
   :subprotocol "as400"
   :naming {:keys str/upper-case
            :fields str/upper-case}
   :make-pool? true
   :subname "jdbc:as400://KNL400S;libraries=KNLEGQAV2;translate binary=true"
   :user "SBDUSER"
   :password "sbd2000"})

(def tpid-db (create-db tpid-db-spec))


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
  {:pre [(s/valid? ::env-key env)]}
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
(defentity knoll_news_a)

;;(select systemusers)
;;(select systemusers (fields :id :username))
;;(select systemusers (where {:username "rhouser"}))
;;(select systemusers (where (raw "ROWNUM = 1")))
;;(select systemusers (fields (raw "count(*) as COUNT")))

(defn- loop-print-helper [f]
  (run! #(do
           (println)
           (f %)
           (println))
        (keys config)))

(defentity systemevents)

(defn show-customindexevents
  ([]
   (loop-print-helper show-customindexevents))
  
  ([env]
   (connect env)
   (print env)
   (print-table
    (select systemevents
            (fields :eventname :enabled :times)
            (where {:eventname [in ["CustomIndexEvent" "CustomTextileIndexEvent"]]})))))

(defentity knollluceneindexjobqueue)

(defn show-index-queue-counts
  ([]
   (loop-print-helper show-index-queue-counts))

  ([env]
   (connect env)
   (print env)
   (print-table
    (select knollluceneindexjobqueue
            (fields :assettype (raw "count(*) as COUNT"))
            (where {:index_status [in ["added" "updated" "update_failed"]]})
            (group :assettype)
            (order :assettype)))))

(defn show-index-queue
  ([]
   (loop-print-helper show-index-queue))
  
  ([env]
   (connect env)
   (print env)
   (print-table
    (select knollluceneindexjobqueue
            (fields :assetid :assettype :index_status)
            (where {:index_status [in ["added" "updated" "update_failed"]]})
            #_(group :assettype)
            (order :assettype)))))

(s/def ::index-q-record (s/keys :req-un [::ASSETTYPE ::COUNT]))
(s/def ::ASSETTYPE string?)
(s/def ::COUNT #(instance? java.math.BigDecimal %))
(s/def ::index-q-records (s/coll-of ::index-q-record :distinct true))

(s/fdef show-index-queue
  :args (s/cat :env ::env-key)
  :ret ::index-q-records)


(defn set-customindexevent [env flag]
  (connect env)
  (upd systemevents
          (set-fields {:enabled (if flag 1M 0M)})
          (where {:eventname "CustomIndexEvent"}))
  (show-customindexevents env))

(defn enable-customindexevent [env]
  (set-customindexevent env true))

(defn disable-customindexevent [env]
  (set-customindexevent env false))

(let [running (atom {::dev2 false ::staging false ::prod false})]
  (defn running? []
    @running)
  
  (defn reindex 
    "Sends the reindex http request for the specified env on a newly created channel.
  Uses running flags to prevent sending multiple overlapping reindex requests."
    [env]
    (if (env @running)
      (println "Reindex in" env "is already running.")
      (do
        (swap! running update env not)
        (println "Starting to reindex" env "...")
        (connect env)
        (let [custom-index-event (first (select systemevents (where {:eventname "CustomIndexEvent"})))
              url (str (-> config env ::url) "/cs/" (:TARGET custom-index-event) "?" (:PARAMS custom-index-event))
              ch (chan)]
          (go (>! ch (http/get url {:socket-timeout 360000 :connection-timeout 360000}))) ; 6 minute timeouts
          (go (when-let [_ (<! ch)]
                (swap! running update env not)
                (println "\nReindexing" env "complete."))))
        nil))))

(defn knolltextile-for-fabricid [env fabid]
  (connect env)
  (select product_c
          (where {:id [in (subselect product_c_mungo
                                     (fields :cs_ownerid)
                                     (where (and
                                             {:cs_attrid 1334871353607M}
                                             {:stringvalue fabid})))]})))

(defn fabricid-for-assetid [env assetid]
  (connect env)
  (-> (select product_c_mungo
              (fields :stringvalue)
              (where (and 
                      {:cs_ownerid assetid}
                      {:cs_attrid 1334871353607M})))
      (first)
      (:STRINGVALUE)))


#_(print-table (select product_a (fields :id :name :description)))

#_(k/delete knollluceneindexjobqueue (where {:assetid 1356039745953}))

(defonce dev2-db-spec {:jdbcUrl "jdbc:oracle:thin:@knldev2wcsdb1.knoll.com:1521:WCSDEV2" :user "ORAWCDEV11" :password "ORAWCDEV11"})
(defonce staging-db-spec {:jdbcUrl "jdbc:oracle:thin:@knlprdwcsdb.knoll.com:1521:WCSPRDG" :user "ORAPRDWCSMGT2" :password "knolloraprdwcsmgt2"})
(defonce prod-db-spec {:jdbcUrl "jdbc:oracle:thin:@knlprdwcsdb.knoll.com:1521:WCSPRDG" :user "ORAWCDLV" :password "knollorawcdlv"})
(defonce dev2-ds (connection/->pool ComboPooledDataSource dev2-db-spec))
(defonce staging-ds (connection/->pool ComboPooledDataSource staging-db-spec))
(defonce prod-ds (connection/->pool ComboPooledDataSource prod-db-spec))
#_(def dev2-ds (jdbc/get-datasource {:jdbcUrl "jdbc:oracle:thin:@knldev2wcsdb1.knoll.com:1521:WCSDEV2" :user "ORAWCDEV11" :password "ORAWCDEV11"}))
#_(def dev2-conn (jdbc/get-connection dev2-ds))
(print-table (jdbc/execute! dev2-ds ["select eventname, enabled, times from systemevents where eventname in ('CustomIndexEvent')"]))
(print-table (jdbc/execute! dev2-ds ["select assettype, count(*), index_status from knollluceneindexjobqueue group by assettype, index_status order by assettype, index_status"]))

#_(print-table (jdbc/execute!
              staging-ds
              ["select * from product_c where id in (select cs_ownerid from product_c_mungo where cs_attrid = '1334871353607' and stringvalue = ?)" "2085"]))

