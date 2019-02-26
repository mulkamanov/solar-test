(defproject solar-test "0.1.0-SNAPSHOT"
  :description "Test project for RT-Solar."
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/core.async "LATEST"]
                 [ring/ring-core "LATEST"]
                 [ring/ring-jetty-adapter "LATEST"]
                 [ring/ring-defaults "LATEST"]
                 [ring/ring-json "LATEST"]
                 [compojure "LATEST"]
                 [clj-http "LATEST"]
                 [mount "LATEST"]]
  :main ^:skip-aot solar-test.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
