(ns knoll.lucene
  (:require [cheshire.core :refer [decode encode]]
            [clojure.java.io :refer [resource]]
            #_[org.httpkit.client :as http]
            [clj-http.client :as http]
            #_[clucy.core :as clucy])
  (:import (org.apache.lucene.document
            Document Field Field$Store Field$Index NumericField)
           (org.apache.lucene.analysis.standard StandardAnalyzer)
           (org.apache.lucene.store NIOFSDirectory RAMDirectory)
           (org.apache.lucene.search
            IndexSearcher QueryWrapperFilter TermQuery Sort)
           (org.apache.lucene.queryParser QueryParser)
           (org.apache.lucene.index IndexWriter IndexWriter$MaxFieldLength
                                    IndexReader Term)
           (org.apache.lucene.util Version)
           (java.io File)))

#_(def index (clucy/disk-index ""))

(def os-directory (File. "/Users/robcorp/Downloads/customindex-20200327"))
(def lucene-directory (NIOFSDirectory. os-directory))
(def index-reader (IndexReader/open lucene-directory))
(def index-searcher (IndexSearcher. index-reader))

