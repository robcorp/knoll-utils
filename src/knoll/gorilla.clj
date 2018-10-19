;; gorilla-repl.fileformat = 1

;; **
;;; # Knoll
;;; 
;;; Shift + enter evaluates code. Hit ctrl+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;;; It's a good habit to run each worksheet in its own namespace: feel free to use the declaration we've provided below if you'd like.
;; **

;; @@
(ns knoll.gorilla
  (:require [gorilla-plot.core :as plot]
            [gorilla-repl.table :as table])
  (:use clojure.pprint
        [knoll.db :as d]
        [knoll.textiles :as t]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
;(d/connect-dev2)
;(d/connect-staging)
(d/connect-prod)

;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:pool</span>","value":":pool"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:classname</span>","value":":classname"},{"type":"html","content":"<span class='clj-string'>&quot;oracle.jdbc.odbc.OracleDriver&quot;</span>","value":"\"oracle.jdbc.odbc.OracleDriver\""}],"value":"[:classname \"oracle.jdbc.odbc.OracleDriver\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:subprotocol</span>","value":":subprotocol"},{"type":"html","content":"<span class='clj-string'>&quot;oracle&quot;</span>","value":"\"oracle\""}],"value":"[:subprotocol \"oracle\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:naming</span>","value":":naming"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:keys</span>","value":":keys"},{"type":"html","content":"<span class='clj-unkown'>#function[clojure.string/upper-case]</span>","value":"#function[clojure.string/upper-case]"}],"value":"[:keys #function[clojure.string/upper-case]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:fields</span>","value":":fields"},{"type":"html","content":"<span class='clj-unkown'>#function[clojure.string/upper-case]</span>","value":"#function[clojure.string/upper-case]"}],"value":"[:fields #function[clojure.string/upper-case]]"}],"value":"{:keys #function[clojure.string/upper-case], :fields #function[clojure.string/upper-case]}"}],"value":"[:naming {:keys #function[clojure.string/upper-case], :fields #function[clojure.string/upper-case]}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:subname</span>","value":":subname"},{"type":"html","content":"<span class='clj-string'>&quot;thin:@knlprdwcsdb.knoll.com:1521:WCSPRDG&quot;</span>","value":"\"thin:@knlprdwcsdb.knoll.com:1521:WCSPRDG\""}],"value":"[:subname \"thin:@knlprdwcsdb.knoll.com:1521:WCSPRDG\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:user</span>","value":":user"},{"type":"html","content":"<span class='clj-string'>&quot;ORAWCDLV&quot;</span>","value":"\"ORAWCDLV\""}],"value":"[:user \"ORAWCDLV\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:password</span>","value":":password"},{"type":"html","content":"<span class='clj-string'>&quot;knollorawcdlv&quot;</span>","value":"\"knollorawcdlv\""}],"value":"[:password \"knollorawcdlv\"]"}],"value":"{:classname \"oracle.jdbc.odbc.OracleDriver\", :subprotocol \"oracle\", :naming {:keys #function[clojure.string/upper-case], :fields #function[clojure.string/upper-case]}, :subname \"thin:@knlprdwcsdb.knoll.com:1521:WCSPRDG\", :user \"ORAWCDLV\", :password \"knollorawcdlv\"}"}],"value":"[:pool {:classname \"oracle.jdbc.odbc.OracleDriver\", :subprotocol \"oracle\", :naming {:keys #function[clojure.string/upper-case], :fields #function[clojure.string/upper-case]}, :subname \"thin:@knlprdwcsdb.knoll.com:1521:WCSPRDG\", :user \"ORAWCDLV\", :password \"knollorawcdlv\"}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:options</span>","value":":options"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:naming</span>","value":":naming"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:keys</span>","value":":keys"},{"type":"html","content":"<span class='clj-unkown'>#function[clojure.string/upper-case]</span>","value":"#function[clojure.string/upper-case]"}],"value":"[:keys #function[clojure.string/upper-case]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:fields</span>","value":":fields"},{"type":"html","content":"<span class='clj-unkown'>#function[clojure.string/upper-case]</span>","value":"#function[clojure.string/upper-case]"}],"value":"[:fields #function[clojure.string/upper-case]]"}],"value":"{:keys #function[clojure.string/upper-case], :fields #function[clojure.string/upper-case]}"}],"value":"[:naming {:keys #function[clojure.string/upper-case], :fields #function[clojure.string/upper-case]}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:delimiters</span>","value":":delimiters"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;\\&quot;&quot;</span>","value":"\"\\\"\""},{"type":"html","content":"<span class='clj-string'>&quot;\\&quot;&quot;</span>","value":"\"\\\"\""}],"value":"[\"\\\"\" \"\\\"\"]"}],"value":"[:delimiters [\"\\\"\" \"\\\"\"]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:alias-delimiter</span>","value":":alias-delimiter"},{"type":"html","content":"<span class='clj-string'>&quot; AS &quot;</span>","value":"\" AS \""}],"value":"[:alias-delimiter \" AS \"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:subprotocol</span>","value":":subprotocol"},{"type":"html","content":"<span class='clj-string'>&quot;oracle&quot;</span>","value":"\"oracle\""}],"value":"[:subprotocol \"oracle\"]"}],"value":"{:naming {:keys #function[clojure.string/upper-case], :fields #function[clojure.string/upper-case]}, :delimiters [\"\\\"\" \"\\\"\"], :alias-delimiter \" AS \", :subprotocol \"oracle\"}"}],"value":"[:options {:naming {:keys #function[clojure.string/upper-case], :fields #function[clojure.string/upper-case]}, :delimiters [\"\\\"\" \"\\\"\"], :alias-delimiter \" AS \", :subprotocol \"oracle\"}]"}],"value":"{:pool {:classname \"oracle.jdbc.odbc.OracleDriver\", :subprotocol \"oracle\", :naming {:keys #function[clojure.string/upper-case], :fields #function[clojure.string/upper-case]}, :subname \"thin:@knlprdwcsdb.knoll.com:1521:WCSPRDG\", :user \"ORAWCDLV\", :password \"knollorawcdlv\"}, :options {:naming {:keys #function[clojure.string/upper-case], :fields #function[clojure.string/upper-case]}, :delimiters [\"\\\"\" \"\\\"\"], :alias-delimiter \" AS \", :subprotocol \"oracle\"}}"}
;; <=

