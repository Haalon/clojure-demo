(defproject crud "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
			:url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.postgresql/postgresql "42.2.15.jre7"]
                 [ring/ring-core "1.8.1"]
                 [ring/ring-jetty-adapter "1.8.1"]
                 [compojure "1.6.2"]
                 [hiccup "1.0.5"]
                 [com.stuartsierra/component "1.0.0"]
                 [org.clojure/clojurescript "1.10.773"]
                 [org.clojure/data.json "1.0.0"]]
  :main ^:skip-aot crud.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
					   :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
					   :dev {:plugins [[lein-cljsbuild "1.1.8"]
					                   [lein-cljfmt "0.6.8"]]
					   	     :dependencies [[reloaded.repl "0.2.4"]]
					   	     :source-paths ["dev"]
              :cljsbuild {:builds [{:source-paths ["src" "dev"]
                                    :figwheel true
                                    :compiler {:output-to "resources/public/app.js"
                                               :output-dir "resources/public/out"
                                               :main "crud.client"
                                               :asset-path "/out"
                                               :optimizations :none
                                               :recompile-dependents true
                                               :source-map true}}]}}})
