(ns demo_clj.models.migration
  (:require [clojure.java.jdbc :as sql]
            [demo_clj.models.util :as util]
            [demo_clj.models.crud :as crud]))

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
                         [:insurance "char(16)" "NOT NULL UNIQUE"]
                         [:gender "char(1)" "NOT NULL"]
                         [:birth :date "NOT NULL"]
                         [:address "varchar(255)" "NOT NULL"]]))
    (println " done")))


(defn mock-insurance [] 
    (clojure.string/join (map str (take 16 (repeatedly #(rand-int 10))))))

(defn mock-name []
    (apply str (take (+ 4 (rand-int 10)) (repeatedly #(char (+ (rand-int 26) 65))))))

(defn mock-gender []
    (if (= 0 (rand-int 2)) "M" "F"))

(defn mock-birth []
    (let [y (str (+ 1920 (rand-int 101)))
          m (format "%02d" (+ 1 (rand-int 12)))
          d (format "%02d" (+ 1 (rand-int 28)))]
         (clojure.string/join \- [y m d])
    ))


(defn populate [len] 
    (sql/insert! crud/url :crud {:name (mock-name),
                                  :insurance (mock-insurance),
                                  :gender (mock-gender)
                                  :birth (util/parse-sql-date (mock-birth))
                                  :address (mock-name),
                                  })
    (if (> len 0) (populate (- len 1)) nil))