;; @@
(d/check-customindexevents)
(d/check-index-queue)
;(pprint (d/check-index-queue))

;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[],"value":"()"}
;; <=

;; @@
;; cause any updated assets to be reindexed by calling the invokeCustomIndexerNow page
(d/reindex-now d/prod-reindex-url)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:opts</span>","value":":opts"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:method</span>","value":":method"},{"type":"html","content":"<span class='clj-keyword'>:get</span>","value":":get"}],"value":"[:method :get]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:url</span>","value":":url"},{"type":"html","content":"<span class='clj-string'>&quot;http://knlprdwcsapp1.knoll.com:8080/cs/ContentServer?pagename=Knoll/Common/Site/search/invokeCustomIndexerNow&amp;clearIndex=false&amp;contentTypes=null&amp;d=&quot;</span>","value":"\"http://knlprdwcsapp1.knoll.com:8080/cs/ContentServer?pagename=Knoll/Common/Site/search/invokeCustomIndexerNow&clearIndex=false&contentTypes=null&d=\""}],"value":"[:url \"http://knlprdwcsapp1.knoll.com:8080/cs/ContentServer?pagename=Knoll/Common/Site/search/invokeCustomIndexerNow&clearIndex=false&contentTypes=null&d=\"]"}],"value":"{:method :get, :url \"http://knlprdwcsapp1.knoll.com:8080/cs/ContentServer?pagename=Knoll/Common/Site/search/invokeCustomIndexerNow&clearIndex=false&contentTypes=null&d=\"}"}],"value":"[:opts {:method :get, :url \"http://knlprdwcsapp1.knoll.com:8080/cs/ContentServer?pagename=Knoll/Common/Site/search/invokeCustomIndexerNow&clearIndex=false&contentTypes=null&d=\"}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:body</span>","value":":body"},{"type":"html","content":"<span class='clj-string'>&quot;\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\t\\r\\n\\t\\r\\n\\t\\r\\n\\t&lt;div&gt;\\r\\n\\t\\t\\r\\n\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t&lt;div&gt;\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t\\t&lt;span&gt;1 Knoll_News_C documents indexed, 0 failed.&lt;/span&gt;\\r\\n\\t\\t\\t\\t\\t\\t&lt;br/&gt;\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t\\t&lt;span&gt;1 Page documents indexed, 0 failed.&lt;/span&gt;\\r\\n\\t\\t\\t\\t\\t\\t&lt;br/&gt;\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t&lt;/div&gt;\\r\\n\\t\\t\\t\\t&lt;br/&gt;\\r\\n\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t&lt;div&gt;\\r\\n\\t\\t\\t\\t\\t\\t&lt;span&gt;Custom Lucene Indexer -- Successfully executed- elapsed time = 00:00:18.&lt;/span&gt;\\r\\n\\t\\t\\t\\t\\t&lt;/div&gt;\\r\\n\\t\\t\\t\\t\\t&lt;br/&gt;\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t&lt;/div&gt;\\r\\n\\r\\n\\r\\n&quot;</span>","value":"\"\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\t\\r\\n\\t\\r\\n\\t\\r\\n\\t<div>\\r\\n\\t\\t\\r\\n\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t<div>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t\\t<span>1 Knoll_News_C documents indexed, 0 failed.</span>\\r\\n\\t\\t\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t\\t<span>1 Page documents indexed, 0 failed.</span>\\r\\n\\t\\t\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t</div>\\r\\n\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t<div>\\r\\n\\t\\t\\t\\t\\t\\t<span>Custom Lucene Indexer -- Successfully executed- elapsed time = 00:00:18.</span>\\r\\n\\t\\t\\t\\t\\t</div>\\r\\n\\t\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t</div>\\r\\n\\r\\n\\r\\n\""}],"value":"[:body \"\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\t\\r\\n\\t\\r\\n\\t\\r\\n\\t<div>\\r\\n\\t\\t\\r\\n\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t<div>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t\\t<span>1 Knoll_News_C documents indexed, 0 failed.</span>\\r\\n\\t\\t\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t\\t<span>1 Page documents indexed, 0 failed.</span>\\r\\n\\t\\t\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t</div>\\r\\n\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t<div>\\r\\n\\t\\t\\t\\t\\t\\t<span>Custom Lucene Indexer -- Successfully executed- elapsed time = 00:00:18.</span>\\r\\n\\t\\t\\t\\t\\t</div>\\r\\n\\t\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t</div>\\r\\n\\r\\n\\r\\n\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:headers</span>","value":":headers"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:content-type</span>","value":":content-type"},{"type":"html","content":"<span class='clj-string'>&quot;text/html; charset=UTF-8&quot;</span>","value":"\"text/html; charset=UTF-8\""}],"value":"[:content-type \"text/html; charset=UTF-8\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:date</span>","value":":date"},{"type":"html","content":"<span class='clj-string'>&quot;Fri, 04 May 2018 15:49:34 GMT&quot;</span>","value":"\"Fri, 04 May 2018 15:49:34 GMT\""}],"value":"[:date \"Fri, 04 May 2018 15:49:34 GMT\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:host_service</span>","value":":host_service"},{"type":"html","content":"<span class='clj-string'>&quot;FutureTenseContentServer:11.1.1.8.0&quot;</span>","value":"\"FutureTenseContentServer:11.1.1.8.0\""}],"value":"[:host_service \"FutureTenseContentServer:11.1.1.8.0\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:set-cookie</span>","value":":set-cookie"},{"type":"html","content":"<span class='clj-string'>&quot;JSESSIONID=0NmMhsBTLFv22NvZpfBSsYTvGRvnhhKjmH0YMN8y05BC5Tmwz4Tl!1738131593; expires=Fri, 04-May-2018 16:19:52 GMT; path=/cs; HttpOnly&quot;</span>","value":"\"JSESSIONID=0NmMhsBTLFv22NvZpfBSsYTvGRvnhhKjmH0YMN8y05BC5Tmwz4Tl!1738131593; expires=Fri, 04-May-2018 16:19:52 GMT; path=/cs; HttpOnly\""}],"value":"[:set-cookie \"JSESSIONID=0NmMhsBTLFv22NvZpfBSsYTvGRvnhhKjmH0YMN8y05BC5Tmwz4Tl!1738131593; expires=Fri, 04-May-2018 16:19:52 GMT; path=/cs; HttpOnly\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:transfer-encoding</span>","value":":transfer-encoding"},{"type":"html","content":"<span class='clj-string'>&quot;chunked&quot;</span>","value":"\"chunked\""}],"value":"[:transfer-encoding \"chunked\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:x-powered-by</span>","value":":x-powered-by"},{"type":"html","content":"<span class='clj-string'>&quot;Servlet/2.5 JSP/2.1&quot;</span>","value":"\"Servlet/2.5 JSP/2.1\""}],"value":"[:x-powered-by \"Servlet/2.5 JSP/2.1\"]"}],"value":"{:content-type \"text/html; charset=UTF-8\", :date \"Fri, 04 May 2018 15:49:34 GMT\", :host_service \"FutureTenseContentServer:11.1.1.8.0\", :set-cookie \"JSESSIONID=0NmMhsBTLFv22NvZpfBSsYTvGRvnhhKjmH0YMN8y05BC5Tmwz4Tl!1738131593; expires=Fri, 04-May-2018 16:19:52 GMT; path=/cs; HttpOnly\", :transfer-encoding \"chunked\", :x-powered-by \"Servlet/2.5 JSP/2.1\"}"}],"value":"[:headers {:content-type \"text/html; charset=UTF-8\", :date \"Fri, 04 May 2018 15:49:34 GMT\", :host_service \"FutureTenseContentServer:11.1.1.8.0\", :set-cookie \"JSESSIONID=0NmMhsBTLFv22NvZpfBSsYTvGRvnhhKjmH0YMN8y05BC5Tmwz4Tl!1738131593; expires=Fri, 04-May-2018 16:19:52 GMT; path=/cs; HttpOnly\", :transfer-encoding \"chunked\", :x-powered-by \"Servlet/2.5 JSP/2.1\"}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:status</span>","value":":status"},{"type":"html","content":"<span class='clj-unkown'>200</span>","value":"200"}],"value":"[:status 200]"}],"value":"{:opts {:method :get, :url \"http://knlprdwcsapp1.knoll.com:8080/cs/ContentServer?pagename=Knoll/Common/Site/search/invokeCustomIndexerNow&clearIndex=false&contentTypes=null&d=\"}, :body \"\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\r\\n\\t\\r\\n\\t\\r\\n\\t\\r\\n\\t<div>\\r\\n\\t\\t\\r\\n\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t<div>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t\\t<span>1 Knoll_News_C documents indexed, 0 failed.</span>\\r\\n\\t\\t\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t\\t<span>1 Page documents indexed, 0 failed.</span>\\r\\n\\t\\t\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t</div>\\r\\n\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\r\\n\\t\\t\\t\\t\\t<div>\\r\\n\\t\\t\\t\\t\\t\\t<span>Custom Lucene Indexer -- Successfully executed- elapsed time = 00:00:18.</span>\\r\\n\\t\\t\\t\\t\\t</div>\\r\\n\\t\\t\\t\\t\\t<br/>\\r\\n\\t\\t\\t\\t\\t\\r\\n\\t</div>\\r\\n\\r\\n\\r\\n\", :headers {:content-type \"text/html; charset=UTF-8\", :date \"Fri, 04 May 2018 15:49:34 GMT\", :host_service \"FutureTenseContentServer:11.1.1.8.0\", :set-cookie \"JSESSIONID=0NmMhsBTLFv22NvZpfBSsYTvGRvnhhKjmH0YMN8y05BC5Tmwz4Tl!1738131593; expires=Fri, 04-May-2018 16:19:52 GMT; path=/cs; HttpOnly\", :transfer-encoding \"chunked\", :x-powered-by \"Servlet/2.5 JSP/2.1\"}, :status 200}"}
;; <=

