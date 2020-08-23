(ns crud.model
  (:require [clojure.java.jdbc :as sql]
            [crud.util :as util]))

(def url "postgresql://localhost:5432/crud")

(defn list []
 (sql/query url ["select * from crud order by id"]))

(defn add [person]
  (sql/insert! url :crud person))

(defn delete [id]
  (sql/delete! url :crud ["id = ?" id]))

(defn update [id person]
  (sql/update! url :crud person ["id = ?" id]))


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
                       "where table_name='crud'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands url "CREATE TYPE sex AS ENUM ('male', 'female', 'not applicable');")
    (sql/db-do-commands url
                        (sql/create-table-ddl
                         :crud
                         [[:id :serial "PRIMARY KEY"]
                          [:name "varchar(255)" "NOT NULL"]
                          [:insurance "char(16)" "NOT NULL UNIQUE"]
                          [:sex :sex "NOT NULL"]
                          [:birth :date "NOT NULL"]
                          [:address "varchar(255)" "NOT NULL"]]))
    (println " done")))


(defn mock-insurance [] 
    (clojure.string/join (map str (take 16 (repeatedly #(rand-int 10))))))

(defn mock-name []
    (apply str (take (+ 4 (rand-int 10)) (repeatedly #(char (+ (rand-int 26) 65))))))

(defn mock-sex []
    (util/cast-enum "sex" (if (= 0 (rand-int 2)) "male" "female")))

(defn mock-birth []
    (let [y (str (+ 1920 (rand-int 101)))
          m (format "%02d" (+ 1 (rand-int 12)))
          d (format "%02d" (+ 1 (rand-int 28)))]
         (util/parse-sql-date (clojure.string/join \- [y m d]))
    ))

(defn populate [len] 
    (sql/insert! url :crud {:name (mock-name),
                                  :insurance (mock-insurance),
                                  :sex (mock-sex)
                                  :birth (mock-birth)
                                  :address (mock-name),
                                  })
    (if (> len 0) (populate (- len 1)) nil))
