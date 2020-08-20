(ns demo_clj.models.crud
  (:require [clojure.java.jdbc :as sql]))

(def url "postgresql://localhost:5432/crud")

(defn get-all []
	(into [] (sql/query url ["select * from crud"])))

; person should be a map
(defn add [person]
  (sql/insert! url :crud person)) 
