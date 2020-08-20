(defproject demo_clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
			:url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
				 [org.clojure/java.jdbc "0.7.11"]
				 [org.postgresql/postgresql "42.2.15.jre7"]
				 [ring/ring-core "1.8.1"]
				 [ring/ring-jetty-adapter "1.8.1"]]
  :main ^:skip-aot demo_clj.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
					   :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
