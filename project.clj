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
                 [com.stuartsierra/component "1.0.0"]
                 [org.clojure/clojurescript "1.10.773"]
                 [org.clojure/data.json "1.0.0"]
                 [cljs-http "0.1.46"]
                 [reagent "0.10.0"]
                 [re-frame "1.1.1"]
                 [day8.re-frame/http-fx "0.2.1"]]
  :main ^:skip-aot crud.server.core
  :source-paths ["src" "dev"]
  :aliases {"fig"       ["trampoline" "run" "-m" "figwheel.main"]
            "fig:build" ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "fig:min"   ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "dev"]
            "fig:test"  ["run" "-m" "figwheel.main" "-co" "test.cljs.edn" "-m" "hello-world.test-runner"]}
  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.2.11"]
                                  [reloaded.repl "0.2.4"]
                                  [ring/ring-mock "0.4.0"]
                                  [com.bhauman/rebel-readline-cljs "0.1.4"]]
                   :plugins [[lein-cljsbuild "1.1.8"]
					                        [lein-cljfmt "0.6.8"]]
                   
                   :resource-paths ["target"]
                   ;; need to add the compiled assets to the :clean-targets
                   :clean-targets ^{:protect false} ["target"]}})
