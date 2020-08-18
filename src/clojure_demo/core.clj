(ns clojure-demo.core
   (:require [ring.adapter.jetty :as ring]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (ring/run-jetty handler {:port  8080 :join? false})
  (println "Started on localhost:8080"))
