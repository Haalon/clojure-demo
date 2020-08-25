(ns crud.core
  (:require [ring.adapter.jetty :as ring]
            [com.stuartsierra.component :as component]
            [compojure.core :refer [defroutes GET DELETE POST PUT]]
            [compojure.route :refer [resources]]
            [crud.model :as model]
            [crud.views :as views]
            [crud.util :as util]
            [clojure.data.json :as json]))

(defn index [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (views/common "CRUD" (views/table (model/list)))})

(defn api-list [request]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/write-str (model/list)
                         :value-fn model/value-reader-sql)})

(defn api-add [request]
  (let [entry (-> request
                  :body
                  slurp
                  (json/read-str :key-fn keyword :value-fn model/value-reader-json))]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/write-str (model/add entry)
                           :value-fn model/value-reader-sql)}))

(defn api-update [request id]
  (let [entry (-> request
                  :body
                  slurp
                  (json/read-str :key-fn keyword :value-fn model/value-reader-json))
        id (Integer/parseInt id)]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (model/update id entry)}))

(defn api-del [id]
  (let [id (Integer/parseInt id)]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (model/delete id)}))

(defroutes app
  (GET "/" [] index)
  (GET "/api" [] api-list)
  (DELETE "/api/:id" [id] (api-del id))
  (POST "/api" req (api-add req))
  (PUT "/api/:id" [id :as req] (api-update req id))
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

