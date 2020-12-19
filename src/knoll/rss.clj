(ns knoll.rss
  (:require [clj-rss.core :refer [channel channel-xml]]))

#_(channel-xml {:title "Foo" :link "http://foo/bar" :description "some channel"}
             {:title "Foo"}
             {:title "post" :author "author@foo.bar"}
             {:description "bar"})

(let [items [{:title "Foo"} {:title "Bar"} {:title "Baz"}]]
  (println (channel-xml {:title "Foo" :link "http://foo/bar" :description "some channel"}
                        items)))
