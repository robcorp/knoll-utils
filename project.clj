(defproject knoll "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.1"]
                 #_[org.clojure/core.async "0.4.490"]
                 [org.clojure/core.async "0.4.500"]
                 [org.clojure/test.check "0.9.0"]
                 [orchestra "2018.12.06-2"]
                 [nrepl "0.5.3"]
                 [com.rpl/specter "1.1.2"]
                 [http-kit "2.3.0"]
                 [clj-http "3.9.0"]
                 [cheshire "5.8.0"]
                 [korma "0.4.3"]
                 [org.clojars.zentrope/ojdbc "11.2.0.3.0"]
                 [net.sf.jt400/jt400 "9.4"] ; for TPID db
                 #_[etaoin "0.2.8-SNAPSHOT"]
                 [etaoin "0.3.5"]
                 [com.novemberain/monger "3.5.0"]
                 [proto-repl "0.3.1"]
                 [itsy "0.1.1"]
                 #_[org.gebish/geb-core "2.0"]
                 #_[clucy "0.4.0"]
                 #_[com.cerner/clara-rules "0.19.0"]]

  :plugins [
            #_[cider/cider-nrepl "0.19.0"]
            #_[cider/cider-nrepl "0.22.0-beta4"]
            #_[lein-gorilla "0.4.0"]
            #_[lein-localrepo "0.5.4"]]

  :repl-options {:port 6439}

  :main knoll.core)
