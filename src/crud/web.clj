(ns crud.web
  (:import org.postgresql.util.PSQLException)
  (:import java.sql.BatchUpdateException)
  (:require [ring.util.response :as resp]
            [compojure.core :refer [defroutes GET DELETE POST PUT]]
            [compojure.route :refer [resources]]
            [crud.model :as model]
            [crud.util :as util]
            [clojure.data.json :as json]))

(defn build-response [body status]
  {:status status
   :headers {"Content-Type" "application/json"}
   :body body})

(defn read-body [request]
  (prn (:remote-addr request) \newline)
  (-> request
      :body
      slurp
      (json/read-str :key-fn keyword
                     :value-fn model/value-reader-json)))

(defn response [method & args]
  (print "\n\nrequest with\n" method \newline args)
  (flush)
  (try
    (let [res (apply method args)]
      (-> res
          (json/write-str :value-fn model/value-reader-sql)
          (build-response 200)))
    (catch Exception e (-> e
                           .getMessage
                           json/write-str
                           (build-response 400)))))

(defn api-list [_] (response model/list))
(defn api-add [req] (response model/add (read-body req)))
(defn api-update [req id] (response model/update id (read-body req)))
(defn api-delete [id] (response model/delete id))

(defroutes app
  ; (GET "/" [] index)
  (GET "/" [] (-> "index.html"
                  (resp/resource-response {:root "public"})
                  (resp/content-type "text/html")))
  (GET "/api" [] api-list)
  (DELETE "/api/:id" [id] (-> id Integer/parseInt api-delete))
  (POST "/api" req (api-add req))
  (PUT "/api/:id" [id :as req] (->> id Integer/parseInt (api-update req)))
  (resources "/"))


