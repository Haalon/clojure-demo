(ns clojure-demo.models.migration
  (:require [clojure.java.jdbc :as sql]
            [clojure-demo.models.crud :as crud]))

(defn migrated? []
  (-> (sql/query crud/url
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='crud'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands crud/url
                        (sql/create-table-ddl
                         :crud
                         [[:id :serial "PRIMARY KEY"]
                         [:name "varchar(255)" "NOT NULL"]
                         [:insurance "char(16)" "NOT NULL"]
                         [:gender "char(1)" "NOT NULL"]
                         [:birth "varchar(32)" "NOT NULL"]
                         [:address "varchar(255)" "NOT NULL"]]))
    (println " done")))
