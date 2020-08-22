(ns crud.util
  (:import org.postgresql.util.PGobject)
  (:require [clojure.java.jdbc :as sql]))

(defn parse-sql-date [datestr]  
				(->> (str datestr "-UTC") ; add "-UTC" so timezones won't matter
				    (.parse	(java.text.SimpleDateFormat. "yyyy-MM-dd-zzz"))
				    .getTime
				    java.sql.Date.))

(defn format-sql-date [date]
    (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") date))


(defn cast-enum [type val] 
				(doto (PGobject.)
        (.setType type)
        (.setValue val)))
    