(ns knoll.db
  (:require [clojure.string :as str]
            [korma.core :as k
             :refer [defentity fields group order raw select set-fields
                     subselect update where]
             :rename {update upd}]
            [korma.db :as kd
             :refer [default-connection defdb create-db]]
            #_[org.httpkit.client :as http]
            [clj-http.client :as http]
            [com.rpl.specter :as spctr]
            [clojure.spec.alpha :as s]
            #_[clojure.spec.test.alpha :as st]
            [orchestra.spec.test :as st]
            [clojure.pprint :refer :all]
            [clojure.core.async :as async :refer [chan >! <! go]]))


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

(defentity systemevents)

(defn show-customindexevents [env]
  (connect env)
  (->
   (select systemevents
           (fields :eventname :enabled :times)
           (where {:eventname [in ["CustomIndexEvent" "CustomTextileIndexEvent"]]}))
   print-table))

(defentity knollluceneindexjobqueue)

(defn show-index-queue-counts [env]
  (connect env)
  (->
   (select knollluceneindexjobqueue
           (fields :assettype (raw "count(*) as COUNT"))
           (where {:index_status [in ["added" "updated" "update_failed"]]})
           (group :assettype)
           (order :assettype))
   print-table))

(defn show-index-queue [env]
  (connect env)
  (->
   (select knollluceneindexjobqueue
           (fields :assetid :assettype :index_status)
           (where {:index_status [in ["added" "updated" "update_failed"]]})
           #_(group :assettype)
           (order :assettype))
   print-table))
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
  (defn reindex [env]
    "Sends the reindex http request for the specified env on a newly created channel.
Uses running flags to prevent sending multiple overlapping reindex requests."
    (if (env @running)
      (println "Reindex in" env "is already running.")
      (do
        (swap! running update env not)
        (println "Starting to reindex" env "...")
        (connect env)
        (let [custom-index-event (first (select systemevents (where {:eventname "CustomIndexEvent"})))
              url (str (-> config env ::url) "/cs/" (:TARGET custom-index-event) "?" (:PARAMS custom-index-event))
              ch (chan)]
          (go (>! ch (http/get url)))
          (go (when-let [_ (<! ch)]
                (swap! running update env not)
                (println "\nReindexing" env "complete."))))
        nil))))

(defn get-knolltextile-for-fabricid [env fabid]
  (connect env)
  (select product_c
          (where {:id [in (subselect product_c_mungo
                                     (fields :cs_ownerid)
                                     (where (and
                                             {:cs_attrid 1334871353607M}
                                             {:stringvalue fabid})))]})))


#_(print-table (select product_a (fields :id :name :description)))

#_(k/delete knollluceneindexjobqueue (where {:assetid 1356039745953}))

(run! #(do
         (println)
         (print %)
         (show-customindexevents %)
         (println))
      (keys config))

(run! #(do
         (println)
         (println %)
         (show-index-queue-counts %)
         (println))
      (keys config))