;; @@
(pprint t/shima)
(t/uses t/shima)
;; @@
;; ->
;;; {:FabricUses [{:Category &quot;K&quot;, :UseName &quot;Upholstery&quot;}],
;;;  :KnollGrade &quot;E&quot;,
;;;  :PatternVerticalFormatted &quot;14.75&quot;,
;;;  :PatternVertical &quot;14.75&quot;,
;;;  :CleaningCode
;;;  &quot;Only mild, pure water-free dry cleaning solvents may be used for cleaning this fabric.&quot;,
;;;  :FabricId 1468,
;;;  :NetPrice 84.0,
;;;  :CuttingDirection &quot;NR - Non-railroaded&quot;,
;;;  :AverageBoltsPerYard &quot;30&quot;,
;;;  :PatternHorizontal &quot;27.75&quot;,
;;;  :WeightFormatted &quot;24.0 oz.&quot;,
;;;  :UseName &quot;Upholstery&quot;,
;;;  :Designer &quot;N/A&quot;,
;;;  :CanadianPrice 84.0,
;;;  :Finishes [],
;;;  :UpholsteryType &quot;Light to Medium&quot;,
;;;  :Backings [],
;;;  :FabricColors
;;;  [{:SkuNumber &quot;1&quot;,
;;;    :ColorName &quot;Creme&quot;,
;;;    :ColorCategory1 &quot;Warm Neutral&quot;,
;;;    :ColorCategory2 &quot;White&quot;,
;;;    :ColorCategory3 &quot;Brown&quot;}
;;;   {:SkuNumber &quot;2&quot;,
;;;    :ColorName &quot;Sage&quot;,
;;;    :ColorCategory1 &quot;Green&quot;,
;;;    :ColorCategory2 &quot;White&quot;,
;;;    :ColorCategory3 &quot;Blue&quot;}
;;;   {:SkuNumber &quot;3&quot;,
;;;    :ColorName &quot;Balmy&quot;,
;;;    :ColorCategory1 &quot;Blue&quot;,
;;;    :ColorCategory2 &quot;White&quot;,
;;;    :ColorCategory3 nil}
;;;   {:SkuNumber &quot;4&quot;,
;;;    :ColorName &quot;Burnt&quot;,
;;;    :ColorCategory1 &quot;Orange&quot;,
;;;    :ColorCategory2 &quot;White&quot;,
;;;    :ColorCategory3 &quot;Brown&quot;}
;;;   {:SkuNumber &quot;5&quot;,
;;;    :ColorName &quot;Indigo&quot;,
;;;    :ColorCategory1 &quot;Blue&quot;,
;;;    :ColorCategory2 &quot;Purple&quot;,
;;;    :ColorCategory3 nil}
;;;   {:SkuNumber &quot;6&quot;,
;;;    :ColorName &quot;Pewter&quot;,
;;;    :ColorCategory1 &quot;Gray&quot;,
;;;    :ColorCategory2 &quot;White&quot;,
;;;    :ColorCategory3 &quot;Purple&quot;}
;;;   {:SkuNumber &quot;7&quot;,
;;;    :ColorName &quot;Mask&quot;,
;;;    :ColorCategory1 &quot;Black&quot;,
;;;    :ColorCategory2 &quot;Gray&quot;,
;;;    :ColorCategory3 &quot;Pink&quot;}],
;;;  :TestingResults
;;;  [{:TestName &quot;Colorfastness Wet&quot;,
;;;    :TestResultName nil,
;;;    :TestResult &quot;5&quot;,
;;;    :DateTested 1287633600000,
;;;    :Status &quot;Tested&quot;}
;;;   {:TestName &quot;Colorfastness Dry&quot;,
;;;    :TestResultName nil,
;;;    :TestResult &quot;5&quot;,
;;;    :DateTested 1287633600000,
;;;    :Status &quot;Tested&quot;}
;;;   {:TestName &quot;Lightfastness 40 hrs&quot;,
;;;    :TestResultName nil,
;;;    :TestResult &quot;4&quot;,
;;;    :DateTested 1288065600000,
;;;    :Status &quot;Tested&quot;}
;;;   {:TestName &quot;Brush Pill&quot;,
;;;    :TestResultName nil,
;;;    :TestResult &quot;4&quot;,
;;;    :DateTested 1290488400000,
;;;    :Status &quot;Tested&quot;}
;;;   {:TestName &quot;Tensile Strength Weft&quot;,
;;;    :TestResultName nil,
;;;    :TestResult &quot;341&quot;,
;;;    :DateTested 1291093200000,
;;;    :Status &quot;Tested&quot;}
;;;   {:TestName &quot;Tensile Strength Warp&quot;,
;;;    :TestResultName nil,
;;;    :TestResult &quot;153&quot;,
;;;    :DateTested 1291093200000,
;;;    :Status &quot;Tested&quot;}
;;;   {:TestName &quot;Seam Slippage Weft&quot;,
;;;    :TestResultName nil,
;;;    :TestResult &quot;89.5 SB&quot;,
;;;    :DateTested 1291093200000,
;;;    :Status &quot;Tested&quot;}
;;;   {:TestName &quot;Seam Slippage Warp&quot;,
;;;    :TestResultName nil,
;;;    :TestResult &quot;76 SB&quot;,
;;;    :DateTested 1291093200000,
;;;    :Status &quot;Tested&quot;}
;;;   {:TestName &quot;Greenguard Certification&quot;,
;;;    :TestResultName &quot;Pass&quot;,
;;;    :TestResult nil,
;;;    :DateTested 1299560400000,
;;;    :Status &quot;&quot;}
;;;   {:TestName &quot;Cal 117 - 2013 Section I&quot;,
;;;    :TestResultName &quot;Pass&quot;,
;;;    :TestResult nil,
;;;    :DateTested 1405569600000,
;;;    :Status &quot;Tested&quot;}
;;;   {:TestName &quot;Wyzenbeek Published&quot;,
;;;    :TestResultName nil,
;;;    :TestResult &quot;20,000&quot;,
;;;    :DateTested nil,
;;;    :Status &quot;&quot;}],
;;;  :WidthFormatted &quot;54 in.&quot;,
;;;  :UpholsteryGrade &quot;E&quot;,
;;;  :Nafta false,
;;;  :Contents [{:Content &quot;Cotton&quot;, :Percentage 100.0}],
;;;  :CleaningCodeName &quot;S&quot;,
;;;  :NetPriceFormatted &quot;$84 USD&quot;,
;;;  :CanadianPriceFormatted &quot;$84 CAD&quot;,
;;;  :Width &quot;54&quot;,
;;;  :Country &quot;United States&quot;,
;;;  :PanelGrade nil,
;;;  :CopyrightYear &quot;2011&quot;,
;;;  :UseCode &quot;K&quot;,
;;;  :Version &quot;K&quot;,
;;;  :PatternHorizontalFormatted &quot;27.75&quot;,
;;;  :Weight &quot;24.0&quot;}
;;; 
;; <-
;; =>
;;; {"type":"list-like","open":"<span class='clj-list'>(</span>","close":"<span class='clj-list'>)</span>","separator":" ","items":[{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:UseCode</span>","value":":UseCode"},{"type":"html","content":"<span class='clj-string'>&quot;K&quot;</span>","value":"\"K\""}],"value":"[:UseCode \"K\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:UseName</span>","value":":UseName"},{"type":"html","content":"<span class='clj-string'>&quot;Upholstery&quot;</span>","value":"\"Upholstery\""}],"value":"[:UseName \"Upholstery\"]"}],"value":"{:UseCode \"K\", :UseName \"Upholstery\"}"}],"value":"({:UseCode \"K\", :UseName \"Upholstery\"})"}
;; <=

;; @@
;(plot/list-plot [1 2 3 4 0 5 0 4 3 2 1] :joined true)
;; @@

;; @@
;(plot/plot #(* % %) [0 10] :plot-points 20)
;; @@

;; @@
;(table/table-view '((a b c)(1 2 3)(3 4 5)(6 7 8)))
;; @@

;; @@

;; @@
