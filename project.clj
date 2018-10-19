(defproject knoll "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async "0.4.474"]
                 [org.clojure/test.check "0.9.0"]
                 [orchestra "2018.09.10-1"]
                 [nrepl "0.4.5"]
                 [com.rpl/specter "1.1.1"]
                 [http-kit "2.3.0"]
                 [clj-http "3.9.0"]
                 [cheshire "5.8.0"]
                 [korma "0.4.3"]
                 [org.clojars.zentrope/ojdbc "11.2.0.3.0"]
                 [net.sf.jt400/jt400 "9.4"]
                 [etaoin "0.2.8-SNAPSHOT"]
                 ;;[org.gebish/geb-core "2.0"]
                 [clucy "0.4.0"]
                 ]

  :plugins [
            #_[cider/cider-nrepl "0.18.0"]
            #_[lein-gorilla "0.4.0"]
            #_[lein-localrepo "0.5.4"]]
  
  :repl-options {:port 6439}
  
  :main knoll.core)
