(ns crud.server.core
  (:require [crud.server.web :refer [app]]
            [ring.adapter.jetty :as ring]
            [com.stuartsierra.component :as component]))

(defrecord Demo []
  component/Lifecycle
  (start [this]
    (assoc this :server
           (ring/run-jetty #'app {:port 8000 :join? false})))
  (stop [this]
    (.stop (:server this))
    (dissoc this :server)))

(defn create-system []
  (Demo.))

(defn -main
  [& args]
  (.start (create-system))
  (println "Started on localhost:8000"))

