(ns demo_clj.core
   (:require [ring.adapter.jetty :as ring]
   			 [demo_clj.models.crud :as model]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (->> (model/get-all)
        (map str)
        (clojure.string/join "<br>"))})

(defn -main
  [& args]
  (ring/run-jetty handler {:port  8000 :join? false})
  (println "Started on localhost:8000"))
