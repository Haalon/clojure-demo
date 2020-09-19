(ns user
  (:require ;;[reloaded.repl :refer [system reset stop start]]
            [crud.server.core :as core]
            [crud.server.util :as util]
            [crud.server.model :as model]
            [clojure.java.jdbc :as sql]
            [figwheel.main.api :as repl]))

;; (reloaded.repl/set-init! #(core/create-system))

(defn mock-insurance []
  (clojure.string/join (map str (take 16 (repeatedly #(rand-int 10))))))

(defn mock-name []
  (apply str (take (+ 4 (rand-int 10)) (repeatedly #(char (+ (rand-int 26) 65))))))

(defn mock-sex []
  (util/cast-enum "sex" (if (= 0 (rand-int 2)) "male" "female")))

(defn mock-birth []
  (let [y (str (+ 1920 (rand-int 101)))
        m (format "%02d" (+ 1 (rand-int 12)))
        d (format "%02d" (+ 1 (rand-int 28)))]
    (util/parse-sql-date (clojure.string/join \- [y m d]))))

(defn populate [len]
  (sql/insert! model/url :crud {:name (mock-name),
                                :insurance (mock-insurance),
                                :sex (mock-sex)
                                :birth (mock-birth)
                                :address (mock-name)})
  (if (> len 0) (populate (- len 1)) nil))

(def figwheel-options
  {:id "app"
   :options {:main 'crud.client.core
             :closure-defines {"re_frame.trace.trace_enabled_QMARK_" true}
             :output-to "resources/public/cljs-out/dev-main.js"
             :output-dir "resources/public/cljs-out/dev"}
   :config {:watch-dirs ["src"]
            :mode :serve
            :ring-server-options {:port 8000}}})


(defn run-ui [opts]
  (repl/start  opts))

(defn run-back
 [& params]
  (core/-main))

(defn start []
  ;; (run-back)
  (run-ui figwheel-options))

(defn stop []
  (repl/stop "app")
  (println "\nUI stopped"))

#_(defonce state
  (future (start)))

(comment
  (start)
  (stop)

  ;;(require '[figwheel.main.api :as repl])
  (repl/cljs-repl "app")
)
