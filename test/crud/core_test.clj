(ns crud.core-test
  (:require [clojure.test :refer :all]
            [crud.server.core :refer :all]
            [crud.server.model :as model]
            [crud.server.web :as web]
            [ring.mock.request :as mock]))


(defn db-fixture [f]
  (with-redefs [model/db-name "test"]
    (model/migrate)
    (f)
    (model/drop-table)))

(use-fixtures :each db-fixture)

(deftest list-test
  (testing "List api route"
    (let [req (mock/request :get "/api")
          res (web/app req)]
      (is (= (:body res) "[]")))))
