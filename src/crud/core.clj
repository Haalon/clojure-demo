(ns crud.core
  (:require [ring.adapter.jetty :as ring]
            [com.stuartsierra.component :as component]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [resources]]
   			      [crud.model :as model]
   			      [crud.views :as views]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (views/common "CRUD" (views/table (model/get-all)))})

(defroutes app
  (GET "/" [] handler)
  (resources "/"))

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

