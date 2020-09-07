(ns crud.server.model
  (:require [clojure.java.jdbc :as sql]
            [crud.server.util :as util]))

(def url "postgresql://localhost:5432/crud")
(def db-name :crud) ; may be changed for testing?

(defn list []
  (sql/query url ["select * from crud order by id"]))

(defn add [person]
  (sql/insert! url db-name person))

(defn delete [id]
  (sql/delete! url db-name ["id = ?" id]))

(defn update [id person]
  (sql/update! url db-name person ["id = ?" id]))

(defn value-reader-sql [key value]
  (cond
    (= key :birth) (util/format-sql-date value)
    :else value))

(defn value-reader-json [key value]
  (cond
    (= key :birth) (util/parse-sql-date value)
    (= key :sex) (util/cast-enum "sex" value)
    :else value))

(defn migrated? []
  (-> (sql/query url
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='"
                       (name db-name)
                       "'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands url "CREATE TYPE sex AS ENUM ('male', 'female', 'not applicable');")
    (sql/db-do-commands url
                        (sql/create-table-ddl
                         db-name
                         [[:id :serial "PRIMARY KEY"]
                          [:name "varchar(255)" "NOT NULL"]
                          [:insurance "char(16)" "NOT NULL UNIQUE"]
                          [:sex :sex "NOT NULL"]
                          [:birth :date "NOT NULL"]
                          [:address "varchar(255)" "NOT NULL"]]))
    (println " done")))
