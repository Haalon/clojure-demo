(ns demo_clj.core
  (:require [ring.adapter.jetty :as ring]
         [com.stuartsierra.component :as component]
   			 [demo_clj.models.crud :as model]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (->> (model/get-all)
        (map str)
        (clojure.string/join "<br>"))})

(defrecord Demo []
  component/Lifecycle
  (start [this]
    (assoc this :server
           (ring/run-jetty #'handler {:port 8000 :join? false})))
  (stop [this]
    (.stop (:server this))
    (dissoc this :server)))

(defn create-system []
  (Demo.))

(defn -main
  [& args]
  (.start (create-system))
  (println "Started on localhost:8000"))

