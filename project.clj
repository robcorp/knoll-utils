(defproject knoll "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.3.610"]
                 [org.clojure/test.check "1.1.0"]
                 [orchestra "2020.09.18-1"]
                 [nrepl "0.8.3"]
                 [com.rpl/specter "1.1.3"]
                 [http-kit "2.5.0"]
                 [clj-http "3.11.0"]
                 [cheshire "5.10.0"]
                 [korma "0.4.3"]
                 [org.clojars.zentrope/ojdbc "11.2.0.3.0"]
                 [net.sf.jt400/jt400 "10.4"] ; for TPID db
                 #_[etaoin "0.2.8-SNAPSHOT"]
                 [etaoin "0.4.1"]
                 [com.novemberain/monger "3.5.0"]
                 [itsy "0.1.1"]
                 #_[org.gebish/geb-core "2.0"]
                 #_[clucy "0.4.0"]
                 #_[com.cerner/clara-rules "0.19.0"]
                 [dk.ative/docjure "1.14.0"]
                 [clj-rss "0.2.6"]
                 [clojureql "1.0.5"]
                 [org.apache.lucene/lucene-core "2.9.2"]
                 [org.apache.lucene/lucene-queries "2.9.2"]
                 [seancorfield/next.jdbc "1.1.613"]
                 [com.mchange/c3p0 "0.9.5.5"]
                 [lambdaisland/uri "1.4.54"]]

  :plugins [[refactor-nrepl "2.5.0"]
            [cider/cider-nrepl "0.25.5"]
            #_[lein-gorilla "0.4.0"]
            #_[lein-localrepo "0.5.4"]]

  :repl-options {:port 6439}

  :main knoll.core)
