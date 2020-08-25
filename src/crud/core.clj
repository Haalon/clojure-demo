(ns crud.core
  (:import org.postgresql.util.PSQLException)
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

(defn build-response [body status]
  {:status status
   :headers {"Content-Type" "application/json"}
   :body body})

(defn read-body [request]
  (-> request
      :body
      slurp
      (json/read-str :key-fn keyword 
                     :value-fn model/value-reader-json)))

(defn response [method & args]
  (try
    (let [res (apply method args)]
      (-> res
          (json/write-str :value-fn model/value-reader-sql)
          (build-response 200)))
    (catch PSQLException e (-> e
                               .getMessage
                               json/write-str
                               (build-response 400)))))

(defn api-list [_] (response model/list))
(defn api-add [req] (response model/add (read-body req)))
(defn api-update [req id] (response model/update id (read-body req)))
(defn api-delete [id] (response model/delete id))

(defroutes app
  (GET "/" [] index)
  (GET "/api" [] api-list)
  (DELETE "/api/:id" [id] (api-delete id))
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

