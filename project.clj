(defproject home-visit-tracker "0.1.0-SNAPSHOT"
  :description "Web application for tracking home visits"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [org.clojure/java.jdbc "0.7.6"]
                 [com.mchange/c3p0 "0.9.5.2"]
                 [org.xerial/sqlite-jdbc "3.23.1"]
                 [ring/ring-anti-forgery "1.3.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [crypto-password "0.2.0"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [enfocus "2.1.1"]
                 [bouncer "1.0.1"]]
  :main ^:skip-aot home-visit-tracker.handler
  :ring {:handler home-visit-tracker.handler/app}
  :uberjar-name "server.jar"
  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]
  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [ring/ring-devel "1.6.3"]
                                  [ring/ring-mock "0.3.2"]]
                   :plugins [[lein-ring "0.12.0"]
                             [lein-cljsbuild "1.1.7"]]}
             :uberjar {:aot :all}}
  :cljsbuild {
    :builds [{:source-paths ["src/cljs" "src/cljc"]
              :compiler {
                :output-to "target/cljsbuild/public/js/main.js"
                :output-dir "target/cljsbuild/public/js"
                :optimizations :whitespace
                :pretty-print true}}]})